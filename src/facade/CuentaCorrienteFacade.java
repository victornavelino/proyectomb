/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.CuentaCorrienteJpaController;
import controladores.DomicilioJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.caja.CuentaCorriente;
import entidades.cliente.Cliente;
import entidades.localidad.Localidad;
import entidades.persona.Domicilio;
import entidades.venta.Venta;
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

/**
 *
 * @author hugo
 */
public class CuentaCorrienteFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static CuentaCorrienteFacade instance = null;

    protected CuentaCorrienteFacade() {
    }

    public static CuentaCorrienteFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CuentaCorrienteFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(CuentaCorriente cuentaCorriente) {
        new CuentaCorrienteJpaController(emf).create(cuentaCorriente);
    }

    public CuentaCorriente buscar(Long id) {
        return new CuentaCorrienteJpaController(emf).findCuentaCorriente(id);
    }

    public void modificar(CuentaCorriente cuentaCorriente) {
        try {
            new CuentaCorrienteJpaController(emf).edit(cuentaCorriente);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CuentaCorrienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CuentaCorrienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new CuentaCorrienteJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CuentaCorrienteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CuentaCorriente> getTodos() {
        return new CuentaCorrienteJpaController(emf).findCuentaCorrienteEntities();
    }

    public List<CuentaCorriente> getCuentasCCliente(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.cliente=:cliente");
        quBuscar.setParameter("cliente", cliente);
        return quBuscar.getResultList();
    }

    public List<CuentaCorriente> getCuentasCClienteOrdenada(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.cliente=:cliente ORDER BY c.id ASC");
        quBuscar.setParameter("cliente", cliente);
        ema.getEntityManagerFactory().getCache().evictAll();
        return quBuscar.getResultList();
    }

    //IDEM AL ANTERIOR ESPECIFICANDO RANGO DE FECHAS Y CLIENTE
    public List<CuentaCorriente> getCuentasCCliente(Cliente cliente, Date fechaDesde, Date fechaHasta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.cliente=:cliente AND c.fecha BETWEEN :fechaDesde AND :fechaHasta");
        quBuscar.setParameter("cliente", cliente);
        quBuscar.setParameter("fechaDesde", fechaDesde);
        quBuscar.setParameter("fechaHasta", Comunes.addDate(fechaHasta, Calendar.DAY_OF_YEAR, 1));
        return quBuscar.getResultList();
    }

    //IDEM AL ANTERIOR ESPECIFICANDO SOLO RANGO DE FECHAS
    public List<CuentaCorriente> getCuentasCCFechas(Date fechaDesde, Date fechaHasta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.fecha BETWEEN :fechaDesde AND :fechaHasta ");
        quBuscar.setParameter("fechaDesde", fechaDesde);
        quBuscar.setParameter("fechaHasta", Comunes.addDate(fechaHasta, Calendar.DAY_OF_YEAR, 1));
        return quBuscar.getResultList();
    }

    public List<CuentaCorriente> getCuentasCClienteConSaldo(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.cliente=:cliente AND c.saldo>0 ORDER BY c.fecha ASC");
        quBuscar.setParameter("cliente", cliente);
        return quBuscar.getResultList();
    }

    public List<Cliente> getClientesCuentasCorrientes() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT DISTINCT(c.cliente) FROM CuentaCorriente c WHERE NOT c.venta.anulado");
        return quBuscar.getResultList();
    }

//    DEVUELVE LISTADO DE CLIENTES CON CUENTA CORRIENTE
//    public List<Cliente> getClientesCC() {
//        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
//        EntityManager ema = emfa.createEntityManager();
//        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE c.cliente=:cliente");
//        quBuscar.setParameter("cliente", cliente);
//        return quBuscar.getResultList();
//    }
    public CuentaCorriente getCuentaCliente(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.cliente=:cliente ");
        quBuscar.setParameter("cliente", cliente);
        try {
            return (CuentaCorriente) quBuscar.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }

    }

    public CuentaCorriente getCuentaCorrienteTicket(Venta venta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.venta=:venta");
        quBuscar.setParameter("venta", venta);
        try {
            return (CuentaCorriente) quBuscar.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }

    }
//16/12

    public List<Object[]> getSaldosClientes(Cliente cliente) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("Select SUM(s.importeCtaCte), s.cliente from CuentaCorriente s WHERE NOT s.venta.anulado AND s.cliente=:cliente group by s.cliente");
        quBuscar.setParameter("cliente", cliente);
        List<Object[]> listaCuenta = quBuscar.getResultList();
        quBuscar = ema.createQuery("Select SUM(s.importe), s.cliente from CobranzaCtaCte s WHERE  s.cliente=:cliente group by s.cliente");
        quBuscar.setParameter("cliente", cliente);
        List<Object[]> listaCobraza = quBuscar.getResultList();

        try {
            for (Object[] cuentas : listaCuenta) {

                for (Object[] cobranzas : listaCobraza) {
                    cuentas[0] = ((BigDecimal) cuentas[0]).subtract(((BigDecimal) cobranzas[0]));
                }
            }

            return listaCuenta;
        } catch (Exception e) {
            return null;
        }

    }
//16/12

    public List<Object[]> getSaldosClientes() {
        List<Object[]> saldos = new ArrayList<>();
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("Select distinct s.cliente from CuentaCorriente s where NOT s.venta.anulado");
        try {
            List<Cliente> todos = quBuscar.getResultList();
            for (Cliente cliente : todos) {
                saldos.addAll(getSaldosClientes(cliente));
            }

            return saldos;
        } catch (Exception e) {
            return null;
        }

    }

    public BigDecimal getSaldoCCporTicket(int numeroTicket) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("Select s.saldo from CuentaCorriente s WHERE NOT s.venta.anulado AND s.venta.numeroTicket=:numeroTicket ");
        quBuscar.setParameter("numeroTicket", numeroTicket);
        try {
            return new BigDecimal(quBuscar.getSingleResult().toString());
        } catch (Exception e) {
            return null;
        }

    }

    public List<CuentaCorriente> getCuentasCCaja(Date fechaDesde) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE NOT c.venta.anulado AND c.fecha >= :fechaDesde");
        quBuscar.setParameter("fechaDesde", fechaDesde);
        return quBuscar.getResultList();
    }

    public String getImporteCuentaVenta(Venta venta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c.importeCtaCte FROM CuentaCorriente c WHERE c.venta=:venta ");
        quBuscar.setParameter("venta", venta);
        try {
            return quBuscar.getSingleResult().toString();
        } catch (Exception e) {
            return "0,00";
        }

    }

}
