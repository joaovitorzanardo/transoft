package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.login.UserAccountDto;
import br.com.transoft.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping(path = "/{userId}/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable String userId) {
        userAccountService.activateUser(userId);
    }

    @GetMapping
    public UserAccountDto getUserAccount(Authentication authentication) {
        return userAccountService.getUserAccount((LoggedUserAccount) authentication.getPrincipal());
    }

}
