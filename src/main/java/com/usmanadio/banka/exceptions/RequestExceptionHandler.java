package com.usmanadio.banka.exceptions;

import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.responses.Response;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllExceptions(Exception exception) {
//        Response<User> response = new Response(HttpStatus.INTERNAL_SERVER_ERROR);
//        response.setMessage(exception.getMessage());
//        response.setDebugMessage(exception.getLocalizedMessage());
//        response.setError(exception.toString());
//        return buildResponseEntity(response);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintValidation(ConstraintViolationException exception) {
        Response<User> response = new Response(HttpStatus.BAD_REQUEST);
        response.addValidationErrors(exception.getConstraintViolations());
        response.setError("Validation Error");
        return buildResponseEntity(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException customException) {
        Response<User> response = new Response(customException.getStatus());
        response.setError(customException.getMessage());
        response.setStatus(customException.getStatus());
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response<User> response = new Response(HttpStatus.BAD_REQUEST);
        response.addValidationError(exception.getBindingResult().getAllErrors());
        response.setError("Validation Error");
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response<User> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.setError("Invalid input for type: " + exception.getRequiredType());
        response.setDebugMessage("Kindly check the request parameter or path variable");
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response<User> response = new Response<>(HttpStatus.METHOD_NOT_ALLOWED);
        response.setError(exception.getMethod());
        response.setMessage("Invalid request method");
        response.setDebugMessage("Put in the correct request type");
        return buildResponseEntity(response);
    }

    private ResponseEntity<Object> buildResponseEntity(Response<User> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }
}
