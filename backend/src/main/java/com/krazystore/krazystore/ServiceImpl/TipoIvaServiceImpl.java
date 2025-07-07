/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.IvaEntity;
import com.krazystore.krazystore.Repository.TipoIvaRepository;
import com.krazystore.krazystore.Service.TipoIvaService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class TipoIvaServiceImpl implements TipoIvaService{

    public TipoIvaServiceImpl(TipoIvaRepository tipoIvaRepository) {
        this.tipoIvaRepository = tipoIvaRepository;
    }
    private final TipoIvaRepository tipoIvaRepository;

    @Override
    public List<IvaEntity> findAll() {
        return tipoIvaRepository.findAll();
    }
}

