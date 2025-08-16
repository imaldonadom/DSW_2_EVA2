package com.ipss.demo.security;

import com.ipss.demo.model.AppUser;
import com.ipss.demo.repository.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;



@Service
public class DbUserDetailsService implements UserDetailsService {
    private final AppUserRepository repo;

    public DbUserDetailsService(AppUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario: " + username));
        String role = "ROLE_" + u.getRole().name();
        return new User(u.getUsername(), u.getPassword(), u.isEnabled(), true, true, true, createAuthorityList(role));
    }
}
