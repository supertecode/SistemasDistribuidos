package trocajsons;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Token {

    public static void main(String[] args) {
        // Chave de autenticação
        String chaveAutenticacao = "DISTRIBUIDOS";

        // Criação do token com base em duas claims
        String claim1 = "valor_claim1";
        String claim2 = "valor_claim2";
        String claim3 = "valor_claim3";
        String claim4 = "valor_claim4";
        String token = criarToken(claim1, claim2, chaveAutenticacao);
        String token2 = criarToken(claim1, claim2, chaveAutenticacao);
        System.out.println("Token criado: " + token);

        // Autenticação do token e verificação das claims
        try {
            verificarToken(token2 , chaveAutenticacao);
        } catch (JWTVerificationException exception) {
            System.out.println("Falha na verificação do token: " + exception.getMessage());
        }
    }

    public static String criarToken(String claim1, String claim2, String chaveAutenticacao) {
        return JWT.create()
                .withClaim("claim1", claim1)
                .withClaim("claim2", claim2)
                .sign(Algorithm.HMAC256(chaveAutenticacao));
    }

    public static String verificarToken(String token, String chaveAutenticacao) throws JWTVerificationException {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(chaveAutenticacao))
                .build()
                .verify(token);

        String claim1Value = jwt.getClaim("claim1").asString();
        String claim2Value = jwt.getClaim("claim2").asString();

        System.out.println("Claim 1: " + claim1Value);
        System.out.println("Claim 2: " + claim2Value);
        
        return claim1Value;
    }
}


