package com.example.runner;

import com.example.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Переконайтеся, що аннотація присутня
public class DemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("DemoRunner: виклик сеттерів...");
        User user = new User();
        user.setFirstName("Ivan");
        user.setFirstName("Petr"); // Зміна значення
        user.setLastName("Ivanov");
    }
}