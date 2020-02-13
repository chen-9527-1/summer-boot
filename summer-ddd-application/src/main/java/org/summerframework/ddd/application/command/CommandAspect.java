package org.summerframework.ddd.application.command;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.summerframework.ddd.core.command.CommandResult;

@Component
@Aspect
public class CommandAspect {

    @Around("@annotation(org.summerframework.ddd.core.command.annotation.CommandService)")
    public Object restAspect(ProceedingJoinPoint pjp) {
        final Class<?> clazz = pjp.getTarget().getClass();
        final String className = clazz.getName();
        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final String methodName = methodSignature.getName();
        final String fullMethodName = className + "." + methodName;

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            result = CommandResult.FAILURE(e);
            LoggerFactory.getLogger(clazz).error(fullMethodName + " error", e);
        }

        return result;
    }

}
