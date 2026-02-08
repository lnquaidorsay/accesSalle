package fr.corpo.salle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeDTO {
    private String nom;
    private String prenom;
    private String email;
    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 10, max = 10, message = "Le matricule doit contenir exactement 10 chiffres")
    private String matricule;
}
