package com.twins.clone.common.exception;

import com.twins.clone.common.dto.CommonErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegal(IllegalArgumentException e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder()
                .status_code(400)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ce_dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder()
                .status_code(400)
                .error_message(e.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ce_dto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> security(AccessDeniedException e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder()
                .status_code(403)
                .error_message("접근 권한이 없습니다")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ce_dto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuch(NoSuchElementException e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder()
                .status_code(404)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ce_dto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notfoundEntity(EntityNotFoundException e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder().status_code(404).error_message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ce_dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        e.printStackTrace();
        CommonErrorDto ce_dto = CommonErrorDto.builder()
                .status_code(500)
                .error_message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ce_dto);
    }
}
