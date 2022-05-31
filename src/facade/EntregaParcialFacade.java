/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.EntregaParcialJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.venta.EntregaParcial;
import entidades.venta.OrdenDeCompraArticulo;
import java.math.BigDecimal;
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
public class EntregaParcialFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static EntregaParcialFacade instance = null;

    protected EntregaParcialFacade() {
    }

    public static EntregaParcialFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new EntregaParcialFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(EntregaParcial entrega) {
        new EntregaParcialJpaController(emf).create(entrega);
    }

    public EntregaParcial buscar(Long id) {
        return new EntregaParcialJpaController(emf).findEntregaParcial(id);
    }

    public void modificar(EntregaParcial entrega) {
        try {
            new EntregaParcialJpaController(emf).edit(entrega);
        } catch (Exception ex) {
            Logger.getLogger(EntregaParcialFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new EntregaParcialJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EntregaParcialFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<EntregaParcial> getTodos() {
        return new EntregaParcialJpaController(emf).findEntregaParcialEntities();
    }

    public List<EntregaParcial> buscar(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT v FROM EntregaParcial v WHERE v.ordenDeCompraArticulo=:ordenDeCompraArticulo ORDER BY v.id DESC");
        q.setParameter("ordenDeCompraArticulo", ordenDeCompraArticulo);
        return q.getResultList();

    }

    public BigDecimal totalOrdenDeCompraArticulo(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT SUM(v.ventaArticulo.cantidadPeso) FROM EntregaParcial v WHERE v.ordenDeCompraArticulo=:ordenDeCompraArticulo");
        q.setParameter("ordenDeCompraArticulo", ordenDeCompraArticulo);
        BigDecimal resultado = (BigDecimal) q.getSingleResult();
        System.out.println("orden: " + ordenDeCompraArticulo.getId() + "suma: " + resultado);
        if (resultado != null) {
            return resultado;
        } else {
            return BigDecimal.ZERO;
        }

    }
}
