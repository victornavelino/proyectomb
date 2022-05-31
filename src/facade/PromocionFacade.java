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
import controladores.PromocionJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.promocion.DiaSemana;
import entidades.promocion.Promocion;
import entidades.promocion.PromocionArticulo;
import includes.Comunes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class PromocionFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static PromocionFacade instance = null;

    protected PromocionFacade() {
    }

    public static PromocionFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new PromocionFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Promocion promocion) {
        new PromocionJpaController(emf).create(promocion);
    }

    public Promocion buscar(Long id) {
        return new PromocionJpaController(emf).findPromocion(id);
    }

    public void modificar(Promocion promocion) {
        try {
            new PromocionJpaController(emf).edit(promocion);
        } catch (NonexistentEntityException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar(long id) {
        try {
            new PromocionJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo borrar\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Promocion> getTodos() {
        return new PromocionJpaController(emf).findPromocionEntities();
    }

    public List<Promocion> buscarPorDescripcion(String descripcion) {
        EntityManager em = emf.createEntityManager();

        Query qu = em.createQuery("SELECT p FROM Promocion p WHERE p.nombre LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        return qu.getResultList();
    }

    public List<Promocion> buscarPorDescripcion(String descripcion, Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query qu = em.createQuery("SELECT p FROM Promocion p WHERE p.sucursal = :sucursal AND p.nombre LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        qu.setParameter("sucursal", sucursal);
        em.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<Promocion> buscarPorSucursal(Sucursal sucursal) {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfs.createEntityManager();
        Query qu = em.createQuery("SELECT p FROM Promocion p WHERE p.sucursal = :sucursal");
        qu.setParameter("sucursal", sucursal);
        em.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public Promocion buscarPorSucursalyDescripcion(Sucursal sucursal, String descripcion) {
        EntityManager em = emf.createEntityManager();

        Query qu = em.createQuery("SELECT p FROM Promocion p WHERE p.sucursal = :sucursal AND "
                + " p.nombre = :descripcion");
        qu.setParameter("sucursal", sucursal);
        qu.setParameter("descripcion", descripcion);
        try {
            return (Promocion) qu.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Promocion buscarPorPromocionArticulo(PromocionArticulo promocionArticulo, Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Date fechaHoy = Comunes.obtenerFechaActualDesdeDB();
        Query qu = ema.createQuery("SELECT p FROM Promocion p, IN(p.promocionesArticulos) pa WHERE pa=:promocionArticulo "
                + "AND p.habilitada=TRUE AND p.fechaInicio<=:fechaHoy AND p.fechaFin>=:fechaHoy AND p.sucursal = :sucursal ORDER BY p.prioridad ASC");
        qu.setParameter("promocionArticulo", promocionArticulo);
        qu.setParameter("fechaHoy", fechaHoy);
        qu.setParameter("sucursal", sucursal);
        return (Promocion) qu.getSingleResult();
    }

    public List<Promocion> buscarPorPromocionArticulo(Articulo articulo, Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Date fechaHoy = Comunes.obtenerFechaActualDesdeDB();
        Query qu = ema.createQuery("SELECT p FROM Promocion p, IN(p.promocionesArticulos) pa WHERE pa.articulo=:articulo "
                + "AND p.habilitada=TRUE AND p.fechaInicio<=:fechaHoy AND p.fechaFin>=:fechaHoy AND p.sucursal = :sucursal ORDER BY p.prioridad ASC");
        qu.setParameter("articulo", articulo);
        qu.setParameter("fechaHoy", fechaHoy);
        qu.setParameter("sucursal", sucursal);
        return qu.getResultList();
    }

    public List<Promocion> buscarPorPorcentaje(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Date fechaHoy = Comunes.obtenerFechaActualDesdeDB();
        BigDecimal cero = new BigDecimal("0.00");
        Query qu = ema.createQuery("SELECT p FROM Promocion p WHERE  p.esPorPrecio=FALSE AND p.porcentajeATodos>:cero AND p.habilitada=TRUE AND p.fechaInicio<=:fechaHoy AND p.fechaFin>=:fechaHoy AND p.sucursal = :sucursal ORDER BY p.prioridad ASC");
        qu.setParameter("fechaHoy", fechaHoy);
        qu.setParameter("cero", cero);
        qu.setParameter("sucursal", sucursal);
        return qu.getResultList();
    }

    public void copiarPromocion(Sucursal origen, Sucursal destino, Promocion promocion) {
        Promocion promocionBuscada = buscarPorSucursalyDescripcion(destino, promocion.getNombre());
        if (promocionBuscada != null) {
            eliminar(promocionBuscada.getId());
        }
        Promocion promocionNueva = new Promocion();
        promocionNueva.setDiasSemanas(promocion.getDiasSemanas());
        promocionNueva.setEsPorPrecio(promocion.isEsPorPrecio());
        promocionNueva.setFechaFin(promocion.getFechaFin());
        promocionNueva.setFechaInicio(promocion.getFechaInicio());
        promocionNueva.setHabilitada(promocion.isHabilitada());
        promocionNueva.setNombre(promocion.getNombre());
        promocionNueva.setPorcentajeATodos(promocion.getPorcentajeATodos());
        promocionNueva.setPrioridad(promocion.getPrioridad());
        List<PromocionArticulo> promocionesArticulos = promocion.getPromocionesArticulos();
        List<PromocionArticulo> promocionesArticulosNuevos = new ArrayList<>();
        for (PromocionArticulo pa : promocionesArticulos) {
            PromocionArticulo promocionArticulo = new PromocionArticulo();
            promocionArticulo.setArticulo(pa.getArticulo());
            promocionArticulo.setValor(pa.getValor());
            promocionesArticulosNuevos.add(promocionArticulo);
        }
        promocionNueva.setPromocionesArticulos(promocionesArticulosNuevos);
        promocionNueva.setSucursal(destino);
        alta(promocionNueva);
    }

    public void copiarTodasPromocionesAUnaSucursal(Sucursal origen, Sucursal destino, List<Promocion> promociones) {
        for (Promocion promocion : promociones) {
            Promocion promocionBuscada = buscarPorSucursalyDescripcion(destino, promocion.getNombre());
            if (promocionBuscada != null) {
                eliminar(promocionBuscada.getId());
            }
            Promocion promocionNueva = new Promocion();
            promocionNueva.setDiasSemanas(promocion.getDiasSemanas());
            promocionNueva.setEsPorPrecio(promocion.isEsPorPrecio());
            promocionNueva.setFechaFin(promocion.getFechaFin());
            promocionNueva.setFechaInicio(promocion.getFechaInicio());
            promocionNueva.setHabilitada(promocion.isHabilitada());
            promocionNueva.setNombre(promocion.getNombre());
            promocionNueva.setPorcentajeATodos(promocion.getPorcentajeATodos());
            promocionNueva.setPrioridad(promocion.getPrioridad());
            List<PromocionArticulo> promocionesArticulos = promocion.getPromocionesArticulos();
            List<PromocionArticulo> promocionesArticulosNuevos = new ArrayList<>();
            for (PromocionArticulo pa : promocionesArticulos) {
                PromocionArticulo promocionArticulo = new PromocionArticulo();
                promocionArticulo.setArticulo(pa.getArticulo());
                promocionArticulo.setValor(pa.getValor());
                promocionesArticulosNuevos.add(promocionArticulo);
            }
            promocionNueva.setPromocionesArticulos(promocionesArticulosNuevos);
            promocionNueva.setSucursal(destino);
            alta(promocionNueva);
        }

    }

    public void copiarTodasPromocionesATodasSucursales(Sucursal origen, List<Promocion> promociones) {
        List<Sucursal> sucursales = SucursalFacade.getInstance().getSucursalesNoCentral();
        for (Sucursal sucursalDestino : sucursales) {
            for (Promocion promocion : promociones) {
                Promocion promocionBuscada = buscarPorSucursalyDescripcion(sucursalDestino, promocion.getNombre());
                if (promocionBuscada != null) {
                    eliminar(promocionBuscada.getId());
                }
                Promocion promocionNueva = new Promocion();
                promocionNueva.setDiasSemanas(promocion.getDiasSemanas());
                promocionNueva.setEsPorPrecio(promocion.isEsPorPrecio());
                promocionNueva.setFechaFin(promocion.getFechaFin());
                promocionNueva.setFechaInicio(promocion.getFechaInicio());
                promocionNueva.setHabilitada(promocion.isHabilitada());
                promocionNueva.setNombre(promocion.getNombre());
                promocionNueva.setPorcentajeATodos(promocion.getPorcentajeATodos());
                promocionNueva.setPrioridad(promocion.getPrioridad());
                List<PromocionArticulo> promocionesArticulos = promocion.getPromocionesArticulos();
                List<PromocionArticulo> promocionesArticulosNuevos = new ArrayList<>();
                for (PromocionArticulo pa : promocionesArticulos) {
                    PromocionArticulo promocionArticulo = new PromocionArticulo();
                    promocionArticulo.setArticulo(pa.getArticulo());
                    promocionArticulo.setValor(pa.getValor());
                    promocionesArticulosNuevos.add(promocionArticulo);
                }
                promocionNueva.setPromocionesArticulos(promocionesArticulosNuevos);
                promocionNueva.setSucursal(sucursalDestino);
                alta(promocionNueva);
            }

        }

    }

    public void copiarPromocion(Sucursal origen, Promocion promocion) {
        List<Sucursal> sucursalesDestino = SucursalFacade.getInstance().getTodos();
        sucursalesDestino.remove(origen);
        for (Sucursal destino : sucursalesDestino) {
            copiarPromocion(origen, destino, promocion);
        }
    }

    public boolean buscarPrioridad(int prioridad, Sucursal sucursal) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Promocion p WHERE p.prioridad=:prioridad AND p.sucursal=:sucursal");
        quBuscar.setParameter("prioridad", prioridad);
        quBuscar.setParameter("sucursal", sucursal);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch, no se encontro el articulo");
            return false;
        }

    }

    public boolean buscarPrioridad(int prioridad, Promocion promocion, Sucursal sucursal) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT p FROM Promocion p WHERE p.prioridad=:prioridad AND p.id<>:id AND p.sucursal=:sucursal");
        quBuscar.setParameter("prioridad", prioridad);
        quBuscar.setParameter("id", promocion.getId());
        quBuscar.setParameter("sucursal", sucursal);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch, no se encontro el articulo");
            return false;
        }

    }

    public int getPrioridadDisponible() {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT MAX(p.prioridad) FROM Promocion p");
        return (int) qu.getSingleResult();

    }

    public void borrarPromociones() {
        System.out.println("entro borrado promo");
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        em.getTransaction().begin();
        //int nativa= em.createNamedQuery("TRUNCATE promocion_promocion_articulo").executeUpdate();
        int qu = em.createQuery("DELETE FROM Promocion p").executeUpdate();
        int qud = em.createQuery("DELETE FROM PromocionArticulo pa").executeUpdate();
        //int quds = em.createQuery("DELETE FROM PrecioArticulo ae").executeUpdate();
        System.out.println("borrado entero " + qu);
        System.out.println("borrado entero 2 " + qud);
        em.getTransaction().commit();

    }

}
