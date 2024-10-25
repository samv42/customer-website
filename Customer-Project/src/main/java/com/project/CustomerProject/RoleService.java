package com.project.CustomerProject;

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
public class RoleService {
    @Autowired
    final RoleRepository roleRepository;

    Role getRoleByName(Role name) {
        Role role = roleRepository.findByRole(name);
        return role;
    }

    List<Role> findAll() {
        return roleRepository.findAll();
    }

    void save(Role role) {
        roleRepository.save(role);
    }
}
