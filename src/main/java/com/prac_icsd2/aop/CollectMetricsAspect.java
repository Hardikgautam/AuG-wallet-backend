package com.prac_icsd2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
@Slf4j
public class CollectMetricsAspect {

    private final ConcurrentHashMap<String, AtomicLong> callCount   = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalTime   = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.prac_icsd2.aop.CollectMetrics) || " +
              "@within(com.prac_icsd2.aop.CollectMetrics)")
    public void collectMetricsPointcut() {}

    @Around("collectMetricsPointcut()")
    public Object collectMetrics(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String key        = className + "." + methodName;

        callCount.putIfAbsent(key, new AtomicLong(0));
        totalTime.putIfAbsent(key, new AtomicLong(0));

        long start  = System.currentTimeMillis();
        Object result = joinPoint.proceed(); 
        long timeTaken = System.currentTimeMillis() - start;

        long calls   = callCount.get(key).incrementAndGet();
        long total   = totalTime.get(key).addAndGet(timeTaken);
        long average = total / calls;

        log.info(">>> METRICS {}.{}() | calls: {} | timeTaken: {} ms | totalTime: {} ms | avgTime: {} ms",
                 className, methodName, calls, timeTaken, total, average);

        return result;
    }
}