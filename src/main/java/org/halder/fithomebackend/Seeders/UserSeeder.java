package org.halder.fithomebackend.Seeders;

import jakarta.validation.Valid;
import org.halder.fithomebackend.Entities.ActivityLevel;
import org.halder.fithomebackend.Entities.Gender;
import org.halder.fithomebackend.Entities.Role;
import org.halder.fithomebackend.Entities.User;
import org.halder.fithomebackend.Repositories.RoleRepository;
import org.halder.fithomebackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(!(roleRepository.existsByName("ROLE_USER") && roleRepository.existsByName("ROLE_ADMIN"))){
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
            role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);

    }
        if(!(userRepository.existsByEmail("admin@admin.com") && userRepository.existsByEmail("user@user.com"))) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode(("1234admin")));
            adminUser.setActivity_level(ActivityLevel.valueOf("Moderado"));
            adminUser.setAge(25);
            adminUser.setGender(Gender.valueOf("Hombre"));
            adminUser.setWeight(70.0);
            adminUser.setHeight(170);
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole.isPresent()) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole.get());
                adminUser.setRoles(roles);
            }
            adminUser.setImagePath(null);
            userRepository.save(adminUser);
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setEmail("user@user.com");
            normalUser.setPassword(passwordEncoder.encode("1234user"));
            normalUser.setActivity_level(ActivityLevel.valueOf("Activo"));
            normalUser.setAge(25);
            normalUser.setGender(Gender.valueOf("Hombre"));
            normalUser.setWeight(70.0);
            normalUser.setHeight(170);
            Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            if (userRole.isPresent()) {
                Set<Role> roles = new HashSet<>();
                roles.add(userRole.get());
                normalUser.setRoles(roles);
            }
            normalUser.setImagePath(null);
            userRepository.save(normalUser);
        }
    }
}
