package com.bluegems.server.gembackend.security;

import com.bluegems.server.gembackend.entity.AccountEntity;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GemUserDetails implements UserDetails {
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    public GemUserDetails(AccountEntity accountEntity) {
        this.username = accountEntity.getEmail();
        this.password = accountEntity.getPassword();
        this.authorities = Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
