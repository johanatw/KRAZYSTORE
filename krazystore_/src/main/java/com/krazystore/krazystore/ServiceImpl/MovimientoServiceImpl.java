/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.FacturaCompraPagadoEvent;
import Utils.FacturaVentaPagadoEvent;
import Utils.PagoPedidoCompraReembolsoEvent;
import Utils.PagoRegistradoEvent;
import Utils.PedidoCompraEvent;
import Utils.PedidoEvent;
import Utils.TipoEvento;
import Utils.TipoFactura;
import Utils.TipoPedido;
import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EgresoVarioDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.IngresoVarioDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PagoPedidoCompraCreationDTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.DTO.ReembolsoAnticipoCreationDTO;
import com.krazystore.krazystore.DTO.ReembolsoPagoPedidoCompraCreationDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.MovimientoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.AplicacionAnticipoService;
import com.krazystore.krazystore.Service.AplicacionPagoPedidoCompraService;
import com.krazystore.krazystore.Service.FormaCobroService;
import com.krazystore.krazystore.Service.FormaPagoService;
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
import com.krazystore.krazystore.Service.PagoPedidoCompraService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.ReembolsoPagoPedidoCompraService;
import java.util.ArrayList;
import java.util.Comparator;
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
    private AplicacionAnticipoService aplicacionAnticipoService;
    
    @Autowired
    private AplicacionPagoPedidoCompraService aplicacionPagoPedidoCompraService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoCompraService pedidoCompraService;
    
    @Autowired
    private ReembolsoService reembolsoService;
    
    @Autowired
    private ReembolsoPagoPedidoCompraService reembolsoPagoPedidoCompraService;
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private PagoPedidoCompraService pagoPedidoCompraService;
    
    @Autowired
    private FormaPagoService formaPagoService;
    
    @Autowired
    private FormaCobroService formaCobroService;

    @Override
    public List<MovimientosDTO> findByIdCaja(Long id) {
        List<MovimientosDTO> movimientos = movimientoRepository.findByIdCaja(id);
        List<MovimientosDTO> anticiposAplicados = movimientoRepository.findAnticiposAplicadosByIdCaja(id);

        List<MovimientosDTO> resultado = new ArrayList<>();
        resultado.addAll(movimientos);
        resultado.addAll(anticiposAplicados);

        // Ordenamos por movimientoId
        resultado.sort(Comparator.comparing(MovimientosDTO::getFecha).reversed());
        return resultado;
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
    public MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoDTO, CajaEntity caja) {
        System.out.println("SAVEANTICIPO");
        anticipoDTO.getAnticipo().setSaldo(anticipoDTO.getAnticipo().getTotal());
        AnticipoEntity newAnticipo = anticipoService.saveAnticipo(anticipoDTO.getAnticipo());
        System.out.println("CREAR MOVIMIENTO");
        MovimientoEntity movimiento = crearMovimiento(newAnticipo, caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        System.out.println("SAVEPAGOS");
        
        anticipoDTO.getCobros().forEach(d -> d.setMovimiento(nuevoMovimiento));
        formaCobroService.saveFormasCobros(anticipoDTO.getCobros());
        System.out.println("ACTUALIZAR ESTADOPEDIDO");
        actualizarEstadoPedido(nuevoMovimiento.getAnticipo().getPedido().getId(), TipoEvento.ANTICIPO_CREADO);
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public MovimientoEntity crearMovimiento(AnticipoEntity anticipo, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setAnticipo(anticipo);
        nuevoMovimiento.setFecha(anticipo.getFecha());
        nuevoMovimiento.setMonto(anticipo.getTotal());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)1,"ANTICIPO"));
        
        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }

    @Override
    public MovimientoEntity saveMovimiento(ReembolsoAnticipoCreationDTO reembolsoDTO, CajaEntity caja) {
        ReembolsoAnticipo reembolso = reembolsoService.saveReembolso(reembolsoDTO.getReembolso());
        
        MovimientoEntity movimiento = crearMovimiento(reembolso, caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        reembolsoDTO.getPagos().forEach(d -> d.setMovimiento(nuevoMovimiento));
        formaPagoService.saveFormasPagos(reembolsoDTO.getPagos());
        
        actualizarSaldoAnticipo(reembolso.getAnticipo().getId(), reembolso.getMonto(), TipoEvento.REEMBOLSO_CREADO);
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(ReembolsoAnticipo reembolso, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setReembolso(reembolso);
        nuevoMovimiento.setFecha(reembolso.getFecha());
        nuevoMovimiento.setMonto(reembolso.getMonto());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)5,"REEMBOLSO CLIENTE"));

        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }
    
    @Transactional
    private void actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado, TipoEvento evento){
        anticipoService.actualizarSaldoAnticipo(idAnticipo, montoReembolsado, evento);
        
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
        System.out.println("9");
        MovimientoEntity movimiento = movimientoRepository.findByIdCompra(compra.getId());
        System.out.println("10");
        movimiento.setMonto(compra.getTotal());
        movimiento.setNroDocumento(compra.getNroFactura());
        System.out.println("11");
        MovimientoEntity updatedMovimiento = movimientoRepository.save(movimiento);
        
        return updatedMovimiento;
    }
    
    @Override
    public MovimientoEntity updateMovimiento(VentaEntity venta) {
        MovimientoEntity movimiento = movimientoRepository.findByIdVenta(venta.getId());
        movimiento.setMonto(venta.getMontoTotal());
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
    
    

    @Override
    public void deleteAnticipo(Long id) {
        //Se obtiene los reembolsos del anticipo
        System.out.println("DELETE ANTICIPO");
        List<Long> reembolsosAnticipo = anticipoService.getIdReembolsosAnticipo(id);
     
        if(!reembolsosAnticipo.isEmpty()){
            //Se obtiene los movimientos de los reembolsos
            System.out.println("DELETE REEMBOLSOS");
            deleteReembolsos(reembolsosAnticipo);
        }
        System.out.println("ANTICIPO");
        //Se obtiene el anticipo
        AnticipoEntity anticipo = anticipoService.findById(id).get();
        System.out.println("MOV ANTICIPO");
        //Se obtiene el movimiento de caja correspondiente al anticipo
        MovimientoEntity movimiento = movimientoRepository.findByIdAnticipo(id);
       System.out.println("ELIMINA PAGOS");
        //Se elimina los detalles de pago del movimiento
        formaCobroService.deleteFormasCobrosByMovimiento(movimiento.getId());
        System.out.println("ELIMINA MOVIMIENTO");
         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        //Se guarda pedido
        Long idPedido = anticipo.getPedido().getId();
       
        //Se elimina el anticipo
        System.out.println("DELETE ANTICIPO2");
        anticipoService.deleteAnticipo(id);
        
        //Se actualiza estado de pago del Pedido
        actualizarEstadoPedido(idPedido, TipoEvento.ANTICIPO_ELIMINADO);
    }
    
    @Override
    @Transactional
    public void deleteReembolso(Long id) {
        ReembolsoAnticipo reembolso = reembolsoService.findById(id).get();
        //Se obtiene el movimiento de caja correspondiente al reembolso
        MovimientoEntity movimiento = movimientoRepository.findByIdReembolso(id);
        //Se elimina los detalles de pago del movimiento
        formaPagoService.deleteFormasPagosByMovimiento(movimiento.getId());
         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        actualizarSaldoAnticipo(reembolso.getAnticipo().getId(), reembolso.getMonto(), TipoEvento.REEMBOLSO_ELIMINADO);
        
        reembolsoService.deleteReembolsoAnticipo(id);
      
        
    }
    
    @Override
    public void deleteReembolsos(List<Long> ids){
        ids.forEach(d->System.out.println(d));
        List<Long> movimientos = movimientoRepository.findByIdsReembolsos(ids);
        movimientos.forEach(d->System.out.println(d));
        System.out.println("delete formas pago reemb");
        //Se eliminan los detalles de pago de los movimientos
        formaPagoService.deleteFormasPagosByMovimientos(movimientos);
        System.out.println("delete movimientos reembolsos");
        //Se eliminan los movimientos
        movimientoRepository.deleteByIds(movimientos);
        System.out.println("delete reemb");
        //Se eliminan los reembolsos
        reembolsoService.deleteReembolsosAnticipo(ids);
    }
    
    @Override
    public void deleteMovimiento(Long id) {
        MovimientoEntity movimiento = movimientoRepository.findById(id).get();
        Boolean esIngreso;
        esIngreso = movimiento.getConcepto().getTipo()== 'I';
        System.out.println("if");
        if(movimiento.getVenta()!= null){
            System.out.println("venta no null");
            formaCobroService.deleteFormasCobrosByMovimiento(id);
            aplicacionAnticipoService.deleteAnticiposAplicadosByIdMovimiento(id);
            System.out.println("despues de delete formas cobro");
            movimiento.setEstado(Estado.PENDIENTEDEPAGO.getCodigo());
            movimiento.setCaja(null);
            movimientoRepository.save(movimiento);
            cambiarEstadoPagoVenta(movimiento.getVenta().getId(), Estado.PENDIENTEDEPAGO.getCodigo());
        }else if(movimiento.getCompra()!= null){
            System.out.println("pagos no null");
            formaPagoService.deleteFormasPagosByMovimiento(id);
            aplicacionPagoPedidoCompraService.deletePagosPedidoCompraAplicadosByIdMovimiento(id);
            System.out.println("despues de delete formas pago");
            movimiento.setEstado(Estado.PENDIENTEDEPAGO.getCodigo());
            movimiento.setCaja(null);
            movimientoRepository.save(movimiento);
            cambiarEstadoPagoCompra(movimiento.getCompra().getId(), Estado.PENDIENTEDEPAGO.getCodigo());
        }else if(esIngreso){
            formaCobroService.deleteFormasCobrosByMovimiento(id);
            movimientoRepository.deleteById(id);
        }else{
            formaPagoService.deleteFormasPagosByMovimiento(id);
            movimientoRepository.deleteById(id);
        }
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
    public void actualizarEstadoPedido(Long idPedido, TipoEvento tipoEvento) {       
        if (idPedido == null) {
            throw new IllegalArgumentException("Parámetros inválidos: idPedido");
        }
        System.out.println("ACTUALIZARESTADOPEDIDO");

        PedidoEvent evento = new PedidoEvent(idPedido, tipoEvento);
        eventPublisher.publishEvent(evento);
        
    }
    
    private void actualizarEstadoMovimiento(MovimientoEntity movimiento, Estado estado) {
        movimiento.setEstado(estado.getCodigo());
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
    public MovimientoEntity saveMovimiento(IngresoVarioDTO ingresoDTO, CajaEntity caja) {
        ingresoDTO.getMovimiento().setCaja(caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(ingresoDTO.getMovimiento());
        
        ingresoDTO.getCobros().forEach(d->d.setMovimiento(nuevoMovimiento));
        formaCobroService.saveFormasCobros(ingresoDTO.getCobros());
        
        return nuevoMovimiento;
    }

    @Override
    public MovimientoEntity saveMovimiento(EgresoVarioDTO egresoDTO, CajaEntity caja) {
        egresoDTO.getMovimiento().setCaja(caja);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(egresoDTO.getMovimiento());
        
        egresoDTO.getPagos().forEach(d->d.setMovimiento(nuevoMovimiento));
        formaPagoService.saveFormasPagos(egresoDTO.getPagos());
        
        return nuevoMovimiento;
        
    }

    @Override
    public List<MovimientoEntity> getMovimientosPendientesDePago() {
        return movimientoRepository.getMovimientosPendientesDePago();
    }

    @Override
    public List<MovimientoEntity> getMovimientosPendientesDeCobro() {
        return movimientoRepository.getMovimientosPendientesDeCobro();
    }

    @Transactional
    @Override
    public MovimientoEntity savePagosPendientes(MovimientoCreationDTO movimientoDTO, CajaEntity caja) {
        MovimientoEntity movimiento = movimientoRepository.findById(movimientoDTO.getMovimiento().getId())
                .orElseThrow(() -> new RuntimeException("Movimiento no existe"));
        
        movimiento.setCaja(caja);
        movimiento.setFecha(new Date());
        actualizarEstadoMovimiento(movimiento, Estado.PAGOCOMPLETO);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        
        movimientoDTO.getPagos().forEach(d->d.setMovimiento(nuevoMovimiento));
        formaPagoService.saveFormasPagos(movimientoDTO.getPagos());
        
        if(!movimientoDTO.getPagosPedidoCompraAplicados().isEmpty()){
            movimientoDTO.getPagosPedidoCompraAplicados().forEach(d->d.setMovimiento(nuevoMovimiento));
            movimientoDTO.getPagosPedidoCompraAplicados().forEach(d->d.setFecha(new Date()));
            aplicacionPagoPedidoCompraService.savePagosPedidoCompraAplicados(movimientoDTO.getPagosPedidoCompraAplicados());
        }
        
        cambiarEstadoPagoCompra(nuevoMovimiento.getCompra().getId(), Estado.PAGOCOMPLETO.getCodigo());
        
        return nuevoMovimiento;
    }

    @Transactional
    @Override
    public MovimientoEntity saveCobrosPendientes(MovimientoCreationDTO movimientoDTO, CajaEntity caja) {
        MovimientoEntity movimiento = movimientoRepository.findById(movimientoDTO.getMovimiento().getId())
                .orElseThrow(() -> new RuntimeException("Movimiento no existe"));
        
        movimiento.setCaja(caja);
        movimiento.setFecha(new Date());
        actualizarEstadoMovimiento(movimiento, Estado.PAGOCOMPLETO);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        
        movimientoDTO.getCobros().forEach(d->d.setMovimiento(nuevoMovimiento));
        formaCobroService.saveFormasCobros(movimientoDTO.getCobros());
        
        
        if(!movimientoDTO.getAnticiposAplicados().isEmpty()){
            movimientoDTO.getAnticiposAplicados().forEach(d->d.setMovimiento(nuevoMovimiento));
            movimientoDTO.getAnticiposAplicados().forEach(d->d.setFecha(new Date()));
            aplicacionAnticipoService.saveAnticiposAplicados(movimientoDTO.getAnticiposAplicados());
        }
            
        cambiarEstadoPagoVenta(nuevoMovimiento.getVenta().getId(), Estado.PAGOCOMPLETO.getCodigo());
        
        return nuevoMovimiento;
    }

    @Transactional
    @Override
    public MovimientoEntity saveMovimiento(PagoPedidoCompraCreationDTO pagoPedidoCreationDTO, CajaEntity caja) {
        PagoPedidoCompra pagoPedidoCompra = pagoPedidoCreationDTO.getPagoPedidoCompra();
        pagoPedidoCompra.setSaldo(pagoPedidoCompra.getTotal());
        pagoPedidoCompraService.savePago(pagoPedidoCompra);
        MovimientoEntity movimiento = crearMovimiento(pagoPedidoCompra, caja);
        movimientoRepository.save(movimiento);
        
        pagoPedidoCreationDTO.getPagos().forEach(d -> d.setMovimiento(movimiento));
        formaPagoService.saveFormasPagos(pagoPedidoCreationDTO.getPagos());

        //actualizarEstadoPedido(nuevoMovimiento.getAnticipo().getPedido().getId(), TipoEvento.ANTICIPO_CREADO);
        
        return movimiento;
    }

    @Transactional
    @Override
    public MovimientoEntity saveMovimiento(ReembolsoPagoPedidoCompraCreationDTO reembolsoPagoPedidoCompraDTO, CajaEntity caja) {
        ReembolsoPagoPedidoCompra reembolso = reembolsoPagoPedidoCompraService.saveReembolso(reembolsoPagoPedidoCompraDTO.getReembolso());
        
        MovimientoEntity movimiento = crearMovimiento(reembolso, caja);
        movimientoRepository.save(movimiento);
        reembolsoPagoPedidoCompraDTO.getCobros().forEach(d -> d.setMovimiento(movimiento));
        formaCobroService.saveFormasCobros(reembolsoPagoPedidoCompraDTO.getCobros());
        
        actualizarSaldoPagoPedidoCompra(
                reembolso.getPagoPedidoCompra().getId(), 
                reembolso.getMonto(), 
                TipoEvento.PAGO_PEDIDO_COMPRA_REEMBOLSADO);

        return movimiento;
    }


    @Transactional
    @Override
    public void deletePagoPedidoCompra(Long id) {
        //Se obtiene los reembolsos del anticipo
        List<Long> reembolsosPagoPedidoCompra = this.reembolsoPagoPedidoCompraService.getIdReembolsosByIdPagosPedidoCompra(id);
     
        if(!reembolsosPagoPedidoCompra.isEmpty()){
            //Se obtiene los movimientos de los reembolsos
            System.out.println("DELETE REEMBOLSOS");
            deleteReembolsosPagoPedidoCompra(reembolsosPagoPedidoCompra);
        }
        System.out.println("ANTICIPO");
        //Se obtiene el anticipo
        PagoPedidoCompra pagoPedidoCompra = pagoPedidoCompraService.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago de pedido compra no existe"));
        
        System.out.println("MOV ANTICIPO");
        //Se obtiene el movimiento de caja correspondiente al anticipo
        MovimientoEntity movimiento = movimientoRepository.findByIdPagoPedidoCompra(id);
       System.out.println("ELIMINA PAGOS");
        //Se elimina los detalles de pago del movimiento
        formaPagoService.deleteFormasPagosByMovimiento(movimiento.getId());
        System.out.println("ELIMINA MOVIMIENTO");
         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        //Se guarda pedido
        Long idPedido = pagoPedidoCompra.getPedidoCompra().getId();
       
        //Se elimina el anticipo
        System.out.println("DELETE ANTICIPO2");
        pagoPedidoCompraService.deletePagoPedidoCompra(id);
        
        //Se actualiza estado de pago del Pedido
        //actualizarEstadoPedido(idPedido, TipoEvento.ANTICIPO_ELIMINADO);
    }

    @Transactional
    @Override
    public void deleteReembolsoPagoPedidoCompra(Long id) {
        ReembolsoPagoPedidoCompra reembolso = this.reembolsoPagoPedidoCompraService.findById(id)
                .orElseThrow(() -> new RuntimeException("Reembolso no existe"));
        //Se obtiene el movimiento de caja correspondiente al reembolso
        MovimientoEntity movimiento = movimientoRepository.findByIdReembolsoPagoPedidoCompra(id);
        //Se elimina los detalles de pago del movimiento
        formaCobroService.deleteFormasCobrosByMovimiento(movimiento.getId());
         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        actualizarSaldoPagoPedidoCompra(
                reembolso.getPagoPedidoCompra().getId(), 
                reembolso.getMonto(), 
                TipoEvento.REEMBOLSO_PAGO_PEDIDO_COMPRA_ELIMINADO);

        
        reembolsoPagoPedidoCompraService.deleteById(id);
    }

    @Override
    public MovimientoEntity crearMovimiento(PagoPedidoCompra pagoPedido, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setPagoPedidoCompra(pagoPedido);
        nuevoMovimiento.setFecha(pagoPedido.getFecha());
        nuevoMovimiento.setMonto(pagoPedido.getTotal());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)2,"ANTICIPO"));
        
        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }

    
    @Override
    public MovimientoEntity crearMovimiento(ReembolsoPagoPedidoCompra reembolsoPagoPedido, CajaEntity caja) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setReembolsoPagoPedidoCompra(reembolsoPagoPedido);
        nuevoMovimiento.setFecha(reembolsoPagoPedido.getFecha());
        nuevoMovimiento.setMonto(reembolsoPagoPedido.getMonto());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)6,"REEMBOLSO CLIENTE"));

        
        nuevoMovimiento.setCaja(caja);
        return nuevoMovimiento;
    }
    
    @Transactional
    public void deleteReembolsosPagoPedidoCompra(List<Long> ids){
        ids.forEach(d->System.out.println(d));
        List<Long> movimientos = movimientoRepository.findByIdsReembolsosPagoPedidoCompra(ids);
        movimientos.forEach(d->System.out.println(d));
        System.out.println("delete formas pago reemb");
        //Se eliminan los detalles de pago de los movimientos
        formaCobroService.deleteFormasCobrosByMovimientos(movimientos);
        System.out.println("delete movimientos reembolsos");
        //Se eliminan los movimientos
        movimientoRepository.deleteByIds(movimientos);
        System.out.println("delete reemb");
        //Se eliminan los reembolsos
        reembolsoPagoPedidoCompraService.deleteReembolsosById(ids);
    }
    
    @Transactional
    private void actualizarSaldoPagoPedidoCompra(Long idPagoPedidoCompra, int montoReembolso, TipoEvento tipoEvento){
        PagoPedidoCompraReembolsoEvent evento = new PagoPedidoCompraReembolsoEvent(idPagoPedidoCompra, montoReembolso, tipoEvento);
        eventPublisher.publishEvent(evento);
        
    }

}
