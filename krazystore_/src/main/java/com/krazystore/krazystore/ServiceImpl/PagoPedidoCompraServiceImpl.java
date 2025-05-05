/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.AplicacionPagosPedidoCompraEvent;
import Utils.PagoPedidoCompraReembolsoEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import com.krazystore.krazystore.Repository.PagoPedidoCompraRepository;
import com.krazystore.krazystore.Service.PagoPedidoCompraService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PagoPedidoCompraServiceImpl  implements PagoPedidoCompraService{

    public PagoPedidoCompraServiceImpl(com.krazystore.krazystore.Repository.PagoPedidoCompraRepository pagoPedidoCompraRepository) {
        this.pagoPedidoCompraRepository = pagoPedidoCompraRepository;
    }
    private final PagoPedidoCompraRepository pagoPedidoCompraRepository;

    @Override
    public PagoPedidoCompra savePago(PagoPedidoCompra pagoPedidoCompra) {
        return pagoPedidoCompraRepository.save(pagoPedidoCompra);
    }

    @Override
    public List<PagoPedidoCompra> findAll() {
        return pagoPedidoCompraRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<PagoPedidoCompra> findById(Long id) {
        return pagoPedidoCompraRepository.findById(id);
    }

    @Override
    public PagoPedidoCompra updatePago(PagoPedidoCompra pagoPedidoCompra, Long id) {
        PagoPedidoCompra updatedPagoPedidoCompra = pagoPedidoCompraRepository.findById(id).get();
        updatedPagoPedidoCompra.setReembolsado(pagoPedidoCompra.getReembolsado());
        updatedPagoPedidoCompra.setSaldo(pagoPedidoCompra.getSaldo());
        return pagoPedidoCompraRepository.save(updatedPagoPedidoCompra);  
    }

    @Override
    public List<PagoPedidoCompra> updatePagos(List<PagoPedidoCompra> pagos) {
        return pagoPedidoCompraRepository.saveAll(pagos);
    }

    @Override
    public void deletePagoPedidoCompra(Long id) {
        pagoPedidoCompraRepository.deleteById(id);
    }

    @Override
    public List<AplicacionPagoPedidoCompra> findPagosByIdPedidoCompra(Long id) {
        return pagoPedidoCompraRepository.findPagosByIdPedidoCompra(id);
    }
    
    @EventListener
    public void handleReembolsosEvents(PagoPedidoCompraReembolsoEvent event) {
        PagoPedidoCompra pagoPedidoCompra = pagoPedidoCompraRepository.findById(event.getIdPagoPedidoCompra())
                .orElseThrow(() -> new RuntimeException("Pago de pedido compra no encontrado"));

        TipoEvento evento = event.getTipoEvento();
        Integer montoReembolso = event.getMontoReembolsado();
        
        if(evento.equals(TipoEvento.PAGO_PEDIDO_COMPRA_REEMBOLSADO)){
            pagoPedidoCompra.setReembolsado(pagoPedidoCompra.getReembolsado() + montoReembolso);
            pagoPedidoCompra.setSaldo(pagoPedidoCompra.getSaldo() - montoReembolso);
        }else{
            pagoPedidoCompra.setReembolsado(pagoPedidoCompra.getReembolsado() - montoReembolso);
            pagoPedidoCompra.setSaldo(pagoPedidoCompra.getSaldo() + montoReembolso);
        }
        
        pagoPedidoCompraRepository.save(pagoPedidoCompra);
    }
    
    @EventListener
    public void handleAplicacionPagosPedidoCompraEvents(AplicacionPagosPedidoCompraEvent event) {
        List<PagoPedidoCompra> pagosPedidoCompraActualizar = new ArrayList<>();
        List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados = event.getPagosPedidoCompraAplicados();
        TipoEvento tipoEvento = event.getTipoEvento();
        
        pagosPedidoCompraAplicados.forEach((ap)->{
            PagoPedidoCompra pagoPedidoCompra = ap.getPagoPedidoCompra();
            if(tipoEvento==TipoEvento.PAGO_PEDIDO_COMPRA_APLICADO){
                pagoPedidoCompra.setUtilizado((int) (pagoPedidoCompra.getUtilizado()+ap.getMonto()));
                pagoPedidoCompra.setSaldo((int) (pagoPedidoCompra.getSaldo()-ap.getMonto()));
            }else if(tipoEvento==TipoEvento.PAGO_PEDIDO_COMPRA_APLICADO_ELIMINADO){
                pagoPedidoCompra.setUtilizado((int) (pagoPedidoCompra.getUtilizado()-ap.getMonto()));
                pagoPedidoCompra.setSaldo((int) (pagoPedidoCompra.getSaldo()+ap.getMonto()));
            }
            pagosPedidoCompraActualizar.add(pagoPedidoCompra);
        });
        
        pagoPedidoCompraRepository.saveAll(pagosPedidoCompraActualizar);
    }

    @Override
    public List<AplicacionPagoPedidoCompra> findPagosAplicarByIdPedidoCompra(Long id) {
        return pagoPedidoCompraRepository.findPagosAplicarByIdPedidoCompra(id);
    }

}
