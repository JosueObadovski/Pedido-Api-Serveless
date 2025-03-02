package com.example.pedidos_api.controller;

import com.example.pedidos_api.entity.Pedido;
import com.example.pedidos_api.entity.StatusPedido;
import com.example.pedidos_api.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPedido() {
        Pedido pedido = new Pedido();
        when(pedidoService.criarPedido(any(Pedido.class))).thenReturn(pedido);

        ResponseEntity<Pedido> response = pedidoController.criarPedido(new Pedido());

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarPedidos() {
        when(pedidoService.listarPedidos()).thenReturn(List.of(new Pedido()));

        ResponseEntity<List<Pedido>> response = pedidoController.listarPedidos();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void deveObterPedidoPorId() {
        UUID id = UUID.randomUUID();
        Pedido pedido = new Pedido();
        when(pedidoService.obterPedido(id)).thenReturn(Optional.of(pedido));

        ResponseEntity<Pedido> response = pedidoController.obterPedido(id);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() != null);
    }

    @Test
    void deveAtualizarStatus() {
        UUID id = UUID.randomUUID();
        Pedido pedido = new Pedido();
        when(pedidoService.atualizarStatus(id, StatusPedido.PENDENTE)).thenReturn(Optional.of(pedido));

        ResponseEntity<Pedido> response = pedidoController.atualizarStatus(id, "PENDENTE");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void deveDeletarPedido() {
        UUID id = UUID.randomUUID();
        doNothing().when(pedidoService).deletarPedido(id);

        ResponseEntity<Void> response = pedidoController.deletarPedido(id);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void deveListarPedidosPorStatus() {
        when(pedidoService.listarPedidosPorStatus(StatusPedido.PENDENTE)).thenReturn(List.of(new Pedido()));

        ResponseEntity<List<Pedido>> response = pedidoController.listarPedidosPorStatus("PENDENTE");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}
