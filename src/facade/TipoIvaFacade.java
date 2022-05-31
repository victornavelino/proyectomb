/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ListaPrecioJpaController;
import controladores.SucursalJpaController;
import controladores.TipoIvaJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.TipoIva;
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
public class TipoIvaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    TipoIvaJpaController tipoIvaJpaController = new TipoIvaJpaController(emf);

    private static TipoIvaFacade instance = null;

    protected TipoIvaFacade() {
    }

    public static TipoIvaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new TipoIvaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(TipoIva tipoIva) {
        new TipoIvaJpaController(emf).create(tipoIva);
    }

    public TipoIva buscar(Long id) {
        return new TipoIvaJpaController(emf).findTipoIva(id);
    }

    public void modificar(TipoIva tipoIva) {
        try {
            new TipoIvaJpaController(emf).edit(tipoIva);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoIvaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TipoIvaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new TipoIvaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoIvaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoIva> getTodos() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = ema.createQuery("SELECT t FROM TipoIva t");
        return qu.getResultList();
    }

    public List<TipoIva> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT t FROM TipoIva t WHERE t.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

    public TipoIva buscarPorDescripcionUnico(String descripcion) {
        try {
            Query qu = em.createQuery("SELECT t FROM TipoIva t WHERE t.descripcion LIKE :descripcion");
            qu.setParameter("descripcion", "%" + descripcion + "%");
            return (TipoIva) qu.getSingleResult();
        } catch (Exception e) {
            System.out.println("salio por el catch Tipo iva buscarPorDescripcionUnico");
            return null;
        }
    }

    public TipoIva getXDescripcion(String descripcion) {
        try {
            Query qu = em.createQuery("SELECT t FROM TipoIva t WHERE t.descripcion = :descripcion");
            qu.setParameter("descripcion", descripcion);
            return (TipoIva) qu.getSingleResult();
        } catch (Exception e) {
            System.out.println("salio por el catch Tipo iva buscarPorDescripcionUnico");
            return null;
        }
    }
}
