package br.com.transoft.backend.config.filters;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.CookieExpiredException;
import br.com.transoft.backend.exception.InvalidTokenException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.UserAccountRepository;
import br.com.transoft.backend.service.CustomUserDetailsService;
import br.com.transoft.backend.dto.TokenInfo;
import br.com.transoft.backend.service.TokenService;
import br.com.transoft.backend.service.UserAccountService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final UserAccountRepository userAccountRepository;
    private final TokenService tokenService;

    public ExtractTokenFilter(CustomUserDetailsService customUserDetailsService, UserAccountRepository userAccountRepository, TokenService tokenService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userAccountRepository = userAccountRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/login") ||
                path.equals("/api/register") ||
                path.equals("/api/refresh-token") ||
                path.equals("/api/register/admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = extractTokenFromRequestHeader(request);

        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Claims claims = tokenService.extractClaimsFromToken(token.get());

        if (tokenService.isClaimsInvalid(claims)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        TokenInfo tokenInfo = tokenService.extractTokenInfo(claims);

        if (tokenService.hasExpired(claims.getExpiration())) {
            Optional<String> refreshToken = extractRefreshTokenFromRequestHeader(request);

            if (refreshToken.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Claims refreshTokenClaims = tokenService.extractClaimsFromToken(refreshToken.get());

            if (tokenService.hasExpired(refreshTokenClaims.getExpiration())) {
                tokenService.deleteRefreshToken(refreshToken.get());

                ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(0)
                        .sameSite("Lax")
                        .build();

                ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(0)
                        .sameSite("Lax")
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UserAccount userAccount = userAccountRepository.findById(tokenInfo.userAccountId()).orElseThrow(() -> new ResourceNotFoundException("User id not found"));

            String newToken = tokenService.generateToken(userAccount);

            ResponseCookie tokenCookie = ResponseCookie.from("token", newToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(900)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        }

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
        if ("web".equals(request.getHeader("X-Client-Type"))) {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) {
                return Optional.empty();
            }

            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    if (cookie.getMaxAge() == 0) {
                        throw new CookieExpiredException();
                    }
                    return Optional.of(cookie.getValue());
                }
            }

            return Optional.empty();
        }

        String authorization = request.getHeader("Authorization");

        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
            return Optional.of(authorization.replace("Bearer ", ""));
        }

        return Optional.empty();
    }

    private Optional<String> extractRefreshTokenFromRequestHeader(HttpServletRequest request) {
        if ("web".equals(request.getHeader("X-Client-Type"))) {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) {
                return Optional.empty();
            }

            for (Cookie cookie : cookies) {
                if ("refresh-token".equals(cookie.getName())) {
                    if (cookie.getMaxAge() == 0) {
                        throw new CookieExpiredException();
                    }
                    return Optional.of(cookie.getValue());
                }
            }

            return Optional.empty();
        }

        String authorization = request.getHeader("Authorization");

        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
            return Optional.of(authorization.replace("Bearer ", ""));
        }

        return Optional.empty();
    }

}
