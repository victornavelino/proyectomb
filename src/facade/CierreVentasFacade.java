/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.CierreVentasJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.venta.CierreVentas;
import entidades.venta.Venta;
import includes.Comunes;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author carlos
 */
public class CierreVentasFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    private static CierreVentasFacade instance = null;

    protected CierreVentasFacade() {
    }

    public static CierreVentasFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CierreVentasFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(CierreVentas cierreVentas) {
        new CierreVentasJpaController(emf).create(cierreVentas);
    }

    public CierreVentas buscar(long id) {
        return new CierreVentasJpaController(emf).findCierreVentas(id);

    }

    public CierreVentas realizarCierre(Sucursal sucursal) {
        List<Venta> listaVentasSinCerrar = VentaFacade.getInstance().listaVentasSinCerrar(sucursal);
        CierreVentas cierreVentas = new CierreVentas();
        cierreVentas.setCantidad(listaVentasSinCerrar.size());
        cierreVentas.setSucursal(sucursal);
        cierreVentas.setNumeroCierre(getUltimoNumeroCierre() + 1);
        cierreVentas.setFecha(Comunes.obtenerFechaActualDesdeDB());
        cierreVentas.setTicketDesde(VentaFacade.getInstance().primeraVentasSinCerrar(sucursal));
        cierreVentas.setTicketHasta(VentaFacade.getInstance().ultimaVentasSinCerrar(sucursal));
        cierreVentas.setImporte(VentaFacade.getInstance().importeVentasSinCerrar(sucursal));
        cierreVentas.setVentas(listaVentasSinCerrar);
        CierreVentasFacade.getInstance().alta(cierreVentas);
        // cerrarVentas hugo
        for (Venta venta : listaVentasSinCerrar) {
            venta.setCierreVentas(cierreVentas);
            VentaFacade.getInstance().modificar(venta);
        }
        return cierreVentas;
    }

    public int getUltimoNumeroCierre() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT v.numeroCierre FROM CierreVentas v ORDER BY v.numeroCierre DESC");
        quBuscar.setMaxResults(1);
        try {
            return (int) quBuscar.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public void editar(CierreVentas cierreVentas) {
        try {
            new CierreVentasJpaController(emf).edit(cierreVentas);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CierreVentasFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CierreVentasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void borrar(CierreVentas cierreVentas) {
//        try {
//            new CierreVentasJpaController(emf).destroy(cierreVentas.getId());
//        } catch (NonexistentEntityException ex) {
//            Logger.getLogger(CierreVentasFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void borrar(Long cierreVentas) {
//        try {
//            new CierreVentasJpaController(emf).destroy(cierreVentas);
//        } catch (NonexistentEntityException ex) {
//            Logger.getLogger(CierreVentasFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public List<CierreVentas> listarCierreVentas(Date fechaInicio, Date fechaFin) {
        Query quBuscar = em.createQuery("SELECT c FROM CierreVentas c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        quBuscar.setParameter("fechaFin", fechaFin);
        return quBuscar.getResultList();
    }

    public List<CierreVentas> listarCierreVentas(Date fechaInicio, Date fechaFin, Sucursal sucursal) {
        Query quBuscar = em.createQuery("SELECT c FROM CierreVentas c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin "
                + "AND c.sucursal = :sucursal");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        quBuscar.setParameter("fechaFin", fechaFin);
        quBuscar.setParameter("sucursal", sucursal);
        return quBuscar.getResultList();
    }

    public List<CierreVentas> listarCierreVentas() {
        return new CierreVentasJpaController(emf).findCierreVentasEntities();
    }

    public List<CierreVentas> listarCierreVentas(Sucursal sucursal) {
        Query quBuscar = em.createQuery("SELECT c FROM CierreVentas c WHERE  "
                + "c.sucursal = :sucursal");
        quBuscar.setParameter("sucursal", sucursal);
        return quBuscar.getResultList();
    }

    public CierreVentas listarCierreVentas(int numeroCierre) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CierreVentas c WHERE c.numeroCierre = :numeroCierre");
        quBuscar.setParameter("numeroCierre", numeroCierre);
        try {
            return (CierreVentas) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean existeCierreVentaSucursal(int numCierre, String codSucursal) {
        boolean flag;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CierreVentas c WHERE c.numeroCierre=:numCierre AND c.sucursal.codigo=:codSucursal");
        quBuscar.setParameter("numCierre", numCierre);
        quBuscar.setParameter("codSucursal", codSucursal);
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
}
