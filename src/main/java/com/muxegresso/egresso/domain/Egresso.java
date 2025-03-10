package com.muxegresso.egresso.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muxegresso.egresso.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_egresso")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class Egresso extends RepresentationModel<Egresso> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "O nome do egresso deve ser informado!")
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank(message = "Digite a senha!")
    @Column(nullable = false, length = 255)
    @Size(min = 5, max = 50)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @NotBlank(message = "O email não foi informado!")
    @Column(unique = true, nullable = false, length = 255)
    @Email
    private String email;

    @NotBlank(message = "O CPF não foi informado!")
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

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
