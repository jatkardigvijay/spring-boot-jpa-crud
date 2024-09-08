package com.jbd.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspectConfig {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspectConfig.class);

	@Pointcut("execution(* com.jbd.controller..*.*(..))")
	public void insideController() {
	}

	@Pointcut("execution(* com.jbd.config..*.*(..))")
	public void insideConfig() {
	}

	@Pointcut("execution(* com.jbd.dao..*.*(..))")
	public void insideDao() {
	}

	@Pointcut("execution(* com.jbd.service..*.*(..))")
	public void insideService() {
	}

	@Before("insideController()")
	public void beforeInsideController(JoinPoint joinPoint) {
		logger.info("Executing the method: {}", joinPoint.getSignature());
	}

	@AfterReturning(value = "insideController()", returning = "result")
	public void afterReturningInsideController(JoinPoint joinPoint, Object result) {
		logger.info("{} Returned with value {}", joinPoint.getSignature(), result);
	}

	@AfterThrowing(value = "insideController()", throwing = "result")
	public void afterThrowingInsideController(JoinPoint joinPoint, Object result) {
		logger.error("{} Exception thrown with value {}", joinPoint.getSignature(), result);
	}

	@Before("insideDao()")
	public void beforeInsideDao(JoinPoint joinPoint) {
		logger.info("Executing the method: {}", joinPoint.getSignature());
	}

	@AfterReturning(value = "insideDao()", returning = "result")
	public void afterReturningInsideDao(JoinPoint joinPoint, Object result) {
		logger.info("{} Returned with value {}", joinPoint.getSignature(), result);
	}

	@AfterThrowing(value = "insideDao()", throwing = "result")
	public void afterThrowingInsideDao(JoinPoint joinPoint, Object result) {
		logger.error("{} Exception thrown with value {}", joinPoint.getSignature(), result);
	}

	@Before("insideService()")
	public void beforeInsideService(JoinPoint joinPoint) {
		logger.info("Executing the method: {}", joinPoint.getSignature());
	}

	@AfterReturning(value = "insideService()", returning = "result")
	public void afterReturningInsideService(JoinPoint joinPoint, Object result) {
		logger.info("{} Returned with value {}", joinPoint.getSignature(), result);
	}

	@AfterThrowing(value = "insideService()", throwing = "result")
	public void afterThrowingInsideService(JoinPoint joinPoint, Object result) {
		logger.error("{} Exception thrown with value {}", joinPoint.getSignature(), result);
	}
}