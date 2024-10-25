package com.project.CustomerProject;

import com.project.CustomerProject.Services.CustomerService;
import com.project.CustomerProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;


@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/user")
    public User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/register")
    public String showNewUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/save-user")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        Role role = new Role(Role.Roles.ROLE_USER);
        user.setAuthorities(Collections.singletonList(role));
        userService.createNewUser(user);
        return "redirect:/";
    }

    @GetMapping("/edit-user")
    public String showEditUserPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(user.getUsername());
        model.addAttribute("user", updateUser);
        return "edit-user";
    }

    @PostMapping(value = "/update-user")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.changeUser(user);
        return "redirect:/";
    }

   @GetMapping("/customer-view")
   public String showCustomerView(Model model){
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Customer customer = customerService.getByUser(user);
       if(customer == null){
           model.addAttribute("username", user.getUsername());
           return "customer-view-incomplete";
       }
    model.addAttribute("customer", customer);
    return "customer-view";
   }

}
