package com.muxegresso.egresso.domain.enums;

public enum UserStatus {
    ACTIVE("ativado"),
    BLOCKED("bloqueado");

    private String userStatus;

    UserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}