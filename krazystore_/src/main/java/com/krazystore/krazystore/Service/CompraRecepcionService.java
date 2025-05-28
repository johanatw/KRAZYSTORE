/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.CompraRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public interface CompraRecepcionService {

    public void saveComprasRecepcion(RecepcionEntity nuevaRecepcion, List<Long> idsCompras);
    List<Long> getIdsPedidosComprasRecepcionadosByIdRecepcion(Long idRecepcion);
    void deleteComprasRecepcionadosByIdRecepcion(Long idRecepcion);

    public List<CompraRecepcion> getCompraRecepcionesByIdCompra(Long id);
    
}
