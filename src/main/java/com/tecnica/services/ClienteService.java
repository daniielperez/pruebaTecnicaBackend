package com.tecnica.services;

import com.tecnica.dto.ClienteDto;

import java.util.List;
public interface ClienteService {
    List<ClienteDto> obtenerTodosLosCliente();
    ClienteDto obtenetClientePorSharedKey(String sharedKey);
    ClienteDto obtenetClientePorEmail(String email);
    ClienteDto guardarCliente(ClienteDto cliente);
    ClienteDto actualizarCliente(ClienteDto cliente);
}
