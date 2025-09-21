package io.github.gugbab2.srt_clone_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.github.gugbab2.srt_clone_service") // Add this line
public class SrtCloneServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrtCloneServiceApplication.class, args);
	}

}
