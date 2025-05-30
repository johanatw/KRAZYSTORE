/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.ProveedorEntity;
import com.krazystore.krazystore.Repository.ProveedorRepository;
import com.krazystore.krazystore.Service.ProveedorService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class ProveedorServiceImpl implements ProveedorService {
    
    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }
    
    @Override
    public List<ProveedorEntity> findAll() {
        
        return proveedorRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<ProveedorEntity> findById(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public ProveedorEntity saveProveedor(ProveedorEntity proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    @Override
    public ProveedorEntity updateProveedor(ProveedorEntity proveedor, Long id) {
        ProveedorEntity updatedProveedor = proveedorRepository.findById(id).get();
        
        updatedProveedor.setDescripcion(proveedor.getDescripcion());
        updatedProveedor.setRuc(proveedor.getRuc());
        updatedProveedor.setTelefono(proveedor.getTelefono());
        updatedProveedor.setCiudad(proveedor.getCiudad());
        updatedProveedor.setCorreo(proveedor.getCorreo());
        updatedProveedor.setDireccion(proveedor.getDireccion());
        updatedProveedor.setTipo(proveedor.getTipo());
        
        return proveedorRepository.save(updatedProveedor);
    }

    @Override
    public void deleteProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public List<ProveedorEntity> findProveedoresProductos() {
        return proveedorRepository.findProveedoresProductos();
    }

    @Override
    public List<ProveedorEntity> findProveedoresImportacion() {
        return proveedorRepository.findProveedoresImportacion();
    }

    @Override
    public List<ProveedorEntity> findProveedoresNacionalesProductos() {
        return proveedorRepository.findProveedoresNacionalesProductos();
    }
    
}
