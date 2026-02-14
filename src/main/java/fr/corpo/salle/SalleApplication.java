package fr.corpo.salle;

import fr.corpo.salle.config.DocumentStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(DocumentStorageProperties.class)
@SpringBootApplication
public class SalleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalleApplication.class, args);
	}

}
