/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetallePedidoCreationRequest;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;

import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.DetallePedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;

import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.ProductoService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{
    private final DetallePedidoRepository detallepedidorepository;
   
    
    @Autowired
    private ProductoService productoService;
    
    
    public DetallePedidoServiceImpl(DetallePedidoRepository detallepedidorepository) {
        this.detallepedidorepository = detallepedidorepository;
    }

    @Override
    public List<DetallePedidoEntity> findAll() {
        return detallepedidorepository.findAll();
    }

    @Override
    public Optional<DetallePedidoEntity> findById(Long id) {
        return detallepedidorepository.findById(id);
    }
    
     @Override
    public List<DetallePedidoDTO> findByNroPedido(Long idPedido) {
        //return detallepedidorepository.findByNroPedido(idPedido);
        return detallepedidorepository.findPedido(idPedido);
    }
    
    @Override
    public List<DetallePedidoEntity> findByPedido(Long idPedido) {
        return detallepedidorepository.findByNroPedido(idPedido);
    }
    

    @Override
    public Iterable<DetallePedidoEntity> saveDetallePedido(Long idPedido, List<DetallePedidoEntity> detalles) {
        System.out.println("entraaca");
        List<ProductoEntity> productosActualizar = new ArrayList<>();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(idPedido);
        detalles.forEach(d -> {
            d.setPedido(pedido);
            d.setSaldoPendiente(d.getSubtotal());
            productosActualizar.add(d.getProducto());
        });
        
        productoService.updateExistencias(productosActualizar);
        return detallepedidorepository.saveAll(detalles);
    }

    @Override
    public Iterable<DetallePedidoEntity> saveDetalle(DetallePedidoCreationRequest detallePedidoDTO) {
        List<DetallePedidoEntity> detallesPedido = detallePedidoDTO.getDetalle();
        System.out.println(detallePedidoDTO.getPedido().getId());
        PedidoEntity pedido = detallePedidoDTO.getPedido();
        System.out.println("gentraguardarpedido");
        System.out.println(pedido);
        detallesPedido.forEach(d -> {
            d.setSaldoPendiente(d.getSubtotal());
            d.setPedido(pedido);
            System.out.println("guardarpedido getsubtotal");
            
            System.out.print(d.getSubtotal());
            System.out.println(d.getPedido().getId());
      
        });
        
        return detallepedidorepository.saveAll(detallesPedido);
    }

    @Transactional
    @Override
    public Iterable<DetallePedidoEntity> updateDetallesPedido(List<DetallePedidoEntity> nuevosDetalles, Long id) throws Exception {

        List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id);
        List<DetallePedidoEntity> itemsRegistrar = new ArrayList<>();
        List<DetallePedidoEntity> itemsModificar = new ArrayList<>();
        List<DetallePedidoEntity> itemsEliminar = new ArrayList<>();
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();
        
        
            //Obtiene los elementos a registrar
            itemsRegistrar = getElementos(nuevosDetalles, detallesAnteriores, "REGISTRAR");      
            //Obtiene los elementos a modificar
            itemsModificar = getElementos(nuevosDetalles, detallesAnteriores, "MODIFICAR");
            //Obtiene los elementos a eliminar
            itemsEliminar = getElementos(detallesAnteriores, nuevosDetalles, "ELIMINAR");
       
        
        //Si hay elementos a eliminar
         if(!itemsEliminar.isEmpty()){
             //Obtiene los productos con las existencias actualizadas
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsEliminar,null,"ELIMINAR"));
            //Elimina detalles anteriores
            detallepedidorepository.deleteAll(itemsEliminar);
        }
        //Si hay elementos a modificar
        if(!itemsModificar.isEmpty()){
            //Obtiene los productos con las existencias actualizadas
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsModificar,detallesAnteriores,"MODIFICAR"));
            //Modifica detalles actualizados
            detallepedidorepository.saveAll(itemsModificar);
        }
        //Si hay elementos a registrar
        if(!itemsRegistrar.isEmpty()){
            //Obtiene los productos con las existencias actualizadas
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsRegistrar,null,"REGISTRAR"));    
            //Registra detalles nuevos
            detallepedidorepository.saveAll(itemsRegistrar);
        }
        //Si hay productos a actualizar
        if(!productosActualizarExistencias.isEmpty()){
            //Actualiza la existencia de los productos modificados
            productoService.updateExistencias(productosActualizarExistencias);
        }
        return null;
        
    }
    
    @Override
    public DetallePedidoEntity updateDetallePedido(DetallePedidoEntity detallePedido, Long id) {
        DetallePedidoEntity updatedDetallePedido = detallepedidorepository.findById(id).get();
        
        updatedDetallePedido.setPedido(detallePedido.getPedido());
        updatedDetallePedido.setProducto(detallePedido.getProducto());
        updatedDetallePedido.setCantidad(detallePedido.getCantidad());
        updatedDetallePedido.setMontoIva(detallePedido.getMontoIva());
        updatedDetallePedido.setSubtotal(detallePedido.getSubtotal());
        return detallepedidorepository.save(updatedDetallePedido);
    }

    
    public List<DetallePedidoEntity> getElementos (List<DetallePedidoEntity> lista1, List<DetallePedidoEntity> lista2, String opcion)throws Exception {
        List<DetallePedidoEntity> elementos = new ArrayList<>();
        
        
                           
        //Recorre lista1
        lista1.forEach((DetallePedidoEntity   d)-> {
            
            
        
                    int idProductoNuevoDetalle = (int)(long) d.getProducto().getId();
                    //Busca el elemento de la lista1 en la lista2
                    boolean existe;
            existe = lista2.stream().anyMatch((obj) -> {
                if (opcion == "MODIFICAR"){
                    //Si el elemento coincide con elemento de la lista2 y la cantidad del detalle es diferente
                    System.out.println("MODIFICaaaaaaAAAAR");
                    if(obj.getProducto().getId() == idProductoNuevoDetalle && obj.getCantidad() != d.getCantidad()){
                        //Modifica detalle
                        
                        d.setId(obj.getId());
                        d.setPedido(obj.getPedido());
                        
                        /*d.setPagado(obj.getPagado());
                        
                        //Verificar si ya hay pagos asociados al detalle
                        //if(obj.getPagado() > d.getSubtotal()){
                        if(d.getCantidadPagado() > d.getCantidad()){
                            throw new RuntimeException("No es posible modificar la cantidad del Producto: "+ obj.getProducto().getNombre() +"\n ya hay pagos asociados a este detalle");
                            
                        }else{
                            d.setSaldoPendiente(d.getSubtotal() - obj.getPagado());
                        }*/
                        return true;
                    }
                    
                }else if(opcion == "REGISTRAR"){
                    //Si el elemento coincide con elemento de la lista2
                    if(obj.getProducto().getId() == idProductoNuevoDetalle){
                        return true;
                    }else{ //Si no, asigna pedido a nuevo detalle
                        d.setPedido(obj.getPedido());
                        d.setSaldoPendiente(d.getSubtotal());
                    }
                }else{
                    //si producto esta en lista 1 y no en lista 2 se elimina

                    if(obj.getProducto().getId() == idProductoNuevoDetalle){
                        System.out.println("TRUE");
                        
                        return true;
                    }
                }
                return false;
            });

                    if (opcion == "MODIFICAR"){
                        if(existe){
                            elementos.add(d);
                        }
                    /*}else if(opcion == "ELIMINAR"){
                        System.out.println("ELIMINARAAAAAADDDDDDD");
                        if(!existe && d.getPagado() < 1){
                            System.out.println("NO EXISTE Y PAGADO MAYOR A 1");
                            System.out.println(d.getProducto().getNombre());
                            System.out.println(d.getPagado());
                            elementos.add(d);
                        }else if(!existe && d.getPagado() > 1){
                            System.out.println("ERROR");
                            System.out.println(d.getProducto().getNombre());
                            System.out.println(d.getPagado());
                            throw new RuntimeException("No es posible eliminar el Producto: "+ d.getProducto().getNombre() +"\n ya hay pagos asociados a este detalle");
                    
                        }*/
                      
                    }else{
                        if(!existe){
                            elementos.add(d);
                        }
                    }
                });
        
      return elementos;
    }
    
    public List<ProductoEntity> getProductosExistenciasActualizadas(List<DetallePedidoEntity> detalles1, List<DetallePedidoEntity> detalles2, String tipoItems) {
       List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();
       
       detalles1.forEach((detalle)-> {
           ProductoEntity producto = detalle.getProducto();
           
           switch (tipoItems) { 
            case "REGISTRAR":
                producto.setCantDisponible(producto.getCantDisponible());
                producto.setCantReservada(producto.getCantReservada());
                break;
            case "MODIFICAR":
                DetallePedidoEntity detalleAnterior = detalles2.stream().filter(obj -> Objects.equals(obj.getProducto().getId(), detalle.getProducto().getId())).findFirst().get();

                int cantDisponibleAnt = detalleAnterior.getProducto().getCantDisponible();
                int cantReservadaAnt = detalleAnterior.getProducto().getCantReservada();
                producto.setCantDisponible( (cantDisponibleAnt + detalleAnterior.getCantidad() - detalle.getCantidad()));
                producto.setCantReservada(cantReservadaAnt - detalleAnterior.getCantidad() + detalle.getCantidad());
            
                break;
            case "ELIMINAR":
                producto.setCantDisponible(producto.getCantDisponible() + detalle.getCantidad());
                producto.setCantReservada(producto.getCantReservada() - detalle.getCantidad()); 
                break;
          }
           
           productosActualizarExistencias.add(producto);
        });
       
       return productosActualizarExistencias;
    }
    /*
    @Override
    public DetallePedidoEntity updateDetallePedido(DetallePedidoEntity detallePedido, Long id) {
        DetallePedidoEntity updatedDetallePedido = detallepedidorepository.findById(id).get();
        
        updatedDetallePedido.setPedido(detallePedido.getPedido());
        updatedDetallePedido.setProducto(detallePedido.getProducto());
        updatedDetallePedido.setCantidad(detallePedido.getCantidad());
        updatedDetallePedido.setMontoIva(detallePedido.getMontoIva());
        updatedDetallePedido.setSubtotal(detallePedido.getSubtotal());
        return detallepedidorepository.save(updatedDetallePedido);
    }
*/
    
  
    @Override
    public void updateDetallesFacturadas(List<DetalleVentaEntity> detalles, PedidoEntity pedido){
        List<DetallePedidoEntity> detallePedido = detallepedidorepository.findByNroPedido(pedido.getId());
        List<DetallePedidoEntity> detallePedidoUpdated = new ArrayList<>();
       detalles.forEach((detalle)-> {
           DetallePedidoEntity detalleAnterior = detallePedido.stream().filter(obj -> Objects.equals(obj.getProducto().getId(), detalle.getProducto().getId())).findFirst().get();

           if(detalleAnterior != null){
               detalleAnterior.setCantidadFacturada(detalleAnterior.getCantidadFacturada() + detalle.getCantidad());
               detallePedidoUpdated.add(detalleAnterior);
           }

        });
       detallepedidorepository.saveAll(detallePedidoUpdated);
       
    }
    
    @Override
    public void deleteDetallePedido(Long id) {
        detallepedidorepository.deleteById(id);
    }
    
    @Override
    public void deleteDetallesPedido(List<Long> ids) {
        detallepedidorepository.deleteAllById(ids);
    }
    
    @Override
    public void deleteByPedido(Long id) {
        detallepedidorepository.deleteByPedido(id);
    }
    

}
