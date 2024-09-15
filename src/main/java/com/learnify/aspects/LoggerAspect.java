package com.learnify.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

    @Around("execution(* com.learnify..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(joinPoint.getSignature().toString() + " Method Execution Start...");
        Instant start = Instant.now();
        Object returnedObj = joinPoint.proceed();
        Instant finish = Instant.now();
        long timeTaken = Duration.between(start, finish).toMillis();
        log.info("Time taken to Execute the method "+ joinPoint.getSignature().toString()+" is: "+
                timeTaken);
        return returnedObj;
    }

    @AfterThrowing(value = "execution(* com.learnify..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex){
        log.error("An Exception Occurred due to: "+ex.getMessage());
        System.out.println("Error is: ");
    }
}
