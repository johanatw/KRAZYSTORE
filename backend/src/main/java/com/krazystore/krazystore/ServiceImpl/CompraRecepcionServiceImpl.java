/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.CompraRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Repository.CompraRecepcionRepository;
import com.krazystore.krazystore.Service.CompraRecepcionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class CompraRecepcionServiceImpl implements  CompraRecepcionService{

    public CompraRecepcionServiceImpl(com.krazystore.krazystore.Repository.CompraRecepcionRepository compraRecepcionRepository) {
        this.compraRecepcionRepository = compraRecepcionRepository;
    }
    private final CompraRecepcionRepository compraRecepcionRepository;

    @Override
    public void saveComprasRecepcion(RecepcionEntity recepcion, List<Long> idsCompras) {
        List<CompraRecepcion> comprasRecepciones = new ArrayList<>();
        idsCompras.forEach((c)->{
            CompraEntity compra = new CompraEntity(c);
            CompraRecepcion compraRecepcion = new CompraRecepcion(recepcion, compra);
            
            comprasRecepciones.add(compraRecepcion);
            
            
        });
        
        compraRecepcionRepository.saveAll(comprasRecepciones);
    }
    
    @Override
    public List<Long> getIdsPedidosComprasRecepcionadosByIdRecepcion(Long idRecepcion) {
        
       return compraRecepcionRepository.getIdsPedidosComprasRecepcionadosByIdRecepcion(idRecepcion);
    }
    
    @Override
    public void deleteComprasRecepcionadosByIdRecepcion(Long idRecepcion) {
        
       compraRecepcionRepository.deleteComprasRecepcionadosByIdRecepcion(idRecepcion);
    }

    @Override
    public List<CompraRecepcion> getCompraRecepcionesByIdCompra(Long id) {
        return compraRecepcionRepository.getCompraRecepcionesByIdCompra(id);
    }
}
