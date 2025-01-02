/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.PagoRegistradoEvent;
import Utils.ProductosRecepcionadosEvent;
import Utils.RecepcionFacturada;
import Utils.TipoEventoExistencias;
import com.krazystore.krazystore.DTO.CompraCreationDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Repository.CompraRepository;
import com.krazystore.krazystore.Service.CompraService;
import com.krazystore.krazystore.Service.DetalleCompraService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
/**
 *
 * @author HP
 */
@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ApplicationEventPublisher eventPublisher;
    static char PAGADO = 'C';
    static char PENDIENTE = 'P';
    
    public CompraServiceImpl(CompraRepository compraRepository, ApplicationEventPublisher eventPublisher) {
        this.compraRepository = compraRepository;
        this.eventPublisher = eventPublisher;
    }
    
    
    
    @Autowired
    private DetalleCompraService detalleService;
    
    @Autowired
    private MovimientoService movimientoService;
    
    @Override
    public List<CompraEntity> findAll() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<CompraEntity> findById(Long id) {
        return compraRepository.findById(id);
    }

    @Transactional
    @Override
    public CompraEntity saveCompra(CompraCreationDTO compraDTO)throws Exception {
        if(compraDTO.getCompra().getProveedor()== null){
            throw new BadRequestException("Falta proveedor " );
        }
        CompraEntity nuevaCompra;
        nuevaCompra = compraRepository.save(compraDTO.getCompra());
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.saveDetCompra(compraDTO.getDetalle(), nuevaCompra.getId());
        
        if(nuevaCompra.getRecepcion() != null){
            actualizarEstadoRecepcion(nuevaCompra.getRecepcion().getId(), 'F');
        }else{
            actualizarExistencias(productosActualizarExistencias);
        }
        
        movimientoService.saveMovimiento(nuevaCompra);
        
        return nuevaCompra;
    }

    @Transactional
    @Override
    public CompraEntity updateCompra(CompraCreationDTO compraDTO, Long id)throws Exception {
        CompraEntity compra = compraDTO.getCompra();
        CompraEntity updatedCompra = compraRepository.findById(id).get();
        //No puede modificar una factura si ya esta pagado
        if(PAGADO == updatedCompra.getEstado()){
            throw new BadRequestException("No se puede modificar " );
        }
        
        updatedCompra.setFecha(compra.getFecha());
        updatedCompra.setNroFactura(compra.getNroFactura());
        
        //Si esta asociado a una recepcion no se puede modificar detalle
        if(updatedCompra.getRecepcion() == null){
            updatedCompra.setProveedor(compra.getProveedor());
            updatedCompra.setTotal(compra.getTotal());
            compraRepository.save(updatedCompra);
            List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.updateDetCompra(compraDTO.getDetalle(), id);
            actualizarExistencias(productosActualizarExistencias);
        }else{
            compraRepository.save(updatedCompra);
        }
        
        return updatedCompra;
    }

    @Transactional
    @Override
    public void deleteCompra(Long id) {
        CompraEntity compra = compraRepository.findById(id).get();
        //No puede eliminar una factura si ya esta pagado
        if(PAGADO == compra.getEstado()){
            throw new BadRequestException("No se puede Eliminar " );
        }
        
        detalleService.deleteDetCompra(id);
        compraRepository.deleteById(id);
    }
    
    @EventListener
    public void handleCambioEstado(PagoRegistradoEvent event) {
        Long compraId = event.getFacturaId();
        char estado = event.getEstado();
        // Buscar la factura y actualizar el estado
        CompraEntity compra = compraRepository.findById(compraId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        compra.setEstado(estado);
        compraRepository.save(compra);

        System.out.println("Estado de la factura actualizado a 'Pagado'");
    }
    
    public void actualizarEstadoRecepcion(Long idRecepcion, char estado) {
        
        // Publicar el evento
        RecepcionFacturada evento = new RecepcionFacturada(idRecepcion, estado, this);
        eventPublisher.publishEvent(evento);
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        ProductosRecepcionadosEvent evento = new ProductosRecepcionadosEvent(productosActualizarExistencias, TipoEventoExistencias.RECEPCIONAR_PRODUCTOS);
        eventPublisher.publishEvent(evento);
    }
    
}
