package com.jbd.security;

import com.jbd.config.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {

    private static final String SECRET = "pjcJ7QMJbe0kcT7p/bP4kcWIISVfNeSnAMiXQW4tUPk=";

    private JwtUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        props.setSecret(SECRET);
        props.setExpiration(86400000L);
        jwtUtils = new JwtUtils(props);
        userDetails = new User("testuser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void generateToken_returnsNonBlankToken() {
        String token = jwtUtils.generateToken(userDetails);
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_returnsCorrectUsername() {
        String token = jwtUtils.generateToken(userDetails);
        assertThat(jwtUtils.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void isTokenValid_returnsTrue_forMatchingUserAndValidToken() {
        String token = jwtUtils.generateToken(userDetails);
        assertThat(jwtUtils.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void isTokenValid_returnsFalse_whenUsernameDoesNotMatch() {
        String token = jwtUtils.generateToken(userDetails);
        UserDetails otherUser = new User("otheruser", "pass", List.of());
        assertThat(jwtUtils.isTokenValid(token, otherUser)).isFalse();
    }

    @Test
    void isTokenValid_throwsExpiredJwtException_forExpiredToken() {
        JwtProperties expiredProps = new JwtProperties();
        expiredProps.setSecret(SECRET);
        expiredProps.setExpiration(-1000L);
        JwtUtils expiredJwtUtils = new JwtUtils(expiredProps);

        String token = expiredJwtUtils.generateToken(userDetails);

        assertThatThrownBy(() -> expiredJwtUtils.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);
    }
}