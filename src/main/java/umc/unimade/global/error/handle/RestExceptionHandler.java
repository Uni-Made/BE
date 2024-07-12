package umc.unimade.global.error.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.unimade.global.error.ErrorResponse;
import umc.unimade.global.error.exception.RestException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleException(RestException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = ErrorResponse.from(httpStatus);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
