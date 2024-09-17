/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.MovimientoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.ReembolsoService;
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

    @Override
    public List<MovimientosDTO> findAll() {

        return movimientoRepository.findMovimientosDTO();
    }

    @Override
    public Optional<MovimientoEntity> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public MovimientoEntity saveMovimiento(VentaEntity ventaEntity, List<PagoEntity> pagos) {
     
        
        MovimientoEntity movimiento = crearMovimiento(ventaEntity);
        MovimientoEntity nuevoMovimiento = movimientoRepository.save(movimiento);
        pagoService.savePagos(nuevoMovimiento, pagos);
        
        if(nuevoMovimiento.getVenta().getPedido() != null){
            EstadoEntity estadoPago = getEstadoPagoPedido(nuevoMovimiento.getVenta().getPedido().getId());
            pedidoService.updateEstadoPagoPedido( nuevoMovimiento.getVenta().getPedido(), estadoPago);
        }
        
        
        
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
    public MovimientoEntity crearMovimiento(VentaEntity venta) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setVenta(venta);
        nuevoMovimiento.setFecha(venta.getFecha());
        nuevoMovimiento.setMonto(venta.getMontoTotal());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)3,"VENTA"));
        nuevoMovimiento.setNroDocumento(venta.getNroFactura());
        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(AnticipoEntity anticipo) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setAnticipo(anticipo);
        nuevoMovimiento.setFecha(anticipo.getFecha());
        nuevoMovimiento.setMonto(anticipo.getTotal());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)1,"ANTICIPO"));

        return nuevoMovimiento;
    }
    
    @Override
    public MovimientoEntity crearMovimiento(ReembolsoEntity reembolso) {
        MovimientoEntity nuevoMovimiento = new MovimientoEntity();
        nuevoMovimiento.setReembolso(reembolso);
        nuevoMovimiento.setFecha(reembolso.getFecha());
        nuevoMovimiento.setMonto(reembolso.getMonto());
        nuevoMovimiento.setConcepto(new ConceptoEntity((long)2,"REEMBOLSO"));

        return nuevoMovimiento;
    }
    
    @Override
    public EstadoEntity getEstadoPagoPedido(Long idPedido) {

        PedidoMontoPagadoDTO pedido = pagoService.getPagosPedido(idPedido).get();
 
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

    @Override
    public void deleteMovimiento(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteMovimientosByReembolsos(List<Long> reembolsos) {
        pagoService.deletePagosByReembolsos(reembolsos);
        movimientoRepository.deleteMovimientosByReembolsos(reembolsos);
        
    }
    @Override
    public void deleteMovimientosByReembolso(ReembolsoEntity reembolso) {
        pagoService.deletePagosByReembolso(reembolso.getId());
        movimientoRepository.deleteMovimientosByReembolso(reembolso.getId());
        
       // pedidoService.updateEstadoPagoPedido( reembolso.getAnticipo().getPedido());
    }

    @Override
    public void deleteMovimientosByAnticipo(AnticipoEntity anticipo) {
        
        pagoService.deletePagosByAnticipo(anticipo.getId());
        movimientoRepository.deleteMovimientosByAnticipo(anticipo.getId());
       // pedidoService.updateEstadoPagoPedido( anticipo.getPedido());
    }
    
    @Override
    public void deleteAnticipo(Long id) {
        AnticipoEntity anticipo = anticipoService.findById(id).get();

        pagoService.deletePagosByAnticipo(anticipo.getId());
        movimientoRepository.deleteMovimientosByAnticipo(anticipo.getId());
        
        EstadoEntity estadoPago = getEstadoPagoPedido(anticipo.getPedido().getId());
        pedidoService.updateEstadoPagoPedido( anticipo.getPedido(), estadoPago);
        
        
        anticipoService.deleteAnticipo(id);

    }
    
     @Override
    public void deleteAnticipoConReembolsos(Long id) {
        AnticipoEntity anticipo = anticipoService.findById(id).get();
        List<Long> reembolsosAnticipo = anticipoService.getIdReembolsosAnticipo(id);
   
        deleteMovimientosByReembolsos(reembolsosAnticipo);
        anticipoService.deleteReembolsosByIdAnticipo(id);
        deleteAnticipo(anticipo.getId());
       
        
    }
    
    @Override
    @Transactional
    public void deleteReembolso(Long id) {
        ReembolsoEntity reembolso = reembolsoService.findById(id).get();
        
        pagoService.deletePagosByReembolso(reembolso.getId());
        
        AnticipoEntity anticipo= new AnticipoEntity();
        
        anticipo.setReembolsado(reembolso.getAnticipo().getReembolsado() - reembolso.getMonto());
        anticipo.setSaldo(reembolso.getAnticipo().getSaldo() + reembolso.getMonto() );
        

        anticipoService.updateAnticipo(anticipo, reembolso.getAnticipo().getId());
        
         movimientoRepository.deleteMovimientosByReembolso(reembolso.getId());
       
         
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
  
    
}
