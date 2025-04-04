package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    // Pointcut для всіх методів у сервісах, крім remove*()
    @Pointcut("execution(* com.example.service..*(..)) && !execution(* com.example.service..*.removeBook*(..))")
    public void allMethodsExceptRemoveBook() {}

    // Логування методів сервісів
    @Around("allMethodsExceptRemoveBook()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs(); // Отримуємо аргументи методу
        String arguments = (args.length > 0) ? getArgumentsAsString(args) : "No Args";

        // Початковий час та логування
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("[Service Log] Method " + methodName + " started at " + startTime.format(TIME_FORMATTER));
        System.out.println("[Service Log] Arguments: " + arguments);

        Object result;

        try {
            result = joinPoint.proceed(); // Виконання методу
        } finally {
            // Логування завершення і часу виконання
            LocalDateTime endTime = LocalDateTime.now();
            System.out.println("[Service Log] Method " + methodName + " completed at " + endTime.format(TIME_FORMATTER));
            System.out.println("[Service Log] Execution time: " + (endTime.getNano() - startTime.getNano()) / 1_000_000 + " ms");
        }

        return result;
    }

    // Форматування аргументів для зручного виводу
    private String getArgumentsAsString(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg).append(", ");
        }
        return sb.substring(0, sb.length() - 2); // Видаляємо зайву кому і пробіл
    }
}