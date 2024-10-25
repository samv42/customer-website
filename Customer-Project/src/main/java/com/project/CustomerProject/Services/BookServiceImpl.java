package com.project.CustomerProject.Services;

import com.project.CustomerProject.Book;
import com.project.CustomerProject.BookRepository;
import com.project.CustomerProject.Services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class BookServiceImpl implements BookService {
    @Autowired
    final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {return bookRepository.save(book);}

    @Override
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Book> saveAllBook(List<Book> bookList) {
        return bookRepository.saveAll(bookList);
    }
}
