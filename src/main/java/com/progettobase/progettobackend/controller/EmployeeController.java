package com.progettobase.progettobackend.controller;
import com.progettobase.progettobackend.entity.Employee;
import com.progettobase.progettobackend.repository.EmployeeRepository;
import com.progettobase.progettobackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:8081"}, maxAge = 3600)
public class EmployeeController {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    // @CrossOrigin()
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Richiede il ruolo 'ROLE_ADMIN' per accedere
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }


    // @CrossOrigin()
    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Richiede il ruolo 'ROLE_ADMIN' per accedere
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id);
    }

    // @CrossOrigin()
    @DeleteMapping("/cancella/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Richiede il ruolo 'ROLE_ADMIN' per accedere
    public void deleteEmployee(@PathVariable Long id) {
        // Stampo qualcosa nella console
        System.out.println("Cancello l'utente con id: " + id);
        employeeService.deleteEmployee(id);
    }


    // @CrossOrigin()
    @PostMapping("/aggiungi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Richiede il ruolo 'ROLE_ADMIN' per accedere
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // Verifica se i campi obbligatori sono vuoti
        if (employee.getNome() == null || employee.getCognome() == null || employee.getEmail() == null) {
            // Campo obbligatorio mancante, restituisci un errore "400 Bad Request"
            return ResponseEntity.badRequest().build();
        }

        // Verifica se l'email è valida (puoi utilizzare una libreria di validazione email)
        if (!isValidEmail(employee.getEmail())) {
            // Email non valida, restituisci un errore "400 Bad Request"
            return ResponseEntity.badRequest().build();
        }

        try {
            // Se tutte le verifiche sono passate, aggiungi l'utente
            employeeService.aggiungiEmployee(employee);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            // Gestisci eventuali eccezioni qui, ad esempio un errore di database
            // Restituisci un errore "500 Internal Server Error"
            System.out.println("Errore: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //Esempio immissione : http:<//localhost:8080/api/modifica/1
    // su postman invece cosa farò, metto in body un json con i dati da modificare
    // e in header metto content-type application/json, e in body metto raw e json, e metto i dati da modificare.
    // @CrossOrigin()
    @PutMapping("/modifica/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        try {
            // Ottieni l'employee esistente dal servizio
            Employee existingEmployee = employeeService.findEmployeeById(id);

            // Verifica se l'employee esistente è null, il che significa che l'ID non esiste
            if (existingEmployee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
            }

            // Verifica se l'indirizzo email è valido
            if (!isValidEmail(employee.getEmail())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
            }

            // Aggiorna i dati dell'employee esistente
            existingEmployee.setNome(employee.getNome());
            existingEmployee.setCognome(employee.getCognome());
            existingEmployee.setEmail(employee.getEmail());

            // Esegui l'aggiornamento tramite il servizio
            Employee updatedEmployee = employeeService.updateEmployee(existingEmployee);

            // Restituisci l'employee aggiornato con uno status 200 OK
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (Exception e) {
            // Gestisci eventuali eccezioni o errori qui
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }



    private boolean isValidEmail(String email) {
        // Definisci un'espressione regolare per validare l'indirizzo email
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Crea il pattern regex
        Pattern pattern = Pattern.compile(regex);

        // Esegui la corrispondenza dell'email con il pattern
        Matcher matcher = pattern.matcher(email);

        // Restituisci true se l'email è valida, altrimenti false
        return matcher.matches();
    }
}
