/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.AplicacionPagosPedidoCompraEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Repository.AplicacionPagoPedidoCompraRepository;
import com.krazystore.krazystore.Service.AplicacionPagoPedidoCompraService;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class AplicacionPagoPedidoCompraServiceImpl implements AplicacionPagoPedidoCompraService {

    public AplicacionPagoPedidoCompraServiceImpl(org.springframework.context.ApplicationEventPublisher eventPublisher, com.krazystore.krazystore.Repository.AplicacionPagoPedidoCompraRepository aplicacionPagoPedidoCompraRepository) {
        this.eventPublisher = eventPublisher;
        this.aplicacionPagoPedidoCompraRepository = aplicacionPagoPedidoCompraRepository;
    }
    private final ApplicationEventPublisher eventPublisher;
    private final AplicacionPagoPedidoCompraRepository aplicacionPagoPedidoCompraRepository;

    @Override
    public void savePagosPedidoCompraAplicados(List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados) {
        aplicacionPagoPedidoCompraRepository.saveAll(pagosPedidoCompraAplicados);
        actualizarSaldoPagoPedidoCompra(pagosPedidoCompraAplicados, TipoEvento.PAGO_PEDIDO_COMPRA_APLICADO);

    }

    @Override
    public void deletePagosPedidoCompraAplicadosByIdMovimiento(Long id) {
        List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados = aplicacionPagoPedidoCompraRepository.findPagosPedidoCompraAplicadosByIdMovimiento(id);
        actualizarSaldoPagoPedidoCompra(pagosPedidoCompraAplicados, TipoEvento.PAGO_PEDIDO_COMPRA_APLICADO_ELIMINADO);
        aplicacionPagoPedidoCompraRepository.deletePagosPedidoCompraAplicadosByIdMovimiento(id);
    }
    
    public void actualizarSaldoPagoPedidoCompra(List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados, TipoEvento tipoEvento) {
        // Publicar el evento
        AplicacionPagosPedidoCompraEvent evento = new AplicacionPagosPedidoCompraEvent(pagosPedidoCompraAplicados, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
}
