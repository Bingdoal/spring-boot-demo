package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.kickstart.spring.error.ErrorContext;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handleStatusException(RestException ex) {
        log.error("{}: {}", ex.getCode(), ex.getMessage(), ex);
        JsonNode jsonNode = ex.getJsonNode();
        return ResponseEntity.status(ex.getCode()).body(jsonNode);
    }

    @ExceptionHandler(value = GraphQLStatusException.class)
    public GraphQLError toCustomError(GraphQLStatusException ex, ErrorContext errorContext) {
        log.error("{}: {}", ex.getCode(), ex.getMessage(), ex);
        Map<String, Object> extensions =
                Optional.ofNullable(errorContext.getExtensions()).orElseGet(HashMap::new);
        extensions.put("status", ex.getCode());
        extensions.put("message", ex.getMessage());
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .extensions(extensions)
                .locations(errorContext.getLocations())
                .errorType(errorContext.getErrorType())
                .path(errorContext.getPath())
                .build();
    }

    @ExceptionHandler(RuntimeStatusException.class)
    public ResponseEntity<?> handleRuntimeStatusException(RuntimeStatusException ex) {
        log.error("{}: {}", ex.getCode(), ex.getMessage(), ex);
        JsonNode jsonNode = ex.getJsonNode();
        return ResponseEntity.status(ex.getCode()).body(jsonNode);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> handleSQLException(PSQLException ex) {
        log.error("PSQLException: {}", ex.getMessage(), ex);
        JsonNode jsonNode;
        if (ex.getServerErrorMessage() != null) {
            jsonNode = objectMapper.createObjectNode().put("message", ex.getServerErrorMessage().getDetail());
        } else {
            jsonNode = objectMapper.createObjectNode().put("message", ex.getMessage());
        }
        return ResponseEntity.status(400).body(jsonNode);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        String defaultMessage = constraintViolation.getMessage();
        log.error("\t[Exception] 輸入的參數資料驗證失敗。"
                + "\t\n 調用類別：" + constraintViolation.getRootBeanClass().getName()
                + "\t\n 参數名稱：" + constraintViolation.getPropertyPath()
                + "\t\n 錯誤訊息：" + ex.getLocalizedMessage());

        JsonNode jsonNode = objectMapper.createObjectNode().put("message", defaultMessage);
        return ResponseEntity.status(400).body(jsonNode);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {

        if (ex instanceof MethodArgumentNotValidException) {
            return handleArgumentInvalid((MethodArgumentNotValidException) ex);
        }
        // 處理入參為 Enum 類型的錯誤訊息
        if (ex.getCause() != null && ex.getCause() instanceof InvalidFormatException) {
            final Pattern enumErrorMessagePattern = Pattern.compile("from String \"(.*)\": not one of the values accepted for Enum class: (\\[.*\\])");
            final Matcher matcher = enumErrorMessagePattern.matcher(ex.getCause().getMessage());
            if (matcher.find()) {
                final Matcher matcher2 = Pattern.compile("\\[\"(.*)\"\\]\\)$").matcher(ex.getCause().getMessage());
                if (matcher2.find()) {
                    final String fieldName = matcher2.group(1);
                    final String rejectedValue = matcher.group(1);
                    final String enumMemberSet = matcher.group(2);
                    final String message = MessageFormat.format("The input value ({0}) of argument ''{1}'' must be one of {2}.", rejectedValue, fieldName, enumMemberSet);
                    log.error("\t[CEH] Enum validation is failed. Message: {}", message);
                    final ObjectNode json = JsonNodeFactory.instance.objectNode().put("code", status.value()).put("message", message);
                    return new ResponseEntity<>(json, status);
                }
            }
        }

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleTypeMismatch((MethodArgumentTypeMismatchException) ex);
        }
        log.error("Error: ", ex);
        JsonNode jsonNode = objectMapper.createObjectNode().put("message", ex.getLocalizedMessage());
        return ResponseEntity.status(status).body(jsonNode);
    }

    // 資料驗證錯誤處理
    private ResponseEntity<Object> handleArgumentInvalid(MethodArgumentNotValidException ex) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        final ArrayNode messages = jsonNode.putArray("message");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            final String rejectedValue = ((FieldError) error).getRejectedValue() == null ? "null" : ((FieldError) error).getRejectedValue().toString();
            final String errorMessage = error.getDefaultMessage();
            final String message = MessageFormat.format("The input value ({0}) of argument ''{1}'' {2}.", rejectedValue, fieldName, errorMessage);
            messages.add(message);
        });

        FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        log.error("\t[Exception] 輸入的參數資料驗證失敗。"
                + "\t\n 調用方法：" + ex.getParameter().getDeclaringClass().getName() + "." + ex.getParameter().getMethod().getName()
                + "\t\n 錯誤的参數：" + ex.getBindingResult().getTarget().getClass().getName() + "." + fieldError.getField()
                + "\t\n 錯誤的值：" + fieldError.getRejectedValue()
                + "\t\n 錯誤訊息：" + ex.getLocalizedMessage());

        return ResponseEntity.status(400).body(jsonNode);
    }

    private ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("\t[Exception] 輸入參數的資料型別不正確。"
                + "\t\n 調用方法：" + ex.getParameter().getDeclaringClass() + "." + ex.getParameter().getMethod().getName()
                + "\t\n 輸入参數名稱：" + ex.getName()
                + "\t\n 錯誤的参數值：" + ex.getValue()
                + "\t\n 錯誤訊息：" + ex.getLocalizedMessage());

        JsonNode jsonNode = objectMapper.createObjectNode().put("message", ex.getLocalizedMessage());
        return ResponseEntity.status(400).body(jsonNode);
    }
}
