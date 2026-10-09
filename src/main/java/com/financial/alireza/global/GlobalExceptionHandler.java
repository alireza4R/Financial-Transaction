package com.financial.alireza.global;

import com.financial.alireza.accounts.exception.AccountExistException;
import com.financial.alireza.accounts.exception.AccountNotFoundException;
import com.financial.alireza.transactions.exception.AccountDoesntExistException;
import com.financial.alireza.transactions.exception.TransactionFailedException;
import com.financial.alireza.transactions.exception.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public record ErrorResponse(String message) {}

    @ExceptionHandler({
            AccountNotFoundException.class,
            AccountDoesntExistException.class
    })
    public ResponseEntity<Object> handleAccountNotFound() {
        return error(HttpStatus.NOT_FOUND, "Account not found");
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> handleTransactionNotFound() {
        return error(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<Object> handleAccountExists() {
        return error(HttpStatus.CONFLICT, "Account already exists");
    }

    @ExceptionHandler(TransactionFailedException.class)
    public ResponseEntity<Object> handleTransactionFailed() {
        return error(HttpStatus.CONFLICT, "Transaction failed");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {

        String message = switch (statusCode.value()) {
            case 400 -> "Invalid request data";
            case 404 -> "Resource not found";
            case 405 -> "HTTP method not allowed";
            case 406 -> "Requested response format is not supported";
            case 415 -> "Unsupported content type";
            default -> statusCode.is5xxServerError()
                    ? "Internal server error"
                    : "Request failed";
        };

        if (statusCode.is5xxServerError()) {
            log.error("Request processing failed", ex);
        }

        return super.handleExceptionInternal(
                ex,
                new ErrorResponse(message),
                headers,
                statusCode,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception ex) {
        log.error("Unexpected request error", ex);

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"
        );
    }

    private ResponseEntity<Object> error(
            HttpStatus status,
            String message) {

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message));
    }
}
