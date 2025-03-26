package com.muxegresso.egresso.domain;

import com.muxegresso.egresso.domain.enums.AproveStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_oportunidade")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Oportunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToMany(mappedBy = "oportunidades") // indica que a "dona" do relacionamento é Curso
    @JsonIgnore
    private Set<Curso> cursos = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "egresso_id")
    private Egresso egresso;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 255, nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AproveStatus homologadoStatus = AproveStatus.PENDING;

    @Transient
    public Integer getEgresso_id() {
        return egresso != null ? egresso.getId() : null;
    }
}
