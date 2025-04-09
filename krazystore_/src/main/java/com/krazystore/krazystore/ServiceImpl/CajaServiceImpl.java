/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.NuevaFacturaEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.ReembolsoCreationDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.CajaRepository;
import com.krazystore.krazystore.Service.CajaService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.krazystore.krazystore.Service.MovimientoService;

/**
 *
 * @author HP
 */

@Service
public class CajaServiceImpl implements CajaService{
    private final CajaRepository cajaRepository;

    public CajaServiceImpl(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Autowired
    private MovimientoService movimientoService;
    
    @Override
    public Optional<CajaEntity> getCajaAbierta() {
        System.out.println("getCajaAbierta");
        return cajaRepository.getCajaAbierta();
    }
    
    @Override
    public CajaEntity abrirCaja() {  
        //Si no, crea caja nueva
        CajaEntity nuevaCaja = new CajaEntity();
        nuevaCaja.setFecha(new Date());
        nuevaCaja.setEstado(Estado.CAJAABIERTA.getCodigo());
        return cajaRepository.save(nuevaCaja);
    }
    
    @Override
    public void cerrarCaja(Long id) {
        CajaEntity caja = cajaRepository.findById(id).get();
        caja.setFechaCierre(new Date());
        caja.setEstado(Estado.CAJACERRADA.getCodigo());
        cajaRepository.save(caja);
    }
    
    @Override
    public Optional<CajaEntity> findById(Long id) {
        return cajaRepository.findById(id);
    }
    
    @Override
    public List<CajaEntity> findAll() {
        return cajaRepository.findAllByOrderByIdDesc();
    }

    @Override
    public EstadoPagoPedidoDTO getEstadoPagoPedidoCompra(Long id) {
        return movimientoService.getEstadoPagoPedidoCompra(id);
    }
    
    @Override
    public EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(Long id) {
        return movimientoService.getEstadoPagoPedidoVenta(id);
    }
    
    @Override
    public List<MovimientoEntity> getFacturasPendientes() {
        return movimientoService.getFacturasPendientes();
    }

    @Override
    public MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoCreationDTO) {
        CajaEntity caja = cajaRepository.getCajaAbierta()
                .orElseThrow(() -> new RuntimeException("Caja cerrada"));
        
        return movimientoService.saveMovimiento(anticipoCreationDTO, caja);
    }

    @Override
    public MovimientoEntity saveMovimiento(ReembolsoCreationDTO reembolsoDTO) {
        CajaEntity caja = cajaRepository.getCajaAbierta()
                .orElseThrow(() -> new RuntimeException("Caja cerrada"));
        
        return movimientoService.saveMovimiento(reembolsoDTO, caja);
    }

    @Override
    public MovimientoEntity saveMovimiento(MovimientoCreationDTO movimientoDTO) {
        CajaEntity caja = cajaRepository.getCajaAbierta()
                .orElseThrow(() -> new RuntimeException("Caja cerrada"));
        
        return movimientoService.saveMovimiento(movimientoDTO, caja);
    }
    
    @EventListener
    public void handleNuevaFacturaEvent(NuevaFacturaEvent event) {
        if (event.getTipoEvento() == TipoEvento.NUEVA_VENTA) {
            movimientoService.saveMovimiento(event.getNuevaVenta());
        }else if (event.getTipoEvento() == TipoEvento.NUEVA_COMPRA) {
            movimientoService.saveMovimiento(event.getNuevaCompra());
        }else if(event.getTipoEvento() == TipoEvento.FACTURA_ANULADA) {
            movimientoService.deleteVenta(event.getNuevaVenta().getId());
        }else if(event.getTipoEvento() == TipoEvento.FACTURA_COMPRA_MODIFICADA) {
            movimientoService.updateMovimiento(event.getNuevaCompra());
        }else if(event.getTipoEvento() == TipoEvento.FACTURA_VENTA_MODIFICADA) {
            movimientoService.updateMovimiento(event.getNuevaVenta());
        }
        
    }

    @Override
    public MovimientoEntity savePagosFactura(MovimientoCreationDTO movimientoDTO) {
        CajaEntity caja = cajaRepository.getCajaAbierta()
                .orElseThrow(() -> new RuntimeException("Caja cerrada"));
        
        return movimientoService.savePagosFactura(movimientoDTO, caja);
    }

    @Override
    public void deleteAnticipo(Long id) {
        movimientoService.deleteAnticipo(id);
    }

    @Override
    public void deleteReembolso(Long id) {
        movimientoService.deleteReembolso(id);
    }

    @Override
    public void deleteMovimiento(Long id) {
        movimientoService.deleteMovimiento(id);
    }

    @Override
    public List<MovimientosDTO> findByIdCaja(Long id) {
        return movimientoService.findByIdCaja(id);
    }
}

