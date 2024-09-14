/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.DepartamentoEntity;
import com.krazystore.krazystore.Repository.DepartamentoRepository;
import com.krazystore.krazystore.Service.DepartamentoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DepartamentoServiceImpl implements DepartamentoService{
    private final DepartamentoRepository departamentorepository;
    
    public DepartamentoServiceImpl(DepartamentoRepository departamentorepository) {
        this.departamentorepository = departamentorepository;
    }


    @Override
    public List<DepartamentoEntity> findAll() {
        return departamentorepository.findAll();
    }

    @Override
    public Optional<DepartamentoEntity> findById(Long id) {
        return departamentorepository.findById(id);
    }

    @Override
    public DepartamentoEntity saveDepartamento(DepartamentoEntity departamentoEntity) {
        return departamentorepository.save(departamentoEntity);
    }

    @Override
    public DepartamentoEntity updateDepartamento(DepartamentoEntity departamentoEntity, Long id) {
        DepartamentoEntity updatedDepartamento = departamentorepository.findById(id).get();
        
        updatedDepartamento.setDescripcion(departamentoEntity.getDescripcion());
        return departamentorepository.save(updatedDepartamento);
    }

    @Override
    public void deleteDepartamento(Long id) {
        departamentorepository.deleteById(id);
    }
    
}
