package com.bluegems.server.gembackend.security;


import com.bluegems.server.gembackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GemUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public GemUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository
                .findByEmail(email)
                .map(GemUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found"));
    }
}
