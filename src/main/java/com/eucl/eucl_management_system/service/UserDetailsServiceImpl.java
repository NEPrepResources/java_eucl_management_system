package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));

            logger.info("User found: {}", user.getEmail());
            return UserDetailsImpl.build(user);
        } catch (Exception e) {
            logger.error("Error loading user: {}", e.getMessage());
            throw new UsernameNotFoundException("User Not Found with email: " + username, e);
        }
    }
}