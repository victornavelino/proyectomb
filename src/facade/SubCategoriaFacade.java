/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ListaPrecioJpaController;
import controladores.SubCategoriaJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.SubCategoria;
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
public class SubCategoriaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    SubCategoriaJpaController subCategoriaJpaController = new SubCategoriaJpaController(emf);

    private static SubCategoriaFacade instance = null;

    protected SubCategoriaFacade() {
    }

    public static SubCategoriaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new SubCategoriaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(SubCategoria subCategoria) {
        new SubCategoriaJpaController(emf).create(subCategoria);
    }

    public SubCategoria buscar(Long id) {
        return new SubCategoriaJpaController(emf).findSubCategoria(id);
    }

    public void modificar(SubCategoria subCategoria) {
        try {
            new SubCategoriaJpaController(emf).edit(subCategoria);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SubCategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SubCategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new SubCategoriaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SubCategoriaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SubCategoria> getTodos() {
        Query qu = em.createQuery("SELECT s FROM SubCategoria s ORDER BY s.descripcion ASC");
        return qu.getResultList();
    }

    public SubCategoria buscarPorDescripcion(String descripcion) {

        try {
            Query qu = em.createQuery("SELECT s FROM SubCategoria s WHERE s.descripcion=:descripcion");
            qu.setParameter("descripcion", descripcion);
            System.out.println("descrip " + qu.getResultList());
            return (SubCategoria) qu.getSingleResult();
        } catch (Exception e) {
            System.out.println("salio por el catch");
            return null;
        }
    }

}
