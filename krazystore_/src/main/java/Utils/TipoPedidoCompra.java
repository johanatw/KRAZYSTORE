/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public enum TipoPedidoCompra {
    PEDIDO_NACIONAL('N'),
    PEDIDO_INTERNACIONAL('I');
    
    private final char codigo;

    private TipoPedidoCompra(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }
    
    
}
