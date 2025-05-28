import { CajaServices } from "@/services/CajaServices";
import { ref, onMounted } from 'vue';

const caja = ref();

  export function formatearFecha(value) {
    return new Date(value).toLocaleDateString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};

export function formatearFechaHora(value) {
    return new Date(value).toLocaleDateString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};

export function formatearNumero(valor) {
  if(typeof(valor) == "number"){
    return valor.toLocaleString("de-DE");
}
};


export async function existeCajaAbierta() {
   let c = (await CajaServices.getCajaAbierta()).data;
   console.log(c);
   if (c == null ) {
    return false;
   } else {
    return true;
   }
   
}

export function getEstadoPedidoVenta(estado) {
  switch (estado) {
       case 'N':
           return 'Nuevo';
        case 'A':
           return 'Con Anticipo';
        case 'P':
           return 'Preparado';
        case 'R':
           return 'Prep. Parcial';
        case 'M':
           return 'Entrega Parcial';
        case 'E':
           return 'Entregado';
        case 'X':
           return 'No Entregado';
           case 'C':
           return 'Cancelado';

       default:
           return null;
   }
};

export function getEstadoFacturaPedidoVenta(estado) {
  switch (estado) {
       case 'F':
           return 'Facturado';
        case 'R':
           return 'Fact. Parcial';
        case 'P':
           return 'Pendiente';

       default:
           return null;
   }
};

export function getEstadoPedidoCompra(estado) {
  switch (estado) {
       case 'N':
           return 'Nuevo';
       case 'R':
           return 'Recepcionado';
        case 'M':
           return 'Recepcion parcial';
        case 'F':
            return 'Facturado';
        case 'R':
            return 'Fact. Parcial';
        case 'P':
            return 'Pendiente';
       default:
           return null;
   }
};

export function getEstadoRecepcion(estado) {
  switch (estado) {
       case 'P':
           return 'Pendiente de Factura';
       case 'F':
           return 'Facturado';

       default:
           return null;
   }
};

export function getEstadoEntrega(estado) {
    switch (estado) {
         case 'P':
             return 'Pendiente de Entrega';
         case 'E':
             return 'Entregado';
        case 'X':
             return 'No Entregado';
        case 'C':
             return 'Cancelado';
  
         default:
             return null;
     }
  };

export function getEstadoFacturaCompra(estado) {
  switch (estado) {
       case 'P':
           return 'Pendiente de pago';
       case 'C':
           return 'Pagado';

       default:
           return null;
   }
};

export function getEstadoFacturaVenta(estado) {
  switch (estado) {
       case 'P':
           return 'Pendiente de pago';
       case 'C':
           return 'Pagado';
       case 'A':
            return 'Anulado';

       default:
           return null;
   }
};

export function getEstadoInventario(estado) {
    switch (estado) {
         case 'A':
             return 'Ajustado';
  
         case 'S':
             return 'En curso';
  
          case 'P':
             return 'Pendiente de ajuste';
  
         default:
             return null;
     }
  };

  export function getEstadoAjuste(estado) {
    switch (estado) {
         case 'A':
             return 'Ajustado';
  
         case 'S':
             return 'En curso';
  
          case 'P':
             return 'Pendiente de ajuste';
  
         default:
             return null;
     }
  };

export function getEstadoCaja(estado) {
  switch (estado) {
       case 'C':
           return 'Cerrado';
       case 'A':
            return 'Activo';

       default:
           return null;
   }
};

