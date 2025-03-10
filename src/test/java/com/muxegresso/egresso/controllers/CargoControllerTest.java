package com.muxegresso.egresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.services.CargoService;
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

@WebMvcTest(controllers = CargoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CargoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CargoService cargoService;

    @Test
    public void deveTestarFindAllCargo() throws Exception {
        // Cria dois cargos para simulação
        Cargo cargo1 = new Cargo();
        cargo1.setId(1);
        cargo1.setDescricao("Cargo 1");
        cargo1.setLocal("Local 1");
        cargo1.setAno_inicio(2010);
        cargo1.setAno_fim(2012);
        cargo1.setEgresso(null);

        Cargo cargo2 = new Cargo();
        cargo2.setId(2);
        cargo2.setDescricao("Cargo 2");
        cargo2.setLocal("Local 2");
        cargo2.setAno_inicio(2013);
        cargo2.setAno_fim(2015);
        cargo2.setEgresso(null);

        Page<Cargo> cargoPage = new PageImpl<>(List.of(cargo1, cargo2));

        // Configura o comportamento do service mockado
        Mockito.when(cargoService.findAll(Mockito.any(Pageable.class))).thenReturn(cargoPage);

        // Configura e executa a requisição GET
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/cargo")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2));
    }

    @Test
    public void deveTestarFindByIdCargo() throws Exception {
        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setDescricao("Cargo Teste");
        cargo.setLocal("Local Teste");
        cargo.setAno_inicio(2011);
        cargo.setAno_fim(2014);
        cargo.setEgresso(null);

        Mockito.when(cargoService.findById(1)).thenReturn(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/cargo/1")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Cargo Teste"));
    }

    @Test
    public void deveTestarCriarCargo() throws Exception {
        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setDescricao("Cargo Novo");
        cargo.setLocal("Local Novo");
        cargo.setAno_inicio(2020);
        cargo.setAno_fim(2022);
        cargo.setEgresso(null);

        Mockito.when(cargoService.save(Mockito.any(Cargo.class))).thenReturn(cargo);

        String cargoJson = new ObjectMapper().writeValueAsString(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/v1/cargo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cargoJson);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deveTestarUpdateCargo() throws Exception {
        // Simula um cargo existente
        Cargo cargoExistente = new Cargo();
        cargoExistente.setId(1);
        cargoExistente.setDescricao("Cargo Antigo");
        cargoExistente.setLocal("Local Antigo");
        cargoExistente.setAno_inicio(2015);
        cargoExistente.setAno_fim(2018);
        cargoExistente.setEgresso(null);

        // Simula os dados atualizados
        Cargo cargoAtualizado = new Cargo();
        cargoAtualizado.setId(1);
        cargoAtualizado.setDescricao("Cargo Atualizado");
        cargoAtualizado.setLocal("Local Atualizado");
        cargoAtualizado.setAno_inicio(2016);
        cargoAtualizado.setAno_fim(2019);
        cargoAtualizado.setEgresso(null);

        // Configura o comportamento do service
        Mockito.when(cargoService.update(Mockito.any(Cargo.class)))
                .thenReturn("Cargo atualizado com sucesso");
        Mockito.when(cargoService.findById(1)).thenReturn(cargoAtualizado);

        String cargoJson = new ObjectMapper().writeValueAsString(cargoAtualizado);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/v1/cargo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cargoJson);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Cargo Atualizado"));
    }

    @Test
    public void deveTestarDeleteCargo() throws Exception {
        Integer idDeletar = 1;
        Cargo cargo = new Cargo();
        cargo.setId(idDeletar);
        cargo.setDescricao("Cargo a ser deletado");
        cargo.setLocal("Local");
        cargo.setAno_inicio(2000);
        cargo.setAno_fim(2005);
        cargo.setEgresso(null);

        Mockito.when(cargoService.delete(idDeletar)).thenReturn(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/v1/cargo/{id}", idDeletar)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
