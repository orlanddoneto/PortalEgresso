package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.services.EgressoService;
import com.muxegresso.egresso.services.impl.EgressoServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(controllers = EgressoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EgressoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    EgressoServiceImpl egressoService;

    public Egresso criaEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        return easyRandom.nextObject(Egresso.class);
    }

    @Test
    @DisplayName("Deve testar o salvamento de um egresso")
    public void deveTestarSalvarEgresso() throws Exception {
        //Cria DTO
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setNome("NomeTeste");
        requestEgressoDto.setCpf("2039413402");
        requestEgressoDto.setEmail("efiwefiwe");
        requestEgressoDto.setSenha("1234");

        //Cria Classe normal
        Egresso egresso = new Egresso();
        egresso.setNome("NomeTeste");
        egresso.setCpf("2039413402");
        egresso.setEmail("efiwefiwe");
        egresso.setSenha("1234");

        Mockito.when(egressoService.save(Mockito.any(RequestEgressoDto.class))).thenReturn(egresso);

        String egressoDtoJson = new ObjectMapper().writeValueAsString(requestEgressoDto);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/v1/egresso") //na URI
                        .accept ( MediaType.APPLICATION_JSON)
                        .contentType (MediaType.APPLICATION_JSON)
                        .content (egressoDtoJson); //mandando o DTO
//ação e verificação
        mvc.perform(request)
                .andExpect( //teste em si
                        MockMvcResultMatchers.status().isCreated()); // espera-se 201,Created

    }

    @Test
    public void deveTestarBuscarPorCPF() throws JsonProcessingException {
        //Cria Classe normal
        Egresso egresso = new Egresso();
        egresso.setNome("NomeTeste");
        egresso.setCpf("2039413402");
        egresso.setEmail("efiwefiwe");
        egresso.setSenha("1234");

        Mockito.when(egressoService.getEgressoByCpf(Mockito.anyString())).thenReturn(Optional.of(egresso));

        String egressoDtoJson = new ObjectMapper().writeValueAsString(egresso);
    /*
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/v1/egresso") //na URI
                        .accept ( MediaType.APPLICATION_JSON)
                        .contentType (MediaType.APPLICATION_JSON)
                        .content (egressoDtoJson); //mandando o DTO
//ação e verificação
        mvc.perform(request)
                .andExpect( //teste em si
                        MockMvcResultMatchers.status().isCreated()); // espera-se 201,Created
*/

    }
}
