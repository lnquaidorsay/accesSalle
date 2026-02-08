package fr.corpo.salle.repository;

import fr.corpo.salle.entites.ConnexionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConnexionLogRepository extends JpaRepository<ConnexionLog, Long> {

    @Query("SELECT COUNT(c) FROM ConnexionLog c WHERE c.dateConnexion = :date")
    Long countByDateConnexion(@Param("date") LocalDate date);

    List<ConnexionLog> findByMatricule(String matricule);
}
