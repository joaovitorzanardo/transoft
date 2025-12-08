package br.com.transoft.backend.service;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.auth.TokenRequest;
import br.com.transoft.backend.dto.TokenInfo;
import br.com.transoft.backend.dto.auth.TokenResponse;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.RefreshToken;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.token.expiration}")
    private Integer tokenExpirationInSeconds;

    @Value("${jwt.refresh-token.expiration}")
    private Integer refreshTokenExpirationInSeconds;

    private final String COMPANY_ID = "company_id";
    private final String ROLE = "role";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public String generateToken(UserAccount userAccount) {
        return Jwts.builder()
                .signWith(privateKey)
                .issuer(issuer)
                .subject(userAccount.getUserAccountId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(tokenExpirationInSeconds)))
                .claims(generateClaimsFromUserAccount(userAccount))
                .compact();
    }

    public String generateRefreshToken(UserAccount userAccount) {
        Date expiresAt = Date.from(Instant.now().plusSeconds(refreshTokenExpirationInSeconds));

        String tokenValue = Jwts.builder()
                .signWith(privateKey)
                .issuer(issuer)
                .subject(userAccount.getUserAccountId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expiresAt)
                .claims(generateClaimsFromUserAccount(userAccount))
                .compact();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(UUID.randomUUID().toString())
                .tokenValue(tokenValue)
                .expiresAt(expiresAt)
                .userAccount(userAccount)
                .build();

        saveRefreshToken(refreshToken);

        return tokenValue;
    }

    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        Claims refreshTokenClaims = extractClaimsFromToken(tokenRequest.refreshToken());

        TokenInfo tokenInfo = extractTokenInfo(refreshTokenClaims);

        if (hasExpired(refreshTokenClaims.getExpiration())) {
            deleteRefreshToken(tokenRequest.refreshToken());
            return new TokenResponse("");
        }

        UserAccount userAccount = UserAccount.builder()
                .userAccountId(tokenInfo.userAccountId())
                .company(new Company(tokenInfo.companyId()))
                .role(tokenInfo.role())
                .build();

        String newToken = generateToken(userAccount);

        return new TokenResponse(newToken);
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String refreshTokenValue) {
        refreshTokenRepository.findByTokenValue(refreshTokenValue)
                .ifPresent((refreshToken) -> refreshTokenRepository.delete(refreshToken));
    }

    private Map<String, String> generateClaimsFromUserAccount(UserAccount userAccount) {
        Map<String, String> claims = new HashMap<>();

        claims.put(COMPANY_ID, userAccount.getCompany().getCompanyId());
        claims.put(ROLE, userAccount.getRole().getRole());

        return claims;
    }

    public TokenInfo extractTokenInfo(Claims claims) {
        return new TokenInfo(
                claims.getSubject(),
                (String) claims.get(COMPANY_ID),
                Role.fromString((String) claims.get(ROLE))
        );
    }

    public boolean isClaimsInvalid(Claims claims) {
        return !issuer.equals(claims.getIssuer());
    }

    public Claims extractClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    public boolean hasExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
