package com.muxegresso.egresso.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
@Table(name = "tb_curso")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Curso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "O nome do curso deve ser informado!")
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank(message = "O nível do curso deve ser informado!")
    @Column(nullable = false, length = 255)
    private String nivel;

    @OneToMany(mappedBy = "curso")
    private Set<Curso_Egresso> cursoEgressos = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "coordenador_id")
    private Coordenador coordenador;

    public Curso(Integer id, String nome, String nivel) {
        this.id = id;
        this.nome = nome;
        this.nivel = nivel;
    }
}
