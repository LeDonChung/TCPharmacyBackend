package vn.edu.iuh.fit.pharmacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TcPharmacyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcPharmacyBackendApplication.class, args);
	}

}
