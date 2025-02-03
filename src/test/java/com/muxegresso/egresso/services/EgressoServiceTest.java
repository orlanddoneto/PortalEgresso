package com.muxegresso.egresso.services;


import com.muxegresso.egresso.domain.ApiResponse;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest

@ExtendWith(MockitoExtension.class)
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
    @Transactional
    public void deveTestarSalvarEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);

        var requestEgressoDto = modelMapper.map(egresso, RequestEgressoDto.class);

        // Simula o comportamento do repository.save(...)
        when(egressoRepositoryMock.save(any(Egresso.class))).thenReturn(egresso);

        Egresso salvoEgresso = egressoServiceImpl.save(requestEgressoDto);

        Assertions.assertNotNull(salvoEgresso);
        Assertions.assertEquals(egresso.getCpf(), salvoEgresso.getCpf());
    }

    @Test
    @DisplayName("Verifica a busca de todos os egressos")
    public void deveTestarFindAllEgresso() {
    }

    @Test
    @DisplayName("Verifica a busca por ID de um egresso")
    void deveTestarFindByID() {
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(100);

        when(egressoRepositoryMock.save(any(Egresso.class)))
                .thenReturn(egresso);

        when(egressoServiceImpl.findById(100))
                .thenReturn(Optional.of(egresso));

        RequestEgressoDto egressoDto = modelMapper.map(egresso, RequestEgressoDto.class);

        Egresso salvoEgresso = egressoServiceImpl.save(egressoDto);

        Egresso searchEgresso = egressoServiceImpl.findById(salvoEgresso.getId()).orElse(null);

        Assertions.assertNotNull(salvoEgresso);
        Assertions.assertNotNull(searchEgresso);
        Assertions.assertEquals(salvoEgresso, searchEgresso);
    }
    @Test
    @DisplayName("Deve Gerar Erro ao Tentar Salvar um Nome vazio (teste unitário com Mockito)")
    void deveGerarErroAoTentarSalvarSemNome() {
        Egresso egresso = new Egresso();
        egresso.setEmail("mux@discente.ufma.br");
        egresso.setSenha("senha teste");

        RequestEgressoDto egressoDto = modelMapper.map(egresso, RequestEgressoDto.class);

        when(egressoRepositoryMock.save(any(Egresso.class)))
                .thenThrow(new RuntimeException("O campo nome deve ser preenchido!"));

        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> egressoServiceImpl.save(egressoDto),
                "O campo nome deve ser preenchido!"
        );
        Assertions.assertEquals("O campo nome deve ser preenchido!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve Gerar Erro ao Tentar Salvar um Email vazio")
    public void deveGerarErroAoTentarSalvarSemEmail() {
        Egresso egresso = new Egresso();
        egresso.setNome("mux");
        egresso.setSenha("senha teste");

        when(egressoRepositoryMock.save(any(Egresso.class)))
                .thenThrow(new RuntimeException("O campo e-mail deve ser preenchido!"));

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O campo e-mail deve ser preenchido!");
        Assertions.assertEquals("O campo e-mail deve ser preenchido!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve Gerar Erro ao Tentar Salvar uma senha vazia")
    public void deveGerarErroAoTentarSalvarSemSenha() {
        Egresso egresso = new Egresso();
        egresso.setNome("mux");
        egresso.setEmail("mux@discente.ufma.br");

        when(egressoRepositoryMock.save(any(Egresso.class)))
                .thenThrow(new RuntimeException("O campo senha deve ser preenchido!"));

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.save(modelMapper.map(egresso, RequestEgressoDto.class)), "O campo senha deve ser preenchido!");
        Assertions.assertEquals("O campo senha deve ser preenchido!", exception.getMessage());
    }


    @Test
    void deveGerarErroAoTentarAtualizarEmailJaCadastrado() {
        Egresso outroEgresso = new Egresso();
        outroEgresso.setId(9999);
        outroEgresso.setEmail("egresso2@discente.ufma.br");

        when(egressoRepositoryMock.findByEmail("egresso2@discente.ufma.br"))
                .thenReturn(Optional.of(outroEgresso));

        Egresso egressoSalvo = new Egresso();
        egressoSalvo.setId(2001);
        egressoSalvo.setEmail("egresso2@discente.ufma.br");

        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> egressoServiceImpl.updateEgresso(egressoSalvo, new RequestEgressoDto()),
                "O email já existe, tente outro por favor"
        );

        Assertions.assertTrue(exception.getMessage().contains("O email já existe"));
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
    public void deveGerarErroAoNaoEncontrarNenhumEgresso() {
        Query deleteCargo = entityManager.createQuery("Delete from Cargo");
        Query deleteDepoimento = entityManager.createQuery("Delete from Depoimento");
        Query deleteCursoEgresso = entityManager.createQuery("Delete from Curso_Egresso");
        Query deleteEgresso = entityManager.createQuery("Delete from Egresso");

        deleteCargo.executeUpdate();
        deleteDepoimento.executeUpdate();
        deleteCursoEgresso.executeUpdate();
        deleteEgresso.executeUpdate();

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> egressoServiceImpl.findAllEgresso(), "Recurso não Encontrado!");
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

        Egresso egresso = egressoRepositoryMock.findById(2001).orElse(null);
        assert egresso != null;

        egresso.setNome("mux atualização");
        egresso.setEmail("atualização@discente.ufma.br");
        egresso.setSenha("senha teste atualização");

        ApiResponse atualizado = egressoServiceImpl.updateEgresso(modelMapper.map(egresso, RequestEgressoDto.class));

        Assertions.assertNotNull(atualizado);
        Assertions.assertTrue(atualizado.isSucess());
        Assertions.assertEquals(egresso.toString(), atualizado.toString());
    }

    @Test
    @Transactional
    public void deveBuscarPorIdExistente() {
        Egresso egresso = egressoRepositoryMock.findById(2001).orElse(null);
        assert egresso != null;

        Egresso encontrado = egressoServiceImpl.findById(egresso.getId()).get();

        Assertions.assertEquals(egresso, encontrado);
    }

    /*
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
    */
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
        Egresso egresso = egressoRepositoryMock.findById(2001).orElse(null);
        assert egresso != null;

        egressoServiceImpl.delete(egresso.getId());
        Assertions.assertFalse(egressoRepositoryMock.existsById(egresso.getId()));
    }

    @Test
    @Transactional
    public void deveEfetuarLogin() {
        String resultado = egressoServiceImpl.efetuarLogin("egresso1@discente.ufma.br", "senha1");
        Assertions.assertEquals(resultado,"Login efetuado com sucesso.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdInexistente() {
        Integer idInexistente = 1234;

        Exception exception = Assertions.assertThrows(Exception.class, () -> egressoServiceImpl.findById(idInexistente), "ID não encontrado");
        Assertions.assertEquals("ID não encontrado", exception.getMessage());
    }

    /*
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
    */
}
