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
import controladores.PromocionArticuloJpaController;
import controladores.PromocionJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.promocion.Promocion;
import entidades.promocion.PromocionArticulo;
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
public class PromocionArticuloFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    PromocionArticuloJpaController promocionArticuloJpaController = new PromocionArticuloJpaController(emf);

    private static PromocionArticuloFacade instance = null;

    protected PromocionArticuloFacade() {
    }

    public static PromocionArticuloFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new PromocionArticuloFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(PromocionArticulo promocionArticulo) {
        new PromocionArticuloJpaController(emf).create(promocionArticulo);
    }

    public PromocionArticulo buscar(Long id) {
        return new PromocionArticuloJpaController(emf).findPromocionArticulo(id);
    }

    public void modificar(PromocionArticulo promocionArticulo) {
        try {
            new PromocionArticuloJpaController(emf).edit(promocionArticulo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PromocionArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PromocionArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new PromocionArticuloJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PromocionArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<PromocionArticulo> getTodos() {
        return new PromocionArticuloJpaController(emf).findPromocionArticuloEntities();
    }

    public List<PromocionArticulo> buscarPorArticuloDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT p FROM PromocionArticulo p WHERE p.articulo.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

    public PromocionArticulo buscarPromocion(Articulo articulo) {
        Query qu = em.createQuery("SELECT p FROM PromocionArticulo p WHERE p.articulo=:articulo");
        qu.setParameter("articulo", articulo);
        return (PromocionArticulo) qu.getSingleResult();
    }

    public List<PromocionArticulo> buscarPromocionesArticulo(Articulo articulo) {
        Query qu = em.createQuery("SELECT p FROM PromocionArticulo p WHERE p.articulo=:articulo");
        qu.setParameter("articulo", articulo);
        return qu.getResultList();
    }

}
