package com.mercadona.promotionmanagement;

import com.mercadona.promotionmanagement.core.entity.Administrateur;
import com.mercadona.promotionmanagement.core.repository.AdministrateurRepository;
import com.mercadona.promotionmanagement.core.service.AdministrateurService;
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
	public CommandLineRunner setupDefaultAdmin(AdministrateurService administrateurService) {
		return args -> {
			administrateurService.saveAdmin("admin", "admin");
		};
	}*/


}
