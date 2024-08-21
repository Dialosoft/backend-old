package com.dialosoft.auth.service;

import com.dialosoft.auth.service.utils.RoleType;
import com.dialosoft.auth.persistence.entity.RoleEntity;
import com.dialosoft.auth.persistence.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dialosoft.auth.persistence.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService{

    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UserEntity getUserByUsername(String identifier) {
        // TODO: Change this support both email and username
//        Optional<UserEntity> userEntityOp = EMAIL_PATTERN.matcher(identifier).matches() ?
//                this.userRepository.findByEmail(identifier) :
//                this.userRepository.findByUsername(identifier);

        Optional<UserEntity> userEntityOp = this.userRepository.findByUsername(identifier);

        if (userEntityOp.isEmpty()) {
            throw new UsernameNotFoundException("User " + identifier + " not found");
        }
        return userEntityOp.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = getUserByUsername(username);

        RoleEntity role = userEntity.getRole();
        GrantedAuthority authority = generateGrantedAuthority(role.getRoleType());

        return User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(authority)
            .accountLocked(userEntity.getLocked())
            .disabled(userEntity.getDisable())
            .build();
    }

    private GrantedAuthority generateGrantedAuthority(RoleType roleType){
        return new SimpleGrantedAuthority(roleType.getRoleName());
    }
}
