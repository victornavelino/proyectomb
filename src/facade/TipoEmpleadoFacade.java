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
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.empleado.TipoEmpleado;
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
public class TipoEmpleadoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    TipoEmpleadoJpaController tipoEmpleadoJpaController = new TipoEmpleadoJpaController(emf);

    private static TipoEmpleadoFacade instance = null;

    protected TipoEmpleadoFacade() {
    }

    public static TipoEmpleadoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new TipoEmpleadoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(TipoEmpleado tipoEmpleado) {
        new TipoEmpleadoJpaController(emf).create(tipoEmpleado);
    }

    public TipoEmpleado buscar(Long id) {
        return new TipoEmpleadoJpaController(emf).findTipoEmpleado(id);   
    }

    public void modificar(TipoEmpleado tipoEmpleado) {
        try {
            new TipoEmpleadoJpaController(emf).edit(tipoEmpleado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoEmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TipoEmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new TipoEmpleadoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoEmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoEmpleado> getTodos() {
        return new TipoEmpleadoJpaController(emf).findTipoEmpleadoEntities();
    }

    public List<TipoEmpleado> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT t FROM TipoEmpleado t WHERE t.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

}
