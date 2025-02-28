package org.example.security;

import lombok.AllArgsConstructor;
import org.example.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch(user.getRole()) {
            case participant -> {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PARTICIPANT"));
            }
            case mentor -> {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_MENTOR"));
            }
             case organizer ->
            {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
            }
            default -> {
                return List.of();
            }
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
