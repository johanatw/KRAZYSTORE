/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.TipoDocEntity;
import com.krazystore.krazystore.Repository.TipoDocRepository;
import com.krazystore.krazystore.Service.TipoDocService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class TipoDocServiceImpl implements TipoDocService {
    private final TipoDocRepository tipodocrepository;

    public TipoDocServiceImpl(TipoDocRepository tipodocrepository) {
        this.tipodocrepository = tipodocrepository;
    }
    
    

    @Override
    public List<TipoDocEntity> findAll() {
        return tipodocrepository.findAll();
    }

    @Override
    public Optional<TipoDocEntity> findById(Long id) {
        return tipodocrepository.findById(id);
    }

    @Override
    public TipoDocEntity saveTipoDoc(TipoDocEntity tipoDocEntity) {
        return tipodocrepository.save(tipoDocEntity);
    }

    @Override
    public TipoDocEntity updateTipoDoc(TipoDocEntity tipoDocEntity, Long id) {
        TipoDocEntity updatedTipoDoc = tipodocrepository.findById(id).get();
        
        updatedTipoDoc.setDescripcion(tipoDocEntity.getDescripcion());
        return tipodocrepository.save(updatedTipoDoc);

    }

    @Override
    public void deleteTipoDoc(Long id) {
        tipodocrepository.deleteById(id);
    }
    
}
