package pofol.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pofol.shop.domain.enums.Role;
import pofol.shop.repository.ItemRepository;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaAuditing
public class ShopApplication {
	public static void main(String[] args) {

		SpringApplication.run(ShopApplication.class, args);
	}
}
