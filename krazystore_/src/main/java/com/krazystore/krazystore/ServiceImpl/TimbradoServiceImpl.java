/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.TimbradoEntity;
import com.krazystore.krazystore.Repository.TimbradoRepository;
import com.krazystore.krazystore.Service.TimbradoService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class TimbradoServiceImpl implements TimbradoService {
    private final TimbradoRepository timbradoRepository;

    public TimbradoServiceImpl(TimbradoRepository timbradoRepository) {
        this.timbradoRepository = timbradoRepository;
    }
    
    @Override
    public String getNroFactura(TimbradoEntity timbrado) {
        String nroFactura;
        int ce = timbrado.getCodEstablecimiento();
        int pe = timbrado.getPuntoExpedicion();
        int ultimoRemitido = timbrado.getUltimoRemitido() + 1;
        if(Integer.toString(ce).length() == 1){
            nroFactura= "00"+ce+"-";
        }else if(Integer.toString(ce).length() == 2){
            nroFactura= "0"+ce+"-";
        }else{
            nroFactura= ""+ce+"-";
        }
        
        if(Integer.toString(pe).length() == 1){
            nroFactura= nroFactura + "00"+pe+"-";
        }else if(Integer.toString(pe).length() == 2){
            nroFactura= nroFactura +"0"+pe+"-";
        }else{
            nroFactura= nroFactura +""+pe+"-";
        }
        
        if(Integer.toString(ultimoRemitido).length() == 1){
            nroFactura= nroFactura + "000000"+ultimoRemitido;
        }else if(Integer.toString(ultimoRemitido).length() == 2){
            nroFactura= nroFactura +"00000"+ultimoRemitido;
            }else if(Integer.toString(ultimoRemitido).length() == 3){
            nroFactura= nroFactura +"0000"+ultimoRemitido;
            }else if(Integer.toString(ultimoRemitido).length() == 4){
            nroFactura= nroFactura +"000"+ultimoRemitido;
            }else if(Integer.toString(ultimoRemitido).length() == 5){
            nroFactura= nroFactura +"00"+ultimoRemitido;
            }else if(Integer.toString(ultimoRemitido).length() == 6){
            nroFactura= nroFactura +"0"+ultimoRemitido;
        }else{
            nroFactura= nroFactura +""+ultimoRemitido;
        }
        return nroFactura;
    }
    @Override
    public Optional<TimbradoEntity> getTimbradoVigente() {
        
        Optional<TimbradoEntity> timbrado = timbradoRepository.findVigente();
        return timbrado;
    }
    @Override
    public TimbradoEntity updateTimbrado(TimbradoEntity timbrado) {
        System.out.println(timbrado.getUltimoRemitido());
        return timbradoRepository.save(timbrado);
    }
}
