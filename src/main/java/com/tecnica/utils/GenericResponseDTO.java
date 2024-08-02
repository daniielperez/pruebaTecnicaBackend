package com.tecnica.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class GenericResponseDTO {

    private Object data;
    private boolean success;
    private String message;
    private HttpStatus status;
    private String title = "";

    public GenericResponseDTO(Object data, boolean success, String message, HttpStatus status) {
        this.data = data;
        this.success = success;
        this.message = message;
        this.status = status;
        this.title = success ? "" : "¡Oops! Ocurrió un error";
    }

    public GenericResponseDTO(Object data, HttpStatus status) {
        this.data = data;
        this.success = true;
        this.message = UtilConstantes.RESPONSE_FIND;
        this.status = status;
        this.title = UtilConstantes.TITTLE_FIND;
    }
}
