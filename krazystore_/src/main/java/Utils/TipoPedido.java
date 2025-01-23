/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public enum TipoPedido {
    PEDIDOCOMPRA('C'),
    PEDIDOVENTA('V');
    
    private final char codigo;

    private TipoPedido(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }
    
    
}
