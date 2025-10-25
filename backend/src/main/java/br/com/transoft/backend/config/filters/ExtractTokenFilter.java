package br.com.transoft.backend.config.filters;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.service.CustomUserDetailsService;
import br.com.transoft.backend.dto.TokenInfo;
import br.com.transoft.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExtractTokenFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    public ExtractTokenFilter(CustomUserDetailsService customUserDetailsService, TokenService tokenService) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = extractTokenFromRequestHeader(request);

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        TokenInfo tokenInfo = tokenService.extractTokenInfo(token.get());

        UserDetails user = customUserDetailsService.loadUserByUsername(tokenInfo.userAccountId());

        Role role = extractRoleFromUser(user);

        LoggedUserAccount loggedUserAccount = new LoggedUserAccount(tokenInfo.userAccountId(), tokenInfo.companyId(), role);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loggedUserAccount, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Role extractRoleFromUser(UserDetails user) {
        GrantedAuthority simpleGrantedAuthority = user.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);

        String roleString = simpleGrantedAuthority
                .getAuthority()
                .replace("ROLE_", "");

        return Role.fromString(roleString);
    }

    private Optional<String> extractTokenFromRequestHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
            return Optional.of(authorization.replace("Bearer ", ""));
        }

        return Optional.empty();
    }

}
