package vn.hhh.phong_tro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhongTroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhongTroApplication.class, args);
	}

}
