package com.elice.slowslow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class SlowslowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlowslowApplication.class, args);
	}

}
