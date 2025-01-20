package com.muxegresso.egresso.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.services.impl.DepoimentoServiceImpl;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = DepoimentoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class DepoimentoControllerTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    DepoimentoServiceImpl depoimentoService;



    @Test
    public void deveTestarFindAllDepoimento() throws Exception {
        // Criação de objetos Depoimento simulados
        Depoimento depoimento1 = new Depoimento();
        depoimento1.setEgresso(null);
        depoimento1.setId(1);
        depoimento1.setData(Date.from(Instant.now()));
        depoimento1.setCreatedAt(LocalDateTime.now());

        Depoimento depoimento2 = new Depoimento();
        depoimento2.setEgresso(null);
        depoimento2.setId(2);
        depoimento2.setData(Date.from(Instant.now()));
        depoimento2.setCreatedAt(LocalDateTime.now());

        // Simulação de uma página de depoimentos
        Page<Depoimento> depoimentosPage = new PageImpl<>(List.of(depoimento1, depoimento2));

        // Configuração do mock do serviço
        Mockito.when(depoimentoService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(depoimentosPage);

        // Configuração da requisição mock
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/depoimento") // Endpoint do controller
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray()) // Verifica se o conteúdo é uma lista
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1)) // Verifica o ID do primeiro depoimento
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2)); // Verifica o ID do segundo depoimento
    }

    @Test
    public void deveTestarFindByID() throws Exception {

        Depoimento depoimento1 = new Depoimento();
        depoimento1.setEgresso(null);
        depoimento1.setId(1);
        depoimento1.setData(Date.from(Instant.now()));
        depoimento1.setCreatedAt(LocalDateTime.now());


        Mockito.when(depoimentoService.findById(depoimento1.getId()))
                .thenReturn(depoimento1);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/depoimento/1")
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se 200, OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve testar a criação de um depoimento")
    public void deveTestarCriarDepoimento() throws Exception {
        Depoimento depoimento1 = new Depoimento();
        depoimento1.setEgresso(null);
        depoimento1.setId(1);
        depoimento1.setData(Date.from(Instant.now()));
        depoimento1.setCreatedAt(LocalDateTime.now());

        Mockito.when(depoimentoService.save(Mockito.any(Depoimento.class))).thenReturn(depoimento1);

        String depoimentoJson = new ObjectMapper().writeValueAsString(depoimento1);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/v1/depoimento") //na URI
                        .accept ( MediaType.APPLICATION_JSON)
                        .contentType (MediaType.APPLICATION_JSON)
                        .content (depoimentoJson);
        mvc.perform(request)
                .andExpect( //teste em si
                        MockMvcResultMatchers.status().isCreated()); // espera-se 201,Created

    }

    @Test
    public void deveTestarDelecaoComSucesso() throws Exception {
        // Configuração do mock para o serviço
        Integer idDeletar = 1;
        Depoimento depoimento = new Depoimento();
        depoimento.setId(idDeletar);

        Mockito.when(depoimentoService.delete(idDeletar)).thenReturn(depoimento); // Simula o retorno

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/depoimento/totalDelete/{id}", idDeletar) // Endpoint de deleção
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent()); // Espera-se status 204 (No Content)
    }

    @Test
    public void deveTestarUpdateComSucesso() throws Exception {
        // Dados para o teste
        Integer idAtualizar = 1;
        Depoimento depoimentoExistente = new Depoimento();
        depoimentoExistente.setId(idAtualizar);
        depoimentoExistente.setData(Date.from(Instant.now()));
        depoimentoExistente.setCreatedAt(LocalDateTime.now());
        depoimentoExistente.setUpdatedAt(LocalDateTime.now());

        Depoimento depoimentoAtualizado = new Depoimento();
        depoimentoAtualizado.setId(idAtualizar);
        depoimentoAtualizado.setData(Date.from(Instant.now().plusSeconds(60))); // Nova data
        depoimentoAtualizado.setCreatedAt(depoimentoExistente.getCreatedAt());
        depoimentoAtualizado.setUpdatedAt(LocalDateTime.now());

        Mockito.when(depoimentoService.update(Mockito.any(Depoimento.class))).thenReturn(new ApiResponse(true,"Depoimento atualizado com sucesso"));

        // Conversão do objeto atualizado para JSON
        String requestJson = new ObjectMapper().writeValueAsString(depoimentoAtualizado);

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/depoimento") // Endpoint de atualização
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtualizar)); // Verifica o ID

    }







}
