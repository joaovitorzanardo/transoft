package br.com.transoft.backend.config.migration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlywayManualRunner {

    private final Flyway flyway;

    public FlywayManualRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runFlywayAfterStartup() {
        System.out.println("Running Flyway migrations after JPA initialization...");
        flyway.migrate();
    }

}
