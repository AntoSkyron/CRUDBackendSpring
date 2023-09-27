package com.progettobase.progettobackend.security;

// Definisci una classe interna per rappresentare la richiesta del token
public class TokenRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}