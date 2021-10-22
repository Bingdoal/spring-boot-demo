package springboot.test.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<?> handleStatusException(StatusException ex) {
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
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body, final HttpHeaders headers,
                                                             final HttpStatus status, final WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            return handleArgumentInvalid((MethodArgumentNotValidException) ex);
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
        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("\t[Exception] 輸入的參數資料驗證失敗。"
                + "\t\n 調用方法：" + ex.getParameter().getDeclaringClass() + "." + ex.getParameter().getMethod().getName()
                + "\t\n 輸入参數名稱：" + ex.getBindingResult().getObjectName()
                + "\t\n 錯誤的参數值：" + ex.getBindingResult().getTarget()
                + "\t\n 錯誤訊息：" + ex.getLocalizedMessage());

        JsonNode jsonNode = objectMapper.createObjectNode().put("message", defaultMessage);
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
