package br.com.transoft.backend.controller;

import br.com.transoft.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
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

}
