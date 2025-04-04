package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    @Pointcut("execution(* com.example.service..*(..)) && !execution(* com.example.service..*.removeBook*(..))")
    public void allMethodsExceptRemoveBook() {}

    @Around("allMethodsExceptRemoveBook()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[Service Log] Method " + methodName + " started");
        Object result = joinPoint.proceed();
        System.out.println("[Service Log] Method " + methodName + " completed");
        return result;
    }
}