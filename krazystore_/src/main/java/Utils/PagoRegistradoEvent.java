/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
import org.springframework.context.ApplicationEvent;
public class PagoRegistradoEvent extends ApplicationEvent{
    private Long facturaId;
    private char estado;
    
    public PagoRegistradoEvent(Object source, Long facturaId, char estado) {
        super(source);
        this.facturaId = facturaId;
        this.estado = estado;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public char getEstado() {
        return estado;
    }
    
    
    
}
