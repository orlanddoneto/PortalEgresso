package com.muxegresso.egresso.repositories;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.Curso;
import com.muxegresso.egresso.domain.Egresso;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CargoRepositoryTest {
    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Test
    @DisplayName("Verifica o salvamento de um cargo")
    public void deveVerificarSalvarCargo(){

        //Randomizador de parametros
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(String.class, () -> "valor-default") // Personalize valores específicos
                .stringLengthRange(5, 15);

        EasyRandom easyRandom = new EasyRandom(parameters);

        // Gera a entidade automaticamente
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        Cargo cargo = easyRandom.nextObject(Cargo.class);

        //Alterações necessárias para que o objeto seja criado randomicamente de forma correta
        cargo.setEgresso(egresso);
        egresso.setId(null);
        cargo.setId(null);

        egressoRepository.save(egresso);
        Cargo salvoCargo = cargoRepository.save(cargo);

        Assertions.assertNotNull(salvoCargo);
        Assertions.assertEquals(cargo.getLocal(), salvoCargo.getLocal());
        Assertions.assertEquals(cargo.getDescricao(), salvoCargo.getDescricao());
        Assertions.assertEquals(cargo.getAno_fim(), salvoCargo.getAno_fim());
        Assertions.assertEquals(cargo.getAno_inicio(), salvoCargo.getAno_inicio());
    }

    @Test
    @DisplayName("Verifica a remoção de um cargo")
    public void deveVerificarRemoverCurso(){
        Cargo cargo = new Cargo(null,"testeDesc","testeLocal",2021,2024,null);

        Cargo salvoCargo = cargoRepository.save(cargo);
        Integer id = salvoCargo.getId();
        cargoRepository.deleteById(salvoCargo.getId());


        Optional<Cargo> temp = cargoRepository.findById(id);
        Assertions.assertFalse(temp.isPresent());
    }

    @Test
    @DisplayName("Verifica a atualização da descrição de um cargo")
    public void deveVerificarAtualizacaoDescCargo(){
        Egresso egresso = new Egresso();

        Cargo cargo = new Cargo(null,"descriptionTeste", "localTeste", 2020, 2021,egresso);

        cargo.setDescricao("Nova_descriptionTeste");

        Cargo salvoCargo = cargoRepository.save(cargo);
        Cargo cargoAtualizado = cargoRepository.findById(salvoCargo.getId()).orElseThrow();

        Assertions.assertNotNull(cargoAtualizado);
        Assertions.assertEquals(cargo.getDescricao(), cargoAtualizado.getDescricao());

    }
}
