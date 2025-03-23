package com.muxegresso.egresso.services.auth;


import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.repositories.CoordenadorRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Override
    public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
        Optional<Egresso> egressoOpt = egressoRepository.findByEmail(data);
        if (egressoOpt.isPresent()) {
            return egressoOpt.get(); // Retorna Egresso
        }

        Optional<Coordenador> adminOpt = coordenadorRepository.findByEmail(data);
        if (adminOpt.isPresent()) {
            return adminOpt.get(); // Retorna o Coordenador
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + data);
    }
}