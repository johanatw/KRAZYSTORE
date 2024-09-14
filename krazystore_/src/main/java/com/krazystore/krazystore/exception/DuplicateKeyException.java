/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author HP
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateKeyException extends RuntimeException{
    private String fieldName;

    public DuplicateKeyException(String fieldName) {
        super(String.format("Este %s está siendo usado por otro cliente",  fieldName));
        this.fieldName = fieldName;

    }
}
