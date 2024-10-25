package com.project.CustomerProject.Services;

import com.project.CustomerProject.Book;
import com.project.CustomerProject.Customer;
import com.project.CustomerProject.CustomerRepository;
import com.project.CustomerProject.Services.CustomerService;
import com.project.CustomerProject.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Customer customer = customerRepository.getById(id);
        customer.setBook(null);
        customerRepository.save(customer);}

    @Override
    @Transactional
    public List<Customer> saveAllCustomer(List<Customer> customerList) {
        return customerRepository.saveAll(customerList);
    }

    @Override
    @Transactional
    public Book findBookByCustomer(Long id) {return customerRepository.getBookById(id);}

    @Override
    @Transactional
    public Customer getByName(String name) {return customerRepository.getByFullName(name);}

    @Override
    @Transactional
    public Customer getByUser(User user) {return customerRepository.getByUser(user);}
}
