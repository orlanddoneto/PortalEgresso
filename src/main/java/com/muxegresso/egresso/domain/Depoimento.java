package com.muxegresso.egresso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.muxegresso.egresso.domain.enums.AproveStatus;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import com.muxegresso.egresso.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_depoimento")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Depoimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_egresso")
    private Egresso egresso;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(nullable = false, length = 255)
    private Date data;

    @Transient
    public Integer getId_Egresso() {
        return egresso != null ? egresso.getId() : null;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AproveStatus homologadoStatus = AproveStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
