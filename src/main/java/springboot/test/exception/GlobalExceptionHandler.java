package springboot.test.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    // 自訂 Exception 錯誤處理
    @ExceptionHandler(StatusException.class)
    public ResponseEntity<?> handleRequestException(StatusException ex) {
        ex.printStackTrace();
        JsonNode jsonNode;
        if (ex.getJsonNode() != null) {
            jsonNode = ex.getJsonNode();
        } else {
            jsonNode = objectMapper.createObjectNode().put("message", ex.getMessage());
        }
        return ResponseEntity.status(ex.getCode()).body(jsonNode);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body, final HttpHeaders headers,
                                                             final HttpStatus status, final WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            return handleArgumentInvalid((MethodArgumentNotValidException) ex);
        }
        ex.printStackTrace();
        JsonNode jsonNode = objectMapper.createObjectNode().put("message", "所輸入的參數資料型別不正確或是某些參數遺漏了。");
        return ResponseEntity.status(status).body(jsonNode);
    }

    // 資料驗證錯誤處理
    private ResponseEntity<Object> handleArgumentInvalid(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (ObjectError allError : ex.getBindingResult().getAllErrors()) {
            message.append(allError);
        }
        log.error("\t[Exception] 輸入參數的資料型別不正確。"
                + "\n 調用方法：" + ex.getParameter().getDeclaringClass() + "." + ex.getParameter().getMethod().getName()
                + "\n 輸入参数名稱：" + ex.getBindingResult().getObjectName()
                + "\n 錯誤的参数值：" + ex.getBindingResult().getTarget()
                + "\n 錯誤訊息：" + message);
        JsonNode jsonNode = objectMapper.createObjectNode().put("message", "所輸入的參數資料型別不正確或是某些參數遺漏了。");

        return ResponseEntity.status(400).body(jsonNode);
    }
}
