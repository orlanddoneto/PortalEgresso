package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.Depoimento;
import com.muxegresso.egresso.repositories.DepoimentoRepository;
import com.muxegresso.egresso.services.impl.DepoimentoServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class DepoimentoServiceTest {

    @Autowired
    private DepoimentoServiceImpl depoimentoService;

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    @Transactional
    public void deveSalvarDepoimento() {
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        depoimento.setEgresso(null);  // Para evitar FK inválida
        depoimento.setTexto("Teste de Depoimento");
        depoimento.setData(new Date());
        Depoimento salvo = depoimentoService.save(depoimento);
        Assertions.assertNotNull(salvo.getId(), "O ID deve ser gerado após salvar");
        Assertions.assertNotNull(salvo.getCreatedAt(), "CreatedAt deve ser preenchido");
        Assertions.assertNotNull(salvo.getUpdatedAt(), "UpdatedAt deve ser preenchido");
    }

    @Test
    @Transactional
    public void deveEncontrarDepoimentoPorId() {
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        depoimento.setEgresso(null);
        depoimento.setTexto("Depoimento para busca");
        depoimento.setData(new Date());
        Depoimento salvo = depoimentoService.save(depoimento);
        Depoimento encontrado = depoimentoService.findById(salvo.getId());
        Assertions.assertEquals(salvo.getId(), encontrado.getId(), "Os IDs devem ser iguais");
    }

    @Test
    @Transactional
    public void deveListarTodosOsDepoimentos() {
        int totalDepoimentos = 3;
        for (int i = 0; i < totalDepoimentos; i++) {
            Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
            depoimento.setId(null);
            depoimento.setEgresso(null); // Evita violação de FK
            depoimento.setTexto("Depoimento " + i);
            depoimento.setData(new Date());
            depoimentoService.save(depoimento);
        }
        Pageable pageable = PageRequest.of(0, 10);
        Page<Depoimento> page = depoimentoService.findAll(pageable);
        Assertions.assertNotNull(page, "A página não deve ser nula");
        Assertions.assertFalse(page.isEmpty(), "A página não deve estar vazia");
        Assertions.assertTrue(page.getTotalElements() >= totalDepoimentos,
                "Total de depoimentos deve ser pelo menos " + totalDepoimentos);
    }

    @Test
    @Transactional
    public void deveAtualizarDepoimento() {
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        depoimento.setEgresso(null);
        depoimento.setTexto("Texto Original");
        depoimento.setData(new Date());
        Depoimento salvo = depoimentoService.save(depoimento);

        // Atualiza o texto e a data
        String novoTexto = "Texto Atualizado";
        Date novaData = new Date(System.currentTimeMillis() + 10000);
        salvo.setTexto(novoTexto);
        salvo.setData(novaData);

        var resposta = depoimentoService.update(salvo);
        Assertions.assertTrue(resposta.isSuccess(), "O update deve ser bem-sucedido");
        Assertions.assertEquals("Depoimento atualizado com sucesso", resposta.getMessage());

        Depoimento atualizado = depoimentoService.findById(salvo.getId());
        Assertions.assertEquals(novoTexto, atualizado.getTexto(), "O texto deve ser atualizado");
        Assertions.assertEquals(novaData, atualizado.getData(), "A data deve ser atualizada");
    }

    @Test
    @Transactional
    public void deveDeletarDepoimento() {
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        depoimento.setEgresso(null);
        depoimento.setTexto("Depoimento para deleção");
        depoimento.setData(new Date());
        Depoimento salvo = depoimentoService.save(depoimento);

        Depoimento deletado = depoimentoService.delete(salvo.getId());
        Assertions.assertEquals(salvo.getId(), deletado.getId(), "O ID do deletado deve ser igual ao salvo");
        Assertions.assertThrows(RuntimeException.class,
                () -> depoimentoService.findById(salvo.getId()),
                "Deve lançar exceção ao buscar um depoimento deletado");
    }

    @Test
    @Transactional
    public void deveBuscarDepoimentosPorAno() {
        int anoDesejado = 2022;
        Depoimento depoimento = easyRandom.nextObject(Depoimento.class);
        depoimento.setId(null);
        depoimento.setEgresso(null);
        depoimento.setTexto("Depoimento do ano 2022");
        // Configura o campo data para o ano de 2022
        LocalDateTime ldt = LocalDateTime.of(2022, 6, 15, 12, 0);
        Date data = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        depoimento.setData(data);
        // Note que createdAt e updatedAt serão sobrescritos no save, por isso usamos o campo "data" na consulta.
        depoimentoService.save(depoimento);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Depoimento> page = depoimentoService.buscarPorData(anoDesejado, pageable);
        Assertions.assertFalse(page.isEmpty(), "A busca por ano não deve retornar página vazia");
        page.getContent().forEach(d -> {
            int anoDepoimento = d.getData().toInstant().atZone(ZoneId.systemDefault()).getYear();
            Assertions.assertEquals(2022, anoDepoimento, "Todos os depoimentos devem ter o ano 2022");
        });
    }

    @Test
    @Transactional
    public void deveBuscarDepoimentosRecentes() {
        LocalDateTime agora = LocalDateTime.now(ZoneId.of("UTC"));

        // Cria um depoimento recente (dentro dos últimos 5 dias)
        Depoimento recente = easyRandom.nextObject(Depoimento.class);
        recente.setId(null);
        recente.setEgresso(null);
        recente.setTexto("Depoimento Recente");
        LocalDateTime recenteTime = agora.minusDays(1);
        Date dataRecente = Date.from(recenteTime.atZone(ZoneId.of("UTC")).toInstant());
        recente.setData(dataRecente);
        // Os campos createdAt e updatedAt serão definidos pelo service,
        // mas você pode configurá-los se necessário para o teste
        recente.setCreatedAt(recenteTime);
        recente.setUpdatedAt(recenteTime);
        Depoimento salvoRecente = depoimentoService.save(recente);

        // Cria um depoimento antigo (fora dos últimos 5 dias)
        Depoimento antigo = easyRandom.nextObject(Depoimento.class);
        antigo.setId(null);
        antigo.setEgresso(null);
        antigo.setTexto("Depoimento Antigo");
        LocalDateTime antigoTime = agora.minusDays(10);
        Date dataAntigo = Date.from(antigoTime.atZone(ZoneId.of("UTC")).toInstant());
        antigo.setData(dataAntigo);
        antigo.setCreatedAt(antigoTime);
        antigo.setUpdatedAt(antigoTime);
        Depoimento salvoAntigo = depoimentoService.save(antigo);

        Pageable pageable = PageRequest.of(0, 10);
        // Busca depoimentos dos últimos 5 dias
        Page<Depoimento> page = depoimentoService.buscarRecentes(5, pageable);
        Assertions.assertTrue(page.getContent().stream().anyMatch(d -> d.getId().equals(salvoRecente.getId())),
                "Depoimento recente deve ser encontrado");
        Assertions.assertTrue(page.getContent().stream().noneMatch(d -> d.getId().equals(salvoAntigo.getId())),
                "Depoimento antigo não deve ser retornado");
    }

}
