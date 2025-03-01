/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.FacturaCompraPagadoEvent;
import Utils.FacturaVentaPagadoEvent;
import Utils.PagoRegistradoEvent;
import Utils.PedidoCompraEvent;
import Utils.PedidoEvent;
import Utils.TipoEvento;
import Utils.TipoFactura;
import Utils.TipoPedido;
import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.DTO.ReembolsoCreationDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.MovimientoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.ReembolsoService;
import com.krazystore.krazystore.Service.VentaService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.PedidoService;
import java.util.Date;

/**
 *
 * @author HP
 */
@Service
public class MovimientoServiceImpl implements MovimientoService {

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, ApplicationEventPublisher eventPublisher) {
        this.movimientoRepository = movimientoRepository;
        this.eventPublisher = eventPublisher;
    }
    
    private final MovimientoRepository movimientoRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private AnticipoService anticipoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoCompraService pedidoCompraService;
    
    @Autowired
    private ReembolsoService reembolsoService;
    
    @Autowired
    private PagoService pagoService;

    @Override
    public List<MovimientosDTO> findByIdCaja(Long id) {
        return movimientoRepository.findByIdCaja(id);
    }
    
    @Override
    public EstadoPagoPedidoDTO getEstadoPagoPedidoCompra(Long id) {
        return movimientoRepository.getEstadoPagoPedidoCompra(id).orElse(null);
    }
    
    @Override
    public EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(Long id) {
        return movimientoRepository.getEstadoPagoPedidoVenta(id).orElse(null);
    }
    
    @Override
    public List<MovimientoEntity> getFacturasPendientes() {
        return movimientoRepository.getFacturasPendientes();
    }
    
    @Override
    public MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoDTO, CajaEntity caja) {
       
        anticipoDTO.getAnticipo().setSaldo(anticipoDTO.getAnticipo().getTotal());
        AnticipoEntity newAnticipo = anticipoService.saveAnticipo(anticipoDTO.getAnticipo());
        
        MovimientoEntity movimiento = crearMovimiento(newAnticipo, caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        pagoService.savePagos(nuevoMovimiento, anticipoDTO.getPagos());
        
        actualizarEstadoPedido(nuevoMovimiento.getAnticipo().getIdPedido(), 
                nuevoMovimiento.getAnticipo().getTipoPedido(), TipoEvento.ANTICIPO_CREADO);
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public MovimientoEntity crearMovimiento(AnticipoEntity anticipo, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setAnticipo(anticipo);
        nuevoMovimiento.setFecha(anticipo.getFecha());
        nuevoMovimiento.setMonto(anticipo.getTotal());
        if(anticipo.getTipoPedido() == TipoPedido.PEDIDOVENTA.getCodigo()){
            nuevoMovimiento.setConcepto(new ConceptoEntity((long)1,"ANTICIPO"));
        }else{
            nuevoMovimiento.setConcepto(new ConceptoEntity((long)2,"ANTICIPO"));
        }
        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }

    @Override
    public MovimientoEntity saveMovimiento(ReembolsoCreationDTO reembolsoDTO, CajaEntity caja) {
        ReembolsoEntity reembolso = reembolsoService.saveReembolso(reembolsoDTO.getReembolso());
        
        MovimientoEntity movimiento = crearMovimiento(reembolso, caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        pagoService.savePagos(nuevoMovimiento, reembolsoDTO.getPagos());
        
        actualizarSaldoAnticipo(reembolso.getAnticipo().getId(), reembolso.getMonto(), TipoEvento.REEMBOLSO_CREADO);
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(ReembolsoEntity reembolso, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        char tipoPedido = reembolso.getAnticipo().getTipoPedido();
        nuevoMovimiento.setReembolso(reembolso);
        nuevoMovimiento.setFecha(reembolso.getFecha());
        nuevoMovimiento.setMonto(reembolso.getMonto());
        if (tipoPedido == TipoPedido.PEDIDOVENTA.getCodigo()){
            nuevoMovimiento.setConcepto(new ConceptoEntity((long)5,"REEMBOLSO CLIENTE"));
        }else{
            nuevoMovimiento.setConcepto(new ConceptoEntity((long)6,"REEMBOLSO PROVEEDOR"));
        }
        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }
    
    @Transactional
    private void actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado, TipoEvento evento){
        anticipoService.actualizarSaldoAnticipo(idAnticipo, montoReembolsado, evento);
        
    }

    @Override
    public MovimientoEntity saveMovimiento(MovimientoCreationDTO movimientoDTO, CajaEntity caja) {
        movimientoDTO.getMovimiento().setCaja(caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimientoDTO.getMovimiento());
        
        pagoService.savePagos(nuevoMovimiento, movimientoDTO.getPago());
        
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity saveMovimiento(VentaEntity ventaEntity) {

        MovimientoEntity movimiento = crearMovimiento(ventaEntity);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public MovimientoEntity crearMovimiento(VentaEntity venta) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        
        nuevoMovimiento.setVenta(venta);
        nuevoMovimiento.setMonto(venta.getMontoTotal());
        nuevoMovimiento.setNroDocumento(venta.getNroFactura());
        nuevoMovimiento.setEstado(Estado.PENDIENTE.getCodigo());       
        nuevoMovimiento.setFecha(venta.getFecha());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)3,"VENTA"));  
        
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity saveMovimiento(CompraEntity compraEntity) {
    
        MovimientoEntity movimiento = crearMovimiento(compraEntity);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity updateMovimiento(CompraEntity compra) {
        MovimientoEntity movimiento = movimientoRepository.findByIdCompra(compra.getId());
        movimiento.setMonto(compra.getTotal());
        movimiento.setNroDocumento(compra.getNroFactura());
        MovimientoEntity updatedMovimiento = movimientoRepository.save(movimiento);
        
        return updatedMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(CompraEntity compraEntity) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        
        nuevoMovimiento.setCompra(compraEntity);
        nuevoMovimiento.setMonto(compraEntity.getTotal());
        nuevoMovimiento.setNroDocumento(compraEntity.getNroFactura());
        nuevoMovimiento.setEstado(Estado.PENDIENTE.getCodigo());
        nuevoMovimiento.setFecha(compraEntity.getFecha());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)4,"COMPRA"));  
        
        return nuevoMovimiento;
    }

    @Transactional
    @Override
    public MovimientoEntity savePagosFactura(MovimientoCreationDTO movimientoDTO, CajaEntity caja) {

        MovimientoEntity movimiento = movimientoRepository.findById(movimientoDTO.getMovimiento().getId())
                .orElseThrow(() -> new RuntimeException("Movimiento no existe"));
        
        System.out.println("SAVEPAGOSMOVIMIENTOID");
        System.out.println(movimiento.getId());
        actualizarMovimiento(movimiento, caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);

        pagoService.savePagos(nuevoMovimiento, movimientoDTO.getPago());

        gestionarPagoVenta(nuevoMovimiento, Estado.PAGOCOMPLETO);
        gestionarPagoCompra(nuevoMovimiento, Estado.PAGOCOMPLETO);
        
        return nuevoMovimiento;
    }
    
    public void cambiarEstadoPagoCompra(Long idCompra, char estado) {
        // Publicar el evento
        FacturaCompraPagadoEvent evento = new FacturaCompraPagadoEvent(idCompra, estado);
        eventPublisher.publishEvent(evento);
    }
    
    public void cambiarEstadoPagoVenta(Long idVenta, char estado) {
        // Publicar el evento
        FacturaVentaPagadoEvent evento = new FacturaVentaPagadoEvent(idVenta, estado);
        eventPublisher.publishEvent(evento);
    }

    @Override
    public void deleteAnticipo(Long id) {
        //Se obtiene los reembolsos del anticipo
        List<Long> reembolsosAnticipo = anticipoService.getIdReembolsosAnticipo(id);
     
        if(!reembolsosAnticipo.isEmpty()){
            //Se obtiene los movimientos de los reembolsos
            deleteReembolsos(reembolsosAnticipo);
        }
        
        //Se obtiene el anticipo
        AnticipoEntity anticipo = anticipoService.findById(id).get();

        //Se obtiene el movimiento de caja correspondiente al anticipo
        MovimientoEntity movimiento = movimientoRepository.findByIdAnticipo(id);
       
        //Se elimina los detalles de pago del movimiento
        pagoService.deletePagosByMovimiento(movimiento.getId());

         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        //Se guarda pedido
        Long idPedido = anticipo.getIdPedido();
        char tipoPedido = anticipo.getTipoPedido();
       
        //Se elimina el anticipo
        anticipoService.deleteAnticipo(id);
        
        //Se actualiza estado de pago del Pedido
        actualizarEstadoPedido(idPedido, 
                tipoPedido, TipoEvento.ANTICIPO_ELIMINADO);
    }
    
    @Override
    @Transactional
    public void deleteReembolso(Long id) {
        ReembolsoEntity reembolso = reembolsoService.findById(id).get();
        //Se obtiene el movimiento de caja correspondiente al reembolso
        MovimientoEntity movimiento = movimientoRepository.findByIdReembolso(id);
        //Se elimina los detalles de pago del movimiento
        pagoService.deletePagosByMovimiento(movimiento.getId());
         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        actualizarSaldoAnticipo(reembolso.getAnticipo().getId(), reembolso.getMonto(), TipoEvento.REEMBOLSO_ELIMINADO);
        
        reembolsoService.deleteReembolso(id);
      
        
    }
    
    @Override
    public void deleteReembolsos(List<Long> ids){
        List<Long> movimientos = movimientoRepository.findByIdsReembolsos(ids);
            
        //Se eliminan los detalles de pago de los movimientos
        pagoService.deletePagosByMovimientos(movimientos);

        //Se eliminan los movimientos
        movimientoRepository.deleteByIds(movimientos);
        
        //Se eliminan los reembolsos
        reembolsoService.deleteReembolsos(ids);
    }
    
    @Override
    public void deleteMovimiento(Long id) {
        MovimientoEntity movimiento = movimientoRepository.findById(id).get();
        if(movimiento.getVenta()!= null){
            deletePagosFactura(movimiento, TipoFactura.FACTURAVENTA );
        }else if(movimiento.getCompra()!= null){
            deletePagosFactura(movimiento, TipoFactura.FACTURACOMPRA );
        }else{
            pagoService.deletePagosByMovimiento(id);
            movimientoRepository.deleteById(id);
        }
    }
    
    public void deletePagosFactura(MovimientoEntity movimiento, TipoFactura tipoFactura ) {
        
        switch(tipoFactura){
            case FACTURAVENTA -> gestionarPagoVenta(movimiento, Estado.PENDIENTEDEPAGO);
            case FACTURACOMPRA -> gestionarPagoCompra(movimiento, Estado.PENDIENTEDEPAGO);
            default -> throw new IllegalArgumentException("Tipo de factura desconocido: " + tipoFactura);
        }
        
        movimiento.setEstado(Estado.PENDIENTEDEPAGO.getCodigo());
        movimiento.setCaja(null);
        movimientoRepository.save(movimiento);
        pagoService.deletePagosByMovimiento(movimiento.getId());

    }
    
    @Override
    public void deleteVenta(Long idVenta) {

        //Se obtiene el movimiento de caja correspondiente a la venta
        MovimientoEntity movimiento = movimientoRepository.findByIdVenta(idVenta);
       
        List<PagoEntity> pagos = pagoService.findByIdMovimiento(movimiento.getId());

        if(!pagos.isEmpty()){
            throw new RuntimeException("Debe eliminar los pagos para eliminar el movimiento");
        }

         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
    }
    
    @Override
    public void deleteCompra(Long id) {

        //Se obtiene el movimiento de caja correspondiente a la venta
        MovimientoEntity movimiento = movimientoRepository.findByIdCompra(id);
       
        List<PagoEntity> pagos = pagoService.findByIdMovimiento(movimiento.getId());

        if(!pagos.isEmpty()){
            throw new RuntimeException("Debe eliminar los pagos para eliminar el movimiento");
        }

         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
    }
    

    @Transactional
    public void actualizarEstadoPedido(Long idPedido, char tipoPedido, TipoEvento tipoEvento) {       
        if (idPedido == null || (tipoPedido != 'V' && tipoPedido != 'C')) {
            throw new IllegalArgumentException("Parámetros inválidos: idPedido o tipoPedido");
        }
        System.out.println("ACTUALIZARESTADOPEDIDO");
        if(tipoPedido == TipoPedido.PEDIDOCOMPRA.getCodigo() ){
            PedidoCompraEvent evento = new PedidoCompraEvent(idPedido, tipoEvento);
            eventPublisher.publishEvent(evento);
        }else{
            System.out.println("ELSE");
            PedidoEvent evento = new PedidoEvent(idPedido, tipoEvento);
            eventPublisher.publishEvent(evento);
        }
        
    }
    
    private void actualizarMovimiento(MovimientoEntity movimiento, CajaEntity caja) {
        movimiento.setCaja(caja);
        movimiento.setFecha(new Date());
        movimiento.setEstado(Estado.PAGOCOMPLETO.getCodigo());
    }

    private void gestionarPagoVenta(MovimientoEntity movimiento, Estado estado) {

        if (movimiento.getVenta() != null) {
            cambiarEstadoPagoVenta(movimiento.getVenta().getId(), estado.getCodigo());

        }
    }

    private void gestionarPagoCompra(MovimientoEntity movimiento, Estado estado) {

        if (movimiento.getCompra() != null) {
            cambiarEstadoPagoCompra(movimiento.getCompra().getId(), estado.getCodigo());
        }
    }
    
    
    
}
