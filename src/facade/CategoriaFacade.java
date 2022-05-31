/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
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
public class CategoriaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    CategoriaJpaController categoriaJpaController = new CategoriaJpaController(emf);

    private static CategoriaFacade instance = null;

    protected CategoriaFacade() {
    }

    public static CategoriaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CategoriaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Categoria categoria) {
        new CategoriaJpaController(emf).create(categoria);
    }

    public Categoria buscar(Long id) {
        return new CategoriaJpaController(emf).findCategoria(id);
    }

    public void modificar(Categoria categoria) {
        try {
            new CategoriaJpaController(emf).edit(categoria);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new CategoriaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Categoria> getTodos() {
        return categoriaJpaController.findCategoriaEntities();
    }

    public List<Categoria> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT c FROM Categoria c WHERE c.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

    public List<Categoria> buscarPorDescripcionIgual(String descripcion) {
        Query qu = em.createQuery("SELECT c FROM Categoria c WHERE c.descripcion = :descripcion");
        qu.setParameter("descripcion", descripcion);
        return qu.getResultList();
    }

    public Categoria buscarPorDescripcionIgualUnico(String descripcion) {
        Query qu = em.createQuery("SELECT c FROM Categoria c WHERE c.descripcion = :descripcion");
        qu.setParameter("descripcion", descripcion);
        try {
            return (Categoria) qu.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Categoria buscarPorDescripcionUnico(String descripcion) {
        try {
            Query qu = em.createQuery("SELECT c FROM Categoria c WHERE c.descripcion LIKE :descripcion");
            qu.setParameter("descripcion", "%" + descripcion + "%");
            return (Categoria) qu.getSingleResult();
        } catch (Exception e) {
            System.out.println("salio por el catch categoria buscarPorDescripcionUnico");
            return null;
        }
    }

}
