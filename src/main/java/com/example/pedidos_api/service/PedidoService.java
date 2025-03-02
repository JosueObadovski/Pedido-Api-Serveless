package com.example.pedidos_api.service;

import com.example.pedidos_api.entity.Pedido;
import com.example.pedidos_api.entity.StatusPedido;
import com.example.pedidos_api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j // Adiciona o Logger do SLF4J automaticamente
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    // Criar um novo pedido
    public Pedido criarPedido(Pedido pedido) {
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        log.info("Pedido criado: ID={} | Cliente={} | Total={} | Status={}",
                pedidoSalvo.getId(), pedidoSalvo.getCliente(), pedidoSalvo.getTotal(), pedidoSalvo.getStatus());
        return pedidoSalvo;
    }

    // Listar todos os pedidos
    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        log.info("Listando todos os pedidos. Total={}", pedidos.size());
        return pedidos;
    }

    // Buscar um pedido pelo ID
    public Optional<Pedido> obterPedido(UUID id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            log.info("Pedido encontrado: ID={}", id);
        } else {
            log.warn("Pedido não encontrado: ID={}", id);
        }
        return pedido;
    }

    // Atualizar o status do pedido
    public Optional<Pedido> atualizarStatus(UUID id, StatusPedido status) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(status);
            pedido.setDataAtualizacao(LocalDateTime.now());
            Pedido pedidoAtualizado = pedidoRepository.save(pedido);
            log.info("Pedido atualizado: ID={} | Novo Status={}", id, status);
            return pedidoAtualizado;
        });
    }

    // Deletar um pedido pelo ID
    public void deletarPedido(UUID id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            log.info("Pedido deletado: ID={}", id);
        } else {
            log.warn("Tentativa de deletar um pedido inexistente: ID={}", id);
        }
    }

    public List<Pedido> listarPedidosPorStatus(StatusPedido status) {
        List<Pedido> pedidos = pedidoRepository.findByStatus(status);
        log.info("Listando pedidos com status: {} | Total={}", status, pedidos.size());
        return pedidos;
    }
}
