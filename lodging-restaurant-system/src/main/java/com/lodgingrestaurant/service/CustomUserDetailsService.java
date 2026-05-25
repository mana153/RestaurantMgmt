package com.lodgingrestaurant.service;

import com.lodging.restaurant.model.Staff;
import com.lodging.restaurant.model.User;
import com.lodging.restaurant.repository.StaffRepository;
import com.lodging.restaurant.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    public CustomUserDetailsService(StaffRepository staffRepository, UserRepository userRepository) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Staff> staffOptional = staffRepository.findByEmail(email);
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + staff.getRole().toUpperCase());
            return new org.springframework.security.core.userdetails.User(staff.getEmail(), staff.getPassword(), Collections.singleton(authority));
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_GUEST");
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
