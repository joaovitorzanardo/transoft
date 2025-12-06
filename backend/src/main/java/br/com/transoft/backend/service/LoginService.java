package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.login.LoginDto;
import br.com.transoft.backend.dto.login.LoginResponse;
import br.com.transoft.backend.dto.login.UserAccountDto;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.DisabledUserException;
import br.com.transoft.backend.exception.IncorrectPasswordException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserAccountRepository userAccountRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserAccountRepository userAccountRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginDto loginDto) {
        UserAccount userAccount = userAccountRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No user with this email was found."));

        if (!passwordEncoder.matches(loginDto.getPassword(), userAccount.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!userAccount.getEnabled()) {
            throw new DisabledUserException();
        }

        UserAccountDto user = new UserAccountDto(userAccount.getName(), userAccount.getEmail(), userAccount.getActive(), userAccount.getRole().getRole());

        String token = tokenService.generateToken(userAccount);
        String refreshToken = tokenService.generateRefreshToken(userAccount);

        return new LoginResponse(user, token, refreshToken);
    }

}
