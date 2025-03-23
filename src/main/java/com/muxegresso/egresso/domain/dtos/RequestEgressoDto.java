package com.muxegresso.egresso.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.muxegresso.egresso.domain.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestEgressoDto implements Serializable {

    public interface EgressoView{
        public static interface RegistrationPost{}
        public static interface EgressoUpdate {}
        public static interface PasswordUpdate {}
        public static interface ImageUpdate {}
    }

    @JsonView({EgressoView.EgressoUpdate.class, EgressoView.PasswordUpdate.class})
    private Integer id;

    @NotBlank(groups = {EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    @JsonView({EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    private String nome;

    @NotBlank(groups = {EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    @JsonView({EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    private String cpf;

    @NotBlank(groups = {EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    @JsonView({EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    private String email;

    private UserStatus status;

    @NotBlank(groups = {EgressoView.RegistrationPost.class, EgressoView.PasswordUpdate.class })
    @JsonView({EgressoView.RegistrationPost.class, EgressoView.PasswordUpdate.class,EgressoView.EgressoUpdate.class})
    private String senha;

    @NotBlank(groups = EgressoView.PasswordUpdate.class)
    @JsonView(EgressoView.PasswordUpdate.class)
    private String old_senha;

    @JsonView({EgressoView.RegistrationPost.class, EgressoView.EgressoUpdate.class})
    private String resumo;

    @NotBlank(groups = EgressoView.ImageUpdate.class)
    @JsonView({EgressoView.ImageUpdate.class})
    private String url_foto;

    private boolean homologado;


}