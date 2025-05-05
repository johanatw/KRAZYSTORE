/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Repository.FormaCobroRepository;
import com.krazystore.krazystore.Service.FormaCobroService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class FormaCobroServiceImpl implements FormaCobroService {

    public FormaCobroServiceImpl(com.krazystore.krazystore.Repository.FormaCobroRepository formaCobroRepository) {
        this.formaCobroRepository = formaCobroRepository;
    }
    private final FormaCobroRepository formaCobroRepository;

    @Override
    public List<FormaCobroEntity> saveFormasCobros(List<FormaCobroEntity> cobros) {
        return formaCobroRepository.saveAll(cobros);
    }

    @Override
    public void deleteFormasCobrosByMovimientos(List<Long> ids) {
        formaCobroRepository.deleteCobrosByMovimientos(ids);
    }

    @Override
    public void deleteFormasCobrosByMovimiento(Long id) {
        formaCobroRepository.deleteCobrosByMovimiento(id);
    }
    
    
}
