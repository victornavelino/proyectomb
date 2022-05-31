/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.CajaJpaController;
import controladores.ClienteJpaController;
import controladores.MovimientoCajaJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.caja.Caja;
import entidades.caja.CobranzaCtaCte;
import entidades.caja.CobroVenta;
import entidades.caja.CuentaCorriente;
import entidades.caja.CuponTarjeta;
import entidades.caja.MovimientoCaja;
import entidades.caja.Gasto;
import entidades.caja.Ingreso;
import entidades.caja.RetiroEfectivo;
import entidades.caja.Sueldo;
import entidades.caja.TipoDeGasto;
import entidades.caja.TipoDeIngreso;
import entidades.cliente.Cliente;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.usuario.Usuario;
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
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class MovimientoCajaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    ClienteJpaController clienteJpaController = new ClienteJpaController(emf);
    MovimientoCajaJpaController movimientoCajaJpaController = new MovimientoCajaJpaController(emf);

    private static MovimientoCajaFacade instance = null;

    protected MovimientoCajaFacade() {
    }

    public static MovimientoCajaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new MovimientoCajaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(MovimientoCaja movimientoCaja) {
        new MovimientoCajaJpaController(emf).create(movimientoCaja);
    }

    public MovimientoCaja buscar(Long id) {
        return new MovimientoCajaJpaController(emf).findMovimientoCaja(id);
    }

    public void modificar(MovimientoCaja movimientoCaja) {
        try {
            movimientoCajaJpaController.edit(movimientoCaja);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovimientoCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MovimientoCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            movimientoCajaJpaController.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovimientoCajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MovimientoCaja> getTodos() {
        return movimientoCajaJpaController.findMovimientoCajaEntities();
    }

    public List<Sueldo> getSueldosAdelantos() {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarSueldos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = Sueldo ORDER BY c.fecha ");
            return quBuscarSueldos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public List<Gasto> getGastos() {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarGastos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = Gasto ORDER BY c.fecha ");
            return quBuscarGastos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public List<Ingreso> getIngresos() {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarGastos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = Ingreso ORDER BY c.fecha ");
            return quBuscarGastos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public List<Gasto> getGastosPorTipo(TipoDeGasto tipoDeGasto, Date fechaInicio) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        System.out.println("fechahha:" + fechaInicio);
        Query quBuscarGastos = ema.createQuery("SELECT g FROM Gasto g WHERE g.tipoDeGasto=:tipoDeGasto AND g.fecha >= :fechaInicio");
        quBuscarGastos.setParameter("tipoDeGasto", tipoDeGasto);
        quBuscarGastos.setParameter("fechaInicio", fechaInicio);
        return quBuscarGastos.getResultList();

    }

    public List<Ingreso> getIngresosPorTipo(TipoDeIngreso tipoDeIngreso, Date fechaInicio) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarGastos = ema.createQuery("SELECT i FROM Ingreso i WHERE i.tipoDeIngreso=:tipoDeIngreso AND i.fecha >= :fechaInicio");
        quBuscarGastos.setParameter("tipoDeIngreso", tipoDeIngreso);
        quBuscarGastos.setParameter("fechaInicio", fechaInicio);
        return quBuscarGastos.getResultList();

    }

    public List<MovimientoCaja> getCajasCobradasDesdeFecha(Date fechaInicio, Sucursal sucursal, Usuario usuario) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarGastos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE c.fecha >= :fechaInicio AND c.sucursal=:sucursal AND c.usuario=:usuario");
        quBuscarGastos.setParameter("fechaInicio", fechaInicio);
        quBuscarGastos.setParameter("sucursal", sucursal);
        quBuscarGastos.setParameter("usuario", usuario);
        return quBuscarGastos.getResultList();
    }

    public List<CuentaCorriente> getCuentasCorrientesDesdeFecha(Date fechaInicio) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CuentaCorriente c WHERE c.fecha >= :fechaInicio");
        quBuscaCCorrientes.setParameter("fechaInicio", fechaInicio);
        return quBuscaCCorrientes.getResultList();
    }

    public List<CuponTarjeta> getCuponesTarjetaDesdeFecha(Date fechaInicio) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CuponTarjeta c WHERE c.fecha >= :fechaInicio");
        quBuscaCCorrientes.setParameter("fechaInicio", fechaInicio);
        return quBuscaCCorrientes.getResultList();
    }

    public List<Gasto> getGastos(Date fechaInicio) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarGastos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = Gasto AND c.fecha >= :fechaInicio ORDER BY c.fecha ");
            quBuscarGastos.setParameter("fechaInicio", fechaInicio);

            return quBuscarGastos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public List<Ingreso> getIngresos(Date fechaInicio) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarGastos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = Ingreso AND c.fecha >= :fechaInicio ORDER BY c.fecha ");
            quBuscarGastos.setParameter("fechaInicio", fechaInicio);

            return quBuscarGastos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }
