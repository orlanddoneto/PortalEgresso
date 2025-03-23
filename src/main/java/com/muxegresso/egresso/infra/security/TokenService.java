package com.muxegresso.egresso.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.muxegresso.egresso.domain.Coordenador;
import com.muxegresso.egresso.domain.Egresso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // gerar token para Client
    public String genToken(Egresso egresso) {
        return generateToken(egresso.getEmail(), egresso.getId(), "client");
    }

    // gerar token para Admin
    public String genToken(Coordenador coordenador) {
        return generateToken(coordenador.getEmail(), coordenador.getId(), "admin");
    }

    public String generateToken(String subject, Integer id, String role){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()// retornando o token
                    .withIssuer("ManinTcg Api")
                    .withSubject(subject) //aguenta cpf ou email
                    .withClaim("id", id)
                    .withClaim("role", role)
                    .withExpiresAt(experitionDate())// validade do token
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw  new RuntimeException("Error ao gerar token", exception);
        }
    }
    public String getSubject(String tokenJWT){
        //metodo responsavel por validar o tokenJWT e após retornar o subject(cpf do client
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ManinTcg Api")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();//pegando o cpf contido do tokenJWT

        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            throw new RuntimeException("TokenJWT inválido ou expirado");
        }
    }
    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ManinTcg Api")
                    .build(); // Verifica o token

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido", exception);
        }
    }
    public boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ManinTcg Api")
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false; // Token inválido
        }
    }

    public String getRoleFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("role").asString(); // Extrai a role do token
    }
    private Instant experitionDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

}
