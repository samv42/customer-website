package com.project.CustomerProject.Services;

import com.project.CustomerProject.Book;
import com.project.CustomerProject.Customer;
import com.project.CustomerProject.User;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Customer saveCustomer(Customer customer);

    Customer getCustomer(Long id);

    void deleteCustomer(Long id);

    void deleteBook(Long id);

    List<Customer> saveAllCustomer(List<Customer> customerList);

    Book findBookByCustomer(Long id);

    Customer getByName(String name);

    Customer getByUser(User user);
}
