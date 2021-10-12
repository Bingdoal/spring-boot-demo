package springboot.test.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springboot.test.dto.ApiLogDto;
import springboot.test.exception.StatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ApiLoggerAspect {

    @Autowired
    ObjectMapper objectMapper;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "&& @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "&& @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void executePointcut() {
    }

    @Around("executePointcut() && @annotation(org.springframework.web.bind.annotation.ResponseStatus)")
    public Object logApi(final ProceedingJoinPoint joinPoint) throws Throwable {
        ApiLogDto apiLogDto = null;
        try {
            Object returnValue = joinPoint.proceed();
            apiLogDto = getApiLog(joinPoint, returnValue);
            return returnValue;
        } catch (StatusException e) {
            apiLogDto = getApiLog(joinPoint, e.getJsonNode());
            apiLogDto.setStatus(e.getCode());
            throw e;
        } finally {
            log.trace("ApiLog: \n{}", Objects.toString(apiLogDto, ""));
        }
    }

    private ApiLogDto getApiLog(ProceedingJoinPoint joinPoint, Object returnValue) throws IOException {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = attributes.getRequest();
        final String requestQueryString = (request.getQueryString() == null) ? "" : "?" + request.getQueryString();
        final String requestUri = request.getRequestURI() + requestQueryString;
        final String methodName = joinPoint.getSignature().getName();
        final HttpServletResponse response = attributes.getResponse();

        ApiLogDto apiLogDto = new ApiLogDto();
        apiLogDto.setUrl(requestUri);
        apiLogDto.setExecMethod(methodName);
        apiLogDto.setMethod(request.getMethod());
        if (request.getMethod().equalsIgnoreCase("POST")
                || request.getMethod().equalsIgnoreCase("PUT")) {
            JsonNode requestBody = objectMapper.valueToTree(joinPoint.getArgs());
            apiLogDto.setRequestBody(requestBody);
        }
        if (response != null) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            ResponseStatus responseStatus = signature.getMethod().getAnnotation(ResponseStatus.class);
            apiLogDto.setStatus(responseStatus.value().value());
            if (returnValue != null) {
                apiLogDto.setResponseBody(objectMapper.valueToTree(returnValue));
            }
        }
        return apiLogDto;
    }
}
