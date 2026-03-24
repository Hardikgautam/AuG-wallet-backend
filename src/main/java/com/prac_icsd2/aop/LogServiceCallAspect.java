package com.prac_icsd2.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogServiceCallAspect {

	@Pointcut(
			
			"@annotation(com.prac_icsd2.aop.LogServiceCall) || "+
			"@within(com.prac_icsd2.aop.LogServiceCall"
			)
	public void logServiceCallPointcut() {}
	
	@Before("logServiceCallPointcut()")
	public void logBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		Object[] args = joinPoint.getArgs();
		
		log.info(">>>>> SERVICE CALL {}.{}() | args: {}",
				className, methodName, Arrays.toString(args));
		
	}
	
	 @AfterReturning(pointcut = "logServiceCallPointcut()", returning = "result")
	    public void logAfterReturning(JoinPoint joinPoint, Object result) {
	        String methodName = joinPoint.getSignature().getName();
	        String className  = joinPoint.getTarget().getClass().getSimpleName();

	        log.info("<<< SERVICE CALL {}.{}() | returned: {}",
	                 className, methodName, result);
	    }
	  @AfterThrowing(pointcut = "logServiceCallPointcut()", throwing = "ex")
	    public void logException(JoinPoint joinPoint, Throwable ex) {
	        String methodName = joinPoint.getSignature().getName();
	        String className  = joinPoint.getTarget().getClass().getSimpleName();

	        log.error("!!! SERVICE CALL FAILED {}.{}() | exception: {} | message: {}",
	                  className, methodName,
	                  ex.getClass().getSimpleName(),
	                  ex.getMessage());
	    }
	
}
