package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Coordenador;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class CoordenadorRepositoryTest {
    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Test
    @DisplayName("Verifica o salvamento de um coordenador")
    public void deveVerificarSalvamentoCoordenador(){
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("email@emailRepository.com");

        Coordenador salvoCoordenador = coordenadorRepository.save(coordenador);

        Assertions.assertNotNull(salvoCoordenador);
        Assertions.assertEquals(salvoCoordenador.getTipo(), coordenador.getTipo());
        Assertions.assertEquals(salvoCoordenador.getEmail(), coordenador.getEmail());
        Assertions.assertEquals(salvoCoordenador.getSenha(), coordenador.getSenha());
    }

    @Test
    @DisplayName("Verifica a deleção de um coordenador")
    public void deveVerificarDelecaoCoordenador(){
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("email2@emailRepository.com");

        Coordenador salvoCoordenador = coordenadorRepository.save(coordenador);
        Integer id = salvoCoordenador.getId();

        coordenadorRepository.deleteById(id);

        Optional<Coordenador> coordenadorFind = coordenadorRepository.findById(id);
        Assertions.assertFalse(coordenadorFind.isPresent());
    }

    @Test
    @DisplayName("Verifica atualização de um coordenador")
    public void deveVerificarAtualizacaoCoordenador(){
        EasyRandom easyRandom = new EasyRandom();
        Coordenador coordenador = easyRandom.nextObject(Coordenador.class);
        coordenador.setId(null);
        coordenador.setEmail("email3@emailRepository.com");

        Coordenador salvoCoordenador = coordenadorRepository.save(coordenador);
        salvoCoordenador.setTipo("tipoTeste");

        Coordenador atualizadoCoordenador = coordenadorRepository.save(salvoCoordenador);
        Assertions.assertNotNull(atualizadoCoordenador);
        Assertions.assertEquals("tipoTeste",atualizadoCoordenador.getTipo());
        Assertions.assertEquals(salvoCoordenador.getSenha(), atualizadoCoordenador.getSenha());
        Assertions.assertEquals(salvoCoordenador.getEmail(),atualizadoCoordenador.getEmail());

    }
}
