/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import Recursos.Soap;
import Recursos.SoapNoEnviados;
import controladores.CajaJpaController;
import entidades.Sucursal;
import entidades.caja.Caja;
import entidades.caja.CobranzaCtaCte;
import entidades.caja.CobroVenta;
import entidades.caja.CuponTarjeta;
import entidades.caja.MovimientoCaja;
import entidades.usuario.Usuario;
import includes.Comunes;
import includes.Impresora;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import reportes.Reportes;
import vista.Caja.DiagAbrirCaja;
import vista.Caja.DiagResumenCierreCaja;

/**
 *
 * @author franco
 */
public class CajaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    CajaJpaController cajaJpaController = new CajaJpaController(emf);
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    private static CajaFacade instance = null;
    private BigDecimal cajaFinal;

    protected CajaFacade() {
    }

    public static CajaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CajaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public List<Caja> buscarCajaAbierta(Sucursal sucursal) {
        Query quCajaAbierta = null;
        EntityManager em = emf.createEntityManager();
        quCajaAbierta = em.createQuery("SELECT c FROM Caja c  WHERE c.fechaFin IS NULL AND c.sucursal.id = " + sucursal.getId());

        return quCajaAbierta.getResultList();

    }

    public boolean hayCajaAbierta(Sucursal sucursal) {
        boolean respuesta = false;
        List listCajasAbiertas = buscarCajaAbierta(sucursal);
        if (listCajasAbiertas.size() > 0) {
            respuesta = true;
        }
        return respuesta;
    }

