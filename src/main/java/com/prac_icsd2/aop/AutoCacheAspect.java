package com.prac_icsd2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class AutoCacheAspect {

   
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Around("@annotation(com.prac_icsd2.aop.AutoCache)")
    public Object autoCache(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method             = signature.getMethod();
        AutoCache autoCache       = method.getAnnotation(AutoCache.class);

        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Object[] args     = joinPoint.getArgs();

        String cacheKey = className + "." + methodName + "(" + Arrays.toString(args) + ")";

        CacheEntry existing = cache.get(cacheKey);
        if (existing != null && !existing.isExpired()) {
            log.info(">>> CACHE HIT  {} | key: {}", methodName, cacheKey);
            return existing.getValue();
        }

        log.info(">>> CACHE MISS {} | key: {} | hitting database...", methodName, cacheKey);

        Object result = joinPoint.proceed();

        int ttlSeconds = autoCache.ttl();
        cache.put(cacheKey, new CacheEntry(result, ttlSeconds));

        log.info(">>> CACHE STORED {} | key: {} | ttl: {} seconds", methodName, cacheKey, ttlSeconds);

        return result;
    }

    private static class CacheEntry {
        private final Object value;
        private final long   expiryTime;

        public CacheEntry(Object value, int ttlSeconds) {
            this.value      = value;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }

        public Object getValue()   { return value; }
        public boolean isExpired() { return System.currentTimeMillis() > expiryTime; }
    }
}