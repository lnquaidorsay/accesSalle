package fr.corpo.salle.service;

import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.entites.Document;
import fr.corpo.salle.mappers.DocumentMapper;
import fr.corpo.salle.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Document> getDocumentByName(String nomDocument) {
        return documentRepository.findByNomDocument(nomDocument);
    }

}
