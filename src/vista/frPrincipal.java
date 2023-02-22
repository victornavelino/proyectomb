/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * Principal.java
 *
 * Created on 06/10/2009, 22:33:56
 */
package vista;

//import entidades.persona.investigador.Investigador;
// entidades.proyecto.Proyecto;
import Recursos.sincro.SincronizaCliente;
import entidades.Configuracion;
import entidades.Paneles;
import entidades.Sucursal;
import entidades.articulo.ListaPrecio;
import entidades.caja.Caja;
import entidades.caja.TarjetaDeCredito;
import entidades.caja.TipoDeFactura;
import entidades.caja.TipoDeGasto;
import entidades.caja.TipoDeIngreso;
import entidades.cliente.Cliente;
import entidades.empleado.TipoEmpleado;
import entidades.localidad.Provincia;
import entidades.persona.TipoDocumento;
import entidades.persona.TipoTelefono;
import entidades.usuario.Usuario;
import entidades.venta.CierreVentas;
import facade.CajaFacade;
import facade.CierreVentasFacade;
import facade.ClienteFacade;
import facade.ConexionFacade;
import facade.ConfiguracionFacade;
import facade.ListaPrecioFacade;
import facade.SucursalFacade;
import facade.VentaFacade;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import includes.Comunes;
import includes.ImagenFondoCentrada;
import includes.Impresora;
import includes.diagCargando;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import vista.Caja.ABMPlanTarjeta;
import vista.Caja.DiagCobroTicket2;
import vista.Caja.DiagCuentasCorrientes;
import vista.Caja.DiagDetalleVenta;
import vista.Caja.DiagExportarImportarCaja;
import vista.Caja.DiagGastos;
import vista.Caja.DiagIngreso;
import vista.Caja.DiagListadoCobroTarjeta;
import vista.Caja.DiagModificacionCobroVentas;
import vista.Caja.DiagReimprimirCierreCaja;
import vista.Caja.DiagReporte_detalleCaja;
import vista.Caja.DiagReporte_resumenCaja;
import vista.Caja.DiagRetiroEfectivo;
import vista.Caja.DiagSueldosAdelantos;
import vista.Grupos.DiagPermisoMenu;
import vista.inventario.FrInventario;
import vista.TipoIva.DiagTipoIva;
import vista.articulos.DiagAdminArticulos;
import vista.articulos.DiagAsignarPrecios;
import vista.articulos.DiagBuscarArticuloMenu;
import vista.articulos.DiagCopiarPrecios;
import vista.articulos.DiagExportarImportarArticulos;
import vista.cliente.DiagCliente;
import vista.cliente.DiagClienteServer;
import vista.cliente.DiagImportarExportarClientes;
import vista.configuracion.diagConfiguracion;
import vista.empleados.DiagEmpleado;
import vista.generico.DiagGenerico;
import vista.inventario.DiagListadoInventario;
import vista.inventario.movimientoInterno.DiagAdministrarMovimientosInternos;
import vista.inventario.movimientoInterno.DiagAltaMovimientosInternos;
import vista.promocion.DiagAdminPromociones;
import vista.promocion.DiagCopiarPromocion;
import vista.rinde.DiagVentasSinPromo;
import vista.sucursal.DiagBuscarSucursal;
import vista.sucursal.DiagSucursalAlta;
import vista.sucursal.DiagSucursalModificacion;
import vista.usuarios.DiagUsuarioAlta;
import vista.usuarios.DiagUsuarioModificacion;
import vista.ventas.DiagAnularVenta;
import vista.ventas.DiagExportarImportarCierreVentas;
import vista.ventas.DiagExportarImportarVentas;
import vista.ventas.DiagListaOrdenDeCompra;
import vista.ventas.DiagListadoVentas;
import vista.ventas.DiagReimprimirCierre;
import vista.ventas.DiagReimprimirTicket;
import vista.ventas.DialogEntregas;
import vista.ventas.FrVentas;
//import vistas.inventario.pedidoMedias.frPedidoDeMedias;

/**
 *
 * @author loschangos
 */
public class frPrincipal extends javax.swing.JFrame implements SerialPortEventListener {

    // Atributos
    private Usuario usuario;
    private Sucursal sucursal;
    private String mensajeSupervision;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Timer timer = new Timer(60000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Date fechaServidor = new Date();
            lbHoraDelServidor.setText(dateFormat.format(fechaServidor));
        }
    });
    private frPrincipal frame;
    private String mensajeProyecto;
    private String mensajeIncentivo;
    private String mensajeBeca;
    private String mensajeCategorizacion;
    private String mensajeVinculacion;
    private String mensajeEconomico;
    private String entradas;
    private String salidas;
    //variables nuevas
    static CommPortIdentifier portId;
    static CommPortIdentifier puertoEncontrado;
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    boolean portFound = false;
    String defaultPort;
    private Thread someThread;
    public static String peso;
    public static Boolean conectadoALaCentral = false;
    private int i;
    private int numBytes;
    public static KeyboardFocusManager manager;
    public static BigDecimal descuentoEmpleado;
    private String nombrePuertoBalanza;

    public frPrincipal(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        leerConfiguracion();
        leerConfiguracionDescuento();
        leerConfiguracionPuertoBalanza();
        //imagenFondo();
        expandirTaskPanels(Boolean.FALSE);
        llenarTaskPanels(this);
        cargarMenuesSegunPermiso();
        // TODO: Agregar la validacion aqui
        inicializarComponentes();
        comprobarClientesDuplicados();

        // sincronizar();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void llenarTaskPanels(frPrincipal jFrame) {
        cargarContadorMensajes();
        this.frame = jFrame;
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Config");
                putValue(Action.SHORT_DESCRIPTION, "Config");
            }

            public void actionPerformed(ActionEvent e) {
                diagConfiguracion configuracion = new diagConfiguracion();
                configuracion.setLocation(Comunes.centrarDialog(configuracion));
                configuracion.setVisible(true);
            }
        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de IVA");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de IVA");
            }

            public void actionPerformed(ActionEvent e) {
                DiagTipoIva diagTipoIva = new DiagTipoIva(frame, true);
                diagTipoIva.setLocation(Comunes.centrarDialog(diagTipoIva));
                diagTipoIva.setVisible(true);
            }
        });

        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Empleado");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Empleado");
            }

            public void actionPerformed(ActionEvent e) {

                DiagGenerico diagGenericoTipoIva = new DiagGenerico(frame, true, "Titulo", "Tipo de Empleado", TipoEmpleado.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoIva.setVisible(true);
            }
        });

        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Provincia");
                putValue(Action.SHORT_DESCRIPTION, "Provincia");
            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoIva = new DiagGenerico(frame, true, "Titulo", "Provincia", Provincia.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoIva.setVisible(true);
            }
        });

        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Documento");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Documento");
            }

            public void actionPerformed(ActionEvent e) {

                DiagGenerico diagGenericoTipoIva = new DiagGenerico(frame, true, "Titulo", "Tipo de Documento", TipoDocumento.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoIva.setVisible(true);
            }
        });

        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Telefono");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Telefono");
            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoIva = new DiagGenerico(frame, true, "Titulo", "Tipo de Telefono", TipoTelefono.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoIva.setVisible(true);
            }
        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de lista de precios");
                putValue(Action.SHORT_DESCRIPTION, "Agregar un nuevo tipo de lista de precios");
            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoIva = new DiagGenerico(frame, true, "Listas de Precio", "Tipo de lista de precios", ListaPrecio.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoIva.setVisible(true);
            }
        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Permisos de Grupo");
                putValue(Action.SHORT_DESCRIPTION, "Agregar permisos de acceso al menu");
            }

            public void actionPerformed(ActionEvent e) {
                DiagPermisoMenu diagPermisoMenu = new DiagPermisoMenu(frame, true);
                diagPermisoMenu.setLocation(Comunes.centrarDialog(diagPermisoMenu));
                diagPermisoMenu.setVisible(true);
            }
        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Egreso");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Egreso");

            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoDeGasto = new DiagGenerico(frame, true, "Titulo", "Tipo de Egreso", TipoDeGasto.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoDeGasto.setVisible(true);
            }

        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Ingreso");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Ingreso");

            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoDeGasto = new DiagGenerico(frame, true, "Titulo", "Tipo de Ingreso", TipoDeIngreso.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoDeGasto.setVisible(true);
            }

        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tarjeta de Credito");
                putValue(Action.SHORT_DESCRIPTION, "Tarjeta de Credito");

            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTarjetaDeCredito = new DiagGenerico(frame, true, "Titulo", "Tarjeta de Credito", TarjetaDeCredito.class);

                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTarjetaDeCredito.setVisible(true);
            }

        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "ABM Plan Tarjeta");
                putValue(Action.SHORT_DESCRIPTION, "ABM Plan Tarjeta");

            }

            public void actionPerformed(ActionEvent e) {
                ABMPlanTarjeta planTarjeta = new ABMPlanTarjeta(frame, true, usuario);
                planTarjeta.setLocation(Comunes.centrarDialog(planTarjeta));
                planTarjeta.setVisible(true);
            }

        });
        jXTaskPnConfiguracion.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Tipo de Factura");
                putValue(Action.SHORT_DESCRIPTION, "Tipo de Factura");

            }

            public void actionPerformed(ActionEvent e) {
                DiagGenerico diagGenericoTipoDeFactura = new DiagGenerico(frame, true, "Titulo", "Tipo de Factura", TipoDeFactura.class);
                //diagGenericoTipoIva.setLocation(Comunes.centrarDialog(administrarEconomicoProyectos));
                diagGenericoTipoDeFactura.setVisible(true);
            }

        });
