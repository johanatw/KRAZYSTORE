/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.FacturaCompraPagadoEvent;
import Utils.NuevaFacturaCompraEvent;
import Utils.PagoRegistradoEvent;
import Utils.PedidoCompraEvent;
import Utils.PreciosCompraActualizadosEvent;
import Utils.ProductosRecepcionadosEvent;
import Utils.RecepcionFacturada;
import Utils.RecepcionesFacturadasEvent;
import Utils.TipoEvento;
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
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
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
    
    @Override
    public List<CompraCreationDTO> findByIdPedido(Long idPedido) {
        List<CompraEntity> compras = compraRepository.findByIdPedido(idPedido);
        
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

    @Transactional
    @Override
    public CompraEntity saveCompra(CompraCreationDTO compraDTO)throws Exception {
        if(compraDTO.getCompra().getProveedor()== null){
            throw new BadRequestException("Falta proveedor " );
        }
        //System.out.println("entra savecompra");
        
        List<Long> idRecepcionesAsociadas = compraDTO.getIdRecepciones();
        
        
        
        compraDTO.getCompra().setEstado(Estado.PENDIENTEDEPAGO.getCodigo());
        CompraEntity nuevaCompra;
        nuevaCompra = compraRepository.save(compraDTO.getCompra());
        
        List<DetalleCompra> detalle = compraDTO.getDetalle()
                .stream()
                .map(detalleCompraMapper)
                .collect(Collectors.toList());
        
        detalle.forEach(det -> det.setCompra(nuevaCompra));
        
        List<CostoEntity> preciosActualizados = detalleService.getPreciosCompraActualizados(detalle, null, nuevaCompra.getFecha(), null);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.saveDetCompra(detalle, nuevaCompra.getId());
        
        if(idRecepcionesAsociadas != null && !idRecepcionesAsociadas.isEmpty()){
            System.out.println("entra if recepcion");
            asociarRecepcionesFactura(nuevaCompra.getId(), idRecepcionesAsociadas);
        }
            
        if(!preciosActualizados.isEmpty()){
            actualizarCostos(preciosActualizados);
        }
        
        if(nuevaCompra.getPedido() != null){
            actualizarEstadoPedido(nuevaCompra.getPedido().getId(), TipoEvento.PEDIDO_FACTURADO);
        }
        actualizarExistencias(productosActualizarExistencias);
        
        
        
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
        
        List<CostoEntity> preciosActualizados = detalleService.getPreciosCompraActualizados(detalle, id, compra.getFecha(), updatedCompra.getFecha());
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
        if(updatedCompra.getPedido() != null){
            actualizarEstadoPedido(updatedCompra.getPedido().getId(), TipoEvento.PEDIDO_MODIFICADO);
        }
            System.out.println("8");
        //notificarFacturaCompraModificada(updatedCompra);
        return updatedCompra;
        
    }

    @Transactional
    @Override
    public void deleteCompra(Long id) {
        CompraEntity compra = compraRepository.findById(id).get();
        //No puede eliminar una factura si ya esta pagado
        if(Estado.PAGADO.getCodigo() == compra.getEstado()){
            throw new BadRequestException("No se puede Eliminar " );
        }
        if(compra.getPedido() != null){
            actualizarEstadoPedido(compra.getPedido().getId(), TipoEvento.PEDIDO_MODIFICADO);
        }
        movimientoService.deleteCompra(id);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.deleteDetCompra(id);
        compraRepository.deleteById(id);
        
        actualizarExistencias(productosActualizarExistencias);
        
        
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
}
