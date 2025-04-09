/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleCompraDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CostoEntity;
import com.krazystore.krazystore.Entity.DetalleCompra;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetalleCompraService {
    List<DetalleCompra> findByIdCompra(Long id);
    List<DetalleCompraDTO> findDetallesByIdCompra(Long idcompra);
    List<ProductoExistenciasDTO> saveDetCompra(List<DetalleCompra> detalle, Long idCompra) throws Exception;
    List<ProductoExistenciasDTO> updateDetCompra(List<DetalleCompra> detalle, Long idCompra)throws Exception;
    List<ProductoExistenciasDTO> deleteDetCompra(Long idCompra);
    boolean esCostoActualizado(Long idProducto, int costo, Date fecha);
    
    List<CostoEntity> getPreciosCompraActualizados(List<DetalleCompra> detalle, Long idCompra, Date fechaFactura, Date fechaAnteriorFactura);
}