//    public TurnoUsuario getTurnoAbierto(Puesto puesto) {
//        TurnoUsuario turnoUsuario = null;
//        if (hayTurnoAbierto(puesto)) {
//            List listTurnoAbierto = buscarTurnoAbierto(puesto);
//            turnoUsuario = (TurnoUsuario) listTurnoAbierto.get(0);
//        }
//        return turnoUsuario;
//    }
//esto se ejecuta siempre que logues con un usuario de caja
    public void abrirCaja(Usuario usuario, Sucursal sucursal) {
        //ENVIAR VENTAS NO FIDELIZADAS
        ReintentarEnviarVentasFidelizadasConError();
        // SI ES DISTINTO DIA, tiene que cerrar el turno 
        if (hayCajaAbierta(sucursal)) {
            Caja cAbierta = getCajaAbierta(sucursal);
            JOptionPane.showMessageDialog(null, "Ya se encuentra abierta una Caja del usuario: " + cAbierta.getUsuario() + "\n "
                    + "El usuario " + cAbierta.getUsuario() + " debe cerrarla para poder abrir otra");
//            String mensaje = "Hay un Turno Abierto, desea cerrarlo?\n";
//            if (getCantidadTurnosAbiertosHoy(puesto) == 0) {
//                mensaje += "El turno abierto es del día anterior, por lo que se recomienda cerrarlo ahora";
//            }
//            if (getCantidadTurnosAbiertosHoy(puesto) == 2) {
//                mensaje += "Solo se pueden abrir dos turnos por día. \n"
//                        + "Si cierra el turno se cerrará el programa "
//                        + "luego de imprimir el informe de cierre de turno.";
//            }
//            int turnoAbierto = JOptionPane.showConfirmDialog(null, mensaje, "Turno Abierto", JOptionPane.ERROR_MESSAGE);
//            if (turnoAbierto == 0) {
//                cerrarTurno(puesto);
//                altaAbrirTurno(usuario, puesto);
//
//            }
        } else {
            altaAbrirCaja(usuario, sucursal);
        }

    }

    public void altaAbrirCaja(Usuario usuario, Sucursal sucursal) {
//        if (getCantidadTurnosAbiertosHoy(puesto) < 2) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        DiagAbrirCaja diagAbrirCaja = new DiagAbrirCaja(null, true);
        diagAbrirCaja.setLocation(Comunes.centrarDialog(diagAbrirCaja));
        diagAbrirCaja.setVisible(true);
        String cadenaCajaInicial = diagAbrirCaja.getCajaInicial();
        if (cadenaCajaInicial != null) {
            while (!Comunes.validarBigDecimal(cadenaCajaInicial)) {
                //revisas que solo sea numero
                JOptionPane.showMessageDialog(null, "Escriba solamente números y . de separador", "ADVERTENCIA", JOptionPane.ERROR_MESSAGE);
                cadenaCajaInicial = JOptionPane.showInputDialog("Ingrese la Caja Inicial");
            }
            BigDecimal cajaInicial = new BigDecimal(cadenaCajaInicial);
            Caja caja = new Caja();
            Date fechaInicio = Comunes.obtenerFechaActualDesdeDB();
            //se me hace un quilombo el exportador al excel para compara la fecha con milisegundos
            String fechaInicioSinMilisegundos = format.format(fechaInicio);
            try {
                caja.setFechaInicio(format.parse(fechaInicioSinMilisegundos));
            } catch (ParseException ex) {
                Logger.getLogger(CajaFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            caja.setUsuario(usuario);
            //movimientosCaja.setTurno(new TurnoFacade().determinarTurno(fechaInicio));
            caja.setSucursal(sucursal);
            caja.setCajaInicial(cajaInicial);
//            movimientosCaja.setSaldoCreditosInicial(new CuentaCorrienteFacade().calcularCreditosPuesto(puesto));
            alta(caja);
//        } else {
//            //si hay mas de dos turnos ya abiertos, muestra mensaje de error y cierra el programa
//            JOptionPane.showMessageDialog(null, "No se puede abrir turno. "
//                    + "Ya se cerraron los dos turnos diarios permitidos\n"
//                    + "Presione Aceptar para cerrar el programa",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//        }
        }
    }

    public void alta(Caja caja) {
        new CajaJpaController(emf).create(caja);
    }

    public void modificar(Caja caja) {
        try {
            new CajaJpaController(emf).edit(caja);
        } catch (Exception ex) {
            Logger.getLogger(CajaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarCaja(Sucursal sucursal, Usuario usuario) {
        if (hayCajaAbierta(sucursal, usuario)) {
            int cantidadVentasSinCobrar = VentaFacade.getInstance().cantidadVentasSinCobrarCajaActual(sucursal, getCajaAbierta(sucursal, usuario));
            if (cantidadVentasSinCobrar == 0) {
                try {
                    /* Recupero la caja abierta */
                    Caja caja = getCajaAbierta(sucursal, usuario);
                    /* Calculo los valores a setear */

                    caja.setMovimientosCtaCte(MovimientoCajaFacade.getInstance().getCuentasCorrientesDesdeFecha(caja.getFechaInicio()));
                    caja.setMovimientosCuponesTarjeta(MovimientoCajaFacade.getInstance().getCuponesTarjetaDesdeFecha(caja.getFechaInicio()));
                    caja.setMovimientosCaja(MovimientoCajaFacade.getInstance().getCajasCobradasDesdeFecha(caja.getFechaInicio(), sucursal, caja.getUsuario()));
                    caja.setFechaFin(Comunes.obtenerFechaActualDesdeDB());
                    //cambio el estado de las cobranzas asi no se puedan borrar
                    List<CobranzaCtaCte> cobranzasDesdeFecha = MovimientoCajaFacade.getInstance()
                            .getCobranzasDesdeFecha(caja.getFechaInicio(),
                                    sucursal, caja.getUsuario());
                    for (CobranzaCtaCte ctacte : cobranzasDesdeFecha) {
                        ctacte.setCerrado(true);
                        MovimientoCajaFacade.getInstance().modificar(ctacte);
                    }

                    // cierro caja
                    cajaFinal = getSaldoFinalCaja(caja);
                    caja.setCajaFinal(cajaFinal);
                    //abrimos dialog con resumen para que decida si cerrar
                    DiagResumenCierreCaja diagResumenCierreCaja = new DiagResumenCierreCaja(null, true, caja);
                    diagResumenCierreCaja.setLocation(Comunes.centrarDialog(diagResumenCierreCaja));
                    diagResumenCierreCaja.setVisible(true);
                    if (diagResumenCierreCaja.isCerrarCaja()) {
                        caja = diagResumenCierreCaja.getCaja();
                        //cerramos los cobros de venta para que no se puedan anular o cambiar forma de pago o modificar
                        for (MovimientoCaja movimientoCaja : MovimientoCajaFacade.getInstance().getVentasCobradasDesdeFecha(caja.getFechaInicio(), sucursal, caja.getUsuario())) {
                            movimientoCaja.setCerrado(true);
                            MovimientoCajaFacade.getInstance().modificar(movimientoCaja);
                        }

                        modificar(caja);
                        // Muestro el reporte
                        new Impresora().imprimir(caja);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se puede Cerrar Caja, existen " + cantidadVentasSinCobrar + " tickets sin cobrar", "Advertencia", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No existe caja abierta para cerrar", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
//
    // TODO: Fijate si hace falta mandarle la fecha también, lo que pasa es que supuestamente solo estaría en blanco la fechaFinal del turno que sigue abierto hoy

    public Caja getCajaAbierta(Sucursal sucursal, Usuario usuario) {
        Caja caja = null;
        EntityManager em = emf.createEntityManager();
        Query quCajaAbierta = em.createQuery("SELECT c FROM Caja c WHERE c.sucursal= :sucursal AND c.usuario = :usuario AND c.fechaFin IS NULL");
        quCajaAbierta.setParameter("usuario", usuario);
        quCajaAbierta.setParameter("sucursal", sucursal);
        List listaCajasAbiertas = quCajaAbierta.getResultList();
        if (listaCajasAbiertas.size() > 0) {
            caja = (Caja) listaCajasAbiertas.get(listaCajasAbiertas.size() - 1);
        }
        return caja;
    }

    public Caja getCajaAbierta(Sucursal sucursal) {
        Caja caja = null;
        EntityManager em = emf.createEntityManager();
        Query quCajaAbierta = em.createQuery("SELECT c FROM Caja c WHERE c.sucursal= :sucursal AND c.fechaFin IS NULL");
        quCajaAbierta.setParameter("sucursal", sucursal);
        List listaCajasAbiertas = quCajaAbierta.getResultList();
        if (listaCajasAbiertas.size() > 0) {
            caja = (Caja) listaCajasAbiertas.get(listaCajasAbiertas.size() - 1);
        }
        return caja;
    }

    public boolean hayCajaAbiertaAyer(Sucursal sucursal) {
        boolean flag = false;
        Caja caja;
        Date fechaActual = Comunes.obtenerFechaActualDesdeDB();
        String fechaSistema = formateador.format(fechaActual);
        EntityManager em = emf.createEntityManager();
        Query quCajaAbierta = em.createQuery("SELECT c FROM Caja c WHERE c.sucursal= :sucursal AND c.fechaFin IS NULL");
        quCajaAbierta.setParameter("sucursal", sucursal);
        List listaCajasAbiertas = quCajaAbierta.getResultList();
        if (listaCajasAbiertas.size() > 0) {
            caja = (Caja) listaCajasAbiertas.get(listaCajasAbiertas.size() - 1);
            //comparamos la fecha de inicio de la caja con lafecha actual
            // del sistema para ver si es de ayer o mas vieja!
            try {
                String fechaAperturaCaja = formateador.format(caja.getFechaInicio());
                Date fechaAperCaja = formateador.parse(fechaAperturaCaja);
                Date fechaAhora = formateador.parse(fechaSistema);
                if (fechaAperCaja.before(fechaAhora)) {
                    flag = true;
                }
            } catch (Exception e) {

            }

        } else {
            flag = false;
        }
        return flag;
    }

    public boolean hayCajaAbierta(Sucursal sucursal, Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        Query quCajaAbierta = em.createQuery("SELECT c FROM Caja c  WHERE c.fechaFin IS NULL AND c.sucursal=:sucursal AND c.usuario=:usuario");
        quCajaAbierta.setParameter("sucursal", sucursal);
        quCajaAbierta.setParameter("usuario", usuario);
        return !quCajaAbierta.getResultList().isEmpty();

    }

    public BigDecimal getSaldoFinalCaja(Caja caja) {
        caja.setMovimientosCtaCte(MovimientoCajaFacade.getInstance().getCuentasCorrientesDesdeFecha(caja.getFechaInicio()));
        caja.setMovimientosCuponesTarjeta(MovimientoCajaFacade.getInstance().getCuponesTarjetaDesdeFecha(caja.getFechaInicio()));

        caja.setMovimientosCaja(MovimientoCajaFacade.getInstance().getCajasCobradasDesdeFecha(caja.getFechaInicio(), caja.getSucursal(), caja.getUsuario()));

        BigDecimal cobranzaCtaCte = new BigDecimal("0.000");
        BigDecimal cobroVenta = new BigDecimal("0.000");
        BigDecimal gasto = new BigDecimal("0.000");
        BigDecimal ingreso = new BigDecimal("0.000");
        BigDecimal sueldo = new BigDecimal("0.000");
        BigDecimal saldoFinalCaja = new BigDecimal("0.000");
        BigDecimal totalIngresosCaja = new BigDecimal("0.000");
        BigDecimal totalEgresosCaja = new BigDecimal("0.000");

        // SEPARAMOS LO INGRESOS  Y EGRESOS DE CAJA
        List<MovimientoCaja> ingresosCaja = new ArrayList<>();
        List<MovimientoCaja> egresosCaja = new ArrayList<>();
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobranzaCtaCte":
                    ingresosCaja.add(movimientoCaja);
                    cobranzaCtaCte = cobranzaCtaCte.add(movimientoCaja.getImporte());
                    break;
                case "Ingreso":
                    ingresosCaja.add(movimientoCaja);
                    ingreso = ingreso.add(movimientoCaja.getImporte());
                    break;
                case "CobroVenta":
                    ingresosCaja.add(movimientoCaja);
                    cobroVenta = cobroVenta.add(movimientoCaja.getImporte());
                    break;
                case "Gasto":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "RetiroEfectivo":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "Sueldo":
                    egresosCaja.add(movimientoCaja);
                    sueldo = sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        totalIngresosCaja = cobranzaCtaCte.add(cobroVenta).add(ingreso);
        totalEgresosCaja = sueldo.add(gasto);
        saldoFinalCaja = caja.getCajaInicial().add(totalIngresosCaja).subtract(totalEgresosCaja);
        return saldoFinalCaja;
    }

    public BigDecimal getSaldoFinalCaja_Reporte(Caja caja) {

        BigDecimal cobranzaCtaCte = new BigDecimal("0.000");
        BigDecimal cobroVenta = new BigDecimal("0.000");
        BigDecimal gasto = new BigDecimal("0.000");
        BigDecimal ingreso = new BigDecimal("0.000");
        BigDecimal sueldo = new BigDecimal("0.000");
        BigDecimal saldoFinalCaja = new BigDecimal("0.000");
        BigDecimal totalIngresosCaja = new BigDecimal("0.000");
        BigDecimal totalEgresosCaja = new BigDecimal("0.000");

        // SEPARAMOS LO INGRESOS  Y EGRESOS DE CAJA
        List<MovimientoCaja> ingresosCaja = new ArrayList<>();
        List<MovimientoCaja> egresosCaja = new ArrayList<>();
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobranzaCtaCte":
                    ingresosCaja.add(movimientoCaja);
                    cobranzaCtaCte = cobranzaCtaCte.add(movimientoCaja.getImporte());
                    break;
                case "Ingreso":
                    ingresosCaja.add(movimientoCaja);
                    ingreso = ingreso.add(movimientoCaja.getImporte());
                    break;
                case "CobroVenta":
                    ingresosCaja.add(movimientoCaja);
                    cobroVenta = cobroVenta.add(movimientoCaja.getImporte());
                    break;
                case "Gasto":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "RetiroEfectivo":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "Sueldo":
                    egresosCaja.add(movimientoCaja);
                    sueldo = sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        totalIngresosCaja = cobranzaCtaCte.add(cobroVenta).add(ingreso);
        totalEgresosCaja = sueldo.add(gasto);
        saldoFinalCaja = caja.getCajaInicial().add(totalIngresosCaja).subtract(totalEgresosCaja);
        return saldoFinalCaja;
    }

    public BigDecimal getCobroVentas(Caja caja) {
        BigDecimal cobroVenta = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobroVenta":
                    cobroVenta = cobroVenta.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return cobroVenta;
    }

    public BigDecimal getCobranazaCtaCteCaja(Caja caja) {
        BigDecimal cobranzaCtaCte = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobranzaCtaCte":
                    cobranzaCtaCte = cobranzaCtaCte.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return cobranzaCtaCte;
    }

    public BigDecimal getIngresosVarios(Caja caja) {
        BigDecimal Ingreso = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "Ingreso":
                    Ingreso = Ingreso.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return Ingreso;
    }

    public BigDecimal getSueldosyAdelantos(Caja caja) {
        BigDecimal Sueldo = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "Sueldo":
                    Sueldo = Sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return Sueldo;
    }

    public BigDecimal getGastos(Caja caja) {
        BigDecimal Gasto = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "Gasto":
                    Gasto = Gasto.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return Gasto;
    }

    public BigDecimal getTotalIngresos(Caja caja) {

        BigDecimal cobranzaCtaCte = new BigDecimal("0.000");
        BigDecimal cobroVenta = new BigDecimal("0.000");
        BigDecimal gasto = new BigDecimal("0.000");
        BigDecimal ingreso = new BigDecimal("0.000");
        BigDecimal sueldo = new BigDecimal("0.000");
        BigDecimal saldoFinalCaja = new BigDecimal("0.000");
        BigDecimal totalIngresosCaja = new BigDecimal("0.000");
        BigDecimal totalEgresosCaja = new BigDecimal("0.000");

        // SEPARAMOS LO INGRESOS  Y EGRESOS DE CAJA
        List<MovimientoCaja> ingresosCaja = new ArrayList<>();
        List<MovimientoCaja> egresosCaja = new ArrayList<>();
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobranzaCtaCte":

                    ingresosCaja.add(movimientoCaja);
                    cobranzaCtaCte = cobranzaCtaCte.add(movimientoCaja.getImporte());
                    break;
                case "Ingreso":
                    ingresosCaja.add(movimientoCaja);
                    ingreso = ingreso.add(movimientoCaja.getImporte());
                    break;
                case "CobroVenta":
                    ingresosCaja.add(movimientoCaja);
                    cobroVenta = cobroVenta.add(movimientoCaja.getImporte());
                    break;
                case "Gasto":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "Sueldo":
                    egresosCaja.add(movimientoCaja);
                    sueldo = sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        //ESTE SEGMENTO BUSCA LAS CUENTAS CORRIENTES ABONADAS CON TARJETAS..Y LAS RESTA DEL TOTAL DE INGRESO DE CAJA
        //YA QUE LAS MISMAS NO SON EFECTIVO
        BigDecimal cuponesCtaCteConTarjeta = new BigDecimal("0.000");
        for (CuponTarjeta cupon : CuponTarjetaFacade.getInstance().getCuponesTarjetaCtaCteCaja(caja.getFechaInicio())) {
            cuponesCtaCteConTarjeta = cuponesCtaCteConTarjeta.add(cupon.getImporteCuponConRecargo());
        }
        //EN EL ULTIMO SUBTRACT SE HACE
        totalIngresosCaja = cobranzaCtaCte.add(cobroVenta).add(ingreso).subtract(cuponesCtaCteConTarjeta);
        totalEgresosCaja = sueldo.add(gasto);
        saldoFinalCaja = caja.getCajaInicial().add(totalIngresosCaja).subtract(totalEgresosCaja);
        return totalIngresosCaja;
    }

    public BigDecimal getTotalEgresos(Caja caja) {

        BigDecimal cobranzaCtaCte = new BigDecimal("0.000");
        BigDecimal cobroVenta = new BigDecimal("0.000");
        BigDecimal gasto = new BigDecimal("0.000");
        BigDecimal ingreso = new BigDecimal("0.000");
        BigDecimal sueldo = new BigDecimal("0.000");
        BigDecimal saldoFinalCaja = new BigDecimal("0.000");
        BigDecimal totalIngresosCaja = new BigDecimal("0.000");
        BigDecimal totalEgresosCaja = new BigDecimal("0.000");

        // SEPARAMOS LO INGRESOS  Y EGRESOS DE CAJA
        List<MovimientoCaja> ingresosCaja = new ArrayList<>();
        List<MovimientoCaja> egresosCaja = new ArrayList<>();
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "CobranzaCtaCte":
                    ingresosCaja.add(movimientoCaja);
                    cobranzaCtaCte = cobranzaCtaCte.add(movimientoCaja.getImporte());
                    break;
                case "Ingreso":
                    ingresosCaja.add(movimientoCaja);
                    ingreso = ingreso.add(movimientoCaja.getImporte());
                    break;
                case "CobroVenta":
                    ingresosCaja.add(movimientoCaja);
                    cobroVenta = cobroVenta.add(movimientoCaja.getImporte());
                    break;
                case "Gasto":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;

                case "RetiroEfectivo":
                    egresosCaja.add(movimientoCaja);
                    gasto = gasto.add(movimientoCaja.getImporte());
                    break;
                case "Sueldo":
                    egresosCaja.add(movimientoCaja);
                    sueldo = sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        totalIngresosCaja = cobranzaCtaCte.add(cobroVenta).add(ingreso);
        totalEgresosCaja = sueldo.add(gasto);
        saldoFinalCaja = caja.getCajaInicial().add(totalIngresosCaja).subtract(totalEgresosCaja);
        return totalEgresosCaja;
    }

    public Caja getUltimoNumeroCierreCaja() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE c.fechaFin IS NOT NULL ORDER BY c.id DESC");
        quBuscar.setMaxResults(1);
        try {
            return (Caja) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUltimoSaldoCaja() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c.cajaFinal FROM Caja c WHERE c.fechaFin IS NOT NULL ORDER BY c.id DESC");
        quBuscar.setMaxResults(1);
        try {
            return quBuscar.getSingleResult().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public Caja getCajaCerrada(Long id) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE c.id=:id AND c.fechaFin IS NOT NULL");
        quBuscar.setParameter("id", id);
        try {
            return (Caja) quBuscar.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<Caja> getCajasCerradas(Sucursal sucursal) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE c.sucursal=:sucursal AND c.fechaFin IS NOT NULL");
        quBuscar.setParameter("sucursal", sucursal);
        return quBuscar.getResultList();

    }

    public List<Caja> getCajasCerradas() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE c.fechaFin IS NOT NULL");
        return quBuscar.getResultList();

    }

    public Object getRetirosEfectivo(Caja caja) {
        BigDecimal Gasto = new BigDecimal("0.000");
        for (MovimientoCaja movimientoCaja : caja.getMovimientosCaja()) {

            switch (movimientoCaja.getClass().getSimpleName()) {
                case "RetiroEfectivo":
                    Gasto = Gasto.add(movimientoCaja.getImporte());
                    break;
            }

        }
        return Gasto;
    }

    public boolean existeCajaSucursal(Date fechaInicio, String codigoSucursal, String nombreUsuario) {
        boolean flag;
        System.out.println("fechaniniico: " + fechaInicio);
        System.out.println("codigosucursal: " + codigoSucursal);
        System.out.println("nobreusuario: " + nombreUsuario);
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE c.fechaInicio = :fechaInicio AND c.sucursal.codigo=:codigoSucursal AND c.usuario.nombreUsuario=:nombreUsuario");
        quBuscar.setParameter("fechaInicio", fechaInicio);
        quBuscar.setParameter("codigoSucursal", codigoSucursal);
        quBuscar.setParameter("nombreUsuario", nombreUsuario);
        return flag = !quBuscar.getResultList().isEmpty();

    }

    public List<Caja> findCajaByFechaCierreSucursal(Date fechaIni, Date fechaFin, Sucursal sucursal) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE "
                + "c.sucursal.id=:idSucursal AND "
                + "c.fechaFin Between :fechaIni AND :fechaFin");

        if (sucursal != null) {
            quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE "
                    + "c.sucursal.id=:idSucursal AND "
                    + "c.fechaFin Between :fechaIni AND :fechaFin");
            quBuscar.setParameter("idSucursal", sucursal.getId());
        } else {
            quBuscar = ema.createQuery("SELECT c FROM Caja c WHERE "
                    + "c.fechaFin Between :fechaIni AND :fechaFin");
        }

        quBuscar.setParameter("fechaIni", fechaIni);
        quBuscar.setParameter("fechaFin", fechaFin);

        return quBuscar.getResultList();
    }

    private void ReintentarEnviarVentasFidelizadasConError() {
        Runnable run = new SoapNoEnviados();
        Thread thread = new Thread(run);
        thread.start();
    }

}
