package com.prac_icsd2.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogExecutionTimeAspect {

	@Pointcut("@annotation(com.prac_icsd2.aop.LogExecutionTime)")
	public void logExecutionTimePointcut() {}
	
	@Around("logExecutionTimePointcut()")
	public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable{
		
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		
		log.info(">>> Entering {}.{}()", className , methodName);
		
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long timeTaken = System.currentTimeMillis() - start;
		
		log.info("<<<Exiting {}.{}() | timeTaken: {} ms", className,methodName,timeTaken);
		return result;
		
		
	}
	
	
}
