/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Repository.CategoriaRepository;
import com.krazystore.krazystore.Service.CategoriaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriarepository;

    public CategoriaServiceImpl(CategoriaRepository categoriarepository) {
        this.categoriarepository = categoriarepository;
    }
    
    

    @Override
    public List<CategoriaEntity> findAll() {
        return categoriarepository.findAll();
    }

    @Override
    public Optional<CategoriaEntity> findById(Long id) {
        return categoriarepository.findById(id);
    }

    @Override
    public CategoriaEntity saveCategoria(CategoriaEntity categoriaEntity) {
        return categoriarepository.save(categoriaEntity);
    }

    @Override
    public CategoriaEntity updateCategoria(CategoriaEntity categoriaEntity, Long id) {
        CategoriaEntity updatedCategoria = categoriarepository.findById(id).get();
        
        updatedCategoria.setDescripcion(categoriaEntity.getDescripcion());
        return categoriarepository.save(updatedCategoria);
    }

    @Override
    public void deleteCategoria(Long id) {
        CategoriaEntity deletedCategoria = categoriarepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no existe"));
        
        deletedCategoria.setActivo(Boolean.FALSE);
        categoriarepository.save(deletedCategoria);
    }
    
    
}
