package com.project.CustomerProject;

import com.project.CustomerProject.Services.BookServiceImpl;
import com.project.CustomerProject.Services.CustomerServiceImpl;
import com.project.CustomerProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    public BookServiceImpl bookService;

    @Autowired
    public CustomerServiceImpl customerService;

    @Autowired
    public UserService userService;

    @GetMapping("/books")
    public String viewBookList(Model model) {
        final List<Book> bookList = bookService.getAllBooks();
        final List<Customer> customerList = customerService.getAllCustomers();
        final List<BookDisplay> bookDisplayList = new ArrayList<>();
        Map<Long, Customer> customerMap = new HashMap<>();
        for(Customer customer: customerList){
            if(customer.book != null) {
                customerMap.put(customer.getBook().getId(), customer);
            }
        }
        for(Book book: bookList){
            BookDisplay display = BookDisplay.builder()
                    .bookId(book.getId())
                    .title(book.getTitle())
                    .genre(book.getGenre())
                    .pageCount(book.getPageCount())
                    .build();
            if(customerMap.get(book.getId()) != null){
                display.setCustomerId(customerMap.get(book.getId()).getId());
                display.setCustomerName(customerMap.get(book.getId()).getFullName());
            }
            bookDisplayList.add(display);
        }
        model.addAttribute("bookDisplayList", bookDisplayList);
        return "book-list";
    }

    @GetMapping("/new-book")
    public String showNewBookPage(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "new-book";
    }

    @PostMapping(value = "/save-book")
    public String saveBook(@ModelAttribute("book") Book book, Model model) {
        if(book.getTitle() == null){
            model.addAttribute("message", "Book needs a title.");
            return "error-page";
        } else if(book.getGenre() == null){
            model.addAttribute("message", "Book needs a genre.");
            return "error-page";
        }else if(book.getPageCount() == null){
            model.addAttribute("message", "Book needs a page count.");
            return "error-page";
        }
        try {
            bookService.saveBook(book);
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
            return "error-page";
        }
            return "redirect:/book/books";
    }

    @GetMapping("/edit-book/{id}")
    public String showEditBookPage(@PathVariable(name = "id") Long id, Model model) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update-book/{id}")
    public String updateBook(@PathVariable(name = "id") Long id, @ModelAttribute("book") Book book, Model model) {
        if (!id.equals(book.getId())) {
            model.addAttribute("message",
                    "Cannot update, book id " + book.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error-page";
        }
        if(book.getTitle() == null){
            model.addAttribute("message", "Book needs a title.");
            return "error-page";
        } else if(book.getGenre() == null){
            model.addAttribute("message", "Book needs a genre.");
            return "error-page";
        }else if(book.getPageCount() == null){
            model.addAttribute("message", "Book needs a page count.");
            return "error-page";
        }
        try {
            bookService.saveBook(book);
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }
        if(bookService.getBook(book.getId()) != null){
            return "redirect:/book/books";
        }
        model.addAttribute("message", "Book was not saved to database.");
        return "error-page";
    }
    @GetMapping("/getBook/{id}")
    public String showGetBookPage(@PathVariable(name = "id") Long id, Model model) {
        List<Book> bookList = bookService.getAllBooks();
        List<Customer> customerList = customerService.getAllCustomers();
        List<BookDisplay> bookDisplayList = new ArrayList<>();
        Customer customer = customerService.getCustomer(id);
        for(Book book: bookList){
            for(Customer customerCheck: customerList){
                if(customer.book != null && book.getId() == customer.book.getId()){
                    bookList.remove(book);
                    customerList.remove(customerCheck);
                    }
                }
            if(bookList.contains(book)){
                BookDisplay display = BookDisplay.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .genre(book.getGenre())
                        .pageCount(book.getPageCount())
                        .customerId(customer.getId())
                        .build();
                bookDisplayList.add(display);
            }
        }

        model.addAttribute("bookDisplayList", bookDisplayList);
        return "get-book";
    }
    @RequestMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable(name = "id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/book/books";
    }
}
