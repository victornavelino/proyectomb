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
import controladores.TipoEmpleadoJpaController;
import controladores.TipoInventarioJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.empleado.TipoEmpleado;
import entidades.inventario.TipoInventario;
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
public class TipoInventarioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    TipoInventarioJpaController tipoInventarioJpaController = new TipoInventarioJpaController(emf);

    private static TipoInventarioFacade instance = null;

    protected TipoInventarioFacade() {
    }

    public static TipoInventarioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new TipoInventarioFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(TipoInventario tipoInventario) {
        new TipoInventarioJpaController(emf).create(tipoInventario);
    }

    public TipoInventario buscar(Long id) {
        return new TipoInventarioJpaController(emf).findTipoInventario(id);   
    }

    public void modificar(TipoInventario tipoInventario) {
        try {
            new TipoInventarioJpaController(emf).edit(tipoInventario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TipoInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new TipoInventarioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoInventario> getTodos() {
        return new TipoInventarioJpaController(emf).findTipoInventarioEntities();
    }

    public List<TipoInventario> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT t FROM TipoInventario t WHERE t.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

}
