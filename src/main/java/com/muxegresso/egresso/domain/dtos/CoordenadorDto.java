package com.muxegresso.egresso.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoordenadorDto implements Serializable {

    // Definindo views para controle de serialização/deserialização (ajuste conforme necessidade)
    public interface CoordenadorView {
        interface Create {}
        interface Update {}
        interface Login {}
    }

    @JsonView({CoordenadorView.Update.class, CoordenadorView.Login.class})
    private Integer id;

    @NotBlank(groups = {CoordenadorView.Create.class, CoordenadorView.Update.class})
    @JsonView({CoordenadorView.Create.class, CoordenadorView.Update.class})
    private String nome;

    @NotBlank(groups = {CoordenadorView.Create.class, CoordenadorView.Update.class, CoordenadorView.Login.class})
    @Email(groups = {CoordenadorView.Create.class, CoordenadorView.Update.class})
    @JsonView({CoordenadorView.Create.class, CoordenadorView.Update.class, CoordenadorView.Login.class})
    private String email;

    @NotBlank(groups = {CoordenadorView.Create.class, CoordenadorView.Login.class})
    @JsonView({CoordenadorView.Create.class, CoordenadorView.Login.class})
    private String senha;

    @NotBlank(groups = {CoordenadorView.Create.class, CoordenadorView.Update.class})
    @JsonView({CoordenadorView.Create.class, CoordenadorView.Update.class})
    private String tipo;
}
