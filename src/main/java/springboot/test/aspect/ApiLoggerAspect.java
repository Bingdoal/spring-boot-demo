package springboot.test.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiLoggerAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void executePointcut() {
    }

    @Around("executePointcut()")
    public Object logApi(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (final Exception e) {
            throw e;
        }
    }
}
