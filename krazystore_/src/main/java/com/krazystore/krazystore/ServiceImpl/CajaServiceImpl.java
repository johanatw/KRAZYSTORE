/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Repository.CajaRepository;
import com.krazystore.krazystore.Service.CajaService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */

@Service
public class CajaServiceImpl implements CajaService{
    private final CajaRepository cajaRepository;

    public CajaServiceImpl(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }
    
    @Override
    public Optional<CajaEntity> getCajaAbierta() {
        System.out.println("getCajaAbierta");
        return cajaRepository.getCajaAbierta();
    }
    
    @Override
    public CajaEntity abrirCaja() {
        //Si existe caja pendiente cambia estado a Abierto
        Optional<CajaEntity> cajaPendiente = getCajaPendiente();
        if(cajaPendiente.isPresent()){
            cajaPendiente.get().setFecha(new Date());
            cajaPendiente.get().setEstado('A');
            return cajaRepository.save(cajaPendiente.get());
        }
     
        //Si no, crea caja nueva
        CajaEntity nuevaCaja = new CajaEntity();
        nuevaCaja.setFecha(new Date());
        nuevaCaja.setEstado('A');
        return cajaRepository.save(nuevaCaja);
    }
    
    @Override
    public void cerrarCaja(Long id) {
        CajaEntity caja = cajaRepository.findById(id).get();
        caja.setFechaCierre(new Date());
        caja.setEstado('C');
        cajaRepository.save(caja);
    }
    
    public CajaEntity crearCajaPreviaApertura() {
        System.out.println("crearcajaPrevia");
        CajaEntity nuevaCaja = new CajaEntity();
        nuevaCaja.setEstado('P');
        return cajaRepository.save(nuevaCaja);
    }
    
    public Optional<CajaEntity> getCajaPendiente() {
        return cajaRepository.getCajaPendiente();
    }
    
    @Override
    public CajaEntity getCaja() {
        System.out.println("getCaja");
        Optional<CajaEntity> caja = getCajaAbierta();
        System.out.println(caja);
        if(caja.isEmpty()){
            return crearCajaPreviaApertura();
        }
        return caja.get();
    }
    
    
    @Override
    public Optional<CajaEntity> findById(Long id) {
        return cajaRepository.findById(id);
    }
    
    @Override
    public List<CajaEntity> findAll() {
        return cajaRepository.findAllByOrderByIdDesc();
    }
}

