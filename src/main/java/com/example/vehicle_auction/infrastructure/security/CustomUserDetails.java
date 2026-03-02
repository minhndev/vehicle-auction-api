package com.example.vehicle_auction.infrastructure.security;

import com.example.vehicle_auction.domain.model.AccountModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final AccountModel account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getRoles() == null) return Collections.emptyList();

        // Map Role và Permission của bạn thành SimpleGrantedAuthority
        return account.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() { return account.getPassword(); }
    @Override
    public String getUsername() { return account.getEmail(); }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return account.isActive(); }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return account.isVerified(); }
}
