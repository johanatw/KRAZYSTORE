/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.EstadoPedido;
import Utils.PedidoCanceladoEvent;
import Utils.PedidoEntregaEvent;
import Utils.PedidoEvent;
import Utils.ProductosReservadosEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.DTO.EntregasPedidoDTO;
import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoCreationDTO;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Mapper.DetallePedidoVentaMapper;
import com.krazystore.krazystore.Repository.PedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.PersonaService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidorepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoServiceImpl(PedidoRepository pedidorepository, ApplicationEventPublisher eventPublisher) {
        this.pedidorepository = pedidorepository;
        this.eventPublisher = eventPublisher;
    }

    
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Autowired
    private DetallePedidoVentaMapper detallePedidoMapper;

    @Override
    public List<PedidoDTO> findAll() {
        return pedidorepository.findAllPedidos();
    }

    @Override
    public PedidoCreationDTO findById(Long id) {
        PedidoCreationDTO pedidoDTO = new PedidoCreationDTO();
        PedidoEntity pedido = pedidorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));
        List<DetallePedidoDTO> detalle = detallePedidoService.findDTOByIdPedido(id);
        System.out.println("findById");
        detalle.forEach(det -> System.out.println(det.getSubTotal()));
        
        pedidoDTO.setPedido(pedido);
        pedidoDTO.setDetalle(detalle);
        return pedidoDTO;
        
    }

    @Transactional
    @Override
    public PedidoEntity savePedido(PedidoCreationDTO pedidoCreationDTO) {
         
        if(pedidoCreationDTO.getPedido().getCliente() == null){
            throw new BadRequestException("Falta cliente " );
        }
        
        pedidoCreationDTO.getPedido().setEstadoPedido(Estado.NUEVO.getCodigo());
        pedidoCreationDTO.getPedido().setEstadoFacturacion(Estado.PENDIENTE_DE_FACTURA.getCodigo());
        
        PedidoEntity nuevoPedido = pedidorepository.save(pedidoCreationDTO.getPedido());
        
        List<DetallePedidoEntity> detalle = pedidoCreationDTO.getDetalle()
                .stream()
                .map(detallePedidoMapper)
                .collect(Collectors.toList());
        
        detalle.forEach(det -> det.setPedido(nuevoPedido) );
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detallePedidoService.saveDetallePedido(nuevoPedido.getId(), detalle);
        actualizarExistencias(productosActualizarExistencias);
        
        return nuevoPedido;
   
    }

    @Transactional
    @Override
    public PedidoEntity updatePedido(PedidoCreationDTO pedidoCreationDTO, Long id) throws Exception {
        PedidoEntity updatedPedido = pedidorepository.findById(id).get();
        
        updatedPedido.setCliente(pedidoCreationDTO.getPedido().getCliente());
        updatedPedido.setFecha(pedidoCreationDTO.getPedido().getFecha());
        updatedPedido.setTotal(pedidoCreationDTO.getPedido().getTotal());
        updatedPedido.setObservaciones(pedidoCreationDTO.getPedido().getObservaciones());

        
        PedidoEntity pedido = pedidorepository.save(updatedPedido);
        
        List<DetallePedidoEntity> detalle = pedidoCreationDTO.getDetalle()
                .stream()
                .map(detallePedidoMapper)
                .collect(Collectors.toList());
        

        detalle.forEach(det -> det.setPedido(pedido) );
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = detallePedidoService.updateDetallesPedido(detalle, id);
        
        actualizarExistencias(productosActualizarExistencias);
        
        this.updateEstadoFacturacionPedido(pedido);
        this.updateEstadoPedido(pedido);

        return pedido;
        
    }
    
    public void actualizarEstadoPedido(Long idPedido, TipoEvento tipoEvento) {
        // Publicar el evento
        PedidoEvent evento = new PedidoEvent(idPedido, tipoEvento);
        eventPublisher.publishEvent(evento);
    }
    
    public void updateEstadoPedido(PedidoEntity pedido) {
        Long pedidoId = pedido.getId();

        boolean tieneAnticipos = hasAnticipos(pedidoId);
        
        Character nuevoEstado = pedido.getEstadoPedido();
        EntregasPedidoDTO entregasAsociados = pedidorepository.findEstadoEntregasPedido(pedidoId);
        
        Long totalProductosPedidos = pedidorepository.totalProductosPedido(pedidoId);
        Long totalPreparado = entregasAsociados.getPreparados() + entregasAsociados.getNoEntregados() + entregasAsociados.getEntregados();
        
        boolean hasEntregasPendientes = entregasAsociados.getPendientes()>0;
        boolean isTotalPreparado = totalPreparado == totalProductosPedidos;
        boolean hasEntregados = entregasAsociados.getEntregados() > 0;
        boolean isTotalEntregado = entregasAsociados.getEntregados() == totalProductosPedidos;
        boolean noEntregado = entregasAsociados.getNoEntregados() == totalProductosPedidos;

        if(noEntregado){
            nuevoEstado = Estado.NO_ENTREGADO.getCodigo();
        }else if(hasEntregados){
            nuevoEstado = isTotalEntregado?Estado.ENTREGADO.getCodigo():Estado.PARCIALMENTE_ENTREGADO.getCodigo();
        }else if(totalPreparado>0){
            nuevoEstado = isTotalPreparado?Estado.PREPARADO.getCodigo():Estado.PARCIALMENTE_PREPARADO.getCodigo();
        }else if(tieneAnticipos){
            nuevoEstado = Estado.CONANTICIPO.getCodigo();
        }else{
            nuevoEstado = Estado.NUEVO.getCodigo();
        }

        if (!Objects.equals(nuevoEstado, pedido.getEstadoPedido())) { 
            pedido.setEstadoPedido(nuevoEstado);
            pedidorepository.save(pedido);
        }
        
    }

    public void updateEstadoFacturacionPedido(PedidoEntity pedido) {
        System.out.println("estadofacturacion");
        Long pedidoId = pedido.getId();

        boolean tieneFacturacion = hasFacturacion(pedidoId);
        boolean esTotalmenteFacturado = pedidorepository.esPedidoTotalmenteFacturado(pedidoId);
        
        Character nuevoEstado = pedido.getEstadoFacturacion();
        
        if (tieneFacturacion) {
            System.out.println("TTIENEfacturacion");
            System.out.println(tieneFacturacion);
            nuevoEstado = esTotalmenteFacturado ? Estado.FACTURADO.getCodigo() : Estado.PARCIALMENTE_FACTURADO.getCodigo();
        }else{
            System.out.println("ELSE");
            nuevoEstado = Estado.PENDIENTE_DE_FACTURA.getCodigo();
        }

        if (!Objects.equals(nuevoEstado, pedido.getEstadoFacturacion())) { 
            pedido.setEstadoFacturacion(nuevoEstado);
            pedidorepository.save(pedido);
        }
        
    }
    
    @EventListener
    public void handlePedidoEvents(PedidoEvent evento) {
        PedidoEntity pedido = pedidorepository.findById(evento.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        System.out.println("HANDELPEDIDOESTADO");
        TipoEvento tipoEvento = evento.getTipoEvento();
        switch (tipoEvento) {
            case ESTADO_PEDIDO:
                updateEstadoPedido(pedido);
                break;

            case ESTADO_FACTURACION:
                updateEstadoFacturacionPedido(pedido);
                break;

            default:
                throw new IllegalArgumentException("Evento no reconocido: " + evento.getTipoEvento());
        }
        
    }
    
    @Override
    public void deletePedido(Long id) {       
        if(puedeEliminarsePedido(id)){
            detallePedidoService.deleteByPedido(id);
            pedidorepository.deleteById(id);
        }
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        ProductosReservadosEvent evento = new ProductosReservadosEvent(productosActualizarExistencias, TipoEvento.ACTUALIZAR_RESERVAS);
        eventPublisher.publishEvent(evento);
    }

    public boolean puedeEliminarsePedido(Long id){
        PedidoEntity pedido = pedidorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));
                
        return pedido.getEstadoPedido() == Estado.NUEVO.getCodigo();
    }
    
    private boolean hasFacturacion(Long id) {
        return !pedidorepository.getFacturas(id).isEmpty();
    }

    private boolean hasAnticipos(Long id) {
        return !pedidorepository.getAnticipos(id).isEmpty();
    }
    
    @Override
    public PedidoEntity cancelarPedido(Long id) {
        PedidoEntity pedido = pedidorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));
        
        if(pedido.getEstadoFacturacion().equals(Estado.FACTURADO.getCodigo()) 
                || pedido.getEstadoFacturacion().equals(Estado.PARCIALMENTE_FACTURADO.getCodigo()) ){
            throw new RuntimeException("No se puede cancelar");
        }
        
        List<ProductoExistenciasDTO> productosRevertirReservas = detallePedidoService.getProductosRevertirReservas(id);
        
        actualizarExistencias(productosRevertirReservas);
        pedido.setEstadoPedido(Estado.CANCELADO.getCodigo());
        
        desvincularAnticipos(id);

        return pedidorepository.save(pedido);
    }

    private void desvincularAnticipos(Long id) {
         PedidoCanceladoEvent evento = new PedidoCanceladoEvent(id);
            eventPublisher.publishEvent(evento);
    }
    
}
