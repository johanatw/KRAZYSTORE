/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;
import Utils.Estado;
import Utils.PedidoCompraEvent;
import org.springframework.context.ApplicationEventPublisher;
import Utils.ProductosPedidoRecepcionadosEvent;
import Utils.ProductosRecepcionadosEvent;
import Utils.RecepcionFacturada;
import Utils.RecepcionesFacturadasEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Mapper.DetalleRecepcionMapper;
import com.krazystore.krazystore.Mapper.RecepcionMapper;
import com.krazystore.krazystore.Repository.RecepcionRepository;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
import com.krazystore.krazystore.Service.PedidoCompraService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    
   @Autowired
    private RecepcionMapper recepcionMapper;
   
   @Autowired
    private DetalleRecepcionMapper detalleRecepcionMapper;
    
    @Override
    public List<RecepcionDTO> findAll() { 
        return recepcionRepository.findAllRecepciones();
    }

    @Override
    public RecepcionCreationDTO findById(Long id) {
        //return recepcionRepository.findById(id);
        RecepcionCreationDTO recepcionDTO = new RecepcionCreationDTO();
        Optional<RecepcionDTO> recepcion = recepcionRepository.findRecepcion(id);
        if (recepcion.isPresent()) {
            List<DetalleRecepcionDTO> detalles = recepcionRepository.findDetallesByRecepcionId(id);
            recepcionDTO.setDetalle(detalles);
            recepcionDTO.setRecepcion(recepcion.get());
        }
        return recepcionDTO;
    }
    
    @Transactional
    @Override
    public RecepcionEntity saveRecepcion(RecepcionCreationDTO recepcionDTO) {
        System.out.println("saveREcepcion");
        RecepcionEntity recepcion;
        recepcion = recepcionMapper.apply(recepcionDTO.getRecepcion());
        //recepcion.setEstado(Estado.PENDIENTEDEFACTURA.getCodigo());
        
        if(recepcionDTO.getRecepcion().getCompra() == null){
            recepcion.setEstado(Estado.PENDIENTEDEFACTURA.getCodigo());
        }else{
            recepcion.setEstado(Estado.FACTURADO.getCodigo());
        }
        RecepcionEntity nuevaRecepcion = recepcionRepository.save(recepcion);
     
        List<DetalleRecepcion> detalle = recepcionDTO.getDetalle()
                .stream()
                .map(detalleRecepcionMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setRecepcion(nuevaRecepcion));
            
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.saveDetRecepcion(detalle, nuevaRecepcion.getId());
        
        actualizarRecepcionPedidoCompra(recepcionDTO.getRecepcion().getIdPedido());
        actualizarExistencias(productosActualizarExistencias);
        
        return nuevaRecepcion;
    }

    @Transactional
    @Override
    public RecepcionEntity updateRecepcion(RecepcionCreationDTO recepcionDTO, Long id) {
        RecepcionEntity updatedRecepcion = recepcionRepository.findById(id).get();
        
        //Si ya se facturo no se puede modificar
        if(Estado.FACTURADO.getCodigo() == updatedRecepcion.getEstado()){
            throw new BadRequestException("No se puede modificar " );
        }
        
        updatedRecepcion.setFecha(recepcionDTO.getRecepcion().getFecha());
        
        List<DetalleRecepcion> detalle = recepcionDTO.getDetalle()
                .stream()
                .map(detalleRecepcionMapper)
                .collect(Collectors.toList());
        
        recepcionRepository.save(updatedRecepcion);      
        
        detalle.forEach(det -> det.setRecepcion(updatedRecepcion));
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.updateDetRecepcion(detalle, id);
        
        actualizarRecepcionPedidoCompra(recepcionDTO.getRecepcion().getIdPedido());
        actualizarExistencias(productosActualizarExistencias);
        
        return updatedRecepcion;
    }

    @Transactional
    @Override
    public void deleteRecepcion(Long id) {
        RecepcionEntity recepcion = recepcionRepository.findById(id).get();
        //Si ya se facturo no se puede eliminar
        if(Estado.FACTURADO.getCodigo() == recepcion.getEstado()){
            throw new BadRequestException("No se puede eliminar " );
        }
        Long idPedidoCompra = recepcionRepository.getIdPedidoCompraByIdRecepcion(id);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.deleteDetRecepcion(id);
        
        recepcionRepository.deleteById(id);
        actualizarExistencias(productosActualizarExistencias);
        actualizarRecepcionPedidoCompra(idPedidoCompra);
        
    }
    

    @Override
    public int getTotalRecepcionadoPorProducto(Long pedidoId, Long productoId){
        return recepcionRepository.getTotalRecepcionadoPorProducto(pedidoId, productoId);
    }
    
    public void actualizarRecepcionPedidoCompra(Long idPedido) {
        
        // Publicar el evento
        PedidoCompraEvent evento = new PedidoCompraEvent(idPedido, TipoEvento.PEDIDO_RECEPCIONADO);
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
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        ProductosRecepcionadosEvent evento = new ProductosRecepcionadosEvent(productosActualizarExistencias, TipoEvento.RECEPCIONAR_PRODUCTOS);
        eventPublisher.publishEvent(evento);
    }
    
    @Override
    public List<DetalleRecepcionDTO> obtenerDetalleFacturaRecepcionar(Long idCompra) {
        return detalleService.obtenerDetalleFacturaRecepcionar(idCompra);
    }
    
    @Override
    public List<RecepcionCreationDTO> findByIdPedido(Long idPedido) {
        List<RecepcionDTO> recepcionesDTO = recepcionRepository.findRecepcionesByIdPedido(idPedido);
        
        if(recepcionesDTO  == null){
            return null;
        }
        List<RecepcionCreationDTO> recepcionesList = new ArrayList<>();
        
        
        for (RecepcionDTO recepcion : recepcionesDTO) {
            RecepcionCreationDTO recepcionDTO = new RecepcionCreationDTO();
            List<DetalleRecepcionDTO> detalle = detalleService.findDetalleByIdRecepcion(recepcion.getId());
            
            recepcionDTO.setRecepcion(recepcion);
            recepcionDTO.setDetalle(detalle);
            
            recepcionesList.add(recepcionDTO);
        }
        
        
        return recepcionesList;
    }
    
    
    @EventListener
    public void handleRecepcionesFacturadas(RecepcionesFacturadasEvent event) {
        List<Long> ids = event.getIdsRecepciones();
        Long idCompra = event.getIdCompra();
        char estado = Estado.FACTURADO.getCodigo();
        System.out.println("handleRecepcionesFacturadas");
        System.out.println(idCompra);
        ids.forEach((i)->System.out.println(i));
        
        recepcionRepository.asociarRecepcionesFactura(idCompra, ids, estado);
       
    }


}
