package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class UserSetterLoggerAspect {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    @Around("execution(* com.example.model.User.set*(*))")
    public Object logSetter(ProceedingJoinPoint joinPoint) throws Throwable {
        // Додано логування для дебагу
        System.out.println("[AOP] Аспект активний для методу: " + joinPoint.getSignature().getName());

        String methodName = joinPoint.getSignature().getName();
        String property = methodName.replace("set", "").toLowerCase();
        Object value = joinPoint.getArgs()[0];

        LocalDateTime start = LocalDateTime.now();
        logToFile("begin " + property + "() " + start.format(TIME_FORMATTER));
        logToFile("data: " + value);

        Object result = joinPoint.proceed();

        LocalDateTime end = LocalDateTime.now();
        logToFile("end " + property + "() " + end.format(TIME_FORMATTER) + "\n");

        return result;
    }

    private void logToFile(String message) {
        String logPath = "C:/Users/music/IdeaProjects/Loguvanya/logs.txt"; // Абсолютний шлях
        try (FileWriter writer = new FileWriter(logPath, true)) {
            writer.write(message + "\n");
            System.out.println("Записано в файл: " + message); // Додано логування
        } catch (IOException e) {
            System.err.println("Помилка запису в файл: " + e.getMessage());
        }
    }
}