package com.tecnica.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.controller.ClienteController;
import com.tecnica.dto.ClienteDto;
import com.tecnica.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ClienteController.class)
public class ClienteConrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void listar() throws Exception {
        when(clienteService.obtenerTodosLosCliente()).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cliente/listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void testBuscarClientePorSharedKey() throws Exception {
        String sharedKey = "sh1";
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setShared(sharedKey);

        when(clienteService.obtenetClientePorSharedKey(sharedKey)).thenReturn(clienteDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cliente/buscar")
                        .param("sharedKey", sharedKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertTrue(jsonResponse.contains("\"id\":1"));
        assertTrue(jsonResponse.contains("\"sharedKey\":\"sh1\""));
    }

    @Test
    void testCrearCliente() throws Exception {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(2L);
        clienteDto.setShared("123456");
        clienteDto.setNombre("John Doe");
        clienteDto.setEmail("johndoe@example.com");


        when(clienteService.guardarCliente(any(ClienteDto.class))).thenReturn(clienteDto);

        String inputJson = new ObjectMapper().writeValueAsString(clienteDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cliente/crear")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertTrue(jsonResponse.contains("\"id\":2"));
        assertTrue(jsonResponse.contains("\"sharedKey\":\"123456\""));
        assertTrue(jsonResponse.contains("\"nombre\":\"John Doe\""));
        assertTrue(jsonResponse.contains("\"email\":\"johndoe@example.com\""));
    }

    @Test
    void testActualizarCliente() throws Exception {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setShared("sh1");
        clienteDto.setNombre("admin");
        clienteDto.setEmail("mail@gmail.com");

        when(clienteService.actualizarCliente(any(ClienteDto.class))).thenReturn(clienteDto);

        String inputJson = new ObjectMapper().writeValueAsString(clienteDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/cliente/editar")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertTrue(jsonResponse.contains("\"id\":1"));
        assertTrue(jsonResponse.contains("\"sharedKey\":\"sh1\""));
        assertTrue(jsonResponse.contains("\"nombre\":\"admin\""));
        assertTrue(jsonResponse.contains("\"email\":\"mail@gmail.com\""));
    }
}

