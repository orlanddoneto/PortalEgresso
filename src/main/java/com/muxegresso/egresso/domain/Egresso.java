package com.muxegresso.egresso.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_egresso")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Egresso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    @Size(min = 5, max = 50)
    private String senha;

    @Column(nullable = false, length = 255)
    @Email
    private String email;

    @Column(nullable = false, length = 50)
    private String cpf;

    @Column(nullable = false)
    private String resumo;

    @Column(length = 255)
    private String url_foto;

    @OneToMany(mappedBy = "egresso")
    private Set<Curso_Egresso> egressoCursos = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "egresso")
    Set<Cargo> cargos= new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "egresso")
    Set<Depoimento> depoimentos = new HashSet<>();



}
