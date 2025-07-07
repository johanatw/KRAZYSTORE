/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.ProveedorEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ProveedorService {
    List<ProveedorEntity> findAll();
    Optional<ProveedorEntity> findById(Long id);
    ProveedorEntity saveProveedor(ProveedorEntity proveedor);
    ProveedorEntity updateProveedor(ProveedorEntity proveedor, Long id);
    void deleteProveedor(Long id);

    public List<ProveedorEntity> findProveedoresProductos();

    public List<ProveedorEntity> findProveedoresImportacion();

    public List<ProveedorEntity> findProveedoresNacionalesProductos();
}
