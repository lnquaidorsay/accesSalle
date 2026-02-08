package fr.corpo.salle.service;

import fr.corpo.salle.repository.ConnexionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StatsService {

    @Autowired
    private ConnexionLogRepository connexionLogRepository;

    public Long getConnexionsParJour(LocalDate date) {
        return connexionLogRepository.countByDateConnexion(date);
    }
}
