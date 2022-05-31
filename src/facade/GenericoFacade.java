/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controlador.generico.GenericoJpaController;
import controladores.exceptions.NonexistentEntityException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author diego
 */
public class GenericoFacade<T> {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    GenericoJpaController genericoJpaController = new GenericoJpaController(emf);

    private static GenericoFacade instance = null;

    protected GenericoFacade() {
    }

    public static GenericoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new GenericoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(T entidad) throws Exception {

        this.validar(entidad, 0);

        genericoJpaController.create(entidad);
    }

    public void modificar(T entidad) throws Exception {

        this.validar(entidad, 1);

        genericoJpaController.edit(entidad);

    }

    public void eliminar(T entidad) throws Exception {
        genericoJpaController.destroy(entidad);
    }

    public List<T> findLike(String cadena, String nombreEntidad) {

        return genericoJpaController.findLike(cadena, nombreEntidad);
    }//fin findLike

    public T buscar(String cadena, String nombreEntidad) {

        return (T) genericoJpaController.buscar(cadena, nombreEntidad);
    }//fin findLike

    public List<T> buscarTodos(Field nombreEntidad) {
        //EntityManager em = getEntityManager();
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = ema.createQuery("SELECT e FROM " + nombreEntidad.getType().getSimpleName() + " e");
        return qu.getResultList();
    }

    public void validar(T entidad, int op) throws Exception {
        Class[] sinParametro = null;

        Method campo;

        campo = entidad.getClass().getDeclaredMethod("getDescripcion", sinParametro);
        Object objetoDesc = campo.invoke(entidad, new Object[]{});

        if (objetoDesc == null || String.valueOf(objetoDesc).trim().isEmpty()) {
            throw new Exception("No ingreso el nombre del campo");
        }//fin if

        //validar si existe otro con el mismo nombre
        switch (op) {
            case 0: //guardar
                if (genericoJpaController.findByDescripcion(String.valueOf(objetoDesc), entidad.getClass().getSimpleName(), null) != null) {
                    throw new Exception("Ya existe un dato con el nombre ingresado");
                }
                break;

            case 1: //editar

                //id 
                campo = entidad.getClass().getDeclaredMethod("getId", sinParametro);
                Object objetoId = campo.invoke(entidad, new Object[]{});

                if (genericoJpaController.findByDescripcion(String.valueOf(objetoDesc),
                        entidad.getClass().getSimpleName(),
                        Long.parseLong(String.valueOf(objetoId))) != null) {

                    throw new Exception("Ya existe un dato con el nombre ingresado");
                }
                break;
        }

    }//fin validar

    public void insertar(T entidad) {

        genericoJpaController.create(entidad);
    }

    public List<T> find(String nombreEntidad) {

        return genericoJpaController.find(nombreEntidad);
    }//fin findLike
}