//    public List<CobranzaCtaCte> getCobranzasConSaldo(Cliente cliente) {
//        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
//        EntityManager ema = emfa.createEntityManager();
//        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.saldoCobranza > 0 AND c.cliente=:cliente");
//        quBuscaCCorrientes.setParameter("cliente", cliente);
//        return quBuscaCCorrientes.getResultList();
//    }

    public List<CobranzaCtaCte> getCobranzasDesdeFecha(Date fechaInicio, Sucursal sucursal, Usuario usuario) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarGastos = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.fecha >= :fechaInicio AND c.sucursal=:sucursal AND c.usuario=:usuario");
        quBuscarGastos.setParameter("fechaInicio", fechaInicio);
        quBuscarGastos.setParameter("sucursal", sucursal);
        quBuscarGastos.setParameter("usuario", usuario);
        return quBuscarGastos.getResultList();
    }

    public List<CobranzaCtaCte> getCobranzas(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.cliente=:cliente");
        quBuscaCCorrientes.setParameter("cliente", cliente);
        return quBuscaCCorrientes.getResultList();
    }

    public BigDecimal getSaldoCobranzas(Cliente cliente) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT SUM(c.importe) FROM CobranzaCtaCte c WHERE c.cliente=:cliente");
        quBuscaCCorrientes.setParameter("cliente", cliente).setMaxResults(0);
        return (BigDecimal) quBuscaCCorrientes.getSingleResult();
    }

    //IDEM AL ANTERIOR PERO CON FECHA
    public List<CobranzaCtaCte> getCobranzasConSaldo(Cliente cliente, Date fechaDesde, Date fechaHasta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.cliente=:cliente AND c.fecha BETWEEN :fechaDesde AND :fechaHasta");
        quBuscaCCorrientes.setParameter("cliente", cliente);
        quBuscaCCorrientes.setParameter("fechaDesde", fechaDesde);
        quBuscaCCorrientes.setParameter("fechaHasta", Comunes.addDate(fechaHasta, Calendar.DAY_OF_YEAR, 1));
        return quBuscaCCorrientes.getResultList();
    }

    //DEVUELVE TODAS LAS COBRANZAS SEGUN FECHA
    public List<CobranzaCtaCte> getCobranzasSegunFecha(Date fechaDesde, Date fechaHasta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.fecha BETWEEN :fechaDesde AND :fechaHasta");
        quBuscaCCorrientes.setParameter("fechaDesde", fechaDesde);
        quBuscaCCorrientes.setParameter("fechaHasta", Comunes.addDate(fechaHasta, Calendar.DAY_OF_YEAR, 1));
        return quBuscaCCorrientes.getResultList();
    }

    public boolean existeNroReciboCobranza(int numero) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscarGastos = ema.createQuery("SELECT c FROM CobranzaCtaCte c WHERE c.numero=:numero");
        quBuscarGastos.setParameter("numero", numero);
        try {
            if (quBuscarGastos.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el numero de recibo de la cobranza");
            return false;
        }

    }

    public List<Sueldo> getSueldosAdelantos(Caja caja) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarSueldos = ema.createQuery("SELECT c FROM Caja ca, IN (ca.movimientosCaja) c WHERE ca=:caja AND TYPE(c) = Sueldo  ORDER BY c.fecha ");
            quBuscarSueldos.setParameter("caja", caja);
            return quBuscarSueldos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public List<Sueldo> getSueldosAdelantos(Date fechaInicio) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarSueldos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE c.fecha >= :fechaInicio AND TYPE(c) = Sueldo  ORDER BY c.fecha ");
            quBuscarSueldos.setParameter("fechaInicio", fechaInicio);
            return quBuscarSueldos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return new ArrayList<>();
        }
    }

    public List<RetiroEfectivo> getRetiroEfectivos(Date fechaInicio) {
        try {
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            Query quBuscarSueldos = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE c.fecha >= :fechaInicio AND TYPE(c) = RetiroEfectivo  ORDER BY c.fecha ");
            quBuscarSueldos.setParameter("fechaInicio", fechaInicio);
            return quBuscarSueldos.getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return new ArrayList<>();
        }
    }

    public List<MovimientoCaja> getVentasCobradasDesdeFecha(Date fechaInicio, Sucursal sucursal, Usuario usuario) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = CobroVenta AND c.fecha >= :fechaInicio AND c.sucursal=:sucursal AND c.usuario=:usuario");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        quBuscar.setParameter("sucursal", sucursal);
        quBuscar.setParameter("usuario", usuario);
        return quBuscar.getResultList();
    }

    public List<CobroVenta> getVentasEnEfectivo(Date fechaInicio, Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM MovimientoCaja c WHERE TYPE(c) = CobroVenta AND c.fecha >= :fechaInicio AND c.sucursal=:sucursal");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        quBuscar.setParameter("sucursal", sucursal);
        return quBuscar.getResultList();
    }
    public String getTotalTarjetaDesdeFecha(Venta venta) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscaCCorrientes = ema.createQuery("SELECT SUM(c.importeCuponConRecargo) FROM CuponTarjeta c WHERE c.venta=:venta");
        quBuscaCCorrientes.setParameter("venta", venta);
        try {
          return quBuscaCCorrientes.getSingleResult().toString();  
        } catch (Exception e) {
          return "0,00";
        }
        
    }

}
