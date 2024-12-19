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
public class ProductosPedidoRecepcionadosEvent extends ApplicationEvent{
    private Long pedidocompraId;

    public ProductosPedidoRecepcionadosEvent(Long pedidocompraId, Object source) {
        super(source);
        this.pedidocompraId = pedidocompraId;
    }

    public Long getPedidocompraId() {
        return pedidocompraId;
    }

    public void setPedidocompraId(Long pedidocompraId) {
        this.pedidocompraId = pedidocompraId;
    }
    
    
}
