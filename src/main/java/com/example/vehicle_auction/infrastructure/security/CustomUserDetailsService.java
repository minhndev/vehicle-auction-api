package com.example.vehicle_auction.infrastructure.security;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository AccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountModel account = AccountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return new CustomUserDetails(account);
    }
}
