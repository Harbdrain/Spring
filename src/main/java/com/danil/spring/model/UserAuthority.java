package com.danil.spring.model;

public enum UserAuthority {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_READALL("user:readall"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    FILE_CREATE("file:create"),
    FILE_READ("file:read"),
    FILE_UPDATE("file:update"),
    FILE_DELETE("file:delete"),

    EVENT_CREATE("event:create"),
    EVENT_READ("event:read"),
    EVENT_UPDATE("event:update"),
    EVENT_DELETE("event:delete");

    private String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
