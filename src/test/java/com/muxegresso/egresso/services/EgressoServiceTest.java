package com.muxegresso.egresso.services;


import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.RequestEgressoDto;
import com.muxegresso.egresso.repositories.CargoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import com.muxegresso.egresso.services.impl.EgressoServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import java.util.List;

@SpringBootTest


public class EgressoServiceTest {

    @Mock
    private EgressoRepository egressoRepositoryMock; // Mock do repositório

    @InjectMocks
    private EgressoServiceImpl egressoServiceImpl; // Serviço com o mock injetado

    ModelMapper modelMapper = new ModelMapper();

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CargoRepository cargoRepository;

    @Test
    @DisplayName("Verifica o salvamento de um egresso")
    public void deveTestarSalvarEgresso(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);

        var egressoEntity = modelMapper.map(egresso, RequestEgressoDto.class);

        Egresso salvoEgresso = egressoServiceImpl.save(egressoEntity);

    public Egresso criaEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        return easyRandom.nextObject(Egresso.class);
    }

    @Test
    @DisplayName("Verifica a busca de todos os egressos")
    public void deveTestarFindAllEgresso() {
    }

    @Test
    @DisplayName("Verifica a busca por ID de um egresso")
    public void deveTestarFindByID(){
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);

        var egressoEntity = modelMapper.map(egresso, RequestEgressoDto.class);
        Egresso salvoEgresso = egressoServiceImpl.save(egressoEntity);

        Integer id = salvoEgresso.getId();
        Egresso searchEgresso = egressoServiceImpl.findById(id).get();
        Assertions.assertEquals(salvoEgresso,searchEgresso);

    }
    @Test
    @Transactional
    @DisplayName("Deve Gerar Erro ao Tentar Salvar um Nome vazio")
    public void deveGerarErroAoTentarSalvarSemNome() {
        Egresso egresso = new Egresso();
        egresso.setEmail("mux@discente.ufma.br");
        egresso.setSenha("senha teste");

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O campo nome deve ser preenchido!");
        Assertions.assertEquals("O campo nome deve ser preenchido!", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Deve Gerar Erro ao Tentar Salvar um Email vazio")
    public void deveGerarErroAoTentarSalvarSemEmail() {
        Egresso egresso = new Egresso();
        egresso.setNome("mux");
        egresso.setSenha("senha teste");

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O campo email deve ser preenchido!");
        Assertions.assertEquals("O campo email deve ser preenchido!", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Deve Gerar Erro ao Tentar Salvar uma senha vazia")
    public void deveGerarErroAoTentarSalvarSemSenha() {
        Egresso egresso = new Egresso();
        egresso.setNome("mux");
        egresso.setEmail("mux@discente.ufma.br");

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O campo senha deve ser preenchido!");
        Assertions.assertEquals("O campo senha deve ser preenchido!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarEmailJaCadastrado() {

        Egresso egresso = new Egresso();
        egresso.setNome("mux1");
        egresso.setEmail("egresso1@discente.ufma.br");
        egresso.setSenha("senha teste");

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O email já existe, tente outro por favor");
        Assertions.assertEquals("O email já existe, tente outro por favor", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarEmailJaCadastrado() {
        Egresso egressoSalvo = entityManager
                .createQuery("SELECT e FROM Egresso e WHERE e.id = :id", Egresso.class)
                .setParameter("id", 2001)
                .getSingleResult();

        egressoSalvo.setEmail("egresso2@discente.ufma.br");
        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.update(egressoSalvo), "O email já existe, tente outro por favor");
        Assertions.assertEquals("O email já existe, tente outro por favor", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarUmEgressoNaoExistente() {
        Egresso egresso = new Egresso();
        egresso.setId(2001);
        egresso.setNome("mux");
        egresso.setEmail("mux@discente.ufma.br");
        egresso.setSenha("senha teste");

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.updateEgresso(modelMapper.map(egresso, RequestEgressoDto.class)), "ID não encontrado");
        Assertions.assertEquals("ID não encontrado", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdVazio() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.findById(null), "ID inválido");
        Assertions.assertEquals("ID inválido", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoNaoEncontrarNenhumEgresso(){
        Query deleteCargo = entityManager.createQuery("Delete from Cargo");
        Query deleteDepoimento = entityManager.createQuery("Delete from Depoimento");
        Query deleteCursoEgresso = entityManager.createQuery("Delete from Curso_Egresso");
        Query deleteEgresso = entityManager.createQuery("Delete from Egresso");

        deleteCargo.executeUpdate();
        deleteDepoimento.executeUpdate();
        deleteCursoEgresso.executeUpdate();
        deleteEgresso.executeUpdate();

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> egressoServiceImpl.findAllEgresso() , "Recurso não Encontrado!");
        Assertions.assertEquals("Recurso não Encontrado!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroQuandoLoginIncorreto() {
        Exception exception = Assertions.assertThrows(Exception.class,
                () -> egressoServiceImpl.efetuarLogin("loginIncorreto@example.com", "senhaIncorreta"),
                "Erro de autenticação");
        Assertions.assertEquals("Erro de autenticação", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveVerificarSalvarOEgresso() {
        Egresso egresso = new Egresso();
        egresso.setNome("mux");
        egresso.setEmail("save@discente.ufma.br");
        egresso.setSenha("senha teste");

        Egresso salvo = egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class));

        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(egresso.getNome(), salvo.getNome());
        Assertions.assertEquals(egresso.getEmail(), salvo.getEmail());
        Assertions.assertEquals(egresso.getSenha(), salvo.getSenha());

    }

    @Test
    @Transactional
    public void deveVerificarAtualizarOEgresso() {

        Egresso egresso = egressoRepository.findById(2001).orElse(null);
        assert egresso != null;

        egresso.setNome("mux atualização");
        egresso.setEmail("atualização@discente.ufma.br");
        egresso.setSenha("senha teste atualização");

        Egresso atualizado = egressoServiceImpl.update(egresso);

        Assertions.assertNotNull(atualizado);
        Assertions.assertEquals(egresso.getNome(), atualizado.getNome());
        Assertions.assertEquals(egresso.getEmail(), atualizado.getEmail());
        Assertions.assertEquals(egresso.getSenha(), atualizado.getSenha());
    }

    @Test
    @Transactional
    public void deveBuscarPorIdExistente() {
        Egresso egresso = egressoRepository.findById(2001).orElse(null);
        assert egresso != null;

        Egresso encontrado = egressoServiceImpl.findById(egresso.getId()).get();

        Assertions.assertEquals(egresso, encontrado);
    }

    @Test
    @Transactional
    public void deveBuscarPorCargoExistente() {

        Cargo cargo = cargoRepository.findById(241).orElse(null);
        assert cargo != null;

        List<Egresso> egressos = egressoServiceImpl.findALLByCargo(cargo);
        List<Egresso> egressosEsperados = entityManager
                .createQuery("SELECT e FROM Egresso e JOIN Cargo c ON c.egresso = e WHERE c.id = :id", Egresso.class)
                .setParameter("id", 241)
                .getResultList();

        Assertions.assertEquals(egressos, egressosEsperados);
    }

    @Test
    @Transactional
    public void deveListarTodosOsEgressos() {

        List<Egresso> egressos = egressoServiceImpl.findAllEgresso();
        List<Egresso> egressosEsperados = entityManager
                .createQuery("SELECT e FROM Egresso e", Egresso.class)
                .getResultList();

        Assertions.assertEquals(egressos, egressosEsperados);
    }

    @Test
    @Transactional
    public void deveDeletarEgresso() {
        Egresso egresso = egressoRepository.findById(2001).orElse(null);
        assert egresso != null;

        egressoServiceImpl.delete(egresso.getId());
        Assertions.assertFalse(egressoRepository.existsById(egresso.getId()));
    }

    @Test
    @Transactional
    public void deveEfetuarLogin() {
        boolean resultado = egressoServiceImpl.efetuarLogin("egresso1@discente.ufma.br", "senha1");
        Assertions.assertTrue(resultado);
    }
    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdInexistente() {
        Integer idInexistente = 1234;

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.findById(idInexistente), "ID não encontrado");
        Assertions.assertEquals("ID não encontrado", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorCursoInexistente() {
        Curso curso = new Curso();

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> egressoServiceImpl.findByCurso(curso), "Recurso não Encontrado!");
        Assertions.assertEquals("Recurso não Encontrado!", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorCargoInexistente() {
        Cargo cargo = new Cargo();
        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> egressoServiceImpl.findByCargo(cargo), "Recurso não Encontrado!");
        Assertions.assertEquals("Recurso não Encontrado!", exception.getMessage());
    }

}
