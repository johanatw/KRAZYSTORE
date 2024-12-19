/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import Utils.ProductosPedidoRecepcionadosEvent;
import Utils.RecepcionFacturada;
import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Repository.RecepcionRepository;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class RecepcionServiceImpl implements RecepcionService {
    private final RecepcionRepository recepcionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RecepcionServiceImpl(RecepcionRepository recepcionRepository, ApplicationEventPublisher eventPublisher) {
        this.recepcionRepository = recepcionRepository;
        this.eventPublisher = eventPublisher;
    }

    
    
    @Autowired
    private DetalleRecepcionService detalleService;
    
   
    
    @Override
    public List<RecepcionDTO> findAll() { 
        return recepcionRepository.findAllRecepciones();
    }

    @Override
    public Optional<RecepcionDTO> findById(Long id) {
        //return recepcionRepository.findById(id);
        Optional<RecepcionDTO> recepcion = recepcionRepository.findRecepcion(id);
        if (recepcion.isPresent()) {
            List<DetalleRecepcionDTO> detalles = recepcionRepository.findDetallesByRecepcionId(id);
            recepcion.get().setDetalle(detalles);
        }
        return recepcion;
    }
    
    @Transactional
    @Override
    public RecepcionEntity saveRecepcion(RecepcionCreationDTO recepcionDTO) {
        List<DetalleRecepcion> detalle = new ArrayList<>();
        RecepcionEntity recepcion;
        recepcion = recepcionDTO.getRecepcion();
        
        RecepcionEntity nuevaRecepcion = recepcionRepository.save(recepcion);
        detalle = detalleService.saveDetRecepcion(recepcionDTO.getDetalle(), nuevaRecepcion.getId());
        
        actualizarRecepcionPedidoCompra(recepcionDTO.getDetalle().get(0).getDetallePedido().getPedidoCompra().getId());
        //pedidoService.actualizarCantidadesRecepcionadas(detalle);
        
        return nuevaRecepcion;
    }

    @Transactional
    @Override
    public RecepcionEntity updateRecepcion(RecepcionCreationDTO recepcionDTO, Long id) {
        RecepcionEntity updatedRecepcion = recepcionRepository.findById(id).get();
        List<DetalleRecepcion> detalleActual = new ArrayList<>();
        List<DetalleRecepcion> detalleAnterior = new ArrayList<>();
        
        //Si ya se facturo no se puede modificar
        if('F' == updatedRecepcion.getEstado()){
            throw new BadRequestException("No se puede modificar " );
        }
        RecepcionEntity recepcion;
        recepcion = recepcionDTO.getRecepcion();
        
        updatedRecepcion.setFecha(recepcion.getFecha());
        
        recepcionRepository.save(updatedRecepcion);
        //detalleService.updateDetRecepcion(recepcionDTO.getDetalle(), id);
        
        detalleAnterior = detalleService.findByIdRecepcion(updatedRecepcion.getId());
        
        
        detalleActual = detalleService.updateDetRecepcion(recepcionDTO.getDetalle(), updatedRecepcion.getId());
        
        actualizarRecepcionPedidoCompra(detalleActual.get(0).getDetallePedido().getPedidoCompra().getId());
        
       // pedidoService.modificarCantidadesRecepcionadas(detalleActual, detalleAnterior);
        
        return updatedRecepcion;
    }

    @Transactional
    @Override
    public void deleteRecepcion(Long id) {
        RecepcionEntity recepcion = recepcionRepository.findById(id).get();
        //Si ya se facturo no se puede eliminar
        if('F' == recepcion.getEstado()){
            throw new BadRequestException("No se puede eliminar " );
        }
        detalleService.deleteDetRecepcion(id);
        recepcionRepository.deleteById(id);
    }
    

    @Override
    public int getTotalRecepcionadoPorProducto(Long pedidoId, Long productoId){
        return recepcionRepository.getTotalRecepcionadoPorProducto(pedidoId, productoId);
    }
    
    public void actualizarRecepcionPedidoCompra(Long idPedido) {
        
        // Publicar el evento
        ProductosPedidoRecepcionadosEvent evento = new ProductosPedidoRecepcionadosEvent(idPedido, this);
        eventPublisher.publishEvent(evento);
    }
    
    @EventListener
    public void handleCambioEstado(RecepcionFacturada event) {
        Long recepcionId = event.getIdRecepcion();
        char estado = event.getEstado();
        // Buscar la factura y actualizar el estado
        RecepcionEntity recepcion = recepcionRepository.findById(recepcionId)
            .orElseThrow(() -> new RuntimeException("Recepcion no encontrada"));

        recepcion.setEstado(estado);
        recepcionRepository.save(recepcion);

       
    }
    
}
