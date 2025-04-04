package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class UserSetterLoggerAspect {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    private static final String LOG_PATH = "logs.txt"; // Шлях до файлу логів

    // Аспект для всіх "set" методів класу User
    @Around("execution(* com.example.model.User.set*(..))")
    public Object logSetter(ProceedingJoinPoint joinPoint) throws Throwable {
        // Перевіряємо, чи аспект активний
        System.out.println("[DEBUG] Аспект активний. Перехоплюється метод: " + joinPoint.getSignature().getName());

        // Отримуємо ім'я методу
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs(); // Аргументи методу (наприклад, значення для "setter")
        String argument = args.length > 0 ? args[0].toString() : "No Args"; // Виводимо аргумент або "No Args"

        // Час початку
        LocalDateTime startTime = LocalDateTime.now();
        logToFile("begin " + methodName + "() " + startTime.format(TIME_FORMATTER));
        logToFile("data: " + argument);

        Object result;

        try {
            // Виконуємо справжній метод (викликаємо setter)
            result = joinPoint.proceed();
        } finally {
            // Час завершення
            LocalDateTime endTime = LocalDateTime.now();
            logToFile("end " + methodName + "() " + endTime.format(TIME_FORMATTER) + "\n");
        }

        return result;
    }

    // Метод для запису даних у файл
    private void logToFile(String message) {
        try {
            // Перевіряємо, чи існує файл, якщо ні — створюємо
            Path filePath = Paths.get(LOG_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("[DEBUG] Файл " + LOG_PATH + " створено.");
            }

            try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
                writer.write(message + "\n"); // Записуємо повідомлення у файл
                System.out.println("[DEBUG] Записано в файл: " + message); // Вивід у консоль для дебагу
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Помилка запису в файл: " + e.getMessage()); // Якщо виникає помилка
        }
    }
}