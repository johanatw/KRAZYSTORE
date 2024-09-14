/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Repository.FormaPagoRepository;
import com.krazystore.krazystore.Service.FormaPagoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class FormaPagoServiceImpl implements FormaPagoService {
    private final FormaPagoRepository formapagorepository;

    public FormaPagoServiceImpl(FormaPagoRepository formapagorepository) {
        this.formapagorepository = formapagorepository;
    }

    @Override
    public List<FormaPagoEntity> findAll() {
        return formapagorepository.findAll();
    }
    
    @Override
    public List<FormaPagoEntity> findAllSinAnticipo() {
        return formapagorepository.findAllSinAnticipo();
    }

    @Override
    public Optional<FormaPagoEntity> findById(Long id) {
        return formapagorepository.findById(id);
    }

    @Override
    public FormaPagoEntity saveFormaPago(FormaPagoEntity formaPagoEntity) {
        return formapagorepository.save(formaPagoEntity);
    }

    @Override
    public FormaPagoEntity updateFormaPago(FormaPagoEntity formaPagoEntity, Long id) {
        FormaPagoEntity updatedFormaPago = formapagorepository.findById(id).get();
        
        updatedFormaPago.setDescripcion(formaPagoEntity.getDescripcion());
    
        return formapagorepository.save(updatedFormaPago);
    }

    @Override
    public void deleteFormaPago(Long id) {
        formapagorepository.deleteById(id);
    }
    
}
