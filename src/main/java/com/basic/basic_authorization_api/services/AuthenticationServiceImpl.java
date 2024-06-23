package com.basic.basic_authorization_api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.basic.basic_authorization_api.dto.JwtDto;
import com.basic.basic_authorization_api.dto.LoginUserDto;
import com.basic.basic_authorization_api.dto.RefreshTokenDto;
import com.basic.basic_authorization_api.dto.RegisterUserDto;
import com.basic.basic_authorization_api.exceptions.PasswordMismatchError;
import com.basic.basic_authorization_api.exceptions.UniquenessError;
import com.basic.basic_authorization_api.models.Users;
import com.basic.basic_authorization_api.repositories.UserRepository;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

/**
 * AuthenticationManager проверяет существует ли логин и пароль, 
 * которей приходят в запросе.
 *
 * @author DESKTOP-A37889R
 * @version 
 */
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenService JwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtDto login(LoginUserDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        } catch (BadCredentialsException exception) {
            throw exception;
        }

        UserDetails userDetails = userService.loadUserByUsername(dto.getUsername());
        JwtDto tokens = this.JwtTokenService.generateTokens(userDetails);

        return tokens;
    }

    @Override
    public void registration(RegisterUserDto dto) throws UniquenessError, PasswordMismatchError {
        if (this.userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UniquenessError("Пользователь с таким именем уже существует");
        }

        if (this.userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UniquenessError("Пользователь с таким email уже существует");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchError("Пароли не совпадают");
        }

        String hash = this.passwordEncoder.encode(dto.getPassword());
        this.userService.createNewUser(
                new Users(null, dto.getUsername(), hash, dto.getEmail(), null));
    }

    @Override
    public JwtDto refresh(RefreshTokenDto dto) throws AuthException {
        return this.JwtTokenService.refreshTokens(dto.getRefreshToken());
    }

}
