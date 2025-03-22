package com.muxegresso.egresso.domain.enums;

public enum StatusDepoimento {
    Rejeitado("rejeitado"),
    Aprovado("aprovado"),
    Enviado("enviado");

    private String status;

    StatusDepoimento(String status) {
        this.status = status;
    }
}
