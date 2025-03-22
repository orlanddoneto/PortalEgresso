package com.muxegresso.egresso.domain.enums;

public enum UserTipo {
    Egresso("egresso"),
    Coordenador("coordenador");

    private String tipo;

    UserTipo(String tipo) {
        this.tipo = tipo;
    }
}
