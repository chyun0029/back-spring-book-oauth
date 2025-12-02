package me.book_ch8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookCh8Application {

	public static void main(String[] args) {
		SpringApplication.run(BookCh8Application.class, args);
	}

}
