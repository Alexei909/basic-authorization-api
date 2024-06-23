package com.basic.basic_authorization_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Метод возвращает UserDetail, в который оборачивается пользователь из базы данных.
     * 
     * @param username                   Имя пользователя
     * @return UserDetails               UserDetails предоставляет необходимую информацию 
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

    @Override
    public void createNewUser(Users user) {
        user.setRoles(List.of(
                roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }

    private Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
