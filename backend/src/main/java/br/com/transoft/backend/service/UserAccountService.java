package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.login.UserAccountDto;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void activateUser(String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id not found"));
        userAccount.setActive(true);
        userAccountRepository.save(userAccount);
    }

    public UserAccountDto getUserAccount(LoggedUserAccount loggedUserAccount) {
        return userAccountRepository
                .findById(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("User id not found"))
                .toDto();
    }

    public void changePassword(String newPassword, LoggedUserAccount loggedUserAccount) {
        UserAccount userAccount = userAccountRepository
                .findById(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("User id not found"));

        if (passwordEncoder.matches(newPassword, userAccount.getPassword())) {
            throw new ResourceConflictException("The new password is the same as the old password.");
        }

        userAccount.setPassword(passwordEncoder.encode(newPassword));

        if (userAccount.getEnabled() && !userAccount.getActive()) {
            userAccount.setActive(true);
        }

        userAccountRepository.save(userAccount);
    }

}
