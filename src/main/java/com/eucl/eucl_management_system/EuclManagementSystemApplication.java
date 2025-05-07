package com.eucl.eucl_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.eucl.eucl_management_system.entity.Erole;
import com.eucl.eucl_management_system.entity.Role;
import com.eucl.eucl_management_system.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EuclManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EuclManagementSystemApplication.class, args);
    }

    @Bean
    public  CommandLineRunner init(RoleRepository roleRepository) {
        return args -> {
            if(roleRepository.findByName(Erole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(Erole.ROLE_ADMIN));
            }
            if(roleRepository.findByName(Erole.ROLE_CUSTOMER).isEmpty()) {
                roleRepository.save(new Role(Erole.ROLE_CUSTOMER));
            }
        };
    }
}
