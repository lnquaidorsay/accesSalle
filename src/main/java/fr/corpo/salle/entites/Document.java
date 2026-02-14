package fr.corpo.salle.entites;

import fr.corpo.salle.enums.DocumentSourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nom_document", nullable = false, unique = true)
    private String nomDocument;

    @Enumerated(EnumType.STRING)
    @Column(name="source_type", nullable = false)
    private DocumentSourceType sourceType;

    /**
     * FILE : chemin relatif dans app.documents.storage-dir (ex: "orga/organigramme.pdf")
     * URL  : URL complète (ex: "https://.../file.pdf")
     */
    @Column(name="location", nullable = false, length = 2000)
    private String location;
}
