package com.mercadona.promotionmanagement;

import com.mercadona.promotionmanagement.core.service.UtilisateurService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class PromotionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromotionManagementApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner setupDefaultAdmin(UtilisateurService utilisateurService) {
		return args -> {
			utilisateurService.saveAdmin("admin", "admin");
		};
	}*/


}
