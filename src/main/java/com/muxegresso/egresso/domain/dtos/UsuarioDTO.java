package com.muxegresso.egresso.domain.dtos;


import com.muxegresso.egresso.domain.enums.UserTipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String email;
    private String senha;
    private UserTipo tipo;
}
