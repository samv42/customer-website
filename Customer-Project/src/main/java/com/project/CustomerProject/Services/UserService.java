package com.project.CustomerProject.Services;

import com.project.CustomerProject.Customer;
import com.project.CustomerProject.Role;
import com.project.CustomerProject.User;
import com.project.CustomerProject.UserRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;


@Component
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User optionalUser = userRepo.findByUsername(username);

        if (optionalUser == null) {
            throw new UsernameNotFoundException(username + " is not a valid username! Check for typos and try again.");
        }

        return optionalUser;
    }

    @Transactional(readOnly = true)
    public User getUserByUserId(Long userId) throws EntityNotFoundException {
        User user = userRepo.getById(userId);

        return (User) Hibernate.unproxy(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String username) throws EntityNotFoundException  {
        return userRepo.findByUsername(username);
    }

    public User createNewUser(User user) {
        user.setId(null);
        user.getAuthorities().forEach(a -> a.setId(null));

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        if(user.getAuthorities().isEmpty()) {
            user.setAuthorities(Collections.singletonList(new Role(Role.Roles.ROLE_USER)));
        }

        checkPassword(user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    public void saveUser(User user) {userRepo.save(user);}

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password is too short. Must be longer than 6 characters");
        }
    }
    public void changeUser(User user){
        User original = getUserByUserId(user.getId());
        checkPassword(user.getPassword());
        original.setPassword(encoder.encode(user.getPassword()));
        original.setUsername(user.getUsername());
        try {
            userRepo.save(original);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }
}
