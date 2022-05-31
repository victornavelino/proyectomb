/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ClienteJpaController;
import controladores.EmpleadoJpaController;
import controladores.ListaPrecioJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.cliente.Cliente;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.empleado.Empleado;
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
public class EmpleadoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    EmpleadoJpaController empleadoJpaController = new EmpleadoJpaController(emf);

    private static EmpleadoFacade instance = null;

    protected EmpleadoFacade() {
    }

    public static EmpleadoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new EmpleadoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Empleado empleado) {
        new EmpleadoJpaController(emf).create(empleado);
    }

    public Empleado buscar(Long id) {
        return new EmpleadoJpaController(emf).findEmpleado(id);
    }

    public void modificar(Empleado empleado) {
        try {
            new EmpleadoJpaController(emf).edit(empleado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarCentral(Empleado empleado) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            new EmpleadoJpaController(emfa).edit(empleado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new EmpleadoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EmpleadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Empleado> getTodos() {
        return new EmpleadoJpaController(emf).findEmpleadoEntities();
    }

    public Empleado getEmpleadoXId(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT e FROM Empleado e WHERE e.id=:id");
        quBuscar.setParameter("id", id);
        return (Empleado) quBuscar.getSingleResult();
    }

    public List<Empleado> getEmpleadoNombre(String descripcion) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT e FROM Empleado e WHERE e.apellido LIKE :descripcion");
        quBuscar.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public List<Empleado> getTodosOrdenadosXApellido() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT e FROM Empleado e ORDER BY e.apellido ASC");
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    public boolean existeDNI(String dni) {

        EntityManager ema = emf.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM Empleado e WHERE e.documentoIdentidad.numero=:dni");
        qu.setParameter("dni", dni);
        return !qu.getResultList().isEmpty();

    }

    public boolean existeEmpleadoCliente(Cliente cliente) {

        EntityManager ema = emf.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM Empleado e WHERE e.documentoIdentidad.numero=:dni AND e.fechaBaja IS NULL");
//        System.out.println("CLIENTE ERROR: "+cliente.getId());
        qu.setParameter("dni", ((Persona) cliente).getDocumentoIdentidad().getNumero().trim());
        return !qu.getResultList().isEmpty();

    }

    public boolean existeEmpleadoClienteCentral(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM Empleado e WHERE e.documentoIdentidad.numero=:dni AND e.fechaBaja IS NULL");
        qu.setParameter("dni", ((Persona) cliente).getDocumentoIdentidad().getNumero().trim());
        return !qu.getResultList().isEmpty();

    }

    public Empleado getEmpleadoXDNI(Cliente cliente) {

        EntityManager ema = emf.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM Empleado e WHERE e.documentoIdentidad.numero=:dni AND e.fechaBaja IS NULL");
        qu.setParameter("dni", ((Persona) cliente).getDocumentoIdentidad().getNumero().trim());
        return (Empleado) qu.getSingleResult();

    }

    public Empleado getEmpleadoXDNICentral(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM Empleado e WHERE e.documentoIdentidad.numero=:dni AND e.fechaBaja IS NULL");
        qu.setParameter("dni", ((Persona) cliente).getDocumentoIdentidad().getNumero().trim());
        return (Empleado) qu.getSingleResult();

    }

    public void darDeBaja(Empleado empleado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
