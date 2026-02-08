package fr.corpo.salle.service;

import fr.corpo.salle.dto.EmployeDTO;
import fr.corpo.salle.entites.ConnexionLog;
import fr.corpo.salle.mappers.EmployeMapper;
import fr.corpo.salle.repository.ConnexionLogRepository;
import fr.corpo.salle.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private ConnexionLogRepository connexionLogRepository;

    @Autowired
    private EmployeMapper employeMapper;

    public Optional<EmployeDTO> validateMatricule(String matricule) {
        return employeRepository.findByMatricule(matricule)
                .map(employe -> {
                    // Enregistrer la connexion
                    ConnexionLog log = new ConnexionLog();
                    log.setMatricule(matricule);
                    log.setDateConnexion(LocalDate.now());
                    connexionLogRepository.save(log);
                    // Mapping manuel
                    EmployeDTO employeDTO = new EmployeDTO();
                    employeDTO.setNom(employe.getNom());
                    employeDTO.setPrenom(employe.getPrenom());
                    employeDTO.setEmail(employe.getEmail());
                    employeDTO.setMatricule(employe.getMatricule());
                    return employeDTO;
                    //return employeMapper.toDTO(employe);
                });
    }
}
