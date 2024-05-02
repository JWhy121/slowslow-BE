package com.hani.examinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class ExaminfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExaminfoApplication.class, args);
	}

	@GetMapping("/")
	public String homeRedirect() {
		return "redirect:/boards";
	}

}
