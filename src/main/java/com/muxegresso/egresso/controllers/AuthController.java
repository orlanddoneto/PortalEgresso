package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.ApiResponse;
import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Egresso;
import com.muxegresso.egresso.domain.dtos.AutenticationDataDto;
import com.muxegresso.egresso.infra.security.TokenJwtData;
import com.muxegresso.egresso.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth" )
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    private ResponseEntity<ApiResponse> efetuarLogin(@RequestBody @Valid AutenticationDataDto dto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.data(), dto.password());
        Authentication authentication = manager.authenticate(authenticationToken);
        // Verifica se o usuário é do tipo Egresso ou Admin
        Object principal = authentication.getPrincipal();
        String tokenJWT;
        if (dto.role().equals("egresso")) {
            tokenJWT = tokenService.genToken((Egresso) principal);
        } else if (dto.role().equals("admin")) {
            tokenJWT = tokenService.genToken((Coordenador) principal);
        } else {
            throw new RuntimeException("Tipo de usuário desconhecido");
        }
        return ResponseEntity.ok(new ApiResponse(true,new TokenJwtData(tokenJWT).token()));
    }
}
