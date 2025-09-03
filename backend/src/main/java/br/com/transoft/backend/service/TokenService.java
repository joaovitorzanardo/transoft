package br.com.transoft.backend.service;

import br.com.transoft.backend.constants.Role;
import br.com.transoft.backend.dto.TokenInfo;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    private final String COMPANY_ID = "company_id";
    private final String ROLE = "role";

    public String generateToken(UserAccount userAccount) {
        return Jwts.builder()
                .signWith(privateKey)
                .issuer(issuer)
                .subject(userAccount.getUserAccountId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(calculateExpirationDate(1))
                .claims(generateClaimsFromUserAccount(userAccount))
                .compact();
    }

    private Map<String, String> generateClaimsFromUserAccount(UserAccount userAccount) {
        Map<String, String> claims = new HashMap<>();

        claims.put(COMPANY_ID, userAccount.getCompany().getCompanyId());
        claims.put(ROLE, userAccount.getRole().getRole());

        return claims;
    }

    public TokenInfo extractTokenInfo(String token) {
        Claims claims = extractClaimsFromToken(token);

        if (isClaimsInvalid(claims)) {
            throw new InvalidTokenException();
        }

        return new TokenInfo(
                claims.getSubject(),
                (String) claims.get(COMPANY_ID),
                Role.fromString((String) claims.get(ROLE))
        );
    }

    public boolean isClaimsInvalid(Claims claims) {
        if (!issuer.equals(claims.getIssuer())) {
            return true;
        }

        return hasExpired(claims.getExpiration());
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date calculateExpirationDate(long daysToExpire) {
        return Date.from(
                LocalDate.now()
                        .plusDays(daysToExpire)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    private boolean hasExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
