/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.InventarioJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.inventario.ArticuloInventario;
import entidades.inventario.Inventario;
import entidades.inventario.TipoInventario;
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
public class InventarioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    InventarioJpaController inventarioJpaController = new InventarioJpaController(emf);

    private static InventarioFacade instance = null;

    protected InventarioFacade() {
    }

    public static InventarioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new InventarioFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Inventario inventario) {
        new InventarioJpaController(emf).create(inventario);
    }

    public Inventario buscar(Long id) {
        return new InventarioJpaController(emf).findInventario(id);
    }

    public void modificar(Inventario inventario) {
        try {
            new InventarioJpaController(emf).edit(inventario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new InventarioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void altaCentral(Inventario inventario) {
        EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        new InventarioJpaController(emf_c).create(inventario);
    }

    public void eliminarCentral(long id) {
        try {
            EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

            new InventarioJpaController(emf_c).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarCentral(Inventario inventario) {
        try {
            EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new InventarioJpaController(emf_c).edit(inventario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Inventario> getTodos() {
        return new InventarioJpaController(emf).findInventarioEntities();
    }

    public List<Inventario> buscarPorSucursal(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = em.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal");
        qu.setParameter("sucursal", sucursal);
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public Inventario buscarPorSucursalMesAnioTipoInventario(Sucursal sucursal,int mes, int anio, TipoInventario tipoInventario) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = em.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal and i.mes=:mes and i.anio=:anio and i.tipoInventario=:tipoInventario");
        qu.setParameter("sucursal", sucursal);
        qu.setParameter("mes", mes);
        qu.setParameter("anio", anio);
        qu.setParameter("tipoInventario", tipoInventario);
        ema.getEntityManagerFactory().getCache().evictAll();
        return (Inventario)qu.getSingleResult();
    }

    public List<Inventario> listarTodosInventarioOrdenados(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal ORDER BY i.fecha ASC");
        quBuscar.setParameter("sucursal", sucursal);
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public Long buscarUltimoInventario() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT MAX(i.id) FROM Inventario i");
        ema.getEntityManagerFactory().getCache().evictAll();
        return (Long) quBuscar.getSingleResult();
    }

    public Long buscarUltimoInventarioFinalSucursal(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT MAX(i.id) FROM Inventario i where i.sucursal=:sucursal and i.tipoInventario.descripcion like :texto");
        quBuscar.setParameter("sucursal", sucursal);
        quBuscar.setParameter("texto", "%INVENTARIO FINAL%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return (Long) quBuscar.getSingleResult();
    }

    public List<Inventario> buscarPorFecha(Date fechaIni, Date fechaFin) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT i FROM Inventario i WHERE i.fecha BETWEEN :fechaIni and :fechaFin ORDER BY i.fecha ASC");
        quBuscar.setParameter("fechaIni", fechaIni);
        quBuscar.setParameter("fechaFin", fechaFin);
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Inventario> buscarPorFecha_Central(Date fechaIni, Date fechaFin, Sucursal suc) {

        EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emf_c = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emf_c.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal AND i.fecha BETWEEN :fechaIni and :fechaFin ORDER BY i.fecha ASC");
        quBuscar.setParameter("fechaIni", fechaIni);
        quBuscar.setParameter("fechaFin", fechaFin);
        quBuscar.setParameter("sucursal", suc);
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Inventario> buscarPorFecha(Date fechaIni, Date fechaFin, Sucursal suc) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT i FROM Inventario i WHERE i.sucursal =:sucursal AND i.fecha BETWEEN :fechaIni and :fechaFin ORDER BY i.fecha ASC");
        quBuscar.setParameter("fechaIni", fechaIni);
        quBuscar.setParameter("fechaFin", fechaFin);
        quBuscar.setParameter("sucursal", suc);
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }
}
