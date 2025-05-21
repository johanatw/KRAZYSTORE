/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Repository.FormaPagoRepository;
import com.krazystore.krazystore.Service.FormaPagoService;
import java.util.ArrayList;
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
        
        //updatedFormaPago.setDescripcion(formaPagoEntity.getDescripcion());
    
        return formapagorepository.save(updatedFormaPago);
    }

    @Override
    public void deleteFormaPago(Long id) {
        formapagorepository.deleteById(id);
    }

    @Override
    public List<FormaPagoEntity> saveFormasPagos(List<FormaPagoEntity> pagos) {
        return formapagorepository.saveAll(pagos); 
    }

    @Override
    public void deleteFormasPagosByMovimientos(List<Long> ids) {
        formapagorepository.deletePagosByMovimientos(ids);
    }

    @Override
    public void deleteFormasPagosByMovimiento(Long id) {
        formapagorepository.deletePagosByMovimiento(id);
    }

    
}
