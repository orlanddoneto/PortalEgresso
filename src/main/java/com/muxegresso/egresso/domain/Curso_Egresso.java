package com.muxegresso.egresso.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_curso_egresso")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Curso_Egresso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_egresso")
    private Egresso egresso;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @Column(nullable = false, length = 255)
    private Integer ano_inicio;
    @Column(nullable = false, length = 255)
    private Integer ano_fim;

}
