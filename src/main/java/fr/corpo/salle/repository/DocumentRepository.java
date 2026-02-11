package fr.corpo.salle.repository;

import fr.corpo.salle.entites.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByNomDocument(String nomDocument);
}
