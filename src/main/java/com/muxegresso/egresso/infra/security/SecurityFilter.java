package com.muxegresso.egresso.infra.security;


import com.muxegresso.egresso.repositories.CoordenadorRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);

            String role = tokenService.getRoleFromToken(tokenJWT); // pegando o role do usuario
            //System.out.println("filter: "+subject+role);
            System.out.println(role);
            if ("EGRESSO_ROLE".equals(role)) {
                System.out.println("regresso token");
                var usuario = egressoRepository.findByEmail(subject).orElseThrow(() -> new ResourceNotFoundException(subject));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if ("COORDENADOR_ROLE".equals(role)) {
                var usuario = coordenadorRepository.findByEmail(subject).orElseThrow(() -> new ResourceNotFoundException(subject));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AccessDeniedException("Acesso negado: você não tem permissão para acessar este recurso.");
            }
        }
        filterChain.doFilter(request, response);
    }


    private String recuperarToken(HttpServletRequest request) {
        var authoriztionHeader = request.getHeader("Authorization");//. retorna null caso o campo n exista ou n tenho valor na requisição
        if (authoriztionHeader != null) {// pq na requisição de login n vem o cabeçalho Authorization
            return authoriztionHeader.replace("Bearer ","");// removendo o prefixo Bearer que é o padrão do JWT
        }
        return null;
    }
}
