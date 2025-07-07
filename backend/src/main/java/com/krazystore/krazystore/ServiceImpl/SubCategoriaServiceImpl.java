/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.SubCategoriaEntity;
import com.krazystore.krazystore.Repository.SubCategoriaRepository;
import com.krazystore.krazystore.Service.SubCategoriaService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class SubCategoriaServiceImpl implements SubCategoriaService {
    private final SubCategoriaRepository subCategoriaRepository;

    public SubCategoriaServiceImpl(SubCategoriaRepository subCategoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
    }

    @Override
    public List<SubCategoriaEntity> findAll() {
        return subCategoriaRepository.findAll();
    }

    @Override
    public List<SubCategoriaEntity> findByIdCategoria(Long id) {
        return subCategoriaRepository.findByIdCategoria(id);
    }

    @Override
    public List<SubCategoriaEntity> findByIdsCategoria(List<Long> ids) {
        return subCategoriaRepository.findByIdsCategoria(ids);
    }
    
}
