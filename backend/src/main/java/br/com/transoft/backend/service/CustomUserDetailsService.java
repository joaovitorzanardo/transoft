package br.com.transoft.backend.service;

import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.repository.UserAccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> authorities = new HashSet<>();

        UserAccount userAccount = userAccountRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with the provided id was found."));

        authorities.add(new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().getRole()));

        return new User(userAccount.getEmail(), userAccount.getPassword(), authorities);
    }
}
