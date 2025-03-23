package com.muxegresso.egresso.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muxegresso.egresso.domain.Curso;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestCoordenadorDto implements Serializable {

    private Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    private String tipo;

    private Set<Curso> cursos;
}
