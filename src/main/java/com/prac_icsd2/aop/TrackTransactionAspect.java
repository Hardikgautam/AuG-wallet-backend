package com.prac_icsd2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TrackTransactionAspect {

    @Around("@annotation(com.prac_icsd2.aop.TrackTransaction)")
    public Object trackTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className  = joinPoint.getTarget().getClass().getSimpleName();

        log.info(">>> TRANSACTION BEGIN  {}.{}()", className, methodName);

        try {
            Object result = joinPoint.proceed();

            log.info("TRANSACTION COMMIT  {}.{}()", className, methodName);
            return result;

        } catch (Throwable ex) {
            log.error("!!! TRANSACTION ROLLBACK {}.{}() | reason: {}",
                      className, methodName, ex.getMessage());
            throw ex; }
    }
}