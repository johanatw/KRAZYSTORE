/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Repository.EstadoRepository;
import com.krazystore.krazystore.Service.EstadoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class EstadoServiceImpl implements EstadoService {
    private final EstadoRepository estadorepository;

    public EstadoServiceImpl(EstadoRepository estadorepository) {
        this.estadorepository = estadorepository;
    }

    @Override
    public List<EstadoEntity> findAll() {
        return estadorepository.findAll();
    }
    
    @Override
    public List<EstadoEntity> findByTipo(String tipo) {
        return estadorepository.findByTipoEstado(tipo);
    }

    @Override
    public Optional<EstadoEntity> findById(Long id) {
        return estadorepository.findById(id);
    }

    @Override
    public EstadoEntity saveEstado(EstadoEntity estadoEntity) {
        return estadorepository.save(estadoEntity);
    }

    @Override
    public EstadoEntity updateEstado(EstadoEntity estadoEntity, Long id) {
        EstadoEntity updatedEstado = estadorepository.findById(id).get();
        
        updatedEstado.setDescripcion(estadoEntity.getDescripcion());
        updatedEstado.setTipoEstado(estadoEntity.getTipoEstado());
        return estadorepository.save(updatedEstado);
    }

    @Override
    public void deleteEstado(Long id) {
        estadorepository.deleteById(id);
    }
    
}
