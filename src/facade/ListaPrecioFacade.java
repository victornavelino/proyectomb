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
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.PrecioArticulo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class ListaPrecioFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    ListaPrecioJpaController listaPrecioJpaController = new ListaPrecioJpaController(emf);

    private static ListaPrecioFacade instance = null;

    protected ListaPrecioFacade() {
    }

    public static ListaPrecioFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ListaPrecioFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(ListaPrecio listaPrecio) {
        new ListaPrecioJpaController(emf).create(listaPrecio);
    }

    public ListaPrecio buscar(Long id) {
        return new ListaPrecioJpaController(emf).findListaPrecio(id);
    }

    public void modificar(ListaPrecio listaPrecio) {
        try {
            new ListaPrecioJpaController(emf).edit(listaPrecio);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ListaPrecioFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ListaPrecioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new ListaPrecioJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ListaPrecioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ListaPrecio> getTodos() {
        return new ListaPrecioJpaController(emf).findListaPrecioEntities();
    }

    public List<ListaPrecio> buscarPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT l FROM ListaPrecio l WHERE l.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion + "%");
        return qu.getResultList();
    }

    public ListaPrecio getPorDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT l FROM ListaPrecio l WHERE l.descripcion=:descripcion");
        qu.setParameter("descripcion", descripcion);
        try {
            return (ListaPrecio) qu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public ListaPrecio getPorDescripcion(String descripcion, EntityManager ema) {
        Query qu = ema.createQuery("SELECT l FROM ListaPrecio l WHERE l.descripcion=:descripcion");
        qu.setParameter("descripcion", descripcion);
        try {
            return (ListaPrecio) qu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public boolean getExisteDescripcion(String descripcion) {
        Query qu = em.createQuery("SELECT l FROM ListaPrecio l WHERE l.descripcion=:descripcion");
        qu.setParameter("descripcion", descripcion);
        try {
            if (qu.getSingleResult() != null) {
            }
            return true;
        } catch (NoResultException e) {
            return false;
        }

    }

    public void copiarLista(Sucursal origen, Sucursal destino, ListaPrecio lista) {
        List<PrecioArticulo> precios = PrecioArticuloFacade.getInstance().get(lista, origen);
        for (PrecioArticulo pa : precios) {
            if (!PrecioArticuloFacade.getInstance().existePrecio(lista, destino, pa.getArticulo())) {
                PrecioArticulo precioArticulo = new PrecioArticulo();
                precioArticulo.setListaPrecio(lista);
                precioArticulo.setSucursal(destino);
                precioArticulo.setArticulo(pa.getArticulo());
                precioArticulo.setPrecio(pa.getPrecio());
                PrecioArticuloFacade.getInstance().alta(precioArticulo);
            } else {
                PrecioArticulo precioArticulo = PrecioArticuloFacade.getInstance().get(pa.getArticulo(), lista, destino);
                precioArticulo.setArticulo(pa.getArticulo());
                precioArticulo.setPrecio(pa.getPrecio());
                PrecioArticuloFacade.getInstance().modificar(precioArticulo);
            }
        }
    }

    public void copiarLista(Sucursal origen, ListaPrecio lista) {
        List<Sucursal> sucursalesDestino = SucursalFacade.getInstance().getTodos();
        sucursalesDestino.remove(origen);
        for (Sucursal destino : sucursalesDestino) {
            copiarLista(origen, destino, lista);
        }
    }

    public void copiarTodasListasAUnaSucursal(Sucursal origen, Sucursal destino) {
        List<ListaPrecio> listas = getTodos();
        for (ListaPrecio lista : listas) {
            copiarLista(origen, destino, lista);
        }
    }

    public void copiarTodasListasATodasSucursales(Sucursal origen) {
        List<Sucursal> sucursalesDestino = SucursalFacade.getInstance().getTodos();
        sucursalesDestino.remove(origen);
        List<ListaPrecio> listas = getTodos();
        for (Sucursal destino : sucursalesDestino) {
            for (ListaPrecio lista : listas) {
                copiarLista(origen, destino, lista);
            }
        }

    }

}
