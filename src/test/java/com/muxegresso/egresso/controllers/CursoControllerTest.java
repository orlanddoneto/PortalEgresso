package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.services.impl.CursoServiceImpl;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CursoControllerTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    CursoServiceImpl cursoService;

    @Test
    public void deveTestarFindAllDepoimento() throws Exception {

        Curso curso1 = new Curso();
        curso1.setId(1);
        curso1.setCoordenador(null);
        curso1.setCursoEgressos(null);
        curso1.setNome("orlando");
        curso1.setNivel("avancado");

        Curso curso2 = new Curso();
        curso2.setId(2);
        curso2.setCoordenador(null);
        curso2.setCursoEgressos(null);
        curso2.setNome("orlando");
        curso2.setNivel("avancado");

        // Simulação de uma página de depoimentos
        Page<Curso> cursoPage = new PageImpl<>(List.of(curso1, curso2));

        // Configuração do mock do serviço
        Mockito.when(cursoService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(cursoPage);

        // Configuração da requisição mock
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/curso") // Endpoint do controller
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

        Curso curso1 = new Curso();
        curso1.setId(1);
        curso1.setCoordenador(null);
        curso1.setCursoEgressos(null);
        curso1.setNome("orlando");
        curso1.setNivel("avancado");


        Mockito.when(cursoService.findById(curso1.getId()))
                .thenReturn(curso1);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/curso/1")
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se 200, OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve testar a criação de um curso")
    public void deveTestarCriarCurso() throws Exception {
        Curso curso1 = new Curso();
        curso1.setId(1);
        curso1.setCoordenador(null);
        curso1.setCursoEgressos(null);
        curso1.setNome("orlando");
        curso1.setNivel("avancado");

        Mockito.when(cursoService.save(Mockito.any(Curso.class))).thenReturn(curso1);

        String cursoJson = new ObjectMapper().writeValueAsString(curso1);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/v1/curso") //na URI
                        .accept ( MediaType.APPLICATION_JSON)
                        .contentType (MediaType.APPLICATION_JSON)
                        .content (cursoJson);
        mvc.perform(request)
                .andExpect( //teste em si
                        MockMvcResultMatchers.status().isCreated()); // espera-se 201,Created

    }

    @Test
    public void deveTestarDelecaoComSucesso() throws Exception {
        Integer idDeletar = 1;
        Curso curso = new Curso();
        curso.setId(idDeletar);

        Mockito.when(cursoService.delete(idDeletar)).thenReturn(curso); // Simula o retorno

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/curso/totalDelete/{id}", idDeletar) // Endpoint de deleção
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent()); // Espera-se status 204 (No Content)
    }

    @Test
    public void deveTestarUpdateComSucesso() throws Exception {
        // Dados para o teste
        Integer idAtualizar = 1;
        Curso curso1 = new Curso();
        curso1.setId(idAtualizar);
        curso1.setCoordenador(null);
        curso1.setCursoEgressos(null);
        curso1.setNome("orlando");
        curso1.setNivel("avancado");

        Curso curso2 = new Curso();
        curso2.setId(idAtualizar);
        curso2.setCoordenador(null);
        curso2.setCursoEgressos(null);
        curso2.setNome("orlando0o");
        curso2.setNivel("avancado0o");

        Mockito.when(cursoService.update(Mockito.any(Curso.class))).thenReturn("Depoimento atualizado com sucesso");

        // Conversão do objeto atualizado para JSON
        String requestJson = new ObjectMapper().writeValueAsString(curso2);

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/curso") // Endpoint de atualização
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtualizar)); // Verifica o ID

    }

}
