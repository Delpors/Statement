package com.example.statement.sequryti;

import com.example.statement.entity.UserEntity;
import com.example.statement.util.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserServiceUtils userServiceUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userServiceUtils.getUserByUserName(username);
        return new UserPrincipal(userEntity);
    }
}
