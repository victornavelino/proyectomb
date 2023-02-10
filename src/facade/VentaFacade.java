/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.VentaJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.TipoIva;
import entidades.caja.Caja;
import entidades.localidad.Localidad;
import entidades.venta.CierreVentas;
import entidades.venta.Venta;
import includes.Comunes;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author hugo
 */
public class VentaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static VentaFacade instance = null;

    protected VentaFacade() {
    }

    public static VentaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new VentaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Venta venta) {
        new VentaJpaController(emf).create(venta);
    }

    public Venta buscar(Long id) {
        return new VentaJpaController(emf).findVenta(id);
    }

    public void modificar(Venta venta) {
        try {
            new VentaJpaController(emf).edit(venta);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new VentaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Venta> getTodos() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v ORDER BY v.fecha DESC");
        return q.getResultList();
    }

    public List<Object> listaVentasDescendente() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v.numeroTicket FROM Venta v ORDER BY v.numeroTicket DESC");
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Venta> listaVentasNoAnuladas() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v where v.anulado=FALSE ORDER BY v.numeroTicket DESC");
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Venta> listaVentasOrdXNroTicket() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v ORDER BY v.numeroTicket DESC");
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Venta> listaVentasOrdXTicketXFechas(Date desde, Date hasta) {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v where v.fecha BETWEEN :desde AND :hasta ORDER BY v.numeroTicket DESC");
        q.setParameter("desde", desde);
        q.setParameter("hasta", hasta);
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Venta> listaVentasXSucursalXCategoriaXFechas(Sucursal sucursal, List<Categoria> categorias, Date desde, Date hasta) {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT DISTINCT(v) FROM Venta v, IN (v.ventasArticulos) va, Articulo art where v.sucursal=:sucursal AND art IN (select a from Articulo a where a.codigoBarra=va.articuloCodigo) AND art.subCategoria.categoria IN :categorias AND v.fecha BETWEEN :desde AND :hasta AND v.anulado=FALSE ORDER BY v.numeroTicket DESC");
        q.setParameter("sucursal", sucursal);
        q.setParameter("categorias", categorias);
        q.setParameter("desde", desde);
        q.setParameter("hasta", hasta);
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Object[]> listaVentasXSucursalXCategoriaXFechas2(Sucursal sucursal, List<Categoria> categorias, Date desde, Date hasta) {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query q = em.createQuery("SELECT v.id, v.sucursal, v.fecha, v.numeroTicket, v.cliente,(va.cantidadPeso * va.precioUnitario),(va.cantidadPeso * va.precioPromocion),((va.cantidadPeso * va.precioUnitario)-(va.cantidadPeso * va.precioPromocion)),v.anulado,v.usuario,va.articuloDescripcion, va.cantidadPeso, va.precioPromocion, va.precioUnitario FROM Venta v, IN (v.ventasArticulos) va, Articulo art where v.sucursal=:sucursal AND art IN (select a from Articulo a where a.codigoBarra=va.articuloCodigo) AND art.subCategoria.categoria IN :categorias AND v.fecha BETWEEN :desde AND :hasta AND v.anulado=FALSE ORDER BY v.numeroTicket DESC");
        q.setParameter("sucursal", sucursal);
        q.setParameter("categorias", categorias);
        q.setParameter("desde", desde);
        q.setParameter("hasta", hasta);
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();

    }

    public List<Venta> listaVentasSinCerrar(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.cierreVentas IS NULL AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        return q.getResultList();
    }

    public List<Venta> listaVentasSinCobrar(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.anulado=FALSE AND v.sucursal=:sucursal AND v.cobroVenta IS NULL ORDER BY v.id DESC");
        q.setParameter("sucursal", sucursal);
        return q.getResultList();

    }

    public List<Venta> listaVentasCerradasCobradas(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.sucursal=:sucursal AND v.cobroVenta IS NOT NULL AND v.cierreVentas IS NOT NULL");
        q.setParameter("sucursal", sucursal);
        return q.getResultList();

    }

    public List<Venta> listaVentasCobradas(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.sucursal=:sucursal AND v.cobroVenta IS NOT NULL");
        q.setParameter("sucursal", sucursal);
        return q.getResultList();

    }

    public List<Object[]> listadoArticulosVendidos(Venta venta, TipoIva iva) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query q = em.createQuery("SELECT  v.articuloDescripcion, v.cantidadPeso, v.precio,v.precioUnitario, v.precioPromocion , art.subCategoria.categoria.tipoIva.descripcion, "
                + "                (v.cantidadPeso * v.precioUnitario) FROM Venta vt , Articulo art, "
                + "                IN (vt.ventasArticulos) v  where art IN (select a from Articulo a where a.codigoBarra=v.articuloCodigo) "
                + "                AND vt=:venta AND art.subCategoria.categoria.tipoIva=:iva "
                + "                ORDER BY art.subCategoria.categoria.tipoIva");
        q.setParameter("venta", venta);
        q.setParameter("iva", iva);
        return q.getResultList();

    }

    public BigDecimal importeVentasSinCerrar(Sucursal sucursal) {
        /*VER SI AQUI NO DEBERIA IR COBROVTA <> NULL*/
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.monto) FROM Venta v WHERE v.cierreVentas IS NULL AND v.anulado=FALSE AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        try {
            return new BigDecimal(q.getSingleResult().toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public int primeraVentasSinCerrar(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT MIN(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        try {
            return (int) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public int cantidadVentasSinCerrar(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        q.setMaxResults(1);
        try {

            return (int) (long) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public int cantidadVentasSinCobrarCajaActual(Sucursal sucursal, Caja caja) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.cobroVenta IS NULL AND v.sucursal=:sucursal AND v.anulado=FALSE AND v.fecha>=:fechaInicioCaja");
        q.setParameter("sucursal", sucursal);
        q.setParameter("fechaInicioCaja", caja.getFechaInicio());
        q.setMaxResults(1);
        try {

            return (int) (long) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public int cantidadVentasSinCobrar(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.cobroVenta IS NULL AND v.sucursal=:sucursal AND v.anulado=FALSE");
        q.setParameter("sucursal", sucursal);
        q.setMaxResults(1);
        try {

            return (int) (long) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public int cantidadVentasAnuladas(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas=:cierreVentas AND v.anulado=TRUE");
        q.setParameter("cierreVentas", cierreVentas);
        try {
            System.out.println("cant anuladoss: " + (int) (long) q.getSingleResult());
            return (int) (long) q.getSingleResult();
        } catch (Exception e) {

            return 0;
        }
    }

    public int cantidadVentasNoAnuladas(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.anulado=FALSE AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        q.setMaxResults(1);
        try {

            return (int) (long) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public String cantidadVentasNoAnuladas(CierreVentas cierreVenta) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v.numeroTicket) FROM Venta v WHERE v.cierreVentas=:cierreVenta AND v.anulado=FALSE ");
        q.setParameter("cierreVenta", cierreVenta);
//        try {
        System.out.println("cantidad de tickets:" + q.getSingleResult().toString());
        return q.getSingleResult().toString();
//        } catch (Exception e) {
//            return 0;
//        }
    }

    public String promedioMontoVenta(CierreVentas cierreVenta) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.monto)/COUNT(v.numeroTicket) promedio FROM Venta v WHERE v.cierreVentas=:cierreVenta AND v.anulado=FALSE ");
        q.setParameter("cierreVenta", cierreVenta);
//        try {
        System.out.println("promedio:" + q.getSingleResult().toString());
        return q.getSingleResult().toString();
//        } catch (Exception e) {
//            return 0;
//        }
    }

    public int ultimaVentasSinCerrar(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT MAX(v.numeroTicket) FROM Venta v WHERE v.cierreVentas IS NULL AND v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        try {
            return (int) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }

    }

    public Venta getVentaNumeroTicket(String numero) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM Venta v WHERE v.numeroTicket='" + numero + "'");
        try {
            return (Venta) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean getVentaNumeroTicketSucursal(String numero, String codSucursal) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM Venta v WHERE v.numeroTicket='" + numero + "' AND v.sucursal.codigo='" + codSucursal + "'");
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            return false;
        }

    }

    public int getUltimoNumeroTicket() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT MAX(v.numeroTicket) FROM Venta v");
        quBuscar.setMaxResults(1);
        ema.getEntityManagerFactory().getCache().evictAll();
        try {
            return (int) quBuscar.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Venta> getVentasEntrega(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.id IN (SELECT o.venta.id FROM Entrega o) AND v.cierreVentas=:cierreVentas AND v.anulado=FALSE ORDER BY v.cliente DESC");
        q.setParameter("cierreVentas", cierreVentas);
        return q.getResultList();

    }

    public List<Object[]> getResumenVentasEntrega(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.descuento) , SUM (v.monto) , v.cliente FROM Venta v WHERE v.id IN (SELECT o.venta.id FROM Entrega o)"
                + " AND v.cierreVentas=:cierreVentas  AND v.anulado=FALSE group BY v.cliente ");
        q.setParameter("cierreVentas", cierreVentas);
        List<Object[]> results = q.getResultList();

        return results;

    }

    public List<Object[]> getResumenVentasOrganismos(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v.cliente, SUM(va.precio)Promocion, sum(va.cantidadPeso*va.precioUnitario)comun,sum(v.descuento)descuentos,(SUM(va.precio)-sum(v.descuento))total FROM Venta v, IN(v.ventasArticulos) va WHERE v.esPersona=FALSE  AND v.cierreVentas=:cierreVentas  AND v.anulado=FALSE group BY v.cliente");
        q.setParameter("cierreVentas", cierreVentas);
        List<Object[]> results = q.getResultList();

        return results;

    }

    public List<Object[]> getResumenVentasPorCategoria(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT art.subCategoria, SUM(va.precio)Promocion, sum(va.cantidadPeso*va.precioUnitario)comun,sum(v.descuento)descuentos,(SUM(va.precio)-sum(v.descuento))total FROM Venta v, IN(v.ventasArticulos) va, Articulo art WHERE art IN (select a from Articulo a where a.codigoBarra=va.articuloCodigo) AND v.cierreVentas=:cierreVentas  AND v.anulado=FALSE group BY art.subCategoria");
        q.setParameter("cierreVentas", cierreVentas);
        List<Object[]> results = q.getResultList();

        return results;

    }

    public List<Object[]> getVentasArticulos(Venta venta) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery(" SELECT  art.descripcionReducida, v.cantidadPeso, v.precio,v.precio,v.precioUnitario, v.precioPromocion , art.subCategoria.categoria.tipoIva.descripcion FROM Venta vt ,IN (vt.ventasArticulos) v, Articulo art WHERE art IN (select a from Articulo a where a.codigoBarra=v.articuloCodigo) AND vt=:venta ORDER BY art.subCategoria.categoria.tipoIva");
        q.setParameter("venta", venta);
        List<Object[]> results = q.getResultList();
        return results;

    }

    public List<Venta> getVentasNoEntrega(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.cierreVentas=:cierreVentas AND v.anulado=FALSE ORDER BY v.cliente DESC");
        q.setParameter("cierreVentas", cierreVentas);
        return q.getResultList();

    }

    public BigDecimal getTotalVentasAnuladas(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.monto) FROM Venta v WHERE  v.cierreVentas=:cierreVentas AND v.anulado=TRUE");
        q.setParameter("cierreVentas", cierreVentas);
        return (BigDecimal) q.getSingleResult();

    }

    public BigDecimal getTotalVentasEventuales(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.monto) FROM Venta v WHERE  v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.cierreVentas=:cierreVentas AND v.anulado=FALSE");
        q.setParameter("cierreVentas", cierreVentas);
        return (BigDecimal) q.getSingleResult();

    }

    public String getCantVentaOtrosArticulos(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v) FROM Venta v, IN (v.ventasArticulos) va, Articulo art WHERE art IN (select a from Articulo a where a.codigoBarra=va.articuloCodigo) AND art.subCategoria.categoria.descripcion='OTROS' AND v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.cierreVentas=:cierreVentas  AND v.anulado=FALSE ");
        q.setParameter("cierreVentas", cierreVentas);
        return q.getSingleResult().toString();

    }

    public String getCantVentaEventualesNoAnuladosNoEntregas(CierreVentas cierreVentas) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT COUNT(v) FROM Venta v WHERE  v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.cierreVentas=:cierreVentas  AND v.anulado=FALSE ");
        q.setParameter("cierreVentas", cierreVentas);
        return q.getSingleResult().toString();

    }

    public BigDecimal getTotalVentasConPromo(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.precioPromocion) FROM Venta vt, IN (vt.ventasArticulos) v "
                + "WHERE vt.sucursal.id=:idSucursal AND vt.anulado=FALSE AND "
                + "vt.fecha Between :fechaIni AND :fechaFin");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    public BigDecimal getTotalVentasSinPromo(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.precio) FROM Venta vt, IN (vt.ventasArticulos) v "
                + " WHERE vt.sucursal.id=:idSucursal AND vt.anulado=FALSE AND "
                + "vt.fecha Between :fechaIni AND :fechaFin");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    /**
     * VENTAS NO SON ENTREGA --> AL CLIENTE COMUN (EVENTUALES) *
     */
    public BigDecimal getTotalVentasNoEntrega(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(vt.precio) total FROM Venta v, IN (v.ventasArticulos) vt "
                + "WHERE v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.anulado=FALSE "
                + "AND v.sucursal.id=:idSucursal AND v.fecha Between :fechaIni AND :fechaFin");//(SUM(v.monto)-SUM(v.descuento))
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    public BigDecimal getTotalDescuentoVentasNoEntrega(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.descuento) total FROM Venta v WHERE v.id NOT IN (SELECT o.venta.id FROM Entrega o) AND v.anulado=FALSE AND v.sucursal.id=:idSucursal AND v.fecha Between :fechaIni AND :fechaFin");//(SUM(v.monto)-SUM(v.descuento))
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    public BigDecimal getTotalVentasNoEntrega_SinPromo(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.precioUnitario * v.cantidadPeso) FROM Venta vt, IN (vt.ventasArticulos) v "
                + " WHERE vt.id NOT IN (SELECT o.venta.id FROM Entrega o) AND vt.sucursal.id=:idSucursal "
                + " AND vt.anulado=FALSE AND "
                + "vt.fecha Between :fechaIni AND :fechaFin");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    /*    public BigDecimal getTotalVentasNoEntrega_ConPromo(Date fechaIni, Date fechaFin, Sucursal sucursal) {
     EntityManager em = emf.createEntityManager();
     Query q = em.createQuery("SELECT SUM(v.precioPromocion) FROM Venta vt, IN (vt.ventasArticulos) v " +
     "WHERE vt.id NOT IN (SELECT o.venta.id FROM Entrega o) AND vt.sucursal.id=:idSucursal " +
     "AND vt.anulado=FALSE AND "
     + "vt.fecha Between :fechaIni AND :fechaFin");
     q.setParameter("fechaIni", fechaIni);
     q.setParameter("fechaFin", fechaFin);
     q.setParameter("idSucursal", sucursal.getId());
     return (BigDecimal) q.getSingleResult();

     }*/
    /**
     * VENTAS SON ENTREGA --> AL ESTADO (POR PEDIDOS) *
     */
    public BigDecimal getTotalVentasEntrega(Date fechaIni, Date fechaFin, Sucursal sucursal) {

        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.monto) total FROM Venta v "
                + "WHERE v.id IN (SELECT o.venta.id FROM Entrega o) AND v.anulado=FALSE "
                + "AND v.sucursal.id=:idSucursal AND v.fecha Between :fechaIni AND :fechaFin"); //-SUM(v.descuento)
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();

    }

    public BigDecimal getTotalVentasEntrega_SinPromo(Date fechaIni, Date fechaFin, Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.precioUnitario * v.cantidadPeso) FROM Venta vt, IN (vt.ventasArticulos) v "
                + " WHERE vt.id IN (SELECT o.venta.id FROM Entrega o) AND vt.sucursal.id=:idSucursal "
                + " AND vt.anulado=FALSE AND "
                + "vt.fecha Between :fechaIni AND :fechaFin");
        q.setParameter("fechaIni", fechaIni);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idSucursal", sucursal.getId());
        return (BigDecimal) q.getSingleResult();
    }

    public List<Venta> getVentasSucursal(Sucursal sucursal) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v.sucursal=:sucursal");
        q.setParameter("sucursal", sucursal);
        em.getEntityManagerFactory().getCache().evictAll();
        return q.getResultList();
    }

    public boolean esVentaEntrega(Venta venta) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Venta v WHERE v=:venta AND v.id IN (SELECT o.venta.id FROM Entrega o)");
        q.setParameter("venta", venta);
        em.getEntityManagerFactory().getCache().evictAll();
        return !q.getResultList().isEmpty();
    }

    public Venta getTicketCobradoNoCerradoCaja(String numero, Sucursal sucursal) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v FROM Venta v WHERE v.numeroTicket=:numero AND v.sucursal=:sucursal AND v.cobroVenta <>null AND NOT v.cobroVenta.cerrado");
        quBuscar.setParameter("numero", Integer.parseInt(numero.trim()));
        quBuscar.setParameter("sucursal", sucursal);
        try {
            return (Venta) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
