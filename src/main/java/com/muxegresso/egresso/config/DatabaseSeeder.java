package com.muxegresso.egresso.config;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.domain.enums.StatusDepoimento;
import com.muxegresso.egresso.domain.enums.UserStatus;
import com.muxegresso.egresso.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner seedDatabase(
            CoordenadorRepository coordRepo,
            CursoRepository cursoRepo,
            EgressoRepository egressoRepo,
            CargoRepository cargoRepo,
            DepoimentoRepository depoRepo,
            OportunidadeRepository oportunidadeRepo,
            Curso_EgressoRepository cursoEgressoRepo
    ) {
        return args -> {

            /*
            Coordenador coord1 = new Coordenador();
            coord1.setNome("João Silva");
            coord1.setEmail("joao.silva@example.com");
            coord1.setSenha("123456");
            coord1.setTipo("COORDENADOR");
            coordRepo.save(coord1);

            Coordenador coord2 = new Coordenador();
            coord2.setNome("Maria Oliveira");
            coord2.setEmail("maria.oliveira@example.com");
            coord2.setSenha("654321");
            coord2.setTipo("COORDENADOR");
            coordRepo.save(coord2);


            //2) CRIAR EGRESSOS
            Egresso e1 = new Egresso();
            e1.setNome("Carlos Santos");
            e1.setSenha("senha123");
            e1.setUserStatus(UserStatus.ACTIVE);
            e1.setEmail("carlos.santos@example.com");
            e1.setCpf("11122233344");
            e1.setResumo("Resumo do Carlos");
            e1.setUrl_foto("http://example.com/foto1.jpg");
            e1.setCreatedAt(LocalDateTime.now());
            e1.setUpdatedAt(LocalDateTime.now());
            e1.setHomologado(true);
            egressoRepo.save(e1);

            Egresso e2 = new Egresso();
            e2.setNome("Ana Beatriz");
            e2.setSenha("senha456");
            e2.setUserStatus(UserStatus.BLOCKED);
            e2.setEmail("ana.beatriz@example.com");
            e2.setCpf("55566677788");
            e2.setResumo("Resumo da Ana");
            e2.setUrl_foto("http://example.com/foto2.jpg");
            e2.setCreatedAt(LocalDateTime.now());
            e2.setUpdatedAt(LocalDateTime.now());
            e2.setHomologado(false);
            egressoRepo.save(e2);


            //3) CRIAR CURSOS (relacionados aos Coordenadores)
            Curso c1 = new Curso();
            c1.setNome("Sistemas de Informação");
            c1.setNivel("Bacharelado");
            c1.setCoordenador(coord1);
            cursoRepo.save(c1);

            Curso c2 = new Curso();
            c2.setNome("Análise e Desenvolvimento de Sistemas");
            c2.setNivel("Tecnólogo");
            c2.setCoordenador(coord2);
            cursoRepo.save(c2);


             //4) CRIAR OPORTUNIDADES (Many-to-Many com Curso)

            Oportunidade op1 = new Oportunidade();
            op1.setDescricao("Vaga de Estágio em Desenvolvimento");
            op1.setCreatedAt(LocalDateTime.now());
            op1.setLink("http://example.com/vaga1");
            op1.setHomologado(true);
            oportunidadeRepo.save(op1);

            Oportunidade op2 = new Oportunidade();
            op2.setDescricao("Vaga de Analista de Sistemas Jr");
            op2.setCreatedAt(LocalDateTime.now());
            op2.setLink("http://example.com/vaga2");
            op2.setHomologado(false);
            oportunidadeRepo.save(op2);

            // Adicionar op1 e op2 ao curso c1
            c1.getOportunidades().add(op1);
            c1.getOportunidades().add(op2);
            // Adicionar c1 ao set de cursos em op1 e op2
            op1.getCursos().add(c1);
            op2.getCursos().add(c1);

            // Salvar novamente para persistir a relação
            cursoRepo.save(c1);
            oportunidadeRepo.save(op1);
            oportunidadeRepo.save(op2);


            //5) CRIAR CARGOS (relacionados a Egresso)

            Cargo cargo1 = new Cargo();
            cargo1.setDescricao("Desenvolvedor Java");
            cargo1.setLocal("Empresa XYZ");
            cargo1.setAno_inicio(2020);
            cargo1.setAno_fim(2022);
            cargo1.setEgresso(e1);
            cargoRepo.save(cargo1);

            Cargo cargo2 = new Cargo();
            cargo2.setDescricao("Analista de Sistemas");
            cargo2.setLocal("Empresa ABC");
            cargo2.setAno_inicio(2021);
            cargo2.setAno_fim(2023);
            cargo2.setEgresso(e2);
            cargoRepo.save(cargo2);


            //6) CRIAR DEPOIMENTOS (relacionados a Egresso)

            Depoimento d1 = new Depoimento();
            d1.setEgresso(e1);
            d1.setTexto("Depoimento do Carlos");
            d1.setData(new Date()); // data obrigatória
            d1.setHomologado(true);
            d1.setCreatedAt(LocalDateTime.now());
            d1.setUpdatedAt(LocalDateTime.now());
            d1.setStatus(StatusDepoimento.Aprovado); // Ajuste conforme seu enum
            depoRepo.save(d1);

            Depoimento d2 = new Depoimento();
            d2.setEgresso(e2);
            d2.setTexto("Depoimento da Ana");
            d2.setData(new Date());
            d2.setHomologado(false);
            d2.setCreatedAt(LocalDateTime.now());
            d2.setUpdatedAt(LocalDateTime.now());
            d2.setStatus(StatusDepoimento.Enviado); // Ajuste conforme seu enum
            depoRepo.save(d2);

            //CRIAR CURSO_EGRESSO (relacionamento N:N com atributos extras)
            Curso_Egresso ce1 = new Curso_Egresso();
            ce1.setEgresso(e1);
            ce1.setCurso(c1);
            ce1.setAno_inicio(2018);
            ce1.setAno_fim(2022);
            cursoEgressoRepo.save(ce1);

            Curso_Egresso ce2 = new Curso_Egresso();
            ce2.setEgresso(e2);
            ce2.setCurso(c2);
            ce2.setAno_inicio(2019);
            ce2.setAno_fim(2021);
            cursoEgressoRepo.save(ce2);

            System.out.println(">>> Banco populado com dados de teste! <<<");
        */
        };
    }
}
