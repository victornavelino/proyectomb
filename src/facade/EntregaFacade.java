/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.EntregaJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.venta.CierreVentas;
import entidades.venta.Entrega;
import entidades.venta.OrdenDeCompra;
import entidades.venta.Venta;
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
public class EntregaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static EntregaFacade instance = null;

    protected EntregaFacade() {
    }

    public static EntregaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new EntregaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Entrega entrega) {
        new EntregaJpaController(emf).create(entrega);
    }

    public Entrega buscar(Long id) {
        return new EntregaJpaController(emf).findEntrega(id);
    }

    public void modificar(Entrega entrega) {
        try {
            new EntregaJpaController(emf).edit(entrega);
        } catch (Exception ex) {
            Logger.getLogger(EntregaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new EntregaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EntregaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Entrega> getTodos() {
        return new EntregaJpaController(emf).findEntregaEntities();
    }

    public List<Entrega> buscar(OrdenDeCompra ordenDeCompra) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM Entrega v WHERE v.ordenDeCompra=:ordenDeCompra ORDER BY v.id DESC");
        q.setParameter("ordenDeCompra", ordenDeCompra);
        return q.getResultList();

    }

    public boolean existeEntregaVenta(Venta venta) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT e FROM Entrega e WHERE e.venta=:venta");
        q.setParameter("venta", venta);
        return !q.getResultList().isEmpty();

    }

    public Entrega getEntrega(Venta venta) {
        EntityManager ema = emf.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT e FROM Entrega e WHERE e.venta=:venta");
        quBuscar.setParameter("venta", venta);
        try {
            return (Entrega) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
