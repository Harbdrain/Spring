package com.danil.spring.model;

public enum UserAuthority {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_READALL("user:readall"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    private String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
