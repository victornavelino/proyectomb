/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.DepartamentoJpaController;
import controladores.ListaPrecioJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Departamento;
import entidades.localidad.Provincia;
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
public class DepartamentoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    DepartamentoJpaController departamentoJpaController = new DepartamentoJpaController(emf);

    private static DepartamentoFacade instance = null;

    protected DepartamentoFacade() {
    }

    public static DepartamentoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new DepartamentoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Departamento departamento) {
        new DepartamentoJpaController(emf).create(departamento);
    }

    public Departamento buscar(Long id) {
        return new DepartamentoJpaController(emf).findDepartamento(id);   
    }

    public void modificar(Departamento departamento) {
        try {
            new DepartamentoJpaController(emf).edit(departamento);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DepartamentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DepartamentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new DepartamentoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DepartamentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Departamento> getTodos() {
        return new DepartamentoJpaController(emf).findDepartamentoEntities();
    }

    public List<Departamento> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT d FROM Departamento d WHERE d.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }
    
     public List<Departamento> listarTodosDepartamentoOrdenados(Provincia provincia) {
        Query quBuscar = em.createQuery("SELECT p FROM Departamento p WHERE p.provincia = :provincia ORDER BY p.descripcion");
        quBuscar.setParameter("provincia", provincia);
        return quBuscar.getResultList();
    }

}
