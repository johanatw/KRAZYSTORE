/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;
import Utils.Estado;
import com.krazystore.krazystore.Events.PedidoCompraEvent;
import org.springframework.context.ApplicationEventPublisher;
import com.krazystore.krazystore.Events.ProductosRecepcionadosEvent;
import com.krazystore.krazystore.Events.RecepcionFacturada;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.CompraRecepcion;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Mapper.DetalleRecepcionMapper;
import com.krazystore.krazystore.Mapper.RecepcionMapper;
import com.krazystore.krazystore.Repository.RecepcionRepository;
import com.krazystore.krazystore.Service.CompraRecepcionService;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.List;
import java.util.Map;
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
   
   @Autowired
    private CompraRecepcionService CompraRecepcionService;
   
   @Override
    public List<RecepcionDTO> findAll() { 
        List<RecepcionDTO> recepcionesDTO = recepcionRepository.findAllRecepciones();
       
        
        // Agrupamos todas las recepciones por su ID
        Map<Long, List<RecepcionDTO>> agrupadasPorId = recepcionesDTO.stream()
            .collect(Collectors.groupingBy(RecepcionDTO::getId));
        
        List<RecepcionDTO> resultado = agrupadasPorId.entrySet().stream()
                .map(entry -> {
                    Long recepcionId = entry.getKey();
                    List<RecepcionDTO> items = entry.getValue();
                
                    RecepcionDTO primero = items.get(0); // porque todos tendrán la misma recepción
                    items.forEach((i)->{System.out.println(i.getIdPedido());});
                    // Convertir lista de pedidos a una sola cadena
                    String pedidosAsociados = items.stream()
                        .map(r -> String.valueOf(r.getIdPedido()))
                        .collect(Collectors.joining(","));
                    System.out.println(pedidosAsociados);
                    
                    String idsPedido = "null".equals(pedidosAsociados)?null:pedidosAsociados;

                    return new RecepcionDTO(
                        recepcionId,
                        primero.getFecha(),
                        "null".equals(pedidosAsociados)?null:pedidosAsociados,
                        primero.getEstado()
                    );
                })
                .collect(Collectors.toList());
        
        
        return resultado;
    }
    
    @Override
    public RecepcionCreationDTO findById(Long id) {
        //return recepcionRepository.findById(id);
        RecepcionCreationDTO recepcionDTO = new RecepcionCreationDTO();
        Optional<RecepcionDTO> recepcion = recepcionRepository.findRecepcion(id);
        if (recepcion.isPresent()) {
            List<DetalleRecepcionDTO> detalles = detalleService.findDetalleByIdRecepcion(id);
            recepcionDTO.setDetalle(detalles);
            recepcionDTO.setRecepcion(recepcion.get());
        }
        return recepcionDTO;
    }
    /*
    

    
    
    

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

    
    

    @Override
    public int getTotalRecepcionadoPorProducto(Long pedidoId, Long productoId){
        return 0;
    }
    
    
    
    */
    
    @Override
    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids) {
        return detalleService.obtenerDetallesFacturasRecepcionar(ids);
    }
    
   /* @Override
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

    @Override
    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids) {
        return detalleService.obtenerDetallesFacturasRecepcionar(ids);
    }

*/
    @Transactional
    @Override
    public RecepcionEntity saveRecepcion(RecepcionCreationDTO recepcionDTO) {
        System.out.println("saveREcepcion");
        RecepcionEntity recepcion;
        recepcion = recepcionMapper.apply(recepcionDTO.getRecepcion());
        //recepcion.setEstado(Estado.PENDIENTE_DE_FACTURA.getCodigo());
        
        
        recepcion.setEstado(Estado.PENDIENTE_DE_FACTURA.getCodigo());
        
        RecepcionEntity nuevaRecepcion = recepcionRepository.save(recepcion);
     
        List<DetalleRecepcion> detalle = recepcionDTO.getDetalle()
                .stream()
                .map(detalleRecepcionMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setRecepcion(nuevaRecepcion));
            
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.saveDetRecepcion(detalle, nuevaRecepcion.getId());
        
        CompraRecepcionService.saveComprasRecepcion(nuevaRecepcion,recepcionDTO.getIdsCompras());
        List<Long> idsPedidosRecepcionados = CompraRecepcionService.getIdsPedidosComprasRecepcionadosByIdRecepcion(nuevaRecepcion.getId());
        actualizarRecepcionPedidosCompra(idsPedidosRecepcionados);
        actualizarExistencias(productosActualizarExistencias);
        
        return nuevaRecepcion;
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

    public void actualizarRecepcionPedidosCompra(List<Long> idsPedidos) {
        
        // Publicar el evento
        PedidoCompraEvent evento = new PedidoCompraEvent( TipoEvento.ESTADO_PEDIDO, idsPedidos);
        eventPublisher.publishEvent(evento);
    }
    
    @Transactional
    @Override
    public void deleteRecepcion(Long id) {
        RecepcionEntity recepcion = recepcionRepository.findById(id).get();
        //Si ya se facturo no se puede eliminar
        if(Estado.FACTURADO.getCodigo() == recepcion.getEstado()){
            throw new BadRequestException("No se puede eliminar " );
        }
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.deleteDetRecepcion(id);
        
        List<Long> idsPedidosRecepcionados = CompraRecepcionService.getIdsPedidosComprasRecepcionadosByIdRecepcion(id);
        CompraRecepcionService.deleteComprasRecepcionadosByIdRecepcion(id);
        
        recepcionRepository.deleteById(id);
        actualizarExistencias(productosActualizarExistencias);
        actualizarRecepcionPedidosCompra(idsPedidosRecepcionados);
        
    }

    @Override
    public Boolean esRecepcionado(Long id) {
        List<CompraRecepcion> compraRecepciones = CompraRecepcionService.getCompraRecepcionesByIdCompra(id);
        return !compraRecepciones.isEmpty();
    }

}
