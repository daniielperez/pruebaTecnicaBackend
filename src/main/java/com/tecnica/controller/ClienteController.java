package com.tecnica.controller;

import com.tecnica.dto.ClienteDto;
import com.tecnica.entity.Cliente;
import com.tecnica.services.ClienteService;
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
    public ResponseEntity<List<ClienteDto>> listar() {
        return new ResponseEntity<>(clienteService.obtenerTodosLosCliente(), HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ClienteDto> buscarPorSharedKey(@RequestParam String sharedKey) {
        return new ResponseEntity<>(clienteService.obtenetClientePorSharedKey(sharedKey), HttpStatus.OK);
    }

    @GetMapping("/buscarMail")
    public ResponseEntity<ClienteDto> buscarPorEmail(@RequestParam String email) {
        return new ResponseEntity<>(clienteService.obtenetClientePorEmail(email), HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<ClienteDto> CrearCliente(@Valid @RequestBody ClienteDto cli){
        return new ResponseEntity<>(clienteService.guardarCliente(cli), HttpStatus.OK);
    }

    @PutMapping("/editar")
    public ResponseEntity<ClienteDto> EditarCliente(@Valid @RequestBody ClienteDto cli){
        return new ResponseEntity<>(clienteService.actualizarCliente(cli), HttpStatus.OK);
    }

}
