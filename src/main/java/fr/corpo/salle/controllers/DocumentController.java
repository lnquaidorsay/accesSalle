package fr.corpo.salle.controllers;

import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.entites.Document;
import fr.corpo.salle.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Endpoints pour la gestion des documents")

public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/path")
    @Operation(summary = "Récupérer le chemin d'un document PDF", description = "Retourne le chemin d'un document PDF en fonction de son nom")
    public ResponseEntity<String> getDocumentPath(@RequestParam String nomDocument) {
        Optional<Document> document = documentService.getDocumentByName(nomDocument);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get().getCheminDocument());
        } else {
            return ResponseEntity.badRequest().body("Document non trouvé");
        }
    }

    @GetMapping
    @Operation(summary = "Lister tous les documents", description = "Retourne la liste de tous les documents disponibles")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/file")
    @Operation(summary = "Récupérer un fichier PDF", description = "Retourne le fichier PDF en fonction de son chemin")
    public ResponseEntity<Resource> getDocumentFile(@RequestParam String cheminDocument) throws IOException, MalformedURLException {
        Path path = Paths.get("src/main/resources/static/" + cheminDocument);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            throw new RuntimeException("Impossible de lire le fichier: " + cheminDocument);
        }
    }
}
