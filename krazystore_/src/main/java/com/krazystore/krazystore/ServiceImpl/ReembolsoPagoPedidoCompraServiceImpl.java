/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import com.krazystore.krazystore.Repository.ReembolsoPagoPedidoCompraRepository;
import com.krazystore.krazystore.Service.ReembolsoPagoPedidoCompraService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ReembolsoPagoPedidoCompraServiceImpl implements ReembolsoPagoPedidoCompraService {

    public ReembolsoPagoPedidoCompraServiceImpl(com.krazystore.krazystore.Repository.ReembolsoPagoPedidoCompraRepository reembolsoPagoPedidoCompraRepository) {
        this.reembolsoPagoPedidoCompraRepository = reembolsoPagoPedidoCompraRepository;
    }
    private final ReembolsoPagoPedidoCompraRepository reembolsoPagoPedidoCompraRepository;

    @Override
    public ReembolsoPagoPedidoCompra saveReembolso(ReembolsoPagoPedidoCompra reembolsoPagoPedidoCompra) {
        return reembolsoPagoPedidoCompraRepository.save(reembolsoPagoPedidoCompra);
    }

    @Override
    public List<Long> getIdReembolsosByIdPagosPedidoCompra(Long id) {
        return reembolsoPagoPedidoCompraRepository.getIdReembolsosByIdPagoPedidoCompra(id);
    }

    @Override
    public void deleteReembolsosById(List<Long> ids) {
        reembolsoPagoPedidoCompraRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public Optional<ReembolsoPagoPedidoCompra> findById(Long id) {
        return reembolsoPagoPedidoCompraRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        reembolsoPagoPedidoCompraRepository.deleteById(id);
    }

    @Override
    public List<ReembolsoPagoPedidoCompra> findAll() {
        return reembolsoPagoPedidoCompraRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    
}
