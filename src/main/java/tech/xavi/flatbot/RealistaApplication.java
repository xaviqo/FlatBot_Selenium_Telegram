package tech.xavi.flatbot;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.xavi.flatbot.service.ScheduledTaskService;

@AllArgsConstructor
@SpringBootApplication
public class RealistaApplication implements CommandLineRunner {

    private final ScheduledTaskService taskService;

    public static void main(String[] args) {
        SpringApplication.run(RealistaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        taskService.run();
    }
}
