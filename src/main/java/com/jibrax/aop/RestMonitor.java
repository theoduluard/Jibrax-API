package com.jibrax.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestMonitor {

    @Before("execution(* com.jibrax.controller..*(..))")
    public void logRestAccess(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        System.out.println("REST call : " + className + "::" + methodName);
    }

}
