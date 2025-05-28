/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author HP
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    /*
    //controla los errores de los campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlderMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                          WebRequest webRequest) {
        System.out.println("entreaaaa");
        Map<String, String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
                    
                    String clave = ((FieldError) error).getField();
                    String valor = error.getDefaultMessage();
                    mapErrors.put(clave, valor);
                }
        );
        ApiResponse apiResponse = new ApiResponse(mapErrors.toString(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    //controla los errores not found 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        System.out.println("entreaaaa2");
        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    
    //controla los errores globales de los path en 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handlerNoHandlerFoundException(NoHandlerFoundException  exception,
                                                                        WebRequest webRequest) {
        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    
    //controla los errores de logica o de los catch en general 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handlerBadRequestException(BadRequestException exception,
                                                                        WebRequest webRequest) {
        System.out.println(exception.getClass());
        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiResponse> handlerDuplicateKeyException(DuplicateKeyException exception,
                                                                        WebRequest webRequest) {
        System.out.println("entreaaaa44");
        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    //controla los errores de varios tipos y globalizrlo con un error 500
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerException(Exception exception,
                                                                  WebRequest webRequest) {
        System.out.println("entreaaaa555");
        ApiResponse apiResponse = new ApiResponse(exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado: " + ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        System.out.println("handleBadCredentialsException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta.");
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        System.out.println("handleAccessDenied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: token inválido o expirado");
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredToken(ExpiredJwtException ex) {
        System.out.println("handleExpiredToken");
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Token expirado");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleInvalidToken(JwtException ex,
                                                                  WebRequest webRequest) {
        System.out.println("handleInvalidToken");
        ApiResponse apiResponse = new ApiResponse("Token inválido", webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
    */
    

}
