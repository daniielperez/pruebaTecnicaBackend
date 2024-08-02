package com.tecnica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDto {
    private Long id;
    private String sharedKey;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    private String telefono;
    @Email(message = "El email debe ser válido")
    private String email;
    private Date fechaInicio;
    private Date fechaFin;
}
