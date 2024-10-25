package com.project.CustomerProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Book getBookById(Long id);
    Customer getById(Long id);
    Customer getByFullName(String name);
    Customer getByUser(User user);
}
