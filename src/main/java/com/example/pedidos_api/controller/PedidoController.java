package com.example.pedidos_api.controller;

import com.example.pedidos_api.entity.Pedido;
import com.example.pedidos_api.entity.StatusPedido;
import com.example.pedidos_api.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // Criar um pedido - Apenas usuários autenticados podem criar pedidos
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.criarPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    // Listar todos os pedidos - Apenas usuários autenticados podem listar pedidos
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    // Obter detalhes de um pedido pelo ID - Apenas usuários autenticados podem visualizar pedidos
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPedido(@PathVariable UUID id) {
        return pedidoService.obterPedido(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar o status do pedido - Apenas usuários autenticados podem atualizar pedidos
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable UUID id, @RequestBody String status) {
        try {
            StatusPedido statusPedido = StatusPedido.valueOf(status.toUpperCase()); // Garante compatibilidade
            Optional<Pedido> pedidoAtualizado = pedidoService.atualizarStatus(id, statusPedido);
            return pedidoAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Retorna erro caso o status seja inválido
        }
    }

    // Deletar um pedido - Apenas usuários com a role ADMIN podem deletar pedidos
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable UUID id) {
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // Listar pedidos por status - Apenas usuários autenticados podem consultar pedidos por status
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> listarPedidosPorStatus(@PathVariable String status) {
        try {
            StatusPedido statusPedido = StatusPedido.valueOf(status.toUpperCase());
            return ResponseEntity.ok(pedidoService.listarPedidosPorStatus(statusPedido));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Retorna erro se o status for inválido
        }
    }
}
