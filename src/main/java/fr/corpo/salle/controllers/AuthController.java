package fr.corpo.salle.controllers;

import fr.corpo.salle.dto.EmployeDTO;
import fr.corpo.salle.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Endpoints pour l'authentification")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/validate")
    @Operation(summary = "Valider un matricule", description = "Retourne les informations de l'employé si le matricule est valide")
    public ResponseEntity<?> validateMatricule(@Valid @RequestBody EmployeDTO employeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<EmployeDTO> employe = authService.validateMatricule(employeDTO.getMatricule());
        if (employe.isPresent()) {
            return ResponseEntity.ok(employe.get());
        } else {
            return ResponseEntity.badRequest().body("Utilisateur inconnu");
        }
    }
}
