package com.project.CustomerProject;

import com.project.CustomerProject.Services.CustomerServiceImpl;
import com.project.CustomerProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
//@EnableJpaRepositories
public class CustomerProjectApplication implements CommandLineRunner{
	@Autowired
	private CustomerServiceImpl customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public CustomerProjectApplication(CustomerServiceImpl customerService, RoleService roleService) {
		this.customerService = customerService;
		this.roleService = roleService;
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(roleService.findAll().isEmpty() || userService.getAllUsers().isEmpty()){
			Role roleAdmin = new Role(Role.Roles.ROLE_ADMIN);
			Role roleUser = new Role(Role.Roles.ROLE_USER);
			User user = User.builder()
					.username("user")
					.password("password")
					.authorities(Collections.singletonList(roleUser))
					.build();
			User admin = User.builder()
					.username("admin")
					.password("password2")
					.authorities(Collections.singletonList(roleAdmin))
					.build();
			Customer customer = Customer.builder()
					.fullName("Customer 1")
					.emailAddress("customer1@gmail.com")
					.address("Customer Address One")
					.age(30)
					.build();
			Customer customer2 = Customer.builder()
					.fullName("Customer 2")
					.emailAddress("customer2@gmail.com")
					.address("Customer Address Two")
					.age(28)
					.build();
			customer.setUser(user);
			customer2.setUser(admin);
			userService.createNewUser(user);
			userService.createNewUser(admin);
			customerService.saveAllCustomer(Arrays.asList(customer, customer2));
		}
	}


}
