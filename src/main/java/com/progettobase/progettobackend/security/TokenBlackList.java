package com.progettobase.progettobackend.security;

import java.util.HashSet;
import java.util.Set;

public class TokenBlackList {
    // Crea un set per mantenere traccia dei token neri
    private static Set<String> blacklist = new HashSet<>();

    // Aggiungi un token alla blacklist
    public static void addToken(String token) {
        blacklist.add(token);
    }

    // Verifica se un token è nella blacklist
    public static boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    // Rimuovi un token dalla blacklist (ad esempio, quando il token scade)
    public static void removeToken(String token) {
        blacklist.remove(token);
    }

    // Restituisce la blacklist
    public static Set<String> getBlacklist() {
        return blacklist;
    }
}
