package br.com.transoft.backend.config.filters;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.service.CustomUserDetailsService;
import br.com.transoft.backend.dto.TokenInfo;
import br.com.transoft.backend.exception.InvalidTokenException;
import br.com.transoft.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    public CustomFilter(CustomUserDetailsService customUserDetailsService, TokenService tokenService) {
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

        LoggedUserAccount loggedUserAccount = new LoggedUserAccount(tokenInfo.userAccountId(), tokenInfo.companyId());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loggedUserAccount, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequestHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
            return Optional.of(authorization.replace("Bearer ", ""));
        }

        return Optional.empty();
    }

}
