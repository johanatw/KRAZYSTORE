import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {



      path: '/',
      name: 'home',
      component: () => import('@/modules/Auth/Login.vue')
      
      
    },
    {
      path: '/pedidos',
      name: 'pedidos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
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
      path: '/anticipos',
      name: 'anticipos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ListaAnticipos.vue')
    }
    ,
    {
      path: '/reembolsos',
      name: 'reembolsos',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/ListaReembolsos.vue')
    },
    {
      path: '/nueva_factura/:id',
      name: 'facturar',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pedidos/views/NuevaFactura.vue')
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
    },
    {
      path: '/consulta_factura/:id',
      name: 'verFactura',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Pagos/views/VerFactura.vue')
    },
    {
      path: '/compras',
      name: 'compras',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ListaCompras.vue')
    },
    {
      path: '/recepciones',
      name: 'recepciones',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ListaRecepciones.vue')
    },
    {
      path: '/modificar_recepcion/:id',
      name: 'modificar_recepcion',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ModificarRecepcion.vue')
    },
    {
      path: '/nueva_compra/:id',
      name: 'nueva_factura_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/NuevaFactura.vue')
    },
    {
      path: '/ver_recepcion/:id',
      name: 'ver_recepcion',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/VerRecepcion.vue')
    },
    {
      path: '/pedidos_compras',
      name: 'pedidos_compras',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ListaPedidos.vue')
    },
    {
      path: '/nuevo_pedido_compra',
      name: 'nuevo_pedido_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/NuevoPedido.vue')
    },
    
    {
      path: '/pedido_compra/:id',
      name: 'ver_pedido_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/VerPedido.vue')
    },
    {
      path: '/modificar_pedido/:id',
      name: 'modificar_pedido_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ModificarPedido.vue')
    },
    {
      path: '/modificar_compra/:id',
      name: 'modificar_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/ModificarCompra.vue')
    },
    {
      path: '/nueva_compra',
      name: 'nueva_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/NuevaCompra.vue')
    },
    {
      path: '/compra/:id',
      name: 'ver_compra',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/VerCompra.vue')
    },
    {
      path: '/recepcionar/:id',
      name: 'recepcionarPedido',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Compras/views/RecepcionarPedido.vue')
    },
    {
      path: '/existencias',
      name: 'existencias',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/Existencias.vue')
    },
    {
      path: '/inventario',
      name: 'inventario',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/ListaInventarios.vue')
    },
    {
      path: '/nuevo_inventario',
      name: 'nuevo_inventario',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/Inventario.vue')
    },
    {
      path: '/inventario/:id',
      name: 'ver_inventario',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/VerInventario.vue')
    },
    {
      path: '/modificar_inventario/:id',
      name: 'modificar_inventario',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/ModificarInventario.vue')
    },
    {
      path: '/nuevo_ajuste',
      name: 'nuevo_ajuste',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/NuevoAjuste.vue')
    },
    {
      path: '/ajustes',
      name: 'ajustes',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/ListaAjustes.vue')
    },
    {
      path: '/ajuste/:id',
      name: 'ver_ajuste',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/VerAjuste.vue')
    },
    {
      path: '/modificar_ajuste/:id',
      name: 'modificar_ajuste',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/modules/Inventario/views/ModificarAjuste.vue')
    },
  ]
})

export default router
