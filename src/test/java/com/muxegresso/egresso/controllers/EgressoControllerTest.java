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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = EgressoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EgressoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    EgressoServiceImpl egressoServiceImpl;


    @Test
    @DisplayName("Deve testar o salvamento de um egresso")
    public void deveTestarSalvarEgresso() throws Exception {
        //Cria DTO
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setNome("NomeTeste");
        requestEgressoDto.setCpf("2039413402");
        requestEgressoDto.setEmail("email@teste.com");
        requestEgressoDto.setSenha("1234");


        //Cria Classe normal
        Egresso egresso = new Egresso();
        egresso.setNome("NomeTeste");
        egresso.setCpf("2039413402");
        egresso.setEmail("email2@teste.com");
        egresso.setSenha("1234");

        Mockito.when(egressoServiceImpl.save(Mockito.any(RequestEgressoDto.class))).thenReturn(egresso);

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
    public void deveTestarBuscarPorCPF() throws Exception {
        // Criação do objeto Egresso
        Egresso egresso = new Egresso();
        egresso.setNome("NomeTeste");
        egresso.setCpf("2039413402");
        egresso.setEmail("email3@teste.com");
        egresso.setSenha("1234");


        Mockito.when(egressoServiceImpl.getEgressoByCpf("2039413402"))
                .thenReturn(Optional.of(egresso));


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/egresso/2039413402") // Incluindo o CPF na URI
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se 200, OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("NomeTeste")) // Verifica o nome
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("2039413402")) // Verifica o CPF
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("efiwefiwe")); // Verifica o email
    }

    @Test
    public void deveTestarGetAllEgresso() throws Exception {
        // Criação de  egresso
        Egresso egresso1 = new Egresso();
        egresso1.setNome("Egresso 1");
        egresso1.setCpf("12345678901");
        egresso1.setEmail("egresso1@example.com");
        egresso1.setSenha("senha1");

        Egresso egresso2 = new Egresso();
        egresso2.setNome("Egresso 2");
        egresso2.setCpf("98765432100");
        egresso2.setEmail("egresso2@example.com");
        egresso2.setSenha("senha2");


        Page<Egresso> egressosPage = new PageImpl<>(List.of(egresso1, egresso2));


        Mockito.when(egressoServiceImpl.findAll(Mockito.any(), Mockito.any(Pageable.class)))
                .thenReturn(egressosPage);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/egresso") // Endpoint para listar todos os egressos
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nome").value("Egresso 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nome").value("Egresso 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].links[0].href").value(
                        "http://localhost/v1/egresso/12345678901")); // Verifica o link gerado para o primeiro egresso
    }

    @Test
    public void deveTestarUpdateEgresso() throws Exception {
        // Simulando o egresso existente no banco
        Egresso egresso = new Egresso();
        egresso.setId(1);
        egresso.setNome("Egresso Antigo");
        egresso.setCpf("12345678901");
        egresso.setEmail("antigo@example.com");
        egresso.setSenha("senhaAntiga");

        // Dados do DTO de atualização
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setId(1);
        requestEgressoDto.setNome("Egresso Atualizado");
        requestEgressoDto.setCpf("12345678901");
        requestEgressoDto.setEmail("atualizado@example.com");

        Mockito.when(egressoServiceImpl.findById(1)).thenReturn(Optional.of(egresso));

        Mockito.doNothing().when(egressoServiceImpl).updateEgresso(Mockito.any(Egresso.class), Mockito.any(RequestEgressoDto.class));

        String requestJson = new ObjectMapper().writeValueAsString(requestEgressoDto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/egresso") // Endpoint de atualização
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true)) // Verifica sucesso da operação
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("id: 1- Egresso Atualizado !")); // Verifica mensagem de sucesso
    }

    @Test
    public void deveRetornarNotFoundAoTentarAtualizarEgressoInexistente() throws Exception {
        // Dados do DTO de atualização
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setId(99); // ID inexistente
        requestEgressoDto.setNome("Egresso Não Encontrado");
        requestEgressoDto.setCpf("00000000000");
        requestEgressoDto.setEmail("naoencontrado@example.com");


        Mockito.when(egressoServiceImpl.findById(99)).thenReturn(Optional.empty());


        String requestJson = new ObjectMapper().writeValueAsString(requestEgressoDto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/egresso") // Endpoint de atualização
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound()) // Espera-se status 404
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false)) // Verifica falha da operação
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Egresso não encontrado")); // Verifica mensagem de erro
    }

    @Test
    public void deveTestarUpdatePasswordComSucesso() throws Exception {
        // Configurando o DTO para a requisição
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setId(1); // ID válido
        requestEgressoDto.setSenha("novaSenha123");

        Mockito.when(egressoServiceImpl.existsById(1)).thenReturn(true);

        String requestJson = new ObjectMapper().writeValueAsString(requestEgressoDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/v1/egresso/updatepass") // Endpoint para atualização de senha
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Senha atualizada"));
    }

    @Test
    public void deveRetornarErroAoTentarAtualizarSenhaComIdInexistente() throws Exception {
        RequestEgressoDto requestEgressoDto = new RequestEgressoDto();
        requestEgressoDto.setId(99); // ID inexistente
        requestEgressoDto.setSenha("novaSenha123");

        Mockito.when(egressoServiceImpl.existsById(99)).thenReturn(false);

        String requestJson = new ObjectMapper().writeValueAsString(requestEgressoDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/v1/egresso/updatepass") // Endpoint para atualização de senha
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict()) // Espera-se status 409
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ID não encontrado"));
    }








}
