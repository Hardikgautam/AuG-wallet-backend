package com.prac_icsd2.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CheckRoleAspect {

	
	private static final String CURRENT_USER_ROLE = "USER";
	
	@Around("@annotation(com.prac_icsd2.aop.CheckRole)")
	public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable{
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		
		CheckRole checkRole = method.getAnnotation(CheckRole.class);
		String requiredRole = checkRole.value();
		
		String methodName = method.getName();
		
		String className = joinPoint.getTarget().getClass().getSimpleName();
		
		log.info(">>>> ROLE CHECK ON {}.{}() | required : {} | current: {}",
				className, methodName, requiredRole,CURRENT_USER_ROLE);
		
		
		
		if (!CURRENT_USER_ROLE.equalsIgnoreCase(requiredRole)) {
			log.error("!!! ACCESS DENIED ON {}.{}() | required:{} | current:{}",
					className, methodName, requiredRole,CURRENT_USER_ROLE);
			
			throw new RuntimeException("Access Denied - required role :"+ requiredRole);
		}
		
		log.info("ACCESS ACCESS GRANTED on {}.{}()", className, methodName);
			       return joinPoint.proceed();
	}
	
}
