package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.services.impl.CoordenadorServiceImpl;
import com.muxegresso.egresso.services.impl.CursoServiceImpl;
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

@WebMvcTest(controllers = CoordenadorController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CoordenadorControllerTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    CoordenadorServiceImpl coordenadorService;

    @Test
    public void deveTestarFindAllCoordenador() throws Exception {

        Coordenador coordenador1 = new Coordenador();
        coordenador1.setId(1);
        coordenador1.setTipo("X");
        coordenador1.setNome("nomeTeste");
        coordenador1.setLogin("loginTeste");

        Coordenador coordenador2 = new Coordenador();
        coordenador2.setId(2);
        coordenador2.setTipo("X");
        coordenador2.setNome("nomeTeste");
        coordenador2.setLogin("loginTeste");


        Page<Coordenador> coordenadorPage = new PageImpl<>(List.of(coordenador1, coordenador2));

        // Configuração do mock do serviço
        Mockito.when(coordenadorService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(coordenadorPage);

        // Configuração da requisição mock
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/coordenador") // Endpoint do controller
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray()) // Verifica se o conteúdo é uma lista
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2));
    }

    @Test
    public void deveTestarFindByID() throws Exception {

        Coordenador coordenador1 = new Coordenador();
        coordenador1.setId(1);
        coordenador1.setTipo("X");
        coordenador1.setNome("nomeTeste");
        coordenador1.setLogin("loginTeste");


        Mockito.when(coordenadorService.findById(coordenador1.getId()))
                .thenReturn(coordenador1);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/coordenador/1")
                .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera-se 200, OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve testar a criação de um Coordenador")
    public void deveTestarCriarCoordenador() throws Exception {
        Coordenador coordenador1 = new Coordenador();
        coordenador1.setId(1);
        coordenador1.setTipo("X");
        coordenador1.setNome("nomeTeste");
        coordenador1.setLogin("loginTeste");
        Mockito.when(coordenadorService.save(Mockito.any(Coordenador.class))).thenReturn(coordenador1);

        String coordenadorJson = new ObjectMapper().writeValueAsString(coordenador1);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/v1/coordenador") //na URI
                        .accept ( MediaType.APPLICATION_JSON)
                        .contentType (MediaType.APPLICATION_JSON)
                        .content (coordenadorJson);
        mvc.perform(request)
                .andExpect( //teste em si
                        MockMvcResultMatchers.status().isCreated()); // espera-se 201,Created

    }

    @Test
    public void deveTestarDelecaoComSucesso() throws Exception {
        Integer idDeletar = 1;
        Coordenador coordenador = new Coordenador();
        coordenador.setId(idDeletar);

        Mockito.when(coordenadorService.delete(idDeletar)).thenReturn(coordenador); // Simula o retorno

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/coordenador/totalDelete/{id}", idDeletar) // Endpoint de deleção
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent()); // Espera-se status 204 (No Content)
    }

    @Test
    public void deveTestarUpdateComSucesso() throws Exception {
        Coordenador coordenador1 = new Coordenador();
        coordenador1.setId(1);
        coordenador1.setTipo("X");
        coordenador1.setNome("nomeTeste");
        coordenador1.setLogin("loginTeste");

        Coordenador coordenador2 = new Coordenador();
        coordenador2.setId(1);
        coordenador2.setTipo("X");
        coordenador2.setNome("nomeTeeeeeste");
        coordenador2.setLogin("loginTeeeste");

        Mockito.when(coordenadorService.update(Mockito.any(Coordenador.class))).thenReturn("Coordenador atualizado com sucesso");

        // Conversão do objeto atualizado para JSON
        String requestJson = new ObjectMapper().writeValueAsString(coordenador2);

        // Configuração da requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/coordenador") // Endpoint de atualização
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        // Execução da requisição e verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica o status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1)); // Verifica o ID

    }


}
