package tech.xavi.flatbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RealistaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RealistaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
