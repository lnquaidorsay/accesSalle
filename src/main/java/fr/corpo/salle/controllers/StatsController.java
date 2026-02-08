package fr.corpo.salle.controllers;

import fr.corpo.salle.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
@Tag(name = "Statistiques", description = "Endpoints pour les statistiques de connexion")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/connexions")
    @Operation(
            summary = "Nombre de connexions par jour",
            description = "Retourne le nombre de connexions pour une date donnée (format : AAAA-MM-JJ). Si aucune date n'est spécifiée, utilise la date du jour."
    )
    public ResponseEntity<Long> getConnexionsParJour(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        // Si aucune date n'est fournie, utiliser la date du jour
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        Long count = statsService.getConnexionsParJour(queryDate);
        return ResponseEntity.ok(count);
    }
}

/*Paramètre date optionnel :

Le paramètre date est marqué comme required = false, ce qui signifie que si l'utilisateur ne fournit pas de date, la date du jour (LocalDate.now()) sera utilisée par défaut.


Annotation @DateTimeFormat :

Permet de spécifier le format de la date attendue dans la requête (ex: 2026-02-08).


Logique de date par défaut :

Si aucune date n'est fournie, le contrôleur utilise la date du jour.

Appel à statsService.getConnexionsParJour(queryDate) :

Appelle la méthode du service pour obtenir le nombre de connexions pour la date spécifiée.

Ouvrez votre navigateur et allez à :
http://localhost:8080/swagger-ui.html
        3.3. Testez l'endpoint /api/stats/connexions
Cas 1 : Sans spécifier de date

Requête :
GET /api/stats/connexions
Résultat attendu :
Le nombre de connexions pour la date du jour (ex: 1 si un utilisateur s'est connecté aujourd'hui).
Cas 2 : Avec une date spécifique

Requête :
GET /api/stats/connexions?date=2026-02-07
Résultat attendu :
Le nombre de connexions pour le 7 février 2026 (ex: 3 si trois utilisateurs se sont connectés ce jour-là).

        4. Exemple de réponse
Réponse réussie
json
        Copier

1

Réponse en cas d'erreur
Si la date fournie est invalide (ex: 2026-02-30), Spring Boot retournera une erreur 400 Bad Request avec un message approprié.

        5. Vérification de la cinématique complète
Pour tester la cinématique complète (de l'authentification à l'affichage des statistiques) :


Authentifiez un utilisateur :

Utilisez l'endpoint POST /api/auth/validate avec un matricule valide (ex: 1234567890).
Exemple de requête :
json
        Copier

{
        "matricule": "1234567890"
        }




Vérifiez que la connexion est enregistrée :

Consultez la table connexion_log dans votre base de données (via pgAdmin ou une requête SQL) pour confirmer que la connexion a bien été enregistrée.


Récupérez les statistiques :

Utilisez l'endpoint GET /api/stats/connexions pour vérifier que le nombre de connexions a été incrémenté.


        6. Requête SQL pour vérifier les logs de connexion
Vous pouvez exécuter cette requête dans pgAdmin pour vérifier que les logs de connexion sont bien enregistrés :
sql
        Copier

SELECT * FROM connexion_log WHERE date_connexion = '2026-02-08';


maven-compiler-plugin :

Ce plugin est responsable de la compilation des sources Java.
La section <annotationProcessorPaths> indique au compilateur d'utiliser les processeurs d'annotations pour MapStruct et Lombok.


mapstruct-processor :

Ce processeur est nécessaire pour générer l'implémentation de vos interfaces MapStruct (comme EmployeMapperImpl).


lombok :

Inclure Lombok dans <annotationProcessorPaths> permet de s'assurer que Lombok et MapStruct fonctionnent ensemble sans conflit.



*/
