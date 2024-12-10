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
        // Gera a entidade automaticamente
        EasyRandom easyRandom = new EasyRandom();
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

        EasyRandom easyRandom = new EasyRandom();

        // Gera um egresso e salva
        Egresso egresso = easyRandom.nextObject(Egresso.class);
        egresso.setId(null);
        Egresso salvoEgresso = egressoRepository.save(egresso);

        Cargo cargo = easyRandom.nextObject(Cargo.class);
        cargo.setEgresso(salvoEgresso);
        cargo.setId(null);

        Cargo salvoCargo = cargoRepository.save(cargo);
        salvoCargo.setDescricao("DescriptionTest");
        Cargo cargoAtualizado = cargoRepository.save(salvoCargo);

        Assertions.assertNotNull(cargoAtualizado);
        Assertions.assertEquals(cargo.getDescricao(), cargoAtualizado.getDescricao());
        Assertions.assertEquals(cargoAtualizado.getLocal(), salvoCargo.getLocal());
        Assertions.assertEquals(cargoAtualizado.getAno_fim(), salvoCargo.getAno_fim());
        Assertions.assertEquals(cargoAtualizado.getAno_inicio(), salvoCargo.getAno_inicio());

    }
}
