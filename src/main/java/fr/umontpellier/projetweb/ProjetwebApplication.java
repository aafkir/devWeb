package fr.umontpellier.projetweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetwebApplication.class, args);
		//pour avoir un numero de port different à chaque run
	}

}
