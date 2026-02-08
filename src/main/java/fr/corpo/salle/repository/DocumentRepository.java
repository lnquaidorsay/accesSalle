package fr.corpo.salle.repository;

import fr.corpo.salle.entites.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
