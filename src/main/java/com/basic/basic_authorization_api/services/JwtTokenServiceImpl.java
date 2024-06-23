package com.basic.basic_authorization_api.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.basic.basic_authorization_api.dto.JwtDto;
import com.basic.basic_authorization_api.models.Users;
import com.basic.basic_authorization_api.repositories.UserRepository;
import com.basic.basic_authorization_api.utils.JwtTokenUtils;
import com.basic.basic_authorization_api.utils.TokenType;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final Map<String, String> refreshTokenStorange = new HashMap<>();

    @Override
    public JwtDto generateTokens(UserDetails userDetails) {
        String accessToken = this.jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = this.jwtTokenUtils.generateRefreshToken(userDetails);
        this.refreshTokenStorange.put(refreshToken, userDetails.getUsername());
        return new JwtDto(accessToken, refreshToken);
    }

    /**
     * Обновление токенов. Метод проверяет наличие в refreshTokenStorange
     * токена, которые передается в качестве аргумента. Проверяет соответсвие
     * данных из токена с данным из базы данных.
     * 
     * @param refreshToken
     * @throws AuthException Если пользователь с именем из токена не найден в
     *                       базе данных выбрасывает исключение.
     */
    @Override
    public JwtDto refreshTokens(String refreshToken) throws AuthException {
        if (this.jwtTokenUtils.validateToken(refreshToken, TokenType.REFRESH)) {
            log.info(this.refreshTokenStorange.get(refreshToken));
            String username = this.jwtTokenUtils.getUsernameFromRefreshToken(refreshToken);
            String savedUsername = this.refreshTokenStorange.get(refreshToken);
            if (savedUsername != null && savedUsername.equals(username)) {
                Users user = this.userRepository.findByUsername(username)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));

                UserDetails userDetails = new User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .toList());

                JwtDto tokens = this.generateTokens(userDetails);
                this.refreshTokenStorange.remove(refreshToken);
                this.refreshTokenStorange.put(tokens.getRefreshToken(), userDetails.getUsername());

                return tokens;
            }
        }

        throw new AuthException();
    }

}
