package com.example.pedidos_api.repository;

import com.example.pedidos_api.entity.Pedido;
import com.example.pedidos_api.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    // Busca pedidos pelo status
    List<Pedido> findByStatus(StatusPedido status);
}
