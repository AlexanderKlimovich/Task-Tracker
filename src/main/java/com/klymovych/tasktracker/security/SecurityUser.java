package com.klymovych.tasktracker.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final long id;
    private final String firstName;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, long id, String firstName) {
        super(username, password, authorities);
        this.id = id;
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

}
