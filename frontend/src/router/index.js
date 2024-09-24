import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {



      path: '/',
      name: 'home',
      component: () => import('@/modules/Pedidos/views/ListaPedidos.vue')
      
      
    },
    {
      path: '/:id',
      name: 'VisualizarPedido',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/views/VerPedido.vue')
    },
    {
      path: '/nuevo_pedido',
      name: 'nuevo_pedido',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/views/NuevoPedido.vue')
    },
    {
      path: '/modificar/:id',
      name: 'ModificarPedido',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/views/ModificarPedido.vue')
    },
    {
      path: '/addPago/:id',
      name: 'AddPago',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/components/RegistrarPago.vue')
    },
    {
      path: '/anticipos',
      name: 'anticipos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ListaAnticipos.vue')
    }
    ,
    {
      path: '/reembolsar/:id',
      name: 'reembolsar',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ReembolsarAnticipo.vue')
    },
    {
      path: '/verAnticipo/:id',
      name: 'verAnticipo',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/VerAnticipo.vue')
    },
    {
      path: '/reembolsos',
      name: 'reembolsos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ListaReembolsos.vue')
    },
    {
      path: '/facturar/:id',
      name: 'facturar',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/components/registrarPagoant.vue')
    },
    {
      path: '/caja',
      name: 'caja',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      //component: () => import('@/modules/Pagos/views/Caja.vue')
    },
    {path: '/cajas',
      name: 'cajas',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ListaCajas.vue')
    },
    {path: '/movimientos/:id',
      name: 'movimientos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/VerMovimientosCaja.vue')
    },
    {
      path: '/ventas',
      name: 'ventas',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/Ventas.vue')
    },
    {
      path: '/nueva_factura',
      name: 'nueva_factura',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/NuevaFactura.vue')
    }
  ]
})

export default router
