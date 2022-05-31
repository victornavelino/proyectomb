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
import controladores.UnidadMedidaJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.UnidadMedida;
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
public class UnidadMedidaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    UnidadMedidaJpaController unidadMedidaJpaController = new UnidadMedidaJpaController(emf);

    private static UnidadMedidaFacade instance = null;

    protected UnidadMedidaFacade() {
    }

    public static UnidadMedidaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new UnidadMedidaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(UnidadMedida unidadMedida) {
        new UnidadMedidaJpaController(emf).create(unidadMedida);
    }

    public UnidadMedida buscar(Long id) {
        return new UnidadMedidaJpaController(emf).findUnidadMedida(id);
    }

    public void modificar(UnidadMedida unidadMedida) {
        try {
            new UnidadMedidaJpaController(emf).edit(unidadMedida);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UnidadMedidaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UnidadMedidaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new UnidadMedidaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UnidadMedidaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<UnidadMedida> getTodos() {
        Query qu = em.createQuery("SELECT u FROM UnidadMedida u ORDER BY u.descripcion ASC");
        return qu.getResultList();
    }

    public List<UnidadMedida> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT u FROM UnidadMedida u WHERE u.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

    public UnidadMedida buscaDescripcion(String descripcion) {
        try {
            Query qu = em.createQuery("SELECT u FROM UnidadMedida u WHERE u.descripcion=:descripcion");
            qu.setParameter("descripcion", descripcion);
            return (UnidadMedida) qu.getSingleResult();
        } catch (Exception e) {
            System.out.println("salio por el catch unidad medida");
            return null;
        }
    }

}
