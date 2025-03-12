package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.*;
import com.muxegresso.egresso.repositories.*;
import com.muxegresso.egresso.services.CoordenadorService;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;


@Validated
@Service
public class CoordenadorServiceImpl implements CoordenadorService {
    @Autowired
    CoordenadorRepository coordenadorRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    DepoimentoRepository depoimentoRepository;

    public boolean efetuarLogin(String email, String senha){
        Coordenador coordenador = coordenadorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email não existente na base de dados."));
        if (coordenador.getSenha().equals(senha)) return true;
        return false;
    }

    @Transactional
    public Coordenador save(@Valid Coordenador coordenador){
        if (coordenadorRepository.existsByEmail(coordenador.getEmail())) {
            throw new RuntimeException("O e-mail informado já está em uso. Por favor, tente outro!");
        }
        return coordenadorRepository.save(coordenador);
    }

    public Coordenador findById( Integer id){
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo.");
        }
        Coordenador coordenador = coordenadorRepository.findById(id).orElseThrow(()->new RuntimeException("ID não presente no sistema."));
        return coordenador;
    }

    @Override
    public Depoimento lincarDepoimentoEgresso(@NotNull Egresso egresso, @NotNull Depoimento depoimento) {
        depoimento.setEgresso(egresso);
        return depoimentoRepository.save(depoimento);
    }

    @Override
    public Curso addCurso(@NotNull String email, @NotBlank String nome, @NotBlank String nivel) {
        Coordenador coordenador = coordenadorRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("E-mail do coordenador é inexistente!"));

        Curso curso = new Curso();

        curso.setCoordenador(coordenador);
        curso.setNome(nome);
        curso.setNivel(nivel);

        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public Cargo addCargo(@NotNull Egresso egresso,@NotNull Cargo cargo) {
        cargo.setEgresso(egresso);
        return cargoRepository.save(cargo);
    }

    public Page<Coordenador> findAll(Pageable pageable){
        return coordenadorRepository.findAll(pageable);
    }

    public Coordenador delete(Integer id){
        Coordenador coordenador = this.findById(id);
        coordenadorRepository.delete(coordenador);
        return coordenador;
    }

    @Transactional
    public Coordenador update(@Valid Coordenador coordenador) {
        // Busca a entidade atual do banco
        Coordenador coordenadorObj = this.findById(coordenador.getId());

        // Se o e-mail foi alterado, verifica duplicidade
        if (!coordenadorObj.getEmail().equals(coordenador.getEmail()) &&
                coordenadorRepository.existsByEmailAndIdNot(coordenador.getEmail(), coordenador.getId())) {
            throw new RuntimeException("O e-mail informado já está em uso. Por favor, tente outro!");
        }

        // Copia todas as propriedades do objeto recebido para o objeto gerenciado (exceto o id)
        BeanUtils.copyProperties(coordenador, coordenadorObj, "id");
        return coordenadorRepository.save(coordenadorObj);
    }

    @Transactional
    public void RegistrarEgresso(Egresso egresso){
        egressoRepository.save(egresso);
    }

    @Transactional
    public Curso registrarCurso(@Valid Curso curso, @Valid Coordenador coordenador){
        curso.setCoordenador(coordenador);
        return cursoRepository.save(curso);
    }

    @Transactional
    public void AdicionarCargo(@Valid Egresso egresso, @Valid Cargo cargo){
        cargo.setEgresso(egresso);
        cargoRepository.save(cargo);
    }

    @Transactional
    public void AdicionarDepoimento(@Valid Egresso egresso, @Valid Depoimento depoimento){
        depoimento.setEgresso(egresso);
        depoimentoRepository.save(depoimento);
    }

    @Override
    public boolean existsByEmail(String email) {
        return coordenadorRepository.existsByEmail(email);
    }


}
