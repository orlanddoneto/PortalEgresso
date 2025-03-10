package com.muxegresso.egresso.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_coordenador")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Coordenador implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "O nome deve ser preenchido!")
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank(message = "O email deve ser preenchido!")
    @Column(nullable = false, length = 255, unique = true)
    @Email
    private String email;

    @NotBlank(message = "A senha deve ser preenchida!")
    @Column(nullable = false, length = 255)
    private String senha;

    @NotBlank(message = "O tipo deve ser preenchido!")
    @Column(nullable = false, length = 255)
    private String tipo;

    @OneToMany(mappedBy = "coordenador")
    private Set<Curso> cursos = new HashSet<>();
}
