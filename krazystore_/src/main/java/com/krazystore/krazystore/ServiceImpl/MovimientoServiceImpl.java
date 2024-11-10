/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.MovimientoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.CajaService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.ReembolsoService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class MovimientoServiceImpl implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
  

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private AnticipoService anticipoService;
    
    @Autowired
    private ReembolsoService reembolsoService;
    
    @Autowired
    private CajaService cajaService;

    @Override
    public List<MovimientosDTO> findAll() {

        return movimientoRepository.findMovimientosDTO();
    }
    

    @Override
    public Optional<MovimientoEntity> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<MovimientosDTO> findByIdCaja(Long id) {
        return movimientoRepository.findByIdCaja(id);
    }

    @Override
    public MovimientoEntity saveMovimiento(MovimientoEntity movimientoEntity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public MovimientoEntity saveMovimiento(MovimientoEntity movimientoEntity, List<PagoEntity> pagos) {
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimientoEntity);
        
        pagoService.savePagos(nuevoMovimiento, pagos);
        
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity saveMovimiento(AnticipoEntity anticipoEntity, List<PagoEntity> pagos) {
       
        anticipoEntity.setSaldo(anticipoEntity.getTotal());
        AnticipoEntity newAnticipo = anticipoService.saveAnticipo(anticipoEntity);
        
        MovimientoEntity movimiento = crearMovimiento(newAnticipo);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        pagoService.savePagos(nuevoMovimiento, pagos);
        
        EstadoEntity estadoPago = getEstadoPagoPedido(nuevoMovimiento.getAnticipo().getPedido().getId());
        pedidoService.updateEstadoPagoPedido( nuevoMovimiento.getAnticipo().getPedido(), estadoPago);
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public MovimientoEntity savePagosMovimiento(MovimientoEntity movimientoEntity, List<PagoEntity> pagos) {
        System.out.println("holaaaa");
        
        MovimientoEntity movimiento = movimientoRepository.findById(movimientoEntity.getId()).get();
        movimiento.setCaja(cajaService.getCaja());
        
        movimiento.setEstado('C');
    
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
     
        pagoService.savePagos(nuevoMovimiento, pagos);
        
        if(nuevoMovimiento.getVenta().getPedido() != null){
            EstadoEntity estadoPago = getEstadoPagoPedido(nuevoMovimiento.getVenta().getPedido().getId());
            pedidoService.updateEstadoPagoPedido( nuevoMovimiento.getVenta().getPedido(), estadoPago);
        }
        
        
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public MovimientoEntity saveMovimiento(VentaEntity ventaEntity) {
     
        
        MovimientoEntity movimiento = crearMovimiento(ventaEntity, false);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        //pagoService.savePagos(nuevoMovimiento, pagos);
        
        /*if(nuevoMovimiento.getVenta().getPedido() != null){
            EstadoEntity estadoPago = getEstadoPagoPedido(nuevoMovimiento.getVenta().getPedido().getId());
            pedidoService.updateEstadoPagoPedido( nuevoMovimiento.getVenta().getPedido(), estadoPago);
        }*/
        
        
        
        return nuevoMovimiento;
        
    }
    
    @Override
    public void saveMovimientos(List<MovimientoEntity> movimientos) {
        movimientoRepository.saveAll(movimientos);
    }

    @Override
    public MovimientoEntity updateMovimiento(MovimientoEntity movimientoEntity, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Override
    public MovimientoEntity crearMovimiento(VentaEntity venta, boolean anular) {
        CajaEntity caja = cajaService.getCaja();
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        
        nuevoMovimiento.setVenta(venta);
        nuevoMovimiento.setMonto(venta.getMontoTotal());
        
        nuevoMovimiento.setNroDocumento(venta.getNroFactura());
        nuevoMovimiento.setEstado('P');
        //nuevoMovimiento.setCaja(caja);
        //if(anular){
          //  nuevoMovimiento.setFecha(new Date());
          //  nuevoMovimiento.setConcepto(new ConceptoEntity((long)11,"ANULACION"));
        //}else{          
            nuevoMovimiento.setFecha(venta.getFecha());
            nuevoMovimiento.setConcepto(new ConceptoEntity((long)3,"VENTA"));  
        //}
        
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(AnticipoEntity anticipo) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setAnticipo(anticipo);
        nuevoMovimiento.setFecha(anticipo.getFecha());
        nuevoMovimiento.setMonto(anticipo.getTotal());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)1,"ANTICIPO"));
        nuevoMovimiento.setCaja(cajaService.getCajaAbierta().get());

        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(ReembolsoEntity reembolso) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setReembolso(reembolso);
        nuevoMovimiento.setFecha(reembolso.getFecha());
        nuevoMovimiento.setMonto(reembolso.getMonto());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)2,"REEMBOLSO"));
        nuevoMovimiento.setCaja(cajaService.getCajaAbierta().get());
        return nuevoMovimiento;
    }
    
    @Override
    public EstadoEntity getEstadoPagoPedido(Long idPedido) {

        PedidoMontoPagadoDTO pedido = getMontoPagadoPedido(idPedido);
 
        EstadoEntity estadoPago = new EstadoEntity();
        if(pedido.getTotalPagos() >= pedido.getTotal()){
            estadoPago.setId((long)2);
        }else if(pedido.getTotalPagos() > 0){
            estadoPago.setId((long)7);   
        }else{
            estadoPago.setId((long)1);
        }
        return estadoPago;
    }

    @Transactional
    @Override
    public void deleteMovimiento(Long id) {
        MovimientoEntity movimiento = movimientoRepository.findById(id).get();
        pagoService.deletePagosByMovimiento(id);
        if(movimiento.getVenta() == null){
            movimientoRepository.deleteById(id);
        }else{
            movimiento.setEstado('P');
            movimiento.setCaja(null);
            movimientoRepository.save(movimiento);
            
            if(movimiento.getVenta().getPedido()!=null){
                //Se actualiza estado de pago del Pedido
                EstadoEntity estadoPago = getEstadoPagoPedido(movimiento.getVenta().getPedido().getId());
                pedidoService.updateEstadoPagoPedido( movimiento.getVenta().getPedido(), estadoPago);
            }
        }
    }
    
    @Transactional
    @Override
    public void deleteVenta(Long id) {
       

        //Se obtiene el movimiento de caja correspondiente al anticipo
        MovimientoEntity movimiento = movimientoRepository.findByIdVenta(id);

         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());       

    }


    @Transactional
    @Override
    public void deleteAnticipo(Long id) {
        //Se obtiene el anticipo
        AnticipoEntity anticipo = anticipoService.findById(id).get();

        //Se obtiene el movimiento de caja correspondiente al anticipo
        MovimientoEntity movimiento = movimientoRepository.findByIdAnticipo(id);
       
        //Se elimina los detalles de pago del movimiento
        pagoService.deletePagosByMovimiento(movimiento.getId());

         // Se elimina el movimiento      
        movimientoRepository.deleteById(movimiento.getId());
        
        //Se actualiza estado de pago del Pedido
        EstadoEntity estadoPago = getEstadoPagoPedido(anticipo.getPedido().getId());
        pedidoService.updateEstadoPagoPedido( anticipo.getPedido(), estadoPago);
       
        //Se elimina el anticipo
        anticipoService.deleteAnticipo(id);
        

    }
    
    @Transactional
     @Override
    public void deleteAnticipoConReembolsos(Long id) {

        //Se obtiene los reembolsos del anticipo
        List<Long> reembolsosAnticipo = anticipoService.getIdReembolsosAnticipo(id);
     
        //Se obtiene los movimientos de los reembolsos
        List<Long> movimientos = movimientoRepository.findByIdsReembolsos(reembolsosAnticipo);
       
        //Se eliminan los detalles de pago de los movimientos
        pagoService.deletePagosByMovimientos(movimientos);
   
        //Se eliminan los movimientos
        movimientoRepository.deleteByIds(movimientos);

        //Se eliminan los reembolsos
        reembolsoService.deleteReembolsos(reembolsosAnticipo);
    
        deleteAnticipo(id);
       
        
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
        
        AnticipoEntity anticipo= new AnticipoEntity();
        
        anticipo.setReembolsado(reembolso.getAnticipo().getReembolsado() - reembolso.getMonto());
        anticipo.setSaldo(reembolso.getAnticipo().getSaldo() + reembolso.getMonto() );
       
        anticipoService.updateAnticipo(anticipo, reembolso.getAnticipo().getId());
        
        EstadoEntity estadoPago = getEstadoPagoPedido(reembolso.getAnticipo().getPedido().getId());
        pedidoService.updateEstadoPagoPedido( reembolso.getAnticipo().getPedido(), estadoPago);
        
        reembolsoService.deleteReembolso(id);
      
        
    }
    


    @Override
    public List<PRUEBADTO> pr(Long id) {
        return movimientoRepository.prueba(id);
    }

    @Override
    public MovimientoEntity saveMovimiento(ReembolsoEntity reembolsoEntity, List<PagoEntity> pagos) {
        
        ReembolsoEntity reembolso = reembolsoService.saveReembolso(reembolsoEntity);
        
        MovimientoEntity movimiento = crearMovimiento(reembolso);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        pagoService.savePagos(nuevoMovimiento, pagos);
        
        EstadoEntity estadoPago = getEstadoPagoPedido(reembolso.getAnticipo().getPedido().getId());
        pedidoService.updateEstadoPagoPedido( reembolso.getAnticipo().getPedido(), estadoPago);
        
        actualizarSaldoAnticipo(reembolso.getAnticipo(), reembolso);
        return nuevoMovimiento;
    }
    
    public void actualizarSaldoAnticipo(AnticipoEntity anticipoReembolsar, ReembolsoEntity reembolso) {
        
        anticipoReembolsar.setReembolsado(anticipoReembolsar.getReembolsado() + reembolso.getMonto());
        anticipoReembolsar.setSaldo(anticipoReembolsar.getSaldo() - reembolso.getMonto());
    
        
        anticipoService.updateAnticipo(anticipoReembolsar, anticipoReembolsar.getId());
    }
  
    @Override
    public PedidoMontoPagadoDTO getMontoPagadoPedido(Long id) {
        Optional<PedidoEntity> pedido = pedidoService.findById(id);
        if(pedido.isEmpty()){
            return null;
        }
        
        Optional<PedidoMontoPagadoDTO> pagosPedido = pagoService.getPagosPedido(id);
        if(pagosPedido.isPresent()){
            return pagosPedido.get();
        }
        return new PedidoMontoPagadoDTO(id, pedido.get().getTotal(), (long)0);
             
    }
    


    @Override
    public Long validarEliminacionPedido(Long id) {
        return movimientoRepository.validateOrderDeletion(id);
    }

    @Override
    public List<MovimientoEntity> getFacturasPendientes() {
        return movimientoRepository.getFacturasPendientes();
    }
    
    @Override
    public char getEstadoPago(VentaEntity ventaEntity) {

         MovimientoEntity movimiento = movimientoRepository.findByIdVenta(ventaEntity.getId());
         if(movimiento != null){
             return movimiento.getEstado();
         }
         
         return 'S';
         

    }
}
