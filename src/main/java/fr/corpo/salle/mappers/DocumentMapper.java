package fr.corpo.salle.mappers;

import fr.corpo.salle.dto.DocumentDTO;
import fr.corpo.salle.entites.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document document);
}
