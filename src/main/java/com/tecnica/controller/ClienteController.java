package com.tecnica.controller;

import com.tecnica.dto.ClienteDto;
import com.tecnica.entity.Cliente;
import com.tecnica.services.ClienteService;
import com.tecnica.utils.GenericResponseDTO;
import com.tecnica.utils.UtilConstantes;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cliente")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/listar")
    public ResponseEntity<GenericResponseDTO> listar() {
        GenericResponseDTO genericResponse = new GenericResponseDTO(
                clienteService.obtenerTodosLosCliente(),
                true,
                UtilConstantes.RESPONSE_FIND,
                HttpStatus.OK,
                UtilConstantes.TITTLE_FIND);
        return new ResponseEntity<GenericResponseDTO>(genericResponse, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<GenericResponseDTO> buscarPorSharedKey(@RequestParam String sharedKey) {
        GenericResponseDTO genericResponse = new GenericResponseDTO(
                clienteService.obtenetClientePorSharedKey(sharedKey),
                true,
                UtilConstantes.RESPONSE_FIND,
                HttpStatus.OK,
                UtilConstantes.TITTLE_FIND);
        return new ResponseEntity<GenericResponseDTO>(genericResponse, HttpStatus.OK);
    }

    @GetMapping("/buscarMail")
    public ResponseEntity<ClienteDto> buscarPorEmail(@RequestParam String email) {
        return new ResponseEntity<>(clienteService.obtenetClientePorEmail(email), HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<GenericResponseDTO> CrearCliente(@Valid @RequestBody ClienteDto cli){
        GenericResponseDTO genericResponse = new GenericResponseDTO(
                clienteService.guardarCliente(cli),
                true,
                UtilConstantes.RESPONSE_CREATED,
                HttpStatus.OK,
                UtilConstantes.TITTLE_CREATED);
        return new ResponseEntity<GenericResponseDTO>(genericResponse, HttpStatus.OK);
    }

    @PutMapping("/editar")
    public ResponseEntity<GenericResponseDTO> EditarCliente(@Valid @RequestBody ClienteDto cli){
        GenericResponseDTO genericResponse = new GenericResponseDTO(
                clienteService.actualizarCliente(cli),
                true,
                UtilConstantes.RESPONSE_UPDATE,
                HttpStatus.OK,
                UtilConstantes.TITTLE_UPDATE);
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

}
