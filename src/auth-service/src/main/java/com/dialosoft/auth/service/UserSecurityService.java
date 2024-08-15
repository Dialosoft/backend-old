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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService{

    private final UserRepository userRepository;

    public UserEntity getUserByUserName(String userName) {
        Optional<UserEntity> userEntityOp = this.userRepository.findByUsername(userName);
        if(userEntityOp.isEmpty()){
            throw new UsernameNotFoundException("User " + userName + " not found");
        }
        return userEntityOp.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = getUserByUserName(username);

        Set<RoleEntity> roles = userEntity.getRoles();
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> generateGrantedAuthority(role.getRoleType()))
                .collect(Collectors.toSet());

        return User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(authorities)
            .accountLocked(userEntity.getLocked())
            .disabled(userEntity.getDisable())
            .build();
    }

    private GrantedAuthority generateGrantedAuthority(RoleType roleType){
        return new SimpleGrantedAuthority(roleType.getRoleName());
    }
}
