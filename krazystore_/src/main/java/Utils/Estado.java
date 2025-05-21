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
    PENDIENTEDEFACTURA('P'),
    RECEPCINADO('R'),
    PARCIALMENTERECEPCINADO('M'),
    PARCIALMENTEFACTURADO('P'),
    PENDIENTE('P'),
    FINALIZADO('F'),
    PENDIENTEAJUSTE('P'),
    AJUSTADO('A'),
    PAGOCOMPLETO('C'),
    
    CAJACERRADA('C'),
    CAJAABIERTA('A'),
    ENCURSO('S'),
    
    ENTREGADO('E'),
    NOENTREGADO('N'),
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
