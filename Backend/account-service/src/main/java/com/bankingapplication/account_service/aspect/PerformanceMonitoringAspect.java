package com.bankingapplication.account_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void controllerEndpoints() {
    }

    @Pointcut("execution(* com.bankingapplication.account_service.service.*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("execution(* com.bankingapplication.account_service.repository.*.*(..))")
    public void repositoryMethods() {
    }

    @Around("controllerEndpoints()")
    public Object measureControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return measurePerformance(joinPoint, "CONTROLLER");
    }

    @Around("serviceMethods()")
    public Object measureServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return measurePerformance(joinPoint, "SERVICE");
    }

    @Around("repositoryMethods() && !execution(* com.bankingapplication.account_service.repository.*.count*(..))")
    public Object measureRepositoryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return measurePerformance(joinPoint, "REPOSITORY");
    }

    private Object measurePerformance(ProceedingJoinPoint joinPoint, String componentType) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            if (executionTime > 500) {
                log.warn("[PERFORMANCE] [{}] {}.{} execution time: {} ms",
                        componentType, className, methodName, executionTime);
            } else {
                log.debug("[PERFORMANCE] [{}] {}.{} execution time: {} ms",
                        componentType, className, methodName, executionTime);
            }
        }
    }
}
