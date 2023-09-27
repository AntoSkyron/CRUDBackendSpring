package com.progettobase.progettobackend.controller;

import com.progettobase.progettobackend.entity.AdminDB;
import com.progettobase.progettobackend.security.*;
import com.progettobase.progettobackend.service.AdminService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:8081"}, maxAge = 3600)
public class AuthController {

    // Aggiungo il service
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;


    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @ResponseBody
    @PostMapping("/admin/login")
    public ResponseEntity adminLogin(@RequestBody LoginReq loginReq){
        try {
            AdminDB adminDB = adminService.findAdminDBByEmail(loginReq.getEmail());
            if(!adminDB.getPassword().equals(loginReq.getPassword())){
                ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            String token = jwtUtil.createTokenAdmin(adminDB);
            LoginRes loginRes = new LoginRes(loginReq.getEmail(),token);
            return ResponseEntity.ok(loginRes);
        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/admin/decrypt")
    public ResponseEntity<?> decryptToken(@RequestBody TokenRequest tokenRequest) {
        String token = tokenRequest.getToken();

        Claims claims = jwtUtil.decodeToken(token);

        if (claims != null) {
            // Il token è stato decodificato con successo
            // Ad esempio, puoi ottenere il nome utente e l'email:
            String username = (String) claims.get("username");
            String email = (String) claims.get("email");

            // return ResponseEntity.ok("Username: " + username);
            // facciamo tornare l'username e l'email
            return ResponseEntity.ok("Username: " + username + " Email: " + email);
        } else {
            // La decodifica del token è fallita
            System.out.println("Errore nella decodifica del token.");
            System.out.println("Token: " + token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella decodifica del token.");
        }
    }


    @PostMapping("/admin/logout")
    public ResponseEntity<String> adminLogout(@RequestBody String token) {

        // Verifica se il token è nella blacklist
        if (TokenBlackList.isTokenBlacklisted(token)) {
            // Il token è stato revocato, quindi rispondi con un errore
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token revocato.");
        }

        // Aggiungi il token alla blacklist per revocarlo
        TokenBlackList.addToken(token);

        //Restituisco la lista dei token nella blacklist
        System.out.println("Lista dei token nella blacklist: " + TokenBlackList.getBlacklist());
        // Restitusico il numero di token presenti nella blacklist
        System.out.println("Numero di token nella blacklist: " + TokenBlackList.getBlacklist().size());

        // Restituisci una risposta positiva
        System.out.println("Logout effettuato con successo.");
        return ResponseEntity.ok("Logout effettuato con successo.");
    }
}