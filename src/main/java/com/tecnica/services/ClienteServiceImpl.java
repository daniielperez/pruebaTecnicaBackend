package com.tecnica.services;

import com.tecnica.dto.ClienteDto;
import com.tecnica.entity.Cliente;
import com.tecnica.manager.Log;
import com.tecnica.mapper.GenericMapper;
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
        List<ClienteDto> clienteDtoList = GenericMapper.mapList(clienteRepository.findAll(),ClienteDto.class);
        return clienteDtoList;
    }

    @Override
    public ClienteDto obtenetClientePorSharedKey(String sharedKey) {
        log.warm("Buscando cliente por shared key");
        Cliente cli = clienteRepository.findOneByShared(sharedKey).orElse(null);
        ClienteDto cliDto = cli != null ? GenericMapper.map(cli,ClienteDto.class) : null;
        return cliDto;
    }

    @Override
    public ClienteDto obtenetClientePorEmail(String email) {
        log.warm("Buscando cliente por email");
        Cliente cli = clienteRepository.findByEmail(email).orElse(null);
        ClienteDto cliDto = cli != null ? GenericMapper.map(cli,ClienteDto.class) : null;
        return cliDto;
    }

    @Override
    public ClienteDto guardarCliente(ClienteDto clienteDto) {
        log.warm("Guardando cliente");
        Cliente cliente = GenericMapper.map(clienteDto,Cliente.class);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return GenericMapper.map(clienteGuardado,ClienteDto.class);
    }

    @Override
    public ClienteDto actualizarCliente(ClienteDto clienteDto) {
        log.info("Actualizando cliente", clienteDto.getId().toString());
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteDto.getId());
        if(clienteOptional.isPresent()) {
            Cliente cliente = GenericMapper.map(clienteDto, Cliente.class);
            Cliente clienteActualizado = clienteRepository.save(cliente);
            log.info("Cliente actualizado exitosamente");
            return GenericMapper.map(clienteActualizado,ClienteDto.class);
        }
        String mensajeError = ExceptionMessages.ERROR203.getMensajeConParametros();
        log.error(mensajeError);
        throw new ResponseStatusException(HttpStatus.CONFLICT, mensajeError);
    }

}