//USUARIOS
        jXTaskPnUsuarios.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Alta de Usuarios");
                putValue(Action.SHORT_DESCRIPTION, "Alta de Usuarios");

            }

            public void actionPerformed(ActionEvent e) {
                DiagUsuarioAlta usuarioAlta = new DiagUsuarioAlta(frame, true, "Alta");
                usuarioAlta.setLocation(Comunes.centrarDialog(usuarioAlta));
                usuarioAlta.setVisible(true);

            }
        });

        jXTaskPnUsuarios.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Modificación de Usuarios");
                putValue(Action.SHORT_DESCRIPTION, "Modificación de Usuarios");

            }

            public void actionPerformed(ActionEvent e) {
                DiagUsuarioModificacion usuarioModificacion = new DiagUsuarioModificacion();
                usuarioModificacion.setLocation(Comunes.centrarDialog(usuarioModificacion));
                usuarioModificacion.setVisible(true);
            }
        });

        //SUCURSAL
        jXTaskPnSucursal.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Alta de Sucursales");
                putValue(Action.SHORT_DESCRIPTION, "Alta de Sucursales");

            }

            public void actionPerformed(ActionEvent e) {
                DiagSucursalAlta sucursalAlta = new DiagSucursalAlta(frame, true, "Alta");
                sucursalAlta.setLocation(Comunes.centrarDialog(sucursalAlta));
                sucursalAlta.setVisible(true);

            }
        });

        jXTaskPnSucursal.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Modificación de Datos");
                putValue(Action.SHORT_DESCRIPTION, "Modificación de Datos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagSucursalModificacion sucursalModificacion = new DiagSucursalModificacion(frame, true, "Modificacion de Datos");
                sucursalModificacion.setLocation(Comunes.centrarDialog(sucursalModificacion));
                sucursalModificacion.setVisible(true);
            }
        });
        //CLIENTE
        //jXTaskPnCliente.setExpanded(false);
        //EMPLEADOS
        jXTaskPnEmpleados.add(new AbstractAction() {
            {
                putValue(Action.NAME, "ABM Empleados");
                putValue(Action.SHORT_DESCRIPTION, "ABM Empleados");

            }

            public void actionPerformed(ActionEvent e) {
                DiagEmpleado diagEmpleado = new DiagEmpleado(frame, true, "Alta");
                diagEmpleado.setLocation(Comunes.centrarDialog(diagEmpleado));
                diagEmpleado.setVisible(true);
            }

        });

        jXTaskPaneArticulos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Articulos");
                putValue(Action.SHORT_DESCRIPTION, "Articulos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAdminArticulos diagAdminArticulos = new DiagAdminArticulos(frame, true);
                diagAdminArticulos.setLocation(Comunes.centrarDialog(diagAdminArticulos));
                diagAdminArticulos.setVisible(true);
            }

        });
        jXTaskPaneArticulos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Consultar Precio");
                putValue(Action.SHORT_DESCRIPTION, "Consultar Precio");

            }

            public void actionPerformed(ActionEvent e) {
                DiagBuscarArticuloMenu buscarArticuloMenu = new DiagBuscarArticuloMenu(frame, false, sucursal,ListaPrecioFacade.getInstance().getPorDescripcion("COMUN"));
                buscarArticuloMenu.setLocation(Comunes.centrarDialog(buscarArticuloMenu));
                buscarArticuloMenu.setVisible(true);
            }

        });
        jXTaskPaneArticulos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Asignar Precios");
                putValue(Action.SHORT_DESCRIPTION, "Asignar Precio a los Articulos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAsignarPrecios asignarPrecios = new DiagAsignarPrecios(frame, true, sucursal, sucursal);
                asignarPrecios.setLocation(Comunes.centrarDialog(asignarPrecios));
                asignarPrecios.setVisible(true);
            }

        });
        jXTaskPaneArticulos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Promociones");
                putValue(Action.SHORT_DESCRIPTION, "Administrar Promociones");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAdminPromociones adminPromociones = new DiagAdminPromociones(frame, true, sucursal);
                adminPromociones.setLocation(Comunes.centrarDialog(adminPromociones));
                adminPromociones.setVisible(true);
            }

        });

        jXTaskPaneArticulos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Exportar/Importar");
                putValue(Action.SHORT_DESCRIPTION, "Exportar/Importar Articulos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagExportarImportarArticulos ExportarImportar = new DiagExportarImportarArticulos(frame, true);
                ExportarImportar.setLocation(Comunes.centrarDialog(ExportarImportar));
                ExportarImportar.setVisible(true);
            }

        });

        /* jXTaskPaneArticulos.add(new AbstractAction() {
         {
         putValue(Action.NAME, "Promociones Importar/Exportar");
         putValue(Action.SHORT_DESCRIPTION, "Promociones Importar/Exportar");

         }

         public void actionPerformed(ActionEvent e) {
         DiagImportarExportarPromociones diagImpExpPro = new DiagImportarExportarPromociones(frame, true);
         diagImpExpPro.setLocation(Comunes.centrarDialog(diagImpExpPro));
         diagImpExpPro.setVisible(true);
         }

         });*/
        jXTaskPnCliente.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Administración de Clientes");
                putValue(Action.SHORT_DESCRIPTION, "Administración de Clientes");

            }

            public void actionPerformed(ActionEvent e) {
                //SI ES LA CENTRAL QUE UTILICE LA CONEXION LOCALHOST
                conectadoALaCentral = true;
                if (sucursal.getNombre().contains("CENTRAL")) {

                    DiagClienteServer diagClienteServer = new DiagClienteServer(frame, true, "Alta", sucursal);
                    diagClienteServer.setLocation(Comunes.centrarDialog(diagClienteServer));
                    diagClienteServer.setVisible(true);
                } else {
                    //SI ES UNA SUCURAL QUE HAGA LA CONEXION REMOTA A LA CENTRAL
                    try {
                        ClienteFacade.getInstance().probarConexionALaCentral();
                        System.err.println("Conectado a la centrallll:::: " + conectadoALaCentral);
                        if (conectadoALaCentral) {
                            DiagCliente diagCliente = new DiagCliente(frame, true, "Alta", sucursal);
                            diagCliente.setLocation(Comunes.centrarDialog(diagCliente));
                            diagCliente.setVisible(true);
                            conectadoALaCentral = false;
                        } else {

                            JOptionPane.showMessageDialog(null, "No se puede Agregar clientes \n No se puede conectar con la central \n"
                                    + "Intene nuevamente mas tarde.");
                            conectadoALaCentral = false;
                        }

                    } catch (Exception ex) {
                        System.err.println("SALE POR EL CATCH DE LA EXEPTCION");
                        JOptionPane.showMessageDialog(null, "No se puede Agregar clientes \n No se puede conectar con la central \n"
                                + "Intene nuevamente mas tarde.");
                        conectadoALaCentral = false;
                    }
                    conectadoALaCentral = false;
                }

            }

        });
        jXTaskPnCliente.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Importar Exportar Clientes");
                putValue(Action.SHORT_DESCRIPTION, "Importar Exportar Clientes");

            }

            public void actionPerformed(ActionEvent e) {
                DiagImportarExportarClientes diagImpExp = new DiagImportarExportarClientes(frame, true);
                diagImpExp.setLocation(Comunes.centrarDialog(diagImpExp));
                diagImpExp.setVisible(true);
            }

        });

        //VENTAS
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Ventas");
                putValue(Action.SHORT_DESCRIPTION, "Ventas");

            }

            public void actionPerformed(ActionEvent e) {

                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal);
                if (cajaAbierta != null) {
                    final FrVentas frVentas = new FrVentas(serialPort, usuario, sucursal);
                    frVentas.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja");
                }

            }

        });
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Anular Ventas");
                putValue(Action.SHORT_DESCRIPTION, "Anular Ventas");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAnularVenta diagAnulVentas = new DiagAnularVenta(frame, true);
                diagAnulVentas.setLocation(Comunes.centrarDialog(diagAnulVentas));
                diagAnulVentas.setVisible(true);
            }

        });
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Realizar Cierre");
                putValue(Action.SHORT_DESCRIPTION, "Realizar Cierre");

            }

            public void actionPerformed(ActionEvent e) {
                int cantidadVtasSinCobrar = VentaFacade.getInstance().cantidadVentasSinCobrar(sucursal);
                if (cantidadVtasSinCobrar == 0) {
                    int cantidadVentasSinCerrar = VentaFacade.getInstance().cantidadVentasSinCerrar(sucursal);
                    if (cantidadVentasSinCerrar > 0) {
                        int reply = JOptionPane.showConfirmDialog(null,
                                "Exiten " + cantidadVentasSinCerrar + " ventas sin cerrar\n"
                                + "¿Desea realizar el cierre?\n"
                                + "Recuerde que no podrá anular una venta ya cerrada", "Realizar Cierre",
                                JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
//                       Comunes.ventanaCargandoUnParametro(CierreVentasFacade.getInstance(), "realizarCierre", "Realizando Cierre", sucursal);
                            CierreVentas realizarCierre = CierreVentasFacade.getInstance().realizarCierre(sucursal);
                            new Impresora().imprimir(realizarCierre);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "No existen ventas para cerrar");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Existen " + cantidadVtasSinCobrar + " ventas sin cobrar");
                }
            }
        });
        //REIMPRIMIR CIERRE
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Reimprimir Cierre");
                putValue(Action.SHORT_DESCRIPTION, "Reimprimir Cierre");

            }

            public void actionPerformed(ActionEvent e) {
                DiagReimprimirCierre diagReimprimirCierre = new DiagReimprimirCierre(null, true);
                diagReimprimirCierre.setLocation(Comunes.centrarDialog(diagReimprimirCierre));
                diagReimprimirCierre.setVisible(true);
            }

        });

        //EXPORTAR CIERRE VENTAS
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Exportar Cierre de Ventas");
                putValue(Action.SHORT_DESCRIPTION, "Exportar Cierre de Ventas");

            }

            public void actionPerformed(ActionEvent e) {
                DiagExportarImportarCierreVentas diagExportarImportarCierreVentas = new DiagExportarImportarCierreVentas(null, true, sucursal);
                diagExportarImportarCierreVentas.setLocation(Comunes.centrarDialog(diagExportarImportarCierreVentas));
                diagExportarImportarCierreVentas.setVisible(true);
            }

        });
        //ORDENES DE COMPRA
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Ordenes de Compra");
                putValue(Action.SHORT_DESCRIPTION, "Ordenes de Compra");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                DiagListaOrdenDeCompra diagOrdenesDeCompra = new DiagListaOrdenDeCompra(frame, true, usuario, sucursal);
                diagOrdenesDeCompra.setLocation(Comunes.centrarDialog(diagOrdenesDeCompra));
                diagOrdenesDeCompra.setTitle("Ordenes de Compra");
                diagOrdenesDeCompra.setVisible(true);
            }

        });

        //ENTREGAS DE ORDENES DE COMPRA
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Entregas");
                putValue(Action.SHORT_DESCRIPTION, "Entregas");
            }

            public void actionPerformed(ActionEvent e) {
//                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
//                if (cajaAbierta != null) {
                DialogEntregas dialogEntregas = new DialogEntregas(frame, true, usuario, sucursal);
                dialogEntregas.setLocation(Comunes.centrarDialog(dialogEntregas));
                dialogEntregas.setVisible(true);
//                } else {
//                    JOptionPane.showMessageDialog(null, "No abrio la caja!");
//                }

            }

        });
        //REIMPRIMIR TICKET
        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Reimprimir Ticket");
                putValue(Action.SHORT_DESCRIPTION, "Reimprimir Ticket");

            }

            public void actionPerformed(ActionEvent e) {
                DiagReimprimirTicket diagReimprimirTicket = new DiagReimprimirTicket(null, true);
                diagReimprimirTicket.setLocation(Comunes.centrarDialog(diagReimprimirTicket));
                diagReimprimirTicket.setVisible(true);
            }

        });

        jXTaskPnVentas.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Listar y Exportar");
                putValue(Action.SHORT_DESCRIPTION, "Listar y Exportar");

            }

            public void actionPerformed(ActionEvent e) {
                DiagListadoVentas ExportarImportar = new DiagListadoVentas(frame, true, sucursal);
                ExportarImportar.setLocation(Comunes.centrarDialog(ExportarImportar));
                ExportarImportar.setVisible(true);
            }

        });
        jXTaskPaneCasaCentral.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Copiar Precios");
                putValue(Action.SHORT_DESCRIPTION, "Copiar Precios");
            }

            public void actionPerformed(ActionEvent e) {
                DiagCopiarPrecios copiarPrecios = new DiagCopiarPrecios(frame, true);
                copiarPrecios.setTitle("Copiar Precios entre Sucursales");
                copiarPrecios.setLocation(Comunes.centrarDialog(copiarPrecios));
                copiarPrecios.setVisible(true);
            }

        });
        jXTaskPaneCasaCentral.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Copiar Promociones");
                putValue(Action.SHORT_DESCRIPTION, "Copiar Promociones");
            }

            public void actionPerformed(ActionEvent e) {
                DiagCopiarPromocion copiarPromocion = new DiagCopiarPromocion(frame, true);
                copiarPromocion.setTitle("Copiar Promociones entre Sucursales");
                copiarPromocion.setLocation(Comunes.centrarDialog(copiarPromocion));
                copiarPromocion.setVisible(true);
            }

        });
        jXTaskPaneCasaCentral.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Asignar Precios de Sucursal");
                putValue(Action.SHORT_DESCRIPTION, "Asignar Precio a los Articulos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagBuscarSucursal buscarSucursal = new DiagBuscarSucursal(frame, true, sucursal);
                buscarSucursal.setLocation(Comunes.centrarDialog(buscarSucursal));
                buscarSucursal.setVisible(true);
                buscarSucursal.getSucursalBuscada();
                if (buscarSucursal.getSucursalBuscada() != null) {
                    DiagAsignarPrecios asignarPrecios = new DiagAsignarPrecios(frame, true, buscarSucursal.getSucursalBuscada(), sucursal);
                    asignarPrecios.setTitle("Asignar Precios de Sucursal " + buscarSucursal.getSucursalBuscada());
                    asignarPrecios.setLocation(Comunes.centrarDialog(asignarPrecios));
                    asignarPrecios.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(rootPane, "No seleccionó "
                            + "ninguna sucursal", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        jXTaskPaneCasaCentral.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Promociones de Sucursal");
                putValue(Action.SHORT_DESCRIPTION, "Administrar Promociones de Sucursal");

            }

            public void actionPerformed(ActionEvent e) {

                if (sucursal.getNombre().contains("CENTRAL")) {
                    DiagBuscarSucursal buscarSucursal = new DiagBuscarSucursal(frame, true);
                    buscarSucursal.setLocation(Comunes.centrarDialog(buscarSucursal));
                    buscarSucursal.setVisible(true);
                    buscarSucursal.getSucursalBuscada();
                    if (buscarSucursal.getSucursalBuscada() != null) {
                        DiagAdminPromociones adminPromociones = new DiagAdminPromociones(frame, true, buscarSucursal.getSucursalBuscada());
                        adminPromociones.setTitle("Administrar promociones de Sucursal " + buscarSucursal.getSucursalBuscada());

                        adminPromociones.setLocation(Comunes.centrarDialog(adminPromociones));
                        adminPromociones.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(rootPane, "No seleccionó "
                                + "ninguna sucursal", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Esta sucursal no puede realizar esta operacion");
                }

            }
        });
        //CAJA
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "ABRIR CAJA");
                putValue(Action.SHORT_DESCRIPTION, "ABRIR CAJA");

            }

            public void actionPerformed(ActionEvent e) {
                CajaFacade.getInstance().abrirCaja(usuario, sucursal);
            }

        });
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Cobrar Tickets");
                putValue(Action.SHORT_DESCRIPTION, "Cobrar Tickets");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagCobroTicket2 diagCobroTicket = new DiagCobroTicket2(frame, enabled, usuario, sucursal, cajaAbierta);
                    diagCobroTicket.setLocation(Comunes.centrarDialog(diagCobroTicket));
                    diagCobroTicket.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");
                }

            }

        });
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Modificar Cobro");
                putValue(Action.SHORT_DESCRIPTION, "Modificar Cobro");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagModificacionCobroVentas diagModificacionCobroVentas = new DiagModificacionCobroVentas(frame, enabled, sucursal);
                    diagModificacionCobroVentas.setLocation(Comunes.centrarDialog(diagModificacionCobroVentas));
                    diagModificacionCobroVentas.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");
                }

            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Sueldos y Adelantos");
                putValue(Action.SHORT_DESCRIPTION, "Sueldos y Adelantos");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagSueldosAdelantos diagAdelSueld = new DiagSueldosAdelantos(frame, true, usuario, sucursal, cajaAbierta);
                    diagAdelSueld.setLocation(Comunes.centrarDialog(diagAdelSueld));
                    diagAdelSueld.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Egresos");
                putValue(Action.SHORT_DESCRIPTION, "Egresos");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagGastos diagGastos = new DiagGastos(frame, true, usuario, sucursal, cajaAbierta);
                    diagGastos.setLocation(Comunes.centrarDialog(diagGastos));
                    diagGastos.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Retiros de Efectivo");
                putValue(Action.SHORT_DESCRIPTION, "Retiros de Efectivo");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagRetiroEfectivo diagRetiroEfectivo = new DiagRetiroEfectivo(frame, true, usuario, sucursal, cajaAbierta);
                    diagRetiroEfectivo.setLocation(Comunes.centrarDialog(diagRetiroEfectivo));
                    diagRetiroEfectivo.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Cuentas Corrientes");
                putValue(Action.SHORT_DESCRIPTION, "Cuentas Corrientes");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagCuentasCorrientes dlgCC = new DiagCuentasCorrientes(frame, true, usuario, sucursal);
                    dlgCC.setLocation(Comunes.centrarDialog(dlgCC));
                    dlgCC.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Ingresos");
                putValue(Action.SHORT_DESCRIPTION, "Ingresos");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagIngreso diagIngreso = new DiagIngreso(frame, true, usuario, sucursal, cajaAbierta);
                    diagIngreso.setLocation(Comunes.centrarDialog(diagIngreso));
                    diagIngreso.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });
        //DESPUES SE LO PODRIA MOSTRAR AL ESTE
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Cobros de caja Actual");
                putValue(Action.SHORT_DESCRIPTION, "Cobros de caja Actual");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagListadoCobroTarjeta cobroTarjeta = new DiagListadoCobroTarjeta(frame, true, cajaAbierta, sucursal);
                    cobroTarjeta.setLocation(Comunes.centrarDialog(cobroTarjeta));
                    cobroTarjeta.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }

        });

        //REIMPRIMIR CIERRE
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Reimprimir Cierre Caja");
                putValue(Action.SHORT_DESCRIPTION, "Reimprimir Cierre Caja");

            }

            public void actionPerformed(ActionEvent e) {
                DiagReimprimirCierreCaja diagReimprimirCierreCaja = new DiagReimprimirCierreCaja(null, true);
                diagReimprimirCierreCaja.setLocation(Comunes.centrarDialog(diagReimprimirCierreCaja));
                diagReimprimirCierreCaja.setVisible(true);
            }

        });
        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "CERRAR CAJA");
                putValue(Action.SHORT_DESCRIPTION, "CERRAR CAJA");

            }

            public void actionPerformed(ActionEvent e) {
                CajaFacade.getInstance().cerrarCaja(sucursal, usuario);
                //cerrar();
            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "EXPORTAR CAJA");
                putValue(Action.SHORT_DESCRIPTION, "EXPORTAR CAJA");

            }

            public void actionPerformed(ActionEvent e) {
                DiagExportarImportarCaja diagExportarImportarCaja = new DiagExportarImportarCaja(frame, true, sucursal);
                diagExportarImportarCaja.setLocation(Comunes.centrarDialog(diagExportarImportarCaja));
                diagExportarImportarCaja.setVisible(true);

//cerrar();
            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Detalle de Caja");
                putValue(Action.SHORT_DESCRIPTION, "Detalle de Caja");

            }

            public void actionPerformed(ActionEvent e) {
                DiagReporte_detalleCaja diagReporteCaja = new DiagReporte_detalleCaja(frame, true, sucursal);
                diagReporteCaja.setLocation(Comunes.centrarDialog(diagReporteCaja));
                diagReporteCaja.setVisible(true);

//cerrar();
            }

        });

        jXTaskPnCaja.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Resumen de Caja");
                putValue(Action.SHORT_DESCRIPTION, "Resumen de Caja");

            }

            public void actionPerformed(ActionEvent e) {
                DiagReporte_resumenCaja diagReporteCaja_Resumen = new DiagReporte_resumenCaja(frame, true, sucursal);
                diagReporteCaja_Resumen.setLocation(Comunes.centrarDialog(diagReporteCaja_Resumen));
                diagReporteCaja_Resumen.setVisible(true);

            }
        });
        jXTaskPnMovimientos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Inventario");
                putValue(Action.SHORT_DESCRIPTION, "Inventario");

            }

            public void actionPerformed(ActionEvent e) {
                FrInventario inventario = new FrInventario(usuario, sucursal);
                inventario.setLocation(Comunes.centrarFrame(inventario));
                inventario.setVisible(true);

            }
        });
        jXTaskPnMovimientos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Admin Inventarios");
                putValue(Action.SHORT_DESCRIPTION, "Admin Inventarios");

            }

            public void actionPerformed(ActionEvent e) {
                DiagListadoInventario listadoInventario = new DiagListadoInventario(frame, true, usuario, sucursal);
                listadoInventario.setLocation(Comunes.centrarDialog(listadoInventario));
                listadoInventario.setVisible(true);

            }
        });
        jXTaskPnMovimientos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Alta Mov. Internos");
                putValue(Action.SHORT_DESCRIPTION, "Alta Mov. Internos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAltaMovimientosInternos altaMovimientoInterno = new DiagAltaMovimientosInternos(frame, true, sucursal, usuario);
                altaMovimientoInterno.setLocation(Comunes.centrarDialog(altaMovimientoInterno));
                altaMovimientoInterno.setVisible(true);

            }
        });

        jXTaskPnMovimientos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Admin. Mov. Internos");
                putValue(Action.SHORT_DESCRIPTION, "Admin. Mov. Internos");

            }

            public void actionPerformed(ActionEvent e) {
                DiagAdministrarMovimientosInternos movimientoInterno = new DiagAdministrarMovimientosInternos(frame, true, usuario, sucursal);
                movimientoInterno.setLocation(Comunes.centrarDialog(movimientoInterno));
                movimientoInterno.setVisible(true);

            }
        });
        jXTaskPnMovimientos.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Ventas Sin Promo (Rinde)");
                putValue(Action.SHORT_DESCRIPTION, "Ventas Sin Promo (Rinde)");

            }

            public void actionPerformed(ActionEvent e) {
                DiagVentasSinPromo diagVentasSinPromo = new DiagVentasSinPromo(frame, true, sucursal);
                diagVentasSinPromo.setLocation(Comunes.centrarDialog(diagVentasSinPromo));
                diagVentasSinPromo.setVisible(true);

            }
        });
        jXTaskPnCocina.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Pedidos Cocina");
                putValue(Action.SHORT_DESCRIPTION, "Pedidos Cocina");

            }

            public void actionPerformed(ActionEvent e) {
                Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
                if (cajaAbierta != null) {
                    DiagDetalleVenta detalleVenta = new DiagDetalleVenta(frame, true, cajaAbierta, sucursal);
                    detalleVenta.setLocation(Comunes.centrarDialog(detalleVenta));
                    detalleVenta.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No abrio la caja!");

                }

            }
        });

        if (sucursal.getCodigo().equals("1")) {
            jXTaskPnMovimientos.add(new AbstractAction() {
                {
                    putValue(Action.NAME, "Nota de pedido de Media Res");
                    putValue(Action.SHORT_DESCRIPTION, "Pedido");

                }

                public void actionPerformed(ActionEvent e) {
//                frPedidoDeMedias inventario = new frPedidoDeMedias(usuario, sucursal);
//                inventario.setLocation(Comunes.centrarFrame(inventario));
//                inventario.setVisible(true);

                }
            });
        }

    }

    /**
     * Creates new form Principal
     */
    public frPrincipal() {
        initComponents();
        this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        imagenFondo();
        llenarTaskPanels(this);
        if (usuario == null) {
            Comunes.mensajeError("No puede ingresar directamente sin autenticarse", "Error de Autenticación");
            System.exit(0);
        }
        cargarMenuesSegunPermiso();
        // TODO: Agregar la validacion aqui

        inicializarComponentes();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu7 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        taSalida = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        tbBarraPrincipal = new javax.swing.JToolBar();
        btonCerrarSesion = new javax.swing.JButton();
        btonTerminar2 = new javax.swing.JButton();
        btnSincronizar = new javax.swing.JButton();
        btnConfiguracion = new javax.swing.JButton();
        btnSincronizarBucardo = new javax.swing.JButton();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        lbEtiquetaHoraDelServidor = new javax.swing.JLabel();
        lbHoraDelServidor = new javax.swing.JLabel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPnConfiguracion = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnUsuarios = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnSucursal = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnEmpleados = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPaneArticulos = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPaneCasaCentral = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnCliente = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnVentas = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnCaja = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnMovimientos = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPnCocina = new org.jdesktop.swingx.JXTaskPane();

        jMenu7.setText("Impresión");
        jMenu7.setActionCommand("jMenu7");
        jMenu7.setName("jMenu7"); // NOI18N

        jMenuItem16.setText("Configurar Recibo");
        jMenuItem16.setName("jMenuItem16"); // NOI18N
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem16);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));

        jDesktopPane1.setName("jDesktopPane1"); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(255, 204, 0));
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        taSalida.setBackground(new java.awt.Color(0, 0, 0));
        taSalida.setColumns(20);
        taSalida.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        taSalida.setForeground(new java.awt.Color(153, 255, 102));
        taSalida.setRows(5);
        taSalida.setBorder(null);
        taSalida.setName("taSalida"); // NOI18N
        jScrollPane1.setViewportView(taSalida);

        jDesktopPane1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 990, 120);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logogrande3.jpg"))); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setMaximumSize(new java.awt.Dimension(1000, 850));
        jLabel1.setMinimumSize(new java.awt.Dimension(500, 450));
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 600));
        jDesktopPane1.add(jLabel1);
        jLabel1.setBounds(340, 130, 330, 410);

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        tbBarraPrincipal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tbBarraPrincipal.setRollover(true);
        tbBarraPrincipal.setAlignmentX(0.0F);
        tbBarraPrincipal.setName("tbBarraPrincipal"); // NOI18N

        btonCerrarSesion.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btonCerrarSesion.setText("CERRAR SESION");
        btonCerrarSesion.setFocusable(false);
        btonCerrarSesion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btonCerrarSesion.setMaximumSize(new java.awt.Dimension(88, 55));
        btonCerrarSesion.setMinimumSize(new java.awt.Dimension(88, 55));
        btonCerrarSesion.setName("btonCerrarSesion"); // NOI18N
        btonCerrarSesion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btonCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btonCerrarSesionActionPerformed(evt);
            }
        });
        tbBarraPrincipal.add(btonCerrarSesion);

        btonTerminar2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btonTerminar2.setText("SALIR");
        btonTerminar2.setFocusable(false);
        btonTerminar2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btonTerminar2.setMaximumSize(new java.awt.Dimension(88, 55));
        btonTerminar2.setMinimumSize(new java.awt.Dimension(88, 55));
        btonTerminar2.setName("btonTerminar2"); // NOI18N
        btonTerminar2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btonTerminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btonTerminar2ActionPerformed(evt);
            }
        });
        tbBarraPrincipal.add(btonTerminar2);

        btnSincronizar.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnSincronizar.setText("ENVIAR VENTAS/CAJAS");
        btnSincronizar.setFocusable(false);
        btnSincronizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSincronizar.setMaximumSize(new java.awt.Dimension(88, 55));
        btnSincronizar.setMinimumSize(new java.awt.Dimension(88, 55));
        btnSincronizar.setName("btnSincronizar"); // NOI18N
        btnSincronizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSincronizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSincronizarActionPerformed(evt);
            }
        });
        tbBarraPrincipal.add(btnSincronizar);

        btnConfiguracion.setText("Configuracion");
        btnConfiguracion.setFocusable(false);
        btnConfiguracion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfiguracion.setName("btnConfiguracion"); // NOI18N
        btnConfiguracion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfiguracionActionPerformed(evt);
            }
        });
        tbBarraPrincipal.add(btnConfiguracion);

        btnSincronizarBucardo.setText("Reiniciar Sincronizacion");
        btnSincronizarBucardo.setFocusable(false);
        btnSincronizarBucardo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSincronizarBucardo.setName("btnSincronizarBucardo"); // NOI18N
        btnSincronizarBucardo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSincronizarBucardo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSincronizarBucardoActionPerformed(evt);
            }
        });
        tbBarraPrincipal.add(btnSincronizarBucardo);

        jToolBar1.add(tbBarraPrincipal);

        jXPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jXPanel1.setName("jXPanel1"); // NOI18N

        lbEtiquetaHoraDelServidor.setText("Hora");
        lbEtiquetaHoraDelServidor.setName("lbEtiquetaHoraDelServidor"); // NOI18N

        lbHoraDelServidor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHoraDelServidor.setText("00:00");
        lbHoraDelServidor.setName("lbHoraDelServidor"); // NOI18N

        javax.swing.GroupLayout jXPanel1Layout = new javax.swing.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbHoraDelServidor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jXPanel1Layout.createSequentialGroup()
                        .addComponent(lbEtiquetaHoraDelServidor)
                        .addGap(26, 26, 26))))
        );
        jXPanel1Layout.setVerticalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbEtiquetaHoraDelServidor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbHoraDelServidor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTaskPaneContainer1.setName("jXTaskPaneContainer1"); // NOI18N

        jXTaskPnConfiguracion.setTitle("Configuracion");
        jXTaskPnConfiguracion.setToolTipText("");
        jXTaskPnConfiguracion.setName("jXTaskPnConfiguracion"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnConfiguracion);

        jXTaskPnUsuarios.setTitle("Usuarios");
        jXTaskPnUsuarios.setToolTipText("");
        jXTaskPnUsuarios.setName("jXTaskPnUsuarios"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnUsuarios);

        jXTaskPnSucursal.setTitle("Sucursales");
        jXTaskPnSucursal.setToolTipText("");
        jXTaskPnSucursal.setName("jXTaskPnSucursal"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnSucursal);

        jXTaskPnEmpleados.setTitle("Empleados");
        jXTaskPnEmpleados.setName("jXTaskPnEmpleados"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnEmpleados);

        jXTaskPaneArticulos.setTitle("Articulos");
        jXTaskPaneArticulos.setName("jXTaskPaneArticulos"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPaneArticulos);

        jXTaskPaneCasaCentral.setTitle("Casa Central");
        jXTaskPaneCasaCentral.setName("jXTaskPaneCasaCentral"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPaneCasaCentral);

        jXTaskPnCliente.setTitle("Clientes");
        jXTaskPnCliente.setName("jXTaskPnCliente"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnCliente);

        jXTaskPnVentas.setTitle("Ventas");
        jXTaskPnVentas.setName("jXTaskPnVentas"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnVentas);

        jXTaskPnCaja.setTitle("Caja");
        jXTaskPnCaja.setName("jXTaskPnCaja"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnCaja);

        jXTaskPnMovimientos.setExpanded(false);
        jXTaskPnMovimientos.setTitle("Movimientos");
        jXTaskPnMovimientos.setName("jXTaskPnMovimientos"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnMovimientos);

        jXTaskPnCocina.setExpanded(false);
        jXTaskPnCocina.setTitle("Cocina");
        jXTaskPnCocina.setName("jXTaskPnCocina"); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPnCocina);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1099, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, 0, 0, Short.MAX_VALUE)
                    .addComponent(jXPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                    .addComponent(jXTaskPaneContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btonCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btonCerrarSesionActionPerformed
        if (CajaFacade.getInstance().hayCajaAbierta(sucursal, usuario)) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "Exite caja sin cerrar\n"
                    + "¿Desea realizar el cierre de caja?\n", "Realizar Cierre",
                    JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                CajaFacade.getInstance().cerrarCaja(sucursal, usuario);
            }

        }
        frLogin login = new frLogin();
        login.setVisible(true);
        cerrarPuertoBalanza();
        this.dispose();
    }//GEN-LAST:event_btonCerrarSesionActionPerformed

    private void btonTerminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btonTerminar2ActionPerformed
        if (CajaFacade.getInstance().hayCajaAbierta(sucursal)) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "Exite caja sin cerrar\n"
                    + "¿Desea realizar el cierre de caja?\n", "Realizar Cierre",
                    JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                CajaFacade.getInstance().cerrarCaja(sucursal, usuario);
            }

        }
        System.exit(0);
    }//GEN-LAST:event_btonTerminar2ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void btnSincronizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSincronizarActionPerformed
        SincronizaCliente cliente = new SincronizaCliente();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        //cliente.pedir();
        cliente.enviar();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        JOptionPane.showMessageDialog(null, "Sincronizacion Finalizada");
        frLogin login = new frLogin();
        login.setVisible(true);
        cerrarPuertoBalanza();
        this.dispose();

    }//GEN-LAST:event_btnSincronizarActionPerformed

    private void btnSincronizarBucardoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSincronizarBucardoActionPerformed
        sincronizarTablasBucardo();
    }//GEN-LAST:event_btnSincronizarBucardoActionPerformed

    private void btnConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfiguracionActionPerformed
        verConfiguracion();
    }//GEN-LAST:event_btnConfiguracionActionPerformed
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new frPrincipal().setVisible(true);
//
//            }
//        });
//
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfiguracion;
    private javax.swing.JButton btnSincronizar;
    private javax.swing.JButton btnSincronizarBucardo;
    private javax.swing.JButton btonCerrarSesion;
    private javax.swing.JButton btonTerminar2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPaneArticulos;
    private org.jdesktop.swingx.JXTaskPane jXTaskPaneCasaCentral;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnCaja;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnCliente;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnCocina;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnConfiguracion;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnEmpleados;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnMovimientos;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnSucursal;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnUsuarios;
    private org.jdesktop.swingx.JXTaskPane jXTaskPnVentas;
    private javax.swing.JLabel lbEtiquetaHoraDelServidor;
    private javax.swing.JLabel lbHoraDelServidor;
    private javax.swing.JTextArea taSalida;
    private javax.swing.JToolBar tbBarraPrincipal;
    // End of variables declaration//GEN-END:variables

    private void imagenFondo() {
        BufferedImage image = null;

        try {
            image = ImageIO.read(this.getClass().getResource("/imagenes/logo.jpg"));
            //image = ImageIO.read(getClass().getResourceAsStream("/imagenes/telecentro.png"));

        } catch (IOException ex) {
            Logger.getLogger(frPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDesktopPane1.setBorder(new ImagenFondoCentrada(image));

    }

    private void inicializarComponentes() {
        //enumerarPuertos();
        conectadoALaCentral = true;
        eventosDeTeclas();
        timer.setRepeats(true);
        timer.start();
        cargarHoraDelServidorAlInicio();
        buscarNombrePuertoSegunSistema();
        if (!portFound) {
            buscarPuerto();
            if (puertoEncontrado != null) {
                abrirPuerto();
            }
        } else {
            System.out.println("No se pudo abrir el puerto");
        }
        if (serialPort != null) {
            leerPuerto();
        }
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                taSalida.append(text);

            }
        });

    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));

            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));

            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);

            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));

    }

    public void cargarHoraDelServidorAlInicio() {
        Date fechaServidor = new Date();//cambiado
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaServidor);

        if (calendario.get(Calendar.MINUTE) < 10) {
            lbHoraDelServidor.setText(calendario.get(Calendar.HOUR_OF_DAY) + ":0" + calendario.get(Calendar.MINUTE));

        } else {
            lbHoraDelServidor.setText(calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE));

        }
    }

    private void cargarMenuesSegunPermiso() {
        //primero deshabilitamos todos lo paneles
        btnConfiguracion.setEnabled(false);
        habilitarJXTaskPanels(false);

        if (usuario.getNombreUsuario().equals("admin") && sucursal.getCodigo().equals("1")) {
            //usuarios del grupo administrador
            habilitarJXTaskPanels(true);
            btnConfiguracion.setEnabled(true);
        } else {
            jXTaskPnCliente.setVisible(true);
            jXTaskPnVentas.setVisible(true);
            jXTaskPnCaja.setVisible(true);
            jXTaskPnMovimientos.setVisible(true);
            jXTaskPnCocina.setVisible(true);
            btnConfiguracion.setEnabled(true);

//            if(usuario.getGrupo().getNombre().contains("admin")&&!sucursal.getNombre().equals("CENTRAl")){
//                System.out.println("usuaro "+usuario);
//                System.out.println("usuaro "+sucursal.getNombre());
//                jXTaskPnCliente.setVisible(true);
//                jXTaskPnVentas.setVisible(true);
//                jXTaskPnCaja.setVisible(true);
//            }else{
//            //para otros usuarios se carga segun los paneles que tenga habilitados
//            for (Paneles p : usuario.getGrupo().getPaneles()) {
//
//                switch (p.getDescripcion()) {
//                    case "CONFIGURACION":
//                        jXTaskPnConfiguracion.setVisible(true);
//                        break;
//                    case "USUARIOS":
//                        jXTaskPnUsuarios.setVisible(true);
//                        break;
//                    case "SUCURSALES":
//                        jXTaskPnSucursal.setVisible(true);
//                        break;
//                    case "EMPLEADOS":
//                        jXTaskPnEmpleados.setVisible(true);
//                        break;
//                    case "ARTICULOS":
//                        jXTaskPaneArticulos.setVisible(true);
//                        break;
//                    case "CASA CENTRAL":
//                        jXTaskPaneCasaCentral.setVisible(true);
//                        break;
//                    case "CLIENTES":
//                        jXTaskPnCliente.setVisible(true);
//                        break;
//                    case "VENTAS":
//                        jXTaskPnVentas.setVisible(true);
//                        break;
//                    case "CAJA":
//                        jXTaskPnCaja.setVisible(true);
//                        break;
//
//                }
//            }
//        }
        }

    }

    private void panelDeControlTitulos() {
        /*   diagEliminarPanelControl diagEliminarPanelControl = new diagEliminarPanelControl(this, true);
         diagEliminarPanelControl.setLocation(Comunes.centrarDialog(diagEliminarPanelControl));
         diagEliminarPanelControl.setVisible(true);*/
    }

    public void actualizarMensajes() {
        /*  jXTaskPnUsuarios.removeAll();
         jXTaskPnSupervision.removeAll();
         jXTaskPnProyectos.removeAll();
         jxTaskPnIncentivos.removeAll();
         jxTaskPnBecas.removeAll();
         jxTaskPnCategorizaciones.removeAll();
         jXTaskPnProyectosVinculacion.removeAll();
         jXTaskPnEconomico.removeAll();
         //actualizamos el contenedor
         jXTaskPaneContainer1.updateUI();
         //y lo cargamos de nuevo
         llenarTaskPanels(frame);*/

    }

    private void cargarContadorMensajes() {
        /*   mensajeSupervision = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos() + ")";
         mensajeProyecto = "Mensajes (" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("PROYECTO") + ")";
         mensajeIncentivo = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("INCENTIVOS") + ")";
         mensajeBeca = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("BECAS") + ")";
         mensajeCategorizacion = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("CATEGORIZACION") + ")";
         mensajeVinculacion = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("VINCULACION") + ")";
         mensajeEconomico = "Mensajes(" + MensajeFacade.getInstance().getCantidadMjesNoLeidos("UNIDAD DE ADMINISTRACION") + ")";
         entradas = "Entradas";
         salidas = "Salidas";*/
    }

    private void expandirTaskPanels(Boolean flag) {

        jXTaskPnCliente.setExpanded(flag);
        jXTaskPnConfiguracion.setExpanded(flag);
        jXTaskPnEmpleados.setExpanded(flag);
        jXTaskPnSucursal.setExpanded(flag);
        jXTaskPnUsuarios.setExpanded(flag);
        jXTaskPnVentas.setExpanded(flag);
        jXTaskPaneArticulos.setExpanded(flag);
        jXTaskPaneCasaCentral.setExpanded(flag);
        jXTaskPnCaja.setExpanded(flag);
        jXTaskPnCocina.setExpanded(flag);
    }

    /**
     * Lee de la tabla de configuraciones
     */
    //z;l;sksllfslfdkf
    private void leerConfiguracion() {
        btnSincronizarBucardo.setEnabled(false);
        try {
            /**
             * Creamos un Objeto de tipo Properties
             */

            Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");

            String codSucursal = configSucursal.getValor();
            /**
             * Seteo los valores
             */
            System.out.println("Leyendo Cod Sucursal: " + codSucursal + "\n");
            sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
            if (sucursal != null) {
                try {
                    this.setTitle("Bienvenido " + usuario.getNombreCompleto() + " al Sistema de Gestion de Carniceria MB - " + sucursal);
                } catch (Exception e) {
                    this.setTitle("Bienvenido al Sistema de Gestion de Carniceria MB");
                    System.out.println(e);
                }
                if (sucursal.getCodigo().equals("1")) {
                    //PARA EL SERVIDOR SE DESHABILITA EL BOTON SINCRONIZAR
                    btnSincronizar.setEnabled(false);
                    // Y SE HABILITA LA SINCRONIZACION DE BUCARDO
                    btnSincronizarBucardo.setEnabled(true);

                }
                System.out.println("Sucursal leída: " + sucursal + "\n");

            } else {
                JOptionPane.showMessageDialog(this, "No se ha podido cargar la "
                        + "sucursal configurada\n"
                        + "Verifique si esta creada en la base de datos");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error, No se puede cargar los valores del"
                    + " archivo de configuración");
        }
    }

    private void configurarCentral() {
        jXTaskPnVentas.setVisible(false);
    }

    private void buscarNombrePuertoSegunSistema() {

        //Asignamos el nombre del puerto desde la tabla de configuraciones
        defaultPort = nombrePuertoBalanza;
        //fin

        //verificamos sistema operativo para comprobar
        //el nombre del puerto com
//        if (System.getProperty("os.name").contentEquals("Linux")) {
//            //nombre del puerto en linux
//            defaultPort = "/dev/ttyS0";
//        } else {
//            //nombre del puerto en windows
//            defaultPort = "COM3";
//        }
    }

    private void buscarPuerto() {
//        System.out.println("port lis" + portList);
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {
                    portFound = true;
                    puertoEncontrado = portId;

                }
            }
        }

    }

    private void abrirPuerto() {
        try {
            serialPort = (SerialPort) puertoEncontrado.open("SimpleReadApp", 2000);
        } catch (PortInUseException ex) {
            Logger.getLogger(frPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cerrarPuertoBalanza() {
        try {
            serialPort.removeEventListener();
            serialPort.close();
        } catch (Exception e) {

        }

    }

    private void leerPuerto() {

        try {
            inputStream = serialPort.getInputStream();

        } catch (IOException e) {
        }

        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
        }

        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_2,
                    SerialPort.PARITY_NONE);
            //agregados
            serialPort.setRTS(true);
            serialPort.setOutputBufferSize(200);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException e) {
        }
    }

    public void serialEvent(SerialPortEvent event) {

        switch (event.getEventType()) {

            case SerialPortEvent.BI:

            case SerialPortEvent.OE:

            case SerialPortEvent.FE:

            case SerialPortEvent.PE:

            case SerialPortEvent.CD:

            case SerialPortEvent.CTS:

            case SerialPortEvent.DSR:

            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[200];

                try {
                    while (inputStream.available() > 0) {

                        inputStream.read(readBuffer);
                        //System.out.println("inputStreeam: "+readBuffer);

                    }
                    peso = new String(readBuffer).trim().substring(0, 6);

                } catch (IOException e) {
                }

                break;
        }
    }

    private void sincronizar() {
        try {
            if (sucursal.getId() != 1L) {
//                JOptionPane.showMessageDialog(rootPane,
//                        "Error intentando conectar con el servidor",
//                        "Sincronizar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException headlessException) {
            JOptionPane.showMessageDialog(rootPane,
                    "Error intentando conectar con el servidor\n compruebe si esta creada la sucursal",
                    "Sincronizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void habilitarJXTaskPanels(boolean flag) {
        jXTaskPnSucursal.setVisible(flag);
        jXTaskPnUsuarios.setVisible(flag);
        jXTaskPnConfiguracion.setVisible(flag);
        jXTaskPnEmpleados.setVisible(flag);
        jXTaskPaneArticulos.setVisible(flag);
        jXTaskPaneCasaCentral.setVisible(flag);
        jXTaskPnCliente.setVisible(flag);
        jXTaskPnVentas.setVisible(flag);
        jXTaskPnCaja.setVisible(flag);
        jXTaskPnMovimientos.setVisible(flag);
        jXTaskPnCocina.setVisible(flag);

    }

    public void eventosDeTeclas() {

    }

    private void leerConfiguracionDescuento() {
        try {
            Configuracion configDescuentoEmpleado = ConfiguracionFacade.getInstance().buscar("descuentoempleado");
            descuentoEmpleado = new BigDecimal(configDescuentoEmpleado.getValor());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se encontro el descuento del empleado");
        }

    }

    private void enumerarPuertos() {

        Enumeration listaPuertos = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier idPuerto = null;
        boolean encontrado = false;

        try {
            while (listaPuertos.hasMoreElements() && !encontrado) {

                idPuerto = (CommPortIdentifier) listaPuertos.nextElement();
                JOptionPane.showMessageDialog(null, "entro while: " + idPuerto.getName());
                if (idPuerto.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                    JOptionPane.showMessageDialog(null, "Puerto: " + listaPuertos.toString());

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }

    }

    private void leerConfiguracionPuertoBalanza() {
        try {
            Configuracion configPuertoBalanza = ConfiguracionFacade.getInstance().buscar("puertobalanza");
            nombrePuertoBalanza = configPuertoBalanza.getValor();
            System.out.println("nombre del puerto de la balanza: " + nombrePuertoBalanza);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se encontro el nombre del puerto COM");
        }
    }

    private void cerrar() {
        frLogin login = new frLogin();
        login.setVisible(true);
        cerrarPuertoBalanza();
        this.dispose();
    }

    private void comprobarClientesDuplicados() {
        comprobarPersonasDuplicadasBucardo();
    }

    private void comprobarPersonasDuplicadasBucardo() {

    }

    private void sincronizarTablasBucardo() {
//        try {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (System.getProperty("os.name").contentEquals("Linux")) {
            //comandos para reiniciar sincronizacion con bucardo
            //en caso de que este estancado
//                Runtime.getRuntime().exec("bucardo stop");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo deactivate all");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo validate all");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo activate all");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo kick all");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo list sync");
//                Runtime.getRuntime().exec("sleep 1");
//                Runtime.getRuntime().exec("bucardo start");
            //Runtime.getRuntime().exec("bucardo status");
            //JOptionPane.showMessageDialog(null, "Operaciones realizadas!!");

            //OTRA FORMAAAAA
            List<String> comandos = new ArrayList<>();
            /*          comandos.add("bucardo stop");
             comandos.add("sleep 1");
             comandos.add("bucardo deactivate all");
             comandos.add("sleep 1");
             comandos.add("bucardo validate all");
             comandos.add("sleep 1");
             comandos.add("bucardo activate all");
             comandos.add("sleep 1");
             comandos.add("bucardo kick all");
             comandos.add("sleep 1");
             comandos.add("bucardo list sync");
             comandos.add("sleep 1");
             comandos.add("bucardo start");
             comandos.add("bucardo kick all");
             comandos.add("echo FINALIZADO"); */

            comandos.add("bucardo stop");
            comandos.add("sleep 1");
            comandos.add("sleep 1");
            comandos.add("rm -R /var/run/bucardo/bucardo.mcp.pid");
            comandos.add("bucardo start");
            comandos.add("bucardo kick all");
            comandos.add("echo FINALIZADO");
            //recorremos los comandos
            for (String cmd : comandos) {
                //System.out.println(cmd);
                try {
                    // Set shell command
                    Process child = Runtime.getRuntime().exec(cmd);

                    InputStream lsOut = child.getInputStream();
                    InputStreamReader r = new InputStreamReader(lsOut);
                    BufferedReader in = new BufferedReader(r);

                    // read the child process' output
                    String line;
                    while ((line = in.readLine()) != null) {
                        //System.out.println(line);
                        taSalida.append(line + "\n");
                    }
                } catch (Exception e) { // exception thrown

                    System.err.println("Command failed!");

                }
            }

        }

//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        }
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        frLogin login = new frLogin();
        login.setVisible(true);
        cerrarPuertoBalanza();
        this.dispose();
    }

    private void verConfiguracion() {
        diagConfiguracion configuracion = new diagConfiguracion();
        configuracion.setLocation(Comunes.centrarDialog(configuracion));
        configuracion.setVisible(true);
    }

}
