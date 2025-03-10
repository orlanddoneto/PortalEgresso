package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.CoordenadorRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorServiceTest {

    @Autowired
    CoordenadorService coordenadorService;

    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemEmail() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setSenha("mux123");
        coordenador.setEmail(null);
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador), "O email deve ser preenchido!");
        Assertions.assertTrue(exception.getMessage().contains("O email deve ser preenchido!"));

    }


    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemSenha() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setEmail("login teste");
        coordenador.setTipo("flip");
        coordenador.setId(null);
        coordenador.setSenha(null);

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador));
        Assertions.assertTrue(exception.getMessage().contains("A senha deve ser preenchida!"));
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarSemTipo() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setTipo(null);
        coordenador.setEmail("login teste");
        coordenador.setSenha("mux123");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.save(coordenador));
        Assertions.assertTrue(exception.getMessage().contains("O tipo deve ser preenchido!"));
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarEmailJaCadastrado() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("emailDeTestree@wwwf.coom");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        coordenadorService.save(coordenador);

        Coordenador coordenador2 = easyRandom.nextObject(Coordenador.class);
        coordenador2.setId(null);
        coordenador2.setEmail("emailDeTestree@wwwf.coom");
        coordenador2.setSenha("mux123");
        coordenador2.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () ->
                coordenadorService.save(coordenador2));
        System.out.println(exception.getMessage());
        Assertions.assertEquals("O e-mail informado já está em uso. Por favor, tente outro!", exception.getMessage());
    }


    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarSemSenha() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("emailDeTestree@wwwf.coom");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        var coordenadorUp = coordenadorService.save(coordenador);

        coordenadorUp .setSenha(null);
        coordenadorUp .setEmail("email testando");
        coordenadorUp .setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenadorUp));
        Assertions.assertTrue(exception.getMessage().contains("A senha deve ser preenchida!"));
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarSemTipo() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("emailAttSentipo@wwwf.coom");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        var coordenadorUp = coordenadorService.save(coordenador);

        coordenadorUp .setSenha(null);
        coordenadorUp .setTipo(null);

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenadorUp));
        Assertions.assertTrue(exception.getMessage().contains("O tipo deve ser preenchido!"));
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarEmailJaCadastrado() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("eeemaiiilTeste@wwwwf.com");
        coordenadorService.save(coordenador);

        Coordenador coordenador2 = easyRandom.nextObject(Coordenador.class);
        coordenador2.setId(null);
        coordenador2.setEmail("outroeeemaiiilTeste@wwwwf.com");
        coordenadorService.save(coordenador2);

        Coordenador coordenador3 = easyRandom.nextObject(Coordenador.class);
        coordenador3.setId(coordenador.getId());
        coordenador3.setEmail("outroeeemaiiilTeste@wwwwf.com");


        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador3));
        System.out.println(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains("O e-mail informado já está em uso. Por favor, tente outro!"));
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCoordenadorNaoExistente() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(2001);
        coordenador.setEmail("email@mailzinho.com");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.update(coordenador));
        Assertions.assertEquals("ID não presente no sistema.", exception.getMessage());
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdVazio() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.findById(null));
        Assertions.assertTrue(exception.getMessage().contains("ID não pode ser nulo."));
    }

    @Test
    @Transactional
    public void deveGerarErroAoBuscarPorIdInexistente() {
        Integer idInexistente = 1812;

        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.findById(idInexistente));
        Assertions.assertTrue(exception.getMessage().contains("ID não presente no sistema."));
    }

    @Test
    @Transactional
    public void deveGerarErroQuandoLoginIncorreto() {
        Exception exception = Assertions.assertThrows(Exception.class,
                () -> coordenadorService.efetuarLogin("loginIncorreto", "senhaIncorreta"));
        Assertions.assertTrue(exception.getMessage().contains("Email não existente na base de dados."));
    }

    @Test
    @Transactional
    public void deveGerarErroAoCadastrarCursoComCoordenadorInvalido() {
        Coordenador coordenador = new Coordenador();
        coordenador.setEmail("email@invalido.com");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> coordenadorService.addCurso(coordenador.getEmail(), "Ciência da Computação", "nivel teste"));

        Assertions.assertTrue(exception.getMessage().contains("E-mail do coordenador é inexistente!"));
    }

    /*
    @Test
    @Transactional
    public void deveGerarErroAoTentarAssociarCargoInvalidoAEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        egresso.setEmail("email@emailzinho.com");
        egresso.setSenha("mux123");
        egressoRepository.save(egresso);

        Cargo cargo = easyRandom.nextObject(Cargo.class);
        cargo.setId(null);
        cargo.setEgresso(egresso);
        Exception exception = Assertions.assertThrows(Exception.class, () -> coordenadorService.addCargo(egresso, cargo), "Cargo inválido");
        Assertions.assertEquals("Cargo inválido", exception.getMessage());
    }*/

    @Test
    @Transactional
    public void deveCadastrarCurso() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("emaiil@mailzinho.com");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");
        coordenadorService.save(coordenador);
        Curso cursoSalvo = coordenadorService.addCurso(coordenador.getEmail(), "Ciência da Computação", "nivel teste");

        Assertions.assertEquals(coordenador, cursoSalvo.getCoordenador());
        Assertions.assertEquals("Ciência da Computação", cursoSalvo.getNome());
        Assertions.assertEquals("nivel teste", cursoSalvo.getNivel());
    }

    @Test
    @Transactional
    public void deveAssociarDepoimentoAEgresso() {
        EasyRandom easyRandom = new EasyRandom();
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        egresso.setEmail("emaail@emailzinho.com");
        egresso.setSenha("mux123");
        egressoRepository.save(egresso);

        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        Depoimento depoimentoSalvo = coordenadorService.lincarDepoimentoEgresso(egresso, depoimento);

        Assertions.assertEquals(depoimento, depoimentoSalvo);
    }

    @Test
    @Transactional
    public void deveVerificarSalvarOCoordenador() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("email@coordenador.com");
        coordenador.setSenha("mux123");
        coordenador.setTipo("flip");
        Coordenador salvo = coordenadorService.save(coordenador);

        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(coordenador.getEmail(), salvo.getEmail());
        Assertions.assertEquals(coordenador.getSenha(), salvo.getSenha());
        Assertions.assertEquals(coordenador.getTipo(), salvo.getTipo());

    }

    @Test
    @Transactional
    public void deveVerificarAtualizarOCoordenador() {
        // Cria uma instância aleatória do coordenador e seta valores válidos
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("coordenador@example.com"); // email válido
        coordenador.setSenha("SenhaOriginal");
        coordenador.setNome("Nome Original");

        // Salva o coordenador para gerar o ID e persistir no banco
        Coordenador savedCoordenador = coordenadorService.save(coordenador);

        // Modifica alguns campos do coordenador salvo para simular uma atualização
        savedCoordenador.setNome("Novo Nome");
        savedCoordenador.setSenha("NovaSenha");
        // Mantemos o mesmo email para não disparar a verificação de duplicidade

        // Chama o método de update e captura o resultado
        String resultado = coordenadorService.update(savedCoordenador);

        // Verifica se a mensagem retornada é a esperada
        Assertions.assertEquals("Coordenador atualizado com sucesso", resultado);

        // Recupera o coordenador atualizado e verifica se as alterações foram persistidas
        Coordenador updatedCoordenador = coordenadorService.findById(savedCoordenador.getId());
        Assertions.assertEquals("Novo Nome", updatedCoordenador.getNome());
        Assertions.assertEquals("NovaSenha", updatedCoordenador.getSenha());
        Assertions.assertEquals("coordenador@example.com", updatedCoordenador.getEmail());
    }


    @Test
    @Transactional
    public void deveBuscarPorIdExistente() {
        // Cria uma instância aleatória do coordenador e seta valores válidos
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("coordenador@example2.com"); // email válido
        coordenador.setSenha("SenhaOriginal");
        coordenador.setNome("Nome Original");
        Coordenador salvo = coordenadorService.save(coordenador);

        Coordenador encontrado = coordenadorService.findById(salvo.getId());

        Assertions.assertEquals(salvo, encontrado);
    }

    @Test
    @Transactional
    public void deveListarTodosOsCoordenadores() {
        // Inserir alguns coordenadores para o teste
        EasyRandom easyRandom = new EasyRandom();
        int totalCoordenadores = 5;
        for (int i = 0; i < totalCoordenadores; i++) {
            Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
            coordenador.setId(null);
            // Garante um email único para evitar problemas de duplicidade
            coordenador.setEmail("coordenador" + i + "@exemplo.com");
            coordenadorService.save(coordenador);
        }

        // Cria um pageable (página 0 com até 10 registros)
        Pageable pageable = PageRequest.of(0, 10);

        // Chama o método de listagem
        Page<Coordenador> page = coordenadorService.findAll(pageable);

        // Verifica se a página não está vazia e se contém pelo menos o total esperado de registros
        Assertions.assertNotNull(page);
        Assertions.assertFalse(page.isEmpty());
        Assertions.assertTrue(page.getTotalElements() >= totalCoordenadores);

        // Opcional: Imprime os dados para conferência
        page.getContent().forEach(System.out::println);
    }


    @Test
    @Transactional
    public void deveDeletarCoordenador() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("coordenador@example10.com"); // email válido
        coordenador.setSenha("SenhaOriginal");
        coordenador.setNome("Nome Original");
        Coordenador salvo = coordenadorService.save(coordenador);

        coordenadorService.delete(salvo.getId());
        Assertions.assertFalse(coordenadorRepository.existsById(salvo.getId()));
    }

    @Test
    @Transactional
    public void deveEfetuarLogin() {
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("coordenador@example11.com"); // email válido
        coordenador.setSenha("SenhaOriginal");
        coordenador.setNome("Nome Original");
        Coordenador salvo = coordenadorService.save(coordenador);

        boolean resultado = coordenadorService.efetuarLogin("coordenador@example11.com", "SenhaOriginal");
        Assertions.assertTrue(resultado);
    }

}