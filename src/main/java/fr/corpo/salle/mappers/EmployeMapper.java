package fr.corpo.salle.mappers;

import fr.corpo.salle.dto.EmployeDTO;
import fr.corpo.salle.entites.Employe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeMapper {
    /*@Mapping(source = "nom", target = "nom")
    @Mapping(source = "prenom", target = "prenom")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "matricule", target = "matricule")*/
    EmployeDTO toDTO(Employe employe);
}
