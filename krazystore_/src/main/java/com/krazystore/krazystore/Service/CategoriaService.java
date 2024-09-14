/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;


import com.krazystore.krazystore.Entity.CategoriaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CategoriaService {
    List<CategoriaEntity> findAll();
    Optional<CategoriaEntity> findById(Long id);
    CategoriaEntity saveCategoria(CategoriaEntity categoriaEntity);
    CategoriaEntity updateCategoria(CategoriaEntity categoriaEntity, Long id);
    void deleteCategoria(Long id);
}
