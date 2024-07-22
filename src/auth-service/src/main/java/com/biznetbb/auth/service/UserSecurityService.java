package com.biznetbb.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.biznetbb.auth.persistence.entity.RoleEntity;
import com.biznetbb.auth.persistence.entity.UserEntity;
import com.biznetbb.auth.persistence.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findByUsername(username);
        if(userEntity == null){
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        RoleEntity roleEntity = userEntity.getRoleId();
        String role = (roleEntity != null && roleEntity.getName() != null) ? roleEntity.getName().toLowerCase() : "user";

        return User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(this.grantedAuthority(role))
            .accountLocked(userEntity.getLocked())
            .disabled(userEntity.getDisable())
            .build();
    }

    private GrantedAuthority grantedAuthority(String role){
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
