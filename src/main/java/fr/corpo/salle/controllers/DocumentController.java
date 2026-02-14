package fr.corpo.salle.controllers;

import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.entites.Document;
import fr.corpo.salle.enums.DocumentSourceType;
import fr.corpo.salle.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Endpoints pour la gestion des documents")
public class DocumentController {

    private final DocumentService documentService;
    private final HttpClient httpClient;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @GetMapping
    @Operation(summary = "Lister tous les documents")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    /**
     * ✅ Endpoint UNIQUE pour afficher un PDF : FILE (dossier externe) ou URL (proxy)
     */
    @GetMapping("/file")
    @Operation(summary = "Récupérer un PDF", description = "Retourne un PDF par nomDocument. Source FILE (storage-dir) ou URL (proxy).")
    public ResponseEntity<?> getDocumentFile(@RequestParam String nomDocument) {

        Document doc = documentService.getDocumentByName(nomDocument)
                .orElse(null);

        if (doc == null) {
            return ResponseEntity.badRequest().body("Document non trouvé");
        }

        try {
            if (doc.getSourceType() == DocumentSourceType.FILE) {
                Resource resource = documentService.resolveFileResource(doc);

                if (!resource.exists() || !resource.isReadable()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichier introuvable");
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            }

            // URL => proxy
            URI uri = documentService.resolveAndValidateUrl(doc);

            HttpRequest req = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(20))
                    .GET()
                    .header("Accept", "application/pdf")
                    .build();

            HttpResponse<byte[]> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofByteArray());

            if (resp.statusCode() >= 400) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body("Erreur proxy URL, status=" + resp.statusCode());
            }

            // Content-Type: si absent, on force PDF
            MediaType ct = MediaType.APPLICATION_PDF;
            String ctHeader = resp.headers().firstValue("content-type").orElse(null);
            if (ctHeader != null) {
                try { ct = MediaType.parseMediaType(ctHeader); } catch (Exception ignored) {}
            }

            byte[] body = resp.body();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getNomDocument() + ".pdf\"")
                    .contentType(ct)
                    .contentLength(body.length)
                    .body(body);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erreur accès document");
        }
    }
}
