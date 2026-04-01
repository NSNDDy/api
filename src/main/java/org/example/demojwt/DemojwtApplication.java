package org.example.demojwt;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DemojwtApplication {

    @PostConstruct
    public void init() {
        // Set default timezone to UTC for server consistency
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DemojwtApplication.class,
                              args);
    }
}
