/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.CompraCreationDTO;
import com.krazystore.krazystore.Entity.CompraEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CompraService {
    List<CompraEntity> findAll();
    //CompraCreationDTO findById(Long id);
    //List<CompraCreationDTO> findByIdPedido(Long idPedido);
    CompraEntity saveCompra(CompraCreationDTO compra)throws Exception;
    CompraEntity updateCompra(CompraCreationDTO compra, Long id)throws Exception;
    void deleteCompra(Long id);
    public CompraCreationDTO findCompraRecepcionarById(Long id);

    public List<CompraCreationDTO> findFacturasProductosByIdsPedidos(List<Long> ids);

    public List<CompraCreationDTO> findFacturasByIdPedido(Long id);

    public CompraCreationDTO findById(Long id);
}
