package com.tecnica.services;

import com.tecnica.dto.ClienteDto;
import com.tecnica.entity.Cliente;
import com.tecnica.manager.Log;
import com.tecnica.messages.ExceptionMessages;
import com.tecnica.repository.ClienteRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private Log log;

    @Override
    public List<ClienteDto> obtenerTodosLosCliente() {
        log.warm("Buscando listado de clientes");
        List<ClienteDto> clienteDtoList = clienteRepository.findAll().stream()
                .map(this::clienteDtoToCliente)
                .collect(Collectors.toList());
        return clienteDtoList;
    }

    @Override
    public ClienteDto obtenetClientePorSharedKey(String sharedKey) {
        log.warm("Buscando cliente por shared key");
        Cliente cli = clienteRepository.findOneByShared(sharedKey).orElse(null);
        ClienteDto cliDto = cli != null ? clienteDtoToCliente(cli) : null;
        return cliDto;
    }

    @Override
    public ClienteDto obtenetClientePorEmail(String email) {
        log.warm("Buscando cliente por email");
        Cliente cli = clienteRepository.findByEmail(email).orElse(null);
        ClienteDto cliDto = cli != null ? clienteDtoToCliente(cli) : null;
        return cliDto;
    }

    @Override
    public ClienteDto guardarCliente(ClienteDto clienteDto) {
        log.warm("Guardando cliente");
        validarCliente(clienteDto);
        Cliente cliente = clienteToClienteDto(clienteDto);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteDtoToCliente(clienteGuardado);
    }

    @Override
    public ClienteDto actualizarCliente(ClienteDto clienteDto) {
        log.info("Actualizando cliente", clienteDto.getId().toString());
        validarCliente(clienteDto);
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteDto.getId());
        if(clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setShared(clienteDto.getSharedKey());
            cliente.setNombre(clienteDto.getNombre());
            cliente.setTelefono(clienteDto.getTelefono());
            cliente.setEmail(clienteDto.getEmail());
            cliente.setInicio(clienteDto.getFechaInicio());
            cliente.setFin(clienteDto.getFechaFin());
            Cliente clienteActualizado = clienteRepository.save(cliente);
            log.info("Cliente actualizado exitosamente");
            return clienteDtoToCliente(clienteActualizado);
        }
        String mensajeError = ExceptionMessages.ERROR203.getMensajeConParametros();
        log.error(mensajeError);
        throw new ResponseStatusException(HttpStatus.CONFLICT, mensajeError);
    }

    private Cliente clienteToClienteDto(ClienteDto clienteDto){
        Cliente cliente =  Cliente.builder()
                .shared(clienteDto.getSharedKey())
                .telefono(clienteDto.getTelefono())
                .nombre(clienteDto.getNombre())
                .email(clienteDto.getEmail())
                .inicio(clienteDto.getFechaInicio())
                .fin(clienteDto.getFechaFin())
                .build();
        return cliente;
    }

    private ClienteDto clienteDtoToCliente(Cliente cliente) {
        return ClienteDto.builder()
                .id(cliente.getId())
                .sharedKey(cliente.getShared())
                .nombre(cliente.getNombre())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .fechaInicio(cliente.getInicio())
                .fechaFin(cliente.getFin())
                .build();
    }

    private void validarCliente(ClienteDto clienteDto){
        Set<ConstraintViolation<ClienteDto>> violations = validator.validate(clienteDto);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            log.warm(errorMessage);
            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
        }

        Optional<Cliente> clienteByEmail = clienteRepository.findByEmail(clienteDto.getEmail());
        clienteByEmail.ifPresent(c -> {
            if (!c.getId().equals(clienteDto.getId())) {
                String mensajeError = ExceptionMessages.ERROR202.getMensajeConParametros(clienteDto.getEmail());
                log.warm(mensajeError);
                throw new ResponseStatusException(HttpStatus.CONFLICT, mensajeError);
            }
        });

        Optional<Cliente> clienteBySharedKey = clienteRepository.findOneByShared(clienteDto.getSharedKey());
        clienteBySharedKey.ifPresent(c -> {
            if (!c.getId().equals(clienteDto.getId())) {
                String mensajeError = ExceptionMessages.ERROR201.getMensajeConParametros(clienteDto.getSharedKey());
                log.warm(mensajeError);
                throw new ResponseStatusException(HttpStatus.CONFLICT, mensajeError);
            }
        });
    }

}
