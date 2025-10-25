package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.account.ChangePasswordDto;
import br.com.transoft.backend.dto.login.UserAccountDto;
import br.com.transoft.backend.service.UserAccountService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public UserAccountDto getUserAccount(Authentication authentication) {
        return userAccountService.getUserAccount((LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping(path = "/password")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        userAccountService.changePassword(changePasswordDto.newPassword(), (LoggedUserAccount) authentication.getPrincipal());
    }

}
