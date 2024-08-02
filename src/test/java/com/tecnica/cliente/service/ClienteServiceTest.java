package com.tecnica.cliente.service;

import com.tecnica.controller.ClienteController;
import com.tecnica.dto.ClienteDto;
import com.tecnica.entity.Cliente;
import com.tecnica.manager.Log;
import com.tecnica.repository.ClienteRepository;
import com.tecnica.services.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Log log;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosLosCliente() {
        List<Cliente> clientes = Arrays.asList(
                new Cliente(1L, "12345", "Admin","3176543637", "admin@example.com", new Date(), new Date()),
                new Cliente(2L, "78653", "David Perez","3178954373", "david@gmail.com", new Date(), new Date())
        );
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<ClienteDto> clienteDtoList = clienteService.obtenerTodosLosCliente();


        assertEquals(2, clienteDtoList.size());
        assertEquals("Admin", clienteDtoList.get(0).getNombre());
        assertEquals("David Perez", clienteDtoList.get(1).getNombre());
    }

    @Test
    void testObtenetClientePorSharedKey_ClienteEncontrado() {
        String sharedKey = "123456";
        Cliente cliente = new Cliente(1L, sharedKey, "David Perez","3178954373", "david@example.com", new Date(), new Date());
        when(clienteRepository.findOneByShared(sharedKey)).thenReturn(Optional.of(cliente));

        ClienteDto clienteDto = clienteService.obtenetClientePorSharedKey(sharedKey);

        assertNotNull(clienteDto);
        assertEquals("David Perez", clienteDto.getNombre());
    }

    @Test
    void testObtenetClientePorSharedKey_ClienteNoEncontrado() {
        String sharedKeyNoExistente = "sharedNoExiste";
        when(clienteRepository.findOneByShared(eq(sharedKeyNoExistente))).thenReturn(Optional.empty());
        ClienteDto clienteDto = clienteService.obtenetClientePorSharedKey(sharedKeyNoExistente);
        assertEquals(clienteDto, null);
    }

    @Test
    public void testGuardarClienteExitosamente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setShared("123456");
        clienteDto.setNombre("David Perez");
        clienteDto.setTelefono("3178954373");
        clienteDto.setEmail("david@gmail.com");
        clienteDto.setInicio(new Date());
        clienteDto.setFin(new Date());

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setShared("123456");
        cliente.setNombre("David Perez");
        cliente.setTelefono("3178954373");
        cliente.setEmail("david@gmail.com");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDto resultado = clienteService.guardarCliente(clienteDto);

        assertEquals("David Perez", resultado.getNombre());
        assertEquals("david@gmail.com", resultado.getEmail());
        assertEquals("123456", resultado.getShared());
    }

    @Test
    public void testGuardarClienteConEmailExistente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setEmail("existente@example.com");
        clienteDto.setNombre("Nombre");
        clienteDto.setTelefono("3046613922");
        clienteDto.setShared("sharedKey");
        clienteDto.setInicio(new Date());
        clienteDto.setFin(new Date());
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(2L);
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(clienteExistente));
        assertThrows(ResponseStatusException.class, () -> clienteService.guardarCliente(clienteDto));
    }

    @Test
    public void testGuardarClienteConSharedKeyExistente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setEmail("nuevo@example.com");
        clienteDto.setNombre("Nombre");
        clienteDto.setTelefono("3046613922");
        clienteDto.setShared("sharedKeyExistente");
        clienteDto.setInicio(new Date());
        clienteDto.setFin(new Date());
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(2L);
        when(clienteRepository.findOneByShared(anyString())).thenReturn(Optional.of(clienteExistente));
        assertThrows(ResponseStatusException.class, () -> clienteService.guardarCliente(clienteDto));
    }

    @Test
    public void testGuardarClienteConDatosInvalidos() {
        ClienteDto clienteInvalido = new ClienteDto();
        clienteInvalido.setShared("");
        clienteInvalido.setNombre("");
        clienteInvalido.setTelefono("");
        clienteInvalido.setEmail("");
        clienteInvalido.setInicio(null);
        clienteInvalido.setFin(null);
        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> clienteService.guardarCliente(clienteInvalido),
                "Se esperaba que lanzara una ResponseStatusException debido a datos inválidos"
        );
        assertEquals(HttpStatus.CONFLICT, thrown.getStatusCode());
    }

    @Test
    public void testActualizarClienteExitosamente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setShared("sharedKEy");
        clienteDto.setNombre("Nombre Nuevo");
        clienteDto.setTelefono("4317829292");
        clienteDto.setEmail("email@nuevo.com");
        clienteDto.setInicio(new Date());
        clienteDto.setFin(new Date());
        Cliente clienteExistente = new Cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);
        ClienteDto resultado = clienteService.actualizarCliente(clienteDto);
        assertEquals(clienteDto.getNombre(), resultado.getNombre());
    }

    @Test
    public void testActualizarClienteNoEncontrado() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(999L);
        clienteDto.setShared("sharedKey");
        clienteDto.setNombre("Nombre Nuevo");
        clienteDto.setTelefono("4317829292");
        clienteDto.setEmail("email@nuevo.com");
        clienteDto.setInicio(new Date());
        clienteDto.setFin(new Date());
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class,
                () -> clienteService.actualizarCliente(clienteDto),
                "Se esperaba que lanzara una ResponseStatusException"
        );
        assertEquals(HttpStatus.CONFLICT, thrown.getStatusCode());
        verify(clienteRepository).findById(999L);
    }


}
