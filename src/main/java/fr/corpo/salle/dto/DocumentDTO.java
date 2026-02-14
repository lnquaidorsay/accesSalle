package fr.corpo.salle.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import fr.corpo.salle.enums.DocumentSourceType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private String nomDocument;
    private DocumentSourceType sourceType;
    private String location;
}

