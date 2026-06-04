package com.example.statement.sequryti;

import com.example.statement.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String rol = userEntity.getRole().toString();

        if (!rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }

        return Collections.singleton(new SimpleGrantedAuthority(rol));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return userEntity.isActive();}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return userEntity.isActive();}

}
