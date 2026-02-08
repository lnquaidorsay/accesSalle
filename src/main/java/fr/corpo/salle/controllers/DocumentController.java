package fr.corpo.salle.controllers;

import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Endpoints pour la gestion des documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    @Operation(summary = "Lister tous les documents", description = "Retourne la liste de tous les documents disponibles")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }
}
