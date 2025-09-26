package com.spedu.tutors.auth.config;

import com.spedu.tutors.auth.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshExpirationMs;

    private UserDetailsService userDetailsService;
    private BlacklistedTokenRepository blacklistRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JwtServiceImpl(
            @Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate,
            UserDetailsService userDetailsService,
            BlacklistedTokenRepository blacklistRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.blacklistRepository = blacklistRepository;
    }

    private Key getSigningKey() {
        String encoded = Base64.getEncoder().encodeToString(secret.getBytes());
        byte[] keyBytes = Base64.getDecoder().decode(encoded);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(com.spedu.tutors.auth.entity.User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().getCode())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(com.spedu.tutors.auth.entity.User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, com.spedu.tutors.auth.entity.User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token) && !blacklistRepository.existsByToken(token));
    }

    @Override
    public boolean isValidRefreshToken(String token) {
        try {
            final String username = extractUsername(token);
            userDetailsService.loadUserByUsername(username); // will throw if invalid
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

