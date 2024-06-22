package com.basic.basic_authorization_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basic.basic_authorization_api.exceptions.UniquenessError;
import com.basic.basic_authorization_api.models.Users;
import com.basic.basic_authorization_api.repositories.RoleRepository;
import com.basic.basic_authorization_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * UserService, класс реализует интерфейс UserDetailsService.
 * Используется в безопасности.
 *
 * @author DESKTOP-A37889R
 * @version 
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Метод возвращает UserDetail, в который оборачивается пользователь из базы данных.
     * @param username                   Имя пользователя
     * @return                           UserDetails предоставляет необходимую информацию 
     *                                   для построения объекта Authentication
     * @throws UsernameNotFoundException Если пользователь с таким именем 
     *                                   не найден, выбрасывает UsernameNotFoundException
     * 
     * @see UserDetails
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = this.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found"
                        .formatted(username)));
        
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList());
    }

    public void createNewUser(Users user) throws UniquenessError {
        Optional<Users> potentialUser = this.findByUsername(user.getUsername());
        if (!potentialUser.isEmpty()) {
            throw new UniquenessError("Пользователь с именем %s уже существует"
                    .formatted(user.getUsername()));
        } else {
            potentialUser = this.userRepository.findByEmail(user.getEmail());
            if (!potentialUser.isEmpty()) {
                throw new UniquenessError("Пользователь с email %s уже существует"
                    .formatted(user.getEmail()));
            }
        }

        user.setRoles(List.of(
                roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }
}
