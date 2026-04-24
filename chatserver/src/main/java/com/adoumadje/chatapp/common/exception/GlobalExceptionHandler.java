package com.adoumadje.chatapp.common.exception;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.common.dto.ValidationErrorDto;
import com.adoumadje.chatapp.common.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Value("spring.profiles.active")
    private String profile;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception) {
        if(!profile.equals("prod")) {
            logger.error(exception.getMessage());
        }
        ResponseDto responseDto = new ResponseDto(Constants.STATUS_NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        if(!profile.equals("prod")) {
            logger.error(exception.getMessage());
        }
        ResponseDto responseDto = new ResponseDto(Constants.STATUS_BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDto>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<ValidationErrorDto> validationErrorDtos = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorDto(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(validationErrorDtos, HttpStatus.BAD_REQUEST);
    }
}
