package br.com.transoft.backend.service;

import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public void activateUser(String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id not found"));
        userAccount.setActive(true);
        userAccountRepository.save(userAccount);
    }

}
