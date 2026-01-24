package subscription.interfaceadapter.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import subscription.application.exception.ApplicationException;
import subscription.domain.exception.DomainException;
import subscription.interfaceadapter.dto.response.ErrorResponse;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Erros de aplicação
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplication(ApplicationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getErrorCode().code(),
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    //Erros de payload inválido
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidPayload(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ErrorCode.INVALID_PAYLOAD.code(),
                        ErrorCode.INVALID_PAYLOAD.defaultMessage(),
                        Instant.now()
                ));
    }

    //Erros de domínio
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getErrorCode().code(),
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    //Endpoint não encontrado
    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundEndpoint(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ErrorCode.ENDPOINT_NOT_FOUND.code(),
                        ErrorCode.ENDPOINT_NOT_FOUND.defaultMessage(),
                        Instant.now()
                ));
    }

    //Erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ErrorCode.INTERNAL_ERROR.code(),
                        ErrorCode.INTERNAL_ERROR.defaultMessage(),
                        Instant.now()
                ));
    }
}
