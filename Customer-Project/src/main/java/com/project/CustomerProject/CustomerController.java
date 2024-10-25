package com.project.CustomerProject;

import com.project.CustomerProject.Services.BookServiceImpl;
import com.project.CustomerProject.Services.CustomerServiceImpl;
import com.project.CustomerProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    public CustomerServiceImpl customerService;

    @Autowired
    public BookServiceImpl bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "index";
    }

    @GetMapping("customer-list")
    public String getCustomerList(Model model) {
        final List<Customer> customerList = customerService.getAllCustomers();
        List<User> userList = userService.getAllUsers();
        List<User> incompleteUserList = new ArrayList<>();
        for(User user : userList){
            if(customerService.getByUser(user) == null){
                incompleteUserList.add(user);
            }
        }
        model.addAttribute("customerList", customerList);
        return "customer-list";
    }

    @GetMapping("/incomplete-user")
    public String showIncompleteUserPage(Model model) {
        List<Customer> customerList = customerService.getAllCustomers();
        List<User> userList = userService.getAllUsers();
        List<User> incompleteUserList = new ArrayList<>();
        for(User user : userList){
            if(customerService.getByUser(user) == null){
                incompleteUserList.add(user);
            }
        }
        model.addAttribute("userList", incompleteUserList);
        return "unregistered-users";
    }

    @GetMapping("/new/{id}")
    public String showNewCustomerPage(Model model, @PathVariable(name = "id") Long id) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        model.addAttribute("id", id);
        return "new-customer";
    }

    @PostMapping(value = "/save/{id}")
    // As the Model is received back from the view, @ModelAttribute
    // creates a Customer based on the object you collected from
    // the HTML page above
    public String saveCustomer(@ModelAttribute("customer") Customer customer, @PathVariable Long id, Model model) {
        if(customer.getFullName() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        } else if(customer.getEmailAddress() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }else if(customer.getAge() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }else if(customer.getAddress() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }
        try {
            customer.setUser(userService.getUserByUserId(id));
            customerService.saveCustomer(customer);
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
            return "error-page";
        }
            return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    // The path variable "id" is used to pull a customer from the database
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit-customer");
        Customer customer = customerService.getCustomer(id);
        mav.addObject("customer", customer);
        return mav;
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable(name = "id") Long id, @ModelAttribute("customer") Customer customer, Model model) {
        if (!id.equals(customer.getId())) {
            model.addAttribute("message",
                    "Cannot update, customer id " + customer.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error-page";
        }
        if(customer.getFullName() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        } else if(customer.getEmailAddress() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }else if(customer.getAge() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }else if(customer.getAddress() == null){
            model.addAttribute("message", "Customer needs a name.");
            return "error-page";
        }
        customer.setUser(userService.getUserByUserId(customer.getUser().getId()));
        try {
            customerService.saveCustomer(customer);
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
            return "error-page";
        }
        if(customerService.getCustomer(customer.getId()) != null){
            return "redirect:/";
        }
        model.addAttribute("message", "Customer was not saved to database.");
        return "error-page";
    }
    //Finish Renting book
    @GetMapping("/rent-book/{customerId}/{bookId}")
    public String rentBook(@PathVariable(name = "customerId") Long customerId,
                           @PathVariable(name = "bookId") Long bookId, Model model) {
        Customer customer = customerService.getCustomer(customerId);
        Book book = bookService.getBook(bookId);
        customer.setBook(book);
        customerService.saveCustomer(customer);
        return "redirect:/";
    }
    @GetMapping("/returnBook/{id}")
    public String returnBook(@PathVariable(name = "id") Long id, Model model){
        Customer customer = customerService.getCustomer(id);
        customer.setBook(null);
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id) {
        Customer customer = customerService.getCustomer(id);
        customerService.deleteCustomer(id);
        return "redirect:/";
    }
}
