package com.danil.spring.model;

import java.util.ArrayList;
import java.util.Collection;

public enum UserRole {
    USER(
            UserAuthority.USER_READ,

            UserAuthority.FILE_READ),

    MODERATOR(
            UserAuthority.USER_READ,
            UserAuthority.USER_READALL,

            UserAuthority.FILE_READ,
            UserAuthority.FILE_UPDATE,
            UserAuthority.FILE_DELETE,

            UserAuthority.EVENT_READ,
            UserAuthority.EVENT_UPDATE,
            UserAuthority.EVENT_DELETE),

    ADMIN(
            UserAuthority.USER_CREATE,
            UserAuthority.USER_READ,
            UserAuthority.USER_READALL,
            UserAuthority.USER_UPDATE,
            UserAuthority.USER_DELETE,

            UserAuthority.FILE_CREATE,
            UserAuthority.FILE_READ,
            UserAuthority.FILE_UPDATE,
            UserAuthority.FILE_DELETE,

            UserAuthority.EVENT_CREATE,
            UserAuthority.EVENT_READ,
            UserAuthority.EVENT_UPDATE,
            UserAuthority.EVENT_DELETE);

    private Collection<UserAuthority> authorities;

    UserRole(UserAuthority... authorities) {
        this.authorities = new ArrayList<>();
        for (int i = 0; i < authorities.length; i++) {
            this.authorities.add(authorities[i]);
        }
    }

    public Collection<UserAuthority> getAuthorities() {
        return authorities;
    }

    public static UserRole getRoleByName(String name) {
        switch (name) {
            case "USER":
                return USER;
            case "MODERATOR":
                return MODERATOR;
            case "ADMIN":
                return ADMIN;
            default:
                return null;
        }
    }
}
