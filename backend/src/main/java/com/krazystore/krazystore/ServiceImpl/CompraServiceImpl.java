/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import com.krazystore.krazystore.Events.FacturaCompraPagadoEvent;
import com.krazystore.krazystore.Events.NuevaFacturaCompraEvent;
import com.krazystore.krazystore.Events.PedidoCompraEvent;
import com.krazystore.krazystore.Events.PreciosCompraActualizadosEvent;
import com.krazystore.krazystore.Events.ProductosRecepcionadosEvent;
import com.krazystore.krazystore.Events.RecepcionFacturada;
import com.krazystore.krazystore.Events.RecepcionesFacturadasEvent;
import Utils.TipoEvento;
import Utils.TipoFacturaCompra;
import com.krazystore.krazystore.DTO.CompraCreationDTO;
import com.krazystore.krazystore.DTO.DetalleCompraDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.CostoEntity;
import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Mapper.DetalleCompraMapper;
import com.krazystore.krazystore.Repository.CompraRepository;
import com.krazystore.krazystore.Service.CompraService;
import com.krazystore.krazystore.Service.DetalleCompraService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.RecepcionService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
/**
 *
 * @author HP
 */
@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public CompraServiceImpl(CompraRepository compraRepository, ApplicationEventPublisher eventPublisher) {
        this.compraRepository = compraRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Autowired
    private MovimientoService movimientoService;
    
    @Autowired
    private RecepcionService recepcionService;
    
    @Autowired
    private DetalleCompraService detalleService;
    
    @Autowired
    private DetalleCompraMapper detalleCompraMapper;
    
    @Override
    public List<CompraEntity> findAll() {
        return compraRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public CompraCreationDTO findById(Long id) {
        CompraEntity compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        List<DetalleCompraDTO> detalle = detalleService.findDetallesByIdCompra(id);
        
        CompraCreationDTO compraDTO = new CompraCreationDTO();
        compraDTO.setCompra(compra);
        compraDTO.setDetalle(detalle);
        return compraDTO;
    }
    

    @Transactional
    @Override
    public CompraEntity saveCompra(CompraCreationDTO compraDTO)throws Exception {
        if(compraDTO.getCompra().getProveedor()== null){
            throw new BadRequestException("Falta proveedor " );
        }

        Boolean recepcionAsociada = compraDTO.getCompra().getRecepcion() != null;
        Boolean pedidoAsociado = compraDTO.getCompra().getPedido() != null;
        String tipoFactura = recepcionAsociada?TipoFacturaCompra.SERVICIO_TRANSPORTE_INTERNACIONAL.getCodigo():TipoFacturaCompra.PRODUCTOS.getCodigo();
        
        compraDTO.getCompra().setEstado(Estado.PENDIENTEDEPAGO.getCodigo());
        compraDTO.getCompra().setTipoFactura(tipoFactura);
        CompraEntity nuevaCompra;
        nuevaCompra = compraRepository.save(compraDTO.getCompra());
        String tipoCompra = nuevaCompra.getProveedor().getTipo().getDescripcion();
        
        List<DetalleCompra> detalle = compraDTO.getDetalle()
                .stream()
                .map(detalleCompraMapper)
                .collect(Collectors.toList());
        
        detalle.forEach(det -> det.setCompra(nuevaCompra));
        
        List<CostoEntity> preciosActualizados = detalleService.obtenerCostosParaActualizarAlGuardarFactura(detalle, nuevaCompra.getFecha());
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.saveDetCompra(detalle, nuevaCompra.getId());
        
        if(!preciosActualizados.isEmpty()){
            actualizarCostos(preciosActualizados);
        }
        
        if("Nacional".equals(tipoCompra)){
            actualizarExistencias(productosActualizarExistencias);
        }
        
        if(recepcionAsociada){
            actualizarEstadoRecepcion(nuevaCompra.getRecepcion().getId(), Estado.FACTURADO);
        }else if(pedidoAsociado){
            actualizarEstadoPedido(nuevaCompra.getPedido().getId(), TipoEvento.ESTADO_FACTURACION);
        }
        
        
        notificarFacturaCompraPendiente(nuevaCompra);
        
        return nuevaCompra;
           
    }

    @Transactional
    @Override
    public CompraEntity updateCompra(CompraCreationDTO compraDTO, Long id)throws Exception {
        System.out.println("1");
        CompraEntity compra = compraDTO.getCompra();
        CompraEntity updatedCompra = compraRepository.findById(id).get();
        //No puede modificar una factura si ya esta pagado
        System.out.println("2");
        if(Estado.PAGADO.getCodigo() == updatedCompra.getEstado()){
            throw new BadRequestException("No se puede modificar " );
        }
        System.out.println("3");
        List<DetalleCompra> detalle = compraDTO.getDetalle()
            .stream()
            .map(detalleCompraMapper)
            .collect(Collectors.toList());
        System.out.println("4");
        
        List<CostoEntity> preciosActualizados = detalleService.
                obtenerCostosParaActualizarAlModificarFactura(detalle, id, updatedCompra.getFecha(), compra.getFecha());
        System.out.println("5");
        updatedCompra.setFecha(compra.getFecha());
        updatedCompra.setNroFactura(compra.getNroFactura());
        updatedCompra.setMontoIva(compra.getMontoIva());
        updatedCompra.setTotalGravada(compra.getTotalGravada());
        updatedCompra.setTotalExentas(0);
        updatedCompra.setTotal(compra.getTotal());
        updatedCompra.setProveedor(compra.getProveedor());
        updatedCompra.setTotal(compra.getTotal());
        updatedCompra.setTimbrado(compra.getTimbrado());
        compraRepository.save(updatedCompra);

        System.out.println("6");

        detalle.forEach(det -> det.setCompra(updatedCompra));
        
        if(!preciosActualizados.isEmpty()){
            actualizarCostos(preciosActualizados);
        }
        
        System.out.println("7");
            List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.updateDetCompra(detalle, id);
            actualizarExistencias(productosActualizarExistencias);
        
        
            System.out.println("8");
        notificarFacturaCompraModificada(updatedCompra);
        return updatedCompra;
        
    }

    @Transactional
    @Override
    public void deleteCompra(Long id) {
        System.out.println("DELETED COMPRA");
        CompraEntity compra = compraRepository.findById(id).get();
        Long pedidoId = compra.getPedido()!=null?compra.getPedido().getId():null;
        Long recepcionId = compra.getRecepcion()!=null?compra.getRecepcion().getId():null;
        String tipoProveedor = compra.getProveedor().getTipo().getDescripcion();
        System.out.println(compra.getProveedor().getDescripcion());
        Boolean esRecepcionado = recepcionService.esRecepcionado(id);
        //No puede eliminar una factura si ya esta pagado
        if(Estado.PAGADO.getCodigo() == compra.getEstado()){
            throw new BadRequestException("No se puede Eliminar " );
        }
        System.out.println("DELETED COMPRA2");
        if(esRecepcionado){
            throw new BadRequestException("Factura asociada a recepciones.\nNo se puede eliminar" );
        }
        
        movimientoService.deleteCompra(id);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.deleteDetCompra(id);
        compraRepository.deleteById(id);
        System.out.println(tipoProveedor);
        if("Nacional".equals(tipoProveedor)){
            System.out.println("IFFFF");
            actualizarExistencias(productosActualizarExistencias);
        }
        
        
        if(pedidoId != null){
            actualizarEstadoPedido(pedidoId, TipoEvento.ESTADO_FACTURACION);
        }
        if(recepcionId != null){
            actualizarEstadoRecepcion(recepcionId, Estado.PENDIENTE_DE_FACTURA);
        }
        
        
    }
    
    @EventListener
    public void handleCambioEstado(FacturaCompraPagadoEvent event) {
        Long compraId = event.getFacturaId();
        char estado = event.getEstado();
        // Buscar la factura y actualizar el estado
        CompraEntity compra = compraRepository.findById(compraId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        compra.setEstado(estado);
        compraRepository.save(compra);

    }
    
    public void actualizarEstadoPedido(Long idPedido, TipoEvento tipoEvento) {
        
        // Publicar el evento
        // Publicar el evento
        PedidoCompraEvent evento = new PedidoCompraEvent(idPedido, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
    
    public void actualizarEstadoRecepcion(Long idRecepcion, Estado estado) {
        
        // Publicar el evento
        // Publicar el evento
        RecepcionFacturada evento = new RecepcionFacturada(idRecepcion, estado.getCodigo());
        eventPublisher.publishEvent(evento);
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        ProductosRecepcionadosEvent evento = new ProductosRecepcionadosEvent(productosActualizarExistencias, TipoEvento.RECEPCIONAR_PRODUCTOS);
        eventPublisher.publishEvent(evento);
    }
    
    public void notificarFacturaCompraPendiente(CompraEntity nuevaCompra) {
        // Publicar el evento
        NuevaFacturaCompraEvent evento = new NuevaFacturaCompraEvent(TipoEvento.NUEVA_COMPRA, nuevaCompra );
        eventPublisher.publishEvent(evento);
    }
    
    public void notificarFacturaCompraModificada(CompraEntity compra) {
        // Publicar el evento
        NuevaFacturaCompraEvent evento = new NuevaFacturaCompraEvent(TipoEvento.FACTURA_COMPRA_MODIFICADA, compra );
        eventPublisher.publishEvent(evento);
    }
    
    public void asociarRecepcionesFactura(Long idCompra, List<Long> idsRecepciones) {
        // Publicar el evento
        RecepcionesFacturadasEvent evento = new RecepcionesFacturadasEvent(idCompra, idsRecepciones );
        eventPublisher.publishEvent(evento);
    }
    
            
    public void actualizarCostos(List<CostoEntity> preciosCompra) {
        // Publicar el evento
        PreciosCompraActualizadosEvent evento = new PreciosCompraActualizadosEvent(preciosCompra );
        eventPublisher.publishEvent(evento);
    }

    @Override
    public List<CompraCreationDTO> findFacturasProductosByIdsPedidos(List<Long> ids) {
        List<CompraEntity> compras = compraRepository.findFacturasProductosByIdsPedidos(ids);
        
        if(compras == null){
            return null;
        }
        List<CompraCreationDTO> comprasList = new ArrayList<>();
        
        
        for (CompraEntity compra : compras) {
            CompraCreationDTO compraDTO = new CompraCreationDTO();
            List<DetalleCompraDTO> detalle = detalleService.findDetallesByIdCompra(compra.getId());
            
            compraDTO.setCompra(compra);
            compraDTO.setDetalle(detalle);
            
            comprasList.add(compraDTO);
        }
        
        return comprasList;
    }

    @Override
    public List<CompraCreationDTO> findFacturasByIdPedido(Long id) {
        List<CompraEntity> compras = compraRepository.findFacturasByIdPedido(id);
        
        if(compras == null){
            return null;
        }
        List<CompraCreationDTO> comprasList = new ArrayList<>();
        
        
        for (CompraEntity compra : compras) {
            CompraCreationDTO compraDTO = new CompraCreationDTO();
            List<DetalleCompraDTO> detalle = detalleService.findDetallesByIdCompra(compra.getId());
            
            compraDTO.setCompra(compra);
            compraDTO.setDetalle(detalle);
            
            comprasList.add(compraDTO);
        }
        
        return comprasList;
    }

    
}
