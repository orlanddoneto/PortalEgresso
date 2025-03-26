package com.muxegresso.egresso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    private String nivel;

    @Column(nullable = false, length = 255)
    private Integer ano_inicio;

    @Column(nullable = false, length = 255)
    private Integer ano_fim;


    @ManyToMany
    @JoinTable(name = "curso_oportunidade",           // nome da tabela de junção
            joinColumns = @JoinColumn(name = "curso_id"),    // FK para Curso
            inverseJoinColumns = @JoinColumn(name = "oportunidade_id")) // FK para Oportunidade
    private Set<Oportunidade> oportunidades = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private Set<Curso_Egresso> cursoEgressos = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "coordenador_id")
    private Coordenador coordenador;

    public Curso(Integer id, String nome, String nivel) {
        this.id = id;
        this.nome = nome;
        this.nivel = nivel;
    }
}
