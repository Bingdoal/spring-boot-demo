package springboot.test.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    // 自訂 Exception 錯誤處理
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
            jsonNode = objectMapper.createObjectNode().put("message", ex.getLocalizedMessage());
        }
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
        if (ex instanceof HttpMessageNotReadableException
                || ex instanceof MissingServletRequestParameterException) {
            return ResponseEntity.status(status).body(objectMapper.createObjectNode().put("message",
                    "所輸入的參數資料型別不正確或是某些參數遺漏了。"));
        }
        JsonNode jsonNode = objectMapper.createObjectNode().put("message", ex.getLocalizedMessage());
        return ResponseEntity.status(status).body(jsonNode);
    }

    // 資料驗證錯誤處理
    private ResponseEntity<Object> handleArgumentInvalid(MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("\t[Exception] 輸入參數的資料型別不正確。"
                + "\n 調用方法：" + ex.getParameter().getDeclaringClass() + "." + ex.getParameter().getMethod().getName()
                + "\n 輸入参数名稱：" + ex.getBindingResult().getObjectName()
                + "\n 錯誤的参数值：" + ex.getBindingResult().getTarget()
                + "\n 錯誤訊息：" + ex.getLocalizedMessage());

        JsonNode jsonNode = objectMapper.createObjectNode().put("message", defaultMessage);
        return ResponseEntity.status(400).body(jsonNode);
    }

    private ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("\t[Exception] 輸入參數的資料型別不正確。"
                + "\n 調用方法：" + ex.getParameter().getDeclaringClass() + "." + ex.getParameter().getMethod().getName()
                + "\n 輸入参数名稱：" + ex.getName()
                + "\n 錯誤的参数值：" + ex.getValue()
                + "\n 錯誤訊息：" + ex.getLocalizedMessage());

        JsonNode jsonNode = objectMapper.createObjectNode().put("message", ex.getLocalizedMessage());
        return ResponseEntity.status(400).body(jsonNode);
    }
}
