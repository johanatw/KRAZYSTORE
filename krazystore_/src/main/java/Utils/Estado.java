/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public enum Estado {
    NUEVO('N'),
    PENDIENTEDEPAGO('P'),
    PAGADO('C'),
    CONANTICIPO('A'),
    ANULADO('A'),
    FACTURADO('F'),
    PENDIENTE_DE_FACTURA('P'),
    RECEPCIONADO('R'),
    PARCIALMENTE_RECEPCIONADO('M'),
    PARCIALMENTE_FACTURADO('R'),
    PENDIENTE('P'),
    FINALIZADO('F'),
    PENDIENTEAJUSTE('P'),
    AJUSTADO('A'),
    PAGOCOMPLETO('C'),
    CANCELADO('C'),
    
    CAJACERRADA('C'),
    CAJAABIERTA('A'),
    ENCURSO('S'),
    PARCIALMENTE_ENTREGADO('M'),
    ENTREGADO('E'),
    NO_ENTREGADO('X'),
    PARCIALMENTE_PREPARADO('R'),
    PREPARADO('P');
    
    private final char codigo;

    private Estado(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    
       
    
}
