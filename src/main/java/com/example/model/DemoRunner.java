package com.example.runner;

import com.example.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final User user;

    // Ін'єкція біну User через конструктор
    public DemoRunner(User user) {
        this.user = user;
    }

    @Override
    public void run(String... args) {
        System.out.println("DemoRunner: виклик сеттерів...");
        user.setFirstName("Ivan");
        user.setFirstName("Petr");
        user.setLastName("Ivanov");
    }
}