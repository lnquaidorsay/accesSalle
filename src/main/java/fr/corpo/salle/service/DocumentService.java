package fr.corpo.salle.service;

import fr.corpo.salle.config.DocumentStorageProperties;
import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.entites.Document;
import fr.corpo.salle.enums.DocumentSourceType;
import fr.corpo.salle.mappers.DocumentMapper;
import fr.corpo.salle.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentStorageProperties props;

    public DocumentService(DocumentRepository documentRepository,
                           DocumentMapper documentMapper,
                           DocumentStorageProperties props) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.props = props;
    }

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Document> getDocumentByName(String nomDocument) {
        return documentRepository.findByNomDocument(nomDocument);
    }

    public Resource resolveFileResource(Document doc) {
        if (doc.getSourceType() != DocumentSourceType.FILE) {
            throw new IllegalArgumentException("Document n'est pas de type FILE");
        }
        if (props.getStorageDir() == null || props.getStorageDir().isBlank()) {
            throw new IllegalStateException("app.documents.storage-dir n'est pas configuré");
        }

        Path baseDir = Paths.get(props.getStorageDir()).toAbsolutePath().normalize();

        // location doit être RELATIF
        String rel = doc.getLocation().trim().replace("\\", "/").replaceFirst("^/+", "");
        Path filePath = baseDir.resolve(rel).normalize();

        // Anti path traversal
        if (!filePath.startsWith(baseDir)) {
            throw new IllegalArgumentException("Chemin invalide");
        }

        try {
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la ressource", e);
        }
    }

    public URI resolveAndValidateUrl(Document doc) {
        if (doc.getSourceType() != DocumentSourceType.URL) {
            throw new IllegalArgumentException("Document n'est pas de type URL");
        }

        URI uri = URI.create(doc.getLocation().trim());

        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
            throw new IllegalArgumentException("URL invalide");
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("URL invalide (host)");
        }

        // Allowlist anti-SSRF
        boolean allowed = props.getAllowedUrlHosts().stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .anyMatch(allowedHost ->
                        host.equalsIgnoreCase(allowedHost) ||
                                host.toLowerCase().endsWith("." + allowedHost.toLowerCase())
                );

        if (!allowed) {
            throw new IllegalArgumentException("Host URL non autorisé: " + host);
        }

        return uri;
    }

}
