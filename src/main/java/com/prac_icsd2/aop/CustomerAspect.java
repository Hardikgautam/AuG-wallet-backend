package com.prac_icsd2.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class CustomerAspect {

	@Pointcut("@annotation(com.prac_icsd2.aop.TrackMethod)")
	public void trackMethodPointcut() {}
	
	
	@Around("trackMethodPointcut()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
		Integer id= (Integer) joinPoint.getArgs()[0];
		
		log.info(">>>>>  ENTERING GETCustomerById() | customerId: {}",id);
		long start =  System.currentTimeMillis();
		
		Object result = joinPoint.proceed();
		
		long timeTaken = System.currentTimeMillis() -start;
		log.info("<<<<<<  EXITING getCustomerById() | customerId;{} | timeTaken: {} ms",id,timeTaken);
	
		return result;
	}
	
	  @AfterThrowing(pointcut = "trackMethodPointcut()", throwing = "e")
	    public void logException(JoinPoint joinPoint, Throwable e) {
	        Integer id = (Integer) joinPoint.getArgs()[0];

	        log.error("!!!!!! EXCEPTION in getCustomerById() | customerId: {} | exception: {} | message: {}",
	                  id,
	                  e.getClass().getSimpleName(),
	                  e.getMessage());
	    }
	  
	  @Before("trackMethodPointcut()")
	  public void validateInput(JoinPoint joinPoint) {
		  Integer id = (Integer) joinPoint.getArgs()[0];
		  
		  if(id==null) {
			  log.warn("Null id is a passes to getCustomerByUd()");
		  }else if(id<=0) {
			  log.warn("Invalid is passed to getcustomerById() | id:{}",id);
		  }else {
			  log.info("Input valid | customerId: {}",id);
		  }
	  }
}
