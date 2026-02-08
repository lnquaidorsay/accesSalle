package fr.corpo.salle.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "connexion_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnexionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String matricule;

    @Column(nullable = false)
    private LocalDate dateConnexion;
}
