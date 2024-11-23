
package com.exampleoctober.octoberproj.Security;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exampleoctober.octoberproj.Registration.RegisterEntity.RegisterEntity;
import com.exampleoctober.octoberproj.Registration.RegisterRepo.RegisterRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private RegisterRepo registerRepo;

    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", email);
        RegisterEntity user = registerRepo.findByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found");
        }
        logger.info("User found: {}", user.getEmail());
        return new User(user.getEmail(), user.getNumber(), getAuthorities(email));
    }

    private List<GrantedAuthority> getAuthorities(String email) {
        logger.debug("Getting authorities for user: {}", email);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(email));
        logger.debug("Authorities: {}", authorities);
        return authorities;
    }
}
