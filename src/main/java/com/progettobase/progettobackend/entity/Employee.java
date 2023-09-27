package com.progettobase.progettobackend.entity;
import jakarta.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Il campo 'nome' non può essere vuoto")
    @Column(name = "Nome", nullable = false)
    private String nome;

    @NotEmpty(message = "Il campo 'cognome' non può essere vuoto")
    @Column(name = "Cognome", nullable = false)
    private String cognome;

    @NotEmpty(message = "Il campo 'email' non può essere vuoto")
    @Email(message = "L'indirizzo email deve essere valido")
    @Column(name = "Email", nullable = false)
    private String email;

    @NotEmpty(message = "Il campo 'telefono' non può essere vuoto")
    @Pattern(regexp = "^(\\+39|0039)?[0-9]{10}$", message = "Il formato del numero di telefono non è valido")
    @Column(name = "Telefono", nullable = false, length = 20)
    private String telefono;

}
















