/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.ventas;

import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.PrecioArticulo;
import entidades.caja.Caja;
import entidades.caja.CuentaCorriente;
import entidades.cliente.Cliente;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.promocion.DiaSemana;
import entidades.promocion.Promocion;
import entidades.promocion.PromocionArticulo;
import entidades.usuario.Usuario;
import entidades.venta.Venta;
import entidades.venta.VentaArticulo;
import facade.ArticuloFacade;
import facade.CajaFacade;
import facade.ClienteFacade;
import facade.CuentaCorrienteFacade;
import facade.EmpleadoFacade;
import facade.PrecioArticuloFacade;
import facade.PromocionFacade;
import facade.UsuarioFacade;
import facade.VentaFacade;
import giovynet.serial.Com;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import includes.Comunes;
import includes.Impresora;
import includes.ModeloTablaNoEditable;
import includes.SuperFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.eclipse.persistence.exceptions.DatabaseException;
import vista.Caja.DiagCobroTicket2;
import vista.articulos.DiagBuscarArticulo;
import vista.frPrincipal;

/**
 *
 * @author hugo
 */
public class FrVentas extends SuperFrame {

    private Cliente cliente;
    CommPortIdentifier idPuerto = null;
    private List<String> listaPuertos;
    private Com com1;
    // variables nuevas 
    private CommPortIdentifier IdPuerto;
    private Enumeration portList;
    private InputStream inputStream;
    private SerialPort serialPort;
    private Thread readThread;
    private boolean hiloIniciado;
    private Articulo articulo;
    private PrecioArticulo precioArticulo;
    private PromocionArticulo promocionArticulo;
    private List<PromocionArticulo> listapromocionArticulos = new ArrayList<>();
    private List<VentaArticulo> listaVentaArticulos = new ArrayList<>();
    private List<Promocion> listaPromocionesHabilitadas;
    private List<Promocion> listaPromocionesDeHoy;
    private List<Promocion> listaPromocionesPorcentajeTodosDeHoy;
    private VentaArticulo ventaArticulo;
    private ModeloTablaNoEditable modeloTablaArticulos;
    private BigDecimal subTotal = new BigDecimal(0.0);
    private BigDecimal precioPromocion = new BigDecimal("0.0");
    private Venta venta;
    private Usuario usuario;
    private Sucursal sucursal;
    private String diaSemana;
    private boolean cumpleanos = Boolean.FALSE;
    private Promocion promocionSeleccionada;
    private BigDecimal subTotalArticulo;
    private BigDecimal descuento = new BigDecimal("0.00");
    private BigDecimal total;
    private List<Promocion> listaPromocionesPorcentajeTodos;
    //private KeyboardFocusManager manager;
    private Persona persona;
    private KeyEventDispatcher keyDispaycher;
    private Color color;
    private Color colorPorDefecto;
    private Color colorBoton;
    private PrecioArticulo precioComun;
    private BigDecimal montoDescuento = new BigDecimal("0.00");

    /**
     * Creates new form FrmVentas
     *
     * @param serialPort
     * @param usuario
     * @param sucursal
     */
    public FrVentas(SerialPort serialPort, Usuario usuario, Sucursal sucursal) {
        initComponents();
        this.usuario = usuario;
        this.sucursal = sucursal;
        this.serialPort = serialPort;
        this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        this.setTitle("Ventas : " + usuario);
        this.setLocation(Comunes.centrarFrame(this));
        inicializarComponentes();
    }

    public FrVentas() {
        initComponents();
        //maximizamos frame
        this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        this.setTitle("Ventas : " + usuario);
        //configuramos tabla
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

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ftfDocumento = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        tfCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taMensaje = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        tfListaPrecio = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        tfSaldo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        tfCodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfPromocion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfSubTotalArticulo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfPesoBalanza = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfCantidad = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblArticulos = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfSubtotalGral = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tfTotal = new javax.swing.JTextField();
        btnConfirmarPesada = new javax.swing.JButton();
        btnEmitirTicket = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        tfDescuento = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cboVendedor = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jPanel2.border.title"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel3.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel3.text")); // NOI18N

        ftfDocumento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("########"))));
        ftfDocumento.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.ftfDocumento.text")); // NOI18N
        ftfDocumento.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        ftfDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ftfDocumentoFocusGained(evt);
            }
        });
        ftfDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfDocumentoActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar2.png"))); // NOI18N
        jButton1.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jButton1.text")); // NOI18N
        jButton1.setNextFocusableComponent(tfCodigo);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tfCliente.setEditable(false);
        tfCliente.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfCliente.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfCliente.text")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel4.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel4.text")); // NOI18N

        taMensaje.setEditable(false);
        taMensaje.setColumns(20);
        taMensaje.setRows(5);
        jScrollPane1.setViewportView(taMensaje);

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel20.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel20.text")); // NOI18N

        tfListaPrecio.setEditable(false);
        tfListaPrecio.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfListaPrecio.text")); // NOI18N

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel24.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel24.text")); // NOI18N

        tfSaldo.setEditable(false);
        tfSaldo.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfSaldo.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ftfDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(82, 82, 82))
                    .addComponent(tfCliente))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tfSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(tfListaPrecio))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ftfDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfListaPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(tfSaldo))
                .addGap(12, 12, 12))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jPanel3.border.title"))); // NOI18N

        tfCodigo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfCodigo.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfCodigo.text")); // NOI18N
        tfCodigo.setNextFocusableComponent(tblArticulos);
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfCodigoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoFocusLost(evt);
            }
        });
        tfCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCodigoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel5.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel5.text")); // NOI18N

        tfDescripcion.setEditable(false);
        tfDescripcion.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfDescripcion.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfDescripcion.text")); // NOI18N

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel6.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel6.text")); // NOI18N

        tfPrecio.setEditable(false);
        tfPrecio.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfPrecio.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfPrecio.text")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel7.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel7.text")); // NOI18N

        tfPromocion.setEditable(false);
        tfPromocion.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfPromocion.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfPromocion.text")); // NOI18N

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel8.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel8.text")); // NOI18N

        tfSubTotalArticulo.setEditable(false);
        tfSubTotalArticulo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfSubTotalArticulo.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfSubTotalArticulo.text")); // NOI18N

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel9.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel9.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jPanel1.border.title"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel1.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel1.text")); // NOI18N

        tfPesoBalanza.setEditable(false);
        tfPesoBalanza.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfPesoBalanza.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfPesoBalanza.text")); // NOI18N
        tfPesoBalanza.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfPesoBalanzaKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel2.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel2.text")); // NOI18N

        tfCantidad.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfCantidad.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfCantidad.text")); // NOI18N
        tfCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCantidadActionPerformed(evt);
            }
        });
        tfCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfCantidadKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addComponent(tfPesoBalanza, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfPesoBalanza)
                    .addComponent(jLabel1)
                    .addComponent(tfCantidad))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfDescripcion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(tfPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(tfSubTotalArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSubTotalArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jPanel4.border.title"))); // NOI18N

        tblArticulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblArticulos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(317, 317, 317))
        );

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel10.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel10.text")); // NOI18N

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel11.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel11.text")); // NOI18N

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel12.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel12.text")); // NOI18N

        tfSubtotalGral.setEditable(false);
        tfSubtotalGral.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfSubtotalGral.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfSubtotalGral.text")); // NOI18N

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel13.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel13.text")); // NOI18N

        tfTotal.setEditable(false);
        tfTotal.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfTotal.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfTotal.text")); // NOI18N

        btnConfirmarPesada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/add.png"))); // NOI18N
        btnConfirmarPesada.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.btnConfirmarPesada.text")); // NOI18N
        btnConfirmarPesada.setNextFocusableComponent(btnEmitirTicket);
        btnConfirmarPesada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnConfirmarPesadaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnConfirmarPesadaFocusLost(evt);
            }
        });
        btnConfirmarPesada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarPesadaActionPerformed(evt);
            }
        });

        btnEmitirTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/accept.png"))); // NOI18N
        btnEmitirTicket.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.btnEmitirTicket.text")); // NOI18N
        btnEmitirTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirTicketActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel14.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel14.text")); // NOI18N

        tfDescuento.setEditable(false);
        tfDescuento.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tfDescuento.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.tfDescuento.text")); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/back.png"))); // NOI18N
        jButton2.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jButton2.text")); // NOI18N
        jButton2.setAlignmentX(0.5F);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel15.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel15.text")); // NOI18N

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel16.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel16.text")); // NOI18N

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel17.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel17.text")); // NOI18N

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel18.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel18.text")); // NOI18N

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel19.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel19.text")); // NOI18N

        cboVendedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboVendedor.setNextFocusableComponent(ftfDocumento);

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel22.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel22.text")); // NOI18N

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel23.setText(org.openide.util.NbBundle.getMessage(FrVentas.class, "FrVentas.jLabel23.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(6, 6, 6)
                                        .addComponent(tfDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel15)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tfSubtotalGral, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(btnConfirmarPesada, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(tfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(btnEmitirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel16)
                                .addGap(42, 42, 42)
                                .addComponent(jLabel17)
                                .addGap(61, 61, 61)
                                .addComponent(jLabel18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(239, 239, 239)
                                .addComponent(jLabel22)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel19))
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel14))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfSubtotalGral, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConfirmarPesada, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13))
                            .addComponent(btnEmitirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(8, 8, 8)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addGap(34, 34, 34))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ftfDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfDocumentoActionPerformed
        tfSaldo.setText("");
        cumpleanos = false;
        limpiarCamposArticulo();
        tfTotal.setText("");
        tfSubtotalGral.setText("");
        buscarClientePorDNI();
        cargarTabla();
        inicilizarListas();
        //articulo = new Articulo();
        listaVentaArticulos = new ArrayList<>();
        ftfDocumento.selectAll();
        tfCodigo.requestFocus();
        quitarDescuentoGral();
    }//GEN-LAST:event_ftfDocumentoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        buscarCliente();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnEmitirTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirTicketActionPerformed
        realizarVenta();
    }//GEN-LAST:event_btnEmitirTicketActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cerrar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tfCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCodigoActionPerformed
        buscarArticulo();
    }//GEN-LAST:event_tfCodigoActionPerformed

    private void btnConfirmarPesadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarPesadaActionPerformed
        agregar();
    }//GEN-LAST:event_btnConfirmarPesadaActionPerformed

    private void tfCodigoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusGained
        tfCodigo.setBackground(color);
    }//GEN-LAST:event_tfCodigoFocusGained

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        tfCodigo.setBackground(colorPorDefecto);
    }//GEN-LAST:event_tfCodigoFocusLost

    private void btnConfirmarPesadaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnConfirmarPesadaFocusGained
        btnConfirmarPesada.setBackground(color);
    }//GEN-LAST:event_btnConfirmarPesadaFocusGained

    private void btnConfirmarPesadaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnConfirmarPesadaFocusLost
        btnConfirmarPesada.setBackground(colorBoton);
    }//GEN-LAST:event_btnConfirmarPesadaFocusLost

    private void tfPesoBalanzaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPesoBalanzaKeyTyped
        // TODO add your handling code here:
        Comunes.soloNumeros(tfPesoBalanza, evt);
    }//GEN-LAST:event_tfPesoBalanzaKeyTyped

    private void tfCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCantidadKeyTyped
        // TODO add your handling code here:
        Comunes.soloNumeros(tfCantidad, evt);
    }//GEN-LAST:event_tfCantidadKeyTyped

    private void tfCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCantidadActionPerformed
        cargarArticuloPeso();
    }//GEN-LAST:event_tfCantidadActionPerformed

    private void ftfDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ftfDocumentoFocusGained
        tfSaldo.setText("");
        ftfDocumento.select(0, ftfDocumento.getText().length());
    }//GEN-LAST:event_ftfDocumentoFocusGained

    /**
     * @param args the command line arguments
     */
    private void main(String args[]) {

        FrVentas frVentas = new FrVentas();
        frVentas.setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmarPesada;
    private javax.swing.JButton btnEmitirTicket;
    private javax.swing.JComboBox<String> cboVendedor;
    private javax.swing.JFormattedTextField ftfDocumento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea taMensaje;
    public javax.swing.JTable tblArticulos;
    private javax.swing.JTextField tfCantidad;
    private javax.swing.JTextField tfCliente;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfDescripcion;
    private javax.swing.JTextField tfDescuento;
    private javax.swing.JTextField tfListaPrecio;
    private javax.swing.JTextField tfPesoBalanza;
    private javax.swing.JTextField tfPrecio;
    private javax.swing.JTextField tfPromocion;
    private javax.swing.JTextField tfSaldo;
    private javax.swing.JTextField tfSubTotalArticulo;
    private javax.swing.JTextField tfSubtotalGral;
    private javax.swing.JTextField tfTotal;
    // End of variables declaration//GEN-END:variables

    private void inicializarComponentes() {
        cboVendedor.requestFocus();
        Comunes.cargarJComboConBlanco(cboVendedor, UsuarioFacade.getInstance().listarTodosUsuarios());
        //ftfDocumento.requestFocus();
        precioPromocion.setScale(12, 2);
        eventosDeTeclas();
        //en caso de que no este la balanza
        if (frPrincipal.peso != null) {
            int delay = 100; //milliseconds (estaba en 40)
            ActionListener taskPerformer = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tfPesoBalanza.setText(frPrincipal.peso);

                }
            };
            new Timer(delay, taskPerformer).start();
        } else {
            tfPesoBalanza.setEditable(true);
        }

        botonesMultilinea();
        if (serialPort != null) {
            //  leerPuerto();
        } else {
            JOptionPane.showMessageDialog(null, "No se encontro el puerto");
        }
        ftfDocumento.setText("1");
        ftfDocumento.selectAll();
        cargarTabla();

    }

    private void buscarClientePorDNI() {
        cliente = ClienteFacade.getInstance().getPersonaXDni(ftfDocumento.getText());
        if (cliente != null) {
            ftfDocumento.setText(((Persona) cliente).getDocumentoIdentidad().getNumero());
            tfCliente.setText(cliente.toString());
            tfListaPrecio.setText(cliente.getListaPrecio().getDescripcion());
            //mostrar saldo Cuenta corriente cliente
            cargarSaldoCliente();
            if (cliente.getClass() == Persona.class) {
                verificarCumpleanos((Persona) cliente);
            }
        } else {
            buscarCliente();

        }

    }

    private void buscarCliente() {
        tfSaldo.setText("");
        limpiarCamposArticulo();
        tfTotal.setText("");
        tfSubtotalGral.setText("");
        DiagBuscarCliente diagBuscarCliente = new DiagBuscarCliente(null, true);
        diagBuscarCliente.setLocation(Comunes.centrarDialog(diagBuscarCliente));
        diagBuscarCliente.setVisible(true);
        if (diagBuscarCliente.getCliente() != null) {
            cliente = diagBuscarCliente.getCliente();
            if (cliente.getClass() == Persona.class) {
                verificarCumpleanos((Persona) cliente);
                try {
                    ftfDocumento.setText(((Persona) cliente).getDocumentoIdentidad().getNumero());
                } catch (Exception e) {

                }

            } else {
                try {
                    ftfDocumento.setText(((Organismo) cliente).getCUIT());
                } catch (Exception e) {

                }
            }
            cargarSaldoCliente();
            tfCliente.setText(cliente.toString());
            tfListaPrecio.setText(cliente.getListaPrecio().getDescripcion());
            tfCodigo.requestFocus();
            cargarTabla();
            inicilizarListas();
            listaVentaArticulos = new ArrayList<>();
            articulo = new Articulo();

        } else if (diagBuscarCliente.getCliente() != null) {
            cliente = diagBuscarCliente.getCliente();
            tfCliente.setText(cliente.toString());
            cargarSaldoCliente();
            tfCodigo.requestFocus();
            cargarTabla();
            inicilizarListas();
            listaVentaArticulos = new ArrayList<>();
            articulo = new Articulo();
        }

    }

    private void verificarCumpleanos(Persona persona) {
        if (persona.getFechaNacimiento() != null) {
            if (Comunes.cumpleanos(persona.getFechaNacimiento())) {
                //taMensaje.setText("FELIZ CUMPLEAÑOS!!");
                System.out.println("true cumpleaños");
                cumpleanos = true;
            } else {
                System.out.println("False cumpleaños");
                cumpleanos = false;
            }

        }

    }

    private void botonesMultilinea() {
        //botones multilinea
        String htmlButton = "<html><div align=\"center\">Confirmar Pesada</div></html>";
        btnConfirmarPesada.setText(htmlButton);
        String htmlButton2 = "<html><div align=\"center\">Emitir Ticket</div></html>";
        btnEmitirTicket.setText(htmlButton2);
        //
    }

    public void cerrar() {

        this.dispose();
    }

    @Override
    public void dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyDispaycher);
        keyDispaycher = null;
        cliente = null;
        articulo = null;
        descuento = null;
        subTotal = null;
        subTotalArticulo = null;
        total = null;
        listaVentaArticulos = null;
        super.dispose();
    }

    private void buscarArticulo() {
        taMensaje.setText("");
        if (cliente != null) {
            articulo = ArticuloFacade.getInstance().buscarPorCodigo(tfCodigo.getText());
            if (articulo != null) {
                if (PrecioArticuloFacade.getInstance().getComun(articulo, sucursal) != null) {
                    if (!articulo.getUnidadMedida().isEsPeso()) {
                        //Es por cantidad
                        limpiarCamposArticulo();
                        tfCodigo.setText(articulo.getCodigoBarra());
                        tfDescripcion.setText(articulo.getDescripcion());
                        tfCantidad.requestFocus();
                    } else {
                        //Es por peso
                        taMensaje.setText("");
                        tfCantidad.setText("");
                        cargarArticulo();
                        tfCodigo.selectAll();

                    }
                } else {
                    tfCodigo.setText("");
                }

            } else {
                DiagBuscarArticulo buscarArticulo = new DiagBuscarArticulo(null, true);
                buscarArticulo.setLocation(Comunes.centrarDialog(buscarArticulo));
                buscarArticulo.setVisible(true);
                if (buscarArticulo.getArticulo() != null) {
                    articulo = buscarArticulo.getArticulo();
                    if (PrecioArticuloFacade.getInstance().getComun(articulo, sucursal) != null) {

                        if (!articulo.getUnidadMedida().isEsPeso()) {
                            //Es por cantidad
                            limpiarCamposArticulo();
                            tfCodigo.setText(articulo.getCodigoBarra());
                            tfDescripcion.setText(articulo.getDescripcion());
                            tfCantidad.requestFocus();
                        } else {
                            //Es por peso
                            cargarArticulo();
                            tfCodigo.selectAll();
                        }
                    } else {
                        tfCodigo.setText("");
                    }

                }

            }

        } else {
            JOptionPane.showMessageDialog(null, "Primero debe seleccionar un Cliente");
            ftfDocumento.requestFocus();
        }

    }

    private void cargarArticulo() {
        inicilizarListas();
        tfCodigo.setText(articulo.getCodigoBarra());
        tfDescripcion.setText(articulo.getDescripcion());
        cargarPrecioArticulo();
        //si es lista comun se aplica promociones
        //PREGUNTAR SI ES EMPLEADO

        if (cliente.getListaPrecio().getDescripcion().toLowerCase().equals("comun")) {
            //buscamos las promociones Activas del articulo
//            System.out.println("Entroo lista precio comun");
            cargarPromocionesActivas();
            //verificamos cual asignar a la venta
            if (!listaPromocionesHabilitadas.isEmpty() || !listaPromocionesPorcentajeTodos.isEmpty()) {
//                System.out.println("Entro LIstrPromoHAbilitada");
                verificarPromocionAAsignar();
                //elegimos la produccion 
                elegirPromocionPorPrioridad();
                //cargamos datos promocion
                cargarPromocion();
            } else {
                verificarSiEsEmpleado();
            }
        } else {
            verificarSiEsEmpleado();
        }

        //Subtotal
        calcularSubtotalArticulo();

    }

    private void agregarArticulo() {
        if (validarArticulo()) {
            ventaArticulo = new VentaArticulo();
            ventaArticulo.setArticuloDescripcion(articulo.getDescripcionReducida());
            ventaArticulo.setArticuloCodigo(articulo.getCodigoBarra());
            //segun sea por peso o cantidad la venta
            if (!articulo.getUnidadMedida().isEsPeso()) {
                ventaArticulo.setCantidadPeso(BigDecimal.valueOf(Double.parseDouble(tfCantidad.getText())));
            } else {
                ventaArticulo.setCantidadPeso(BigDecimal.valueOf(Double.parseDouble(tfPesoBalanza.getText())));
            }
            ventaArticulo.setPrecio(subTotalArticulo);
            ventaArticulo.setPrecioPromocion(precioPromocion);
            ventaArticulo.setPrecioUnitario(precioComun.getPrecio());
            //Lista para cargar tabla
            listaVentaArticulos.add(ventaArticulo);
            cargarTablaArticulos(listaVentaArticulos);
            limpiarCamposArticulo();
            //agregados
            calcularSubtotalGral();
            quitarDescuentoGral();
            tfCodigo.setText("");
            tfCodigo.requestFocus();
            articulo = new Articulo();

        }

    }

    private void cargarTablaArticulos(List<VentaArticulo> listaVentaArticulos) {
        modeloTablaArticulos = new ModeloTablaNoEditable();
        cargarEncabezadosTablaArticulos(modeloTablaArticulos);
        configurarTabla(tblArticulos);
        try {
            cargarArticulos(listaVentaArticulos);
        } catch (Exception ex) {
            Logger.getLogger(FrVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarEncabezadosTablaArticulos(ModeloTablaNoEditable modeloTablaArticulos) {
        modeloTablaArticulos.addColumn("Id");
        modeloTablaArticulos.addColumn("Codigo");
        modeloTablaArticulos.addColumn("Articulo");
        modeloTablaArticulos.addColumn("Peso Neto/Cantidad");
        modeloTablaArticulos.addColumn("Precio U.");
        modeloTablaArticulos.addColumn("Promo");
        modeloTablaArticulos.addColumn("SubTotal");
        tblArticulos.setModel(modeloTablaArticulos);
    }

    private void configurarTabla(JTable tbl) {
        JViewport scroll = (JViewport) tbl.getParent();
        int ancho = scroll.getWidth();
        int anchoColumna = 0;
        TableColumnModel modeloColumna = tbl.getColumnModel();
        TableColumn columnaTabla;
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            columnaTabla = modeloColumna.getColumn(i);
            switch (i) {
                case 0:
                    anchoColumna = (1 * ancho) / 100;
                    break;
                case 1:
                    anchoColumna = (20 * ancho) / 100;
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    anchoColumna = (5 * ancho) / 100;
                    break;
            }
            columnaTabla.setPreferredWidth(anchoColumna);
            tbl.setColumnModel(modeloColumna);
        }
        tbl.getTableHeader().setFont(new java.awt.Font("Dialog",
                java.awt.Font.PLAIN, 10));
        tbl.getTableHeader().setBackground(java.awt.Color.WHITE);
        tbl.getTableHeader().setForeground(Color.BLACK);
        tbl.getTableHeader().setFont(new java.awt.Font("Dialog",
                java.awt.Font.PLAIN, 18));
        //Si le queremos cambiar el tamaño a la tablita
        tbl.setFont(new java.awt.Font("Dialog",
                java.awt.Font.PLAIN, 18));
    }

    private void cargarArticulos(List<VentaArticulo> listaVentaArticulos) {
        try {
            modeloTablaArticulos = new ModeloTablaNoEditable();
            cargarEncabezadosTablaArticulos(modeloTablaArticulos);
            for (VentaArticulo ventaArt : listaVentaArticulos) {
                cargarArticulos(ventaArt);
            }
            tblArticulos.setModel(modeloTablaArticulos);
            Comunes.setOcultarColumnasJTable(tblArticulos, 0);
        } catch (Exception ex) {
            Logger.getLogger(FrVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarArticulos(VentaArticulo ventaArticulo) {

        Object[] fila = new Object[7];
        fila[0] = ventaArticulo.getId();
        fila[1] = ventaArticulo.getArticuloCodigo();
        fila[2] = ventaArticulo.getArticuloDescripcion();
        fila[3] = new DecimalFormat("0.00").format(ventaArticulo.getCantidadPeso());
        fila[4] = ventaArticulo.getPrecioUnitario();
        fila[5] = new DecimalFormat("0.00").format(Double.parseDouble(ventaArticulo.getPrecioPromocion().toString()));
        fila[6] = new DecimalFormat("0.00").format(Double.parseDouble(ventaArticulo.getPrecio().toString()));
        modeloTablaArticulos.addRow(fila);
    }

    private void cambiarEstadoTFPesoBalanza() {
        if (tfCantidad.getText().length() > 0) {
            tfPesoBalanza.setEnabled(false);
        } else {
            tfPesoBalanza.setEnabled(true);
        }
    }

    private void calcularSubtotalGral() {
        subTotal = BigDecimal.ZERO;
        for (VentaArticulo va  : listaVentaArticulos) {
//            System.out.println("vaaa" + va.getPrecio());
            subTotal = subTotal.add(va.getPrecio());
//            System.out.println("suptotal: " + subTotal);
        }
        tfSubtotalGral.setText(new DecimalFormat("0.00").format(Double.parseDouble(subTotal.toString())));
        total = subTotal;
        tfTotal.setText(new DecimalFormat("0.00").format(Double.parseDouble(total.toString())));
    }

    public void mostrardiagDescuento() {
        if (!listaVentaArticulos.isEmpty()) {
            DiagDescuento diagDescuento = new DiagDescuento(null, true);
            diagDescuento.setLocation(Comunes.centrarDialog(diagDescuento));
            diagDescuento.setVisible(true);
            if (diagDescuento.getDescuento() != null) {
                descuento = diagDescuento.getDescuento();
                tfDescuento.setText(new DecimalFormat("0.00").format(Double.parseDouble(descuento.toString())));
                aplicarDescuentoGral();
                btnEmitirTicket.requestFocus();
            }
        } else if (cliente != null) {
            JOptionPane.showMessageDialog(null, "Debe cargar articulos");
            tfCodigo.requestFocus();

        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar cliente");
            ftfDocumento.requestFocus();
        }

    }

//    private void eventosDeTeclas() {
//        frPrincipal.manager.addKeyEventDispatcher(new KeyEventDispatcher() {
//            public boolean dispatchKeyEvent(KeyEvent e) {
//                if (e.getID() == KeyEvent.KEY_PRESSED) {
//                    if (e.getKeyCode() == KeyEvent.VK_F3) {
//                        agregar();
//
//                    }
//                    if (e.getKeyCode() == KeyEvent.VK_F7) {
//                        realizarVenta();
//                    }
//                    if (e.getKeyCode() == KeyEvent.VK_F4) {
//                        cerrar();
//                    }
//                    if (e.getKeyCode() == KeyEvent.VK_F10) {
//                        mostrardiagDescuento();
//                    }
//                    if (e.getKeyCode() == KeyEvent.VK_DELETE && tblArticulos.getSelectedRow() != -1) {
//                        quitarArticuloLista();
//
//                    }
//
//                }
//                return false;
//            }
//
//        });
//
//    }
    private void limpiarCamposArticulo() {
        tfCantidad.setText("");
        tfDescripcion.setText("");
        tfPrecio.setText("");
        tfPromocion.setText("");
        tfSubTotalArticulo.setText("");
        taMensaje.setText("");

    }

    private void verificarPromocionAAsignar() {
        listaPromocionesDeHoy = new ArrayList<>();
        listaPromocionesPorcentajeTodosDeHoy = new ArrayList<>();
        //verificamos que dia es hoy, para poder comparar con la promocion
        ObtenerDiaDeLaSemana();
        //verificamos cual promocion utilizar segun el dia y la prioridad
        for (Promocion promocion : listaPromocionesHabilitadas) {
            if (promocion.getDiasSemanas().contains(DiaSemana.valueOf(diaSemana))) {
                listaPromocionesDeHoy.add(promocion);
            }

        }
        //verificamos cual promocion procentaje todo a utilizar segun el dia y la prioridad
        for (Promocion promocion : listaPromocionesPorcentajeTodos) {
            if (promocion.getDiasSemanas().contains(DiaSemana.valueOf(diaSemana))) {
                listaPromocionesPorcentajeTodosDeHoy.add(promocion);
            }

        }
    }

    private void cargarPrecioArticulo() {
        precioComun = new PrecioArticulo();
        precioComun = PrecioArticuloFacade.getInstance().getComun(articulo, sucursal);
        tfPrecio.setText(precioComun.getPrecio().toString());
        precioArticulo = PrecioArticuloFacade.getInstance().get(articulo, cliente.getListaPrecio(), sucursal);
        tfPromocion.setText(precioArticulo.getPrecio().toString());
    }

    private void cargarPromocionesActivas() {
        //agregar aqui promocion porcentaje a todos(tratar de que sea en un solo select)
        listaPromocionesPorcentajeTodos = PromocionFacade.getInstance().buscarPorPorcentaje(sucursal);
//        System.out.println("listapromocionPorcentaje: " + listaPromocionesPorcentajeTodos);
        //promociones por articulo
        listaPromocionesHabilitadas = PromocionFacade.getInstance().buscarPorPromocionArticulo(articulo, sucursal);

    }

    private void ObtenerDiaDeLaSemana() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Comunes.obtenerFechaActualDesdeDB());
        int dia = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dia) {
            case 1:
                diaSemana = "DOMINGO";
                break;
            case 2:
                diaSemana = "LUNES";
                break;
            case 3:
                diaSemana = "MARTES";
                break;
            case 4:
                diaSemana = "MIERCOLES";
                break;
            case 5:
                diaSemana = "JUEVES";
                break;
            case 6:
                diaSemana = "VIERNES";
                break;
            case 7:
                diaSemana = "SABADO";
                break;
        }

    }

    private void elegirPromocionPorPrioridad() {
        //comparamos las dos listas las de porcentaje a todos y la de hablitadas
        if (!listaPromocionesDeHoy.isEmpty() && !listaPromocionesPorcentajeTodosDeHoy.isEmpty()) {
            //tomamos la de menor prioridad de ambas listas
            if (listaPromocionesDeHoy.get(0).getPrioridad() < listaPromocionesPorcentajeTodosDeHoy.get(0).getPrioridad()) {
                //como ya viene ordenada por prioridad de la query
                //tomamos la primera
                promocionSeleccionada = listaPromocionesDeHoy.get(0);

            } else {
                promocionSeleccionada = listaPromocionesPorcentajeTodosDeHoy.get(0);

            }

        } else if (!listaPromocionesDeHoy.isEmpty() && listaPromocionesPorcentajeTodosDeHoy.isEmpty()) {
            //tomamos la primera
            promocionSeleccionada = listaPromocionesDeHoy.get(0);

        } else if (listaPromocionesDeHoy.isEmpty() && !listaPromocionesPorcentajeTodosDeHoy.isEmpty()) {
            promocionSeleccionada = listaPromocionesPorcentajeTodosDeHoy.get(0);

        } else {
            promocionSeleccionada = null;
        }

    }

    private void cargarPromocion() {
        if (promocionSeleccionada != null) {
            //verificamos si la promo es de cumpleaños
            //y si el cliente cumpleaño
            if (promocionSeleccionada.getNombre().toLowerCase().trim().contains("cumpleaño")) {
                if (cumpleanos) {
                    System.out.println("entro cumpleaññoss");
                    taMensaje.setText("FELIZ CUMPLEAÑOS!!");
                    cargarPromocionDelArticulo();
                } else {
                    verificarSiEsEmpleado();
                    //cargarPrecioComun();
                }

            } else {

                // este es el caso de una promo que no es cumpleaños
                cargarPromocionDelArticulo();
            }

        } else {
            verificarSiEsEmpleado();
            //cargarPrecioComun();
        }
    }

    private void cargarPromocionDelArticulo() {
        //verificar si es por precio o porcentaje
        if (promocionSeleccionada.isEsPorPrecio()) {
            cargarPrecioPromocionArticulo();

        } else {
            cargarPorcentajePromocionArticulo();
        }
    }

    private void cargarPrecioPromocionArticulo() {
        //seleccionamos la promo articulo de la lista
        //existente en la promocion
        cargarPromoArticulo(promocionSeleccionada);
        //cargando Promo
        precioPromocion = promocionArticulo.getValor();
        taMensaje.setText(promocionSeleccionada.toString());
        tfPromocion.setText(new DecimalFormat("0.00").format(Double.parseDouble(promocionArticulo.getValor().toString())));
    }

    private void cargarPromoArticulo(Promocion promocionSeleccionada) {
        for (PromocionArticulo promoArt : promocionSeleccionada.getPromocionesArticulos()) {
            if (promoArt.getArticulo().equals(articulo)) {
                promocionArticulo = promoArt;

            }
        }
    }

    private void cargarPorcentajePromocionArticulo() {

        if (!promocionSeleccionada.getPromocionesArticulos().isEmpty()) {
            System.out.println("promo porcentaje para algunos" + promocionSeleccionada.getPromocionesArticulos().get(0));
            //promocion a algunos articulos
            cargarPromoArticulo(promocionSeleccionada);
            //multiplicamos el precio por el porcentaje de promocion del articulo;
            taMensaje.setText(promocionSeleccionada.toString());
            calcularPorcentaje(promocionArticulo.getValor());
        } else {
            //promocion a todos los articulos
            System.out.println("promocion seleccionada: " + promocionSeleccionada.getNombre());
            taMensaje.setText(promocionSeleccionada.toString());
            calcularPorcentaje(promocionSeleccionada.getPorcentajeATodos());
        }

    }

    private void calcularPorcentaje(BigDecimal valor) {
        //hacemos la operacion para calcular el precio resultante aplicando el descuento de la promocion
        precioPromocion = precioArticulo.getPrecio().subtract(precioArticulo.getPrecio().multiply(valor).divide(new BigDecimal(100)));
        tfPromocion.setText(new DecimalFormat("0.00").format(Double.parseDouble(precioPromocion.toString())));

    }

    private void cargarPrecioComun() {
        precioPromocion = precioArticulo.getPrecio();
        tfPromocion.setText(new DecimalFormat("0.00").format(Double.parseDouble(precioPromocion.toString())));
    }

    private void calcularSubtotalArticulo() {
        if (!articulo.getUnidadMedida().isEsPeso()) {
            //por cantidad

            subTotalArticulo = precioPromocion.multiply(BigDecimal.valueOf(Double.parseDouble(tfCantidad.getText())));
            tfSubTotalArticulo.setText(new DecimalFormat("0.00").format(Double.parseDouble(subTotalArticulo.toString())));
            btnConfirmarPesada.requestFocus();
        } else //por peso
        if (!tfPesoBalanza.getText().isEmpty()) {

            subTotalArticulo = precioPromocion.multiply(BigDecimal.valueOf(Double.parseDouble(tfPesoBalanza.getText())));
            tfSubTotalArticulo.setText(new DecimalFormat("0.00").format(Double.parseDouble(subTotalArticulo.toString())));
            btnConfirmarPesada.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Este es un articulo para pesar!");
            limpiarCamposArticulo();
            tfCodigo.requestFocus();

        }
    }

    private void inicilizarListas() {
        listaPromocionesHabilitadas = new ArrayList<>();
        listaPromocionesDeHoy = new ArrayList<>();
        promocionSeleccionada = new Promocion();
        promocionArticulo = new PromocionArticulo();

    }

    private boolean validarArticulo() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un cliente", "Mensaje", JOptionPane.ERROR_MESSAGE);
            ftfDocumento.requestFocus();
            return false;
        }

        if (articulo == null) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un articulo", "Mensaje", JOptionPane.ERROR_MESSAGE);
            tfCodigo.requestFocus();
            return false;
        }
        if (articulo.getId() != null) {
            if (!articulo.getUnidadMedida().isEsPeso()) {
                if (tfCantidad.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar cantidad y presionar Enter", "Mensaje", JOptionPane.ERROR_MESSAGE);
                    tfCantidad.requestFocus();
                    return false;

                }
            }
        }
        if (tfSubTotalArticulo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe presionar enter para cargar precio del articulo", "Mensaje", JOptionPane.ERROR_MESSAGE);
//            ftfCantidad.requestFocus();
            return false;

        }
        if (subTotalArticulo.compareTo(BigDecimal.ZERO) == 0) {
            JOptionPane.showMessageDialog(null,
                    "No se puede cargar precio/peso/cantidad nulos de articulos", "Mensaje", JOptionPane.ERROR_MESSAGE);
            tfCodigo.requestFocus();
            return false;
        }
        return true;
    }

    private void cargarTabla() {
        modeloTablaArticulos = new ModeloTablaNoEditable();
        cargarEncabezadosTablaArticulos(modeloTablaArticulos);
        configurarTabla(tblArticulos);
        Comunes.setOcultarColumnasJTable(tblArticulos, 0);
    }

    private void aplicarDescuentoGral() {
        //hacemos la operacion para calcular el precio resultante aplicando el descuento general
        montoDescuento = subTotal.multiply(descuento).divide(new BigDecimal(100));
        //asignamos nuevo total con descuento aplicado
        total = subTotal.subtract(montoDescuento);
        tfTotal.setText(new DecimalFormat("0.00").format(Double.parseDouble(total.toString())));

    }

    private void quitarDescuentoGral() {
        descuento = null;
        montoDescuento = new BigDecimal("0.00");
        tfDescuento.setText("");
    }

    private void cargarArticuloPeso() {
        if (cliente != null) {
            if (articulo != null) {
                cargarArticulo();
            } else {
                //JOptionPane.showMessageDialog(null, "Debe seleccionar un Articulo");
                tfCantidad.setText("");
                tfCodigo.requestFocus();
                tfCodigo.selectAll();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente");
            tfCantidad.setText("");
            ftfDocumento.requestFocus();
            ftfDocumento.selectAll();

        }
    }

    private void limpiarCampos() {
        //Comunes.cargarJComboConBlanco(cboVendedor, UsuarioFacade.getInstance().listarTodosUsuarios());
        cargarTabla();
        limpiarCamposArticulo();
        inicilizarListas();
        listaVentaArticulos = new ArrayList<>();
        tfCodigo.setText("");
        tfCantidad.setText("");
        ftfDocumento.setText("");
        tfCliente.setText("");
        taMensaje.setText("");
        tfDescuento.setText("");
        tfSubtotalGral.setText("");
        tfTotal.setText("");
        cliente = null;
        articulo = null;
        descuento = null;
        montoDescuento = new BigDecimal("0.00");
        subTotal = null;
        subTotalArticulo = null;
        total = null;
        ftfDocumento.requestFocus();
        ftfDocumento.setText("1");
        ftfDocumento.selectAll();
        cumpleanos = Boolean.FALSE;
        tfListaPrecio.setText("");

    }

    public void realizarVenta() {

        Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal);
        if (cajaAbierta != null) {
            if (validarVenta()) {
                venta = new Venta();
                venta.setFecha(Comunes.obtenerFechaActualDesdeDB());
                venta.setMonto(total);
                venta.setVentasArticulos(listaVentaArticulos);

                //verificamos si es empleado para setear el % de descuento
//            if (cliente.getClass() == Persona.class) {
//                if (EmpleadoFacade.getInstance().existeEmpleadoCliente(cliente)) {
//                    descuento = frPrincipal.descuentoEmpleado;
//                }
//            }
                venta.setDescuento(montoDescuento);
                if (cliente.getClass() == Persona.class) {
                    //ES PERSONA
                    venta.setCliente(((Persona) cliente).toString());
                    try {
                        venta.setDniCliente(((Persona) cliente).getDocumentoIdentidad().getNumero());
                    } catch (Exception e) {
                        venta.setDniCliente("");
                    }
                    venta.setEsPersona(true);

                } else {
                    //ES ORGANISMO
                    venta.setCliente(((Organismo) cliente).toString());
                    try {
                        venta.setDniCliente(((Organismo) cliente).getCUIT());
                    } catch (Exception e) {
                        venta.setDniCliente("");
                    }
                    venta.setEsPersona(false);
                }
                venta.setUsuario((Usuario) cboVendedor.getSelectedItem());
                venta.setSucursal(sucursal);
                try {
                    venta.setNumeroTicket(VentaFacade.getInstance().getUltimoNumeroTicket()+1);
                    VentaFacade.getInstance().alta(venta);
                    JOptionPane.showMessageDialog(null, "Venta realizada!");
                    limpiarCampos();
                    try {
                        new Impresora().imprimir(venta);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error imprimiendo, compruebe impresora!");

                    }
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyDispaycher);
                    keyDispaycher = null;
                    eventosDeTeclas();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR AL GENERAR EL NUMERO DE TICKET \n"+ e.getMessage());
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "La caja se cerro durante la venta. Debe abrir la caja");
        }
        tfSaldo.setText("");
    }

    public void seleccionarDocumento() {
        tfSaldo.setText("");
        ftfDocumento.requestFocus();
        ftfDocumento.select(0, ftfDocumento.getText().length());

    }

    public void seleccionarCodigo() {
        tfCodigo.requestFocus();
        tfCodigo.select(0, tfCodigo.getText().length());

    }

    private boolean validarVenta() {
        if (cboVendedor.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un Vendedor", "Mensaje", JOptionPane.ERROR_MESSAGE);
            cboVendedor.requestFocus();
            return false;
        }
        if (cliente == null) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar cliente", "Mensaje", JOptionPane.ERROR_MESSAGE);
            ftfDocumento.requestFocus();
            ftfDocumento.selectAll();
            return false;
        }
        if (listaVentaArticulos.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe cargar articulos", "Mensaje", JOptionPane.ERROR_MESSAGE);
            tfCodigo.requestFocus();
            tfCodigo.selectAll();
            return false;
        }
        if (total == null) {
            JOptionPane.showMessageDialog(null,
                    "Debe confirmar pesadas", "Mensaje", JOptionPane.ERROR_MESSAGE);
            tfCodigo.requestFocus();
            tfCodigo.selectAll();
            return false;
        }
        return true;
    }

    public void quitarArticuloLista() {

        int i = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el articulo seleccionado?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            int selectedRow = tblArticulos.getSelectedRow();
            listaVentaArticulos.remove(selectedRow);
            ((ModeloTablaNoEditable) tblArticulos.getModel()).removeRow(selectedRow);
            calcularSubtotalGral();
            tfCodigo.requestFocus();
            tfCodigo.selectAll();
            quitarDescuentoGral();
        }
    }

    public void agregar() {
        agregarArticulo();

    }

    private void eventosDeTeclas() {
        //Fz
        //agregar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "agregar");
        getRootPane().getActionMap().put("agregar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar();
            }
        });
//realizar venta
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "venta");
        getRootPane().getActionMap().put("venta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("entroooooooo realizar venta");
                realizarVenta();
            }
        });
//CARGAR DOCUMENTO
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "documento");
        getRootPane().getActionMap().put("documento", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarDocumento();
            }

        });
        //CARGAR CODIGO
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "codigo");
        getRootPane().getActionMap().put("codigo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarCodigo();
            }

        });
//cerrar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "cerrar");
        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrar();
            }
        });
        //descuento
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "descuento");
        getRootPane().getActionMap().put("descuento", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrardiagDescuento();
            }
        });
//borrar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "borrar");
        getRootPane().getActionMap().put("borrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblArticulos.getSelectedRow() != -1) {
                    quitarArticuloLista();
                }
            }
        });
//confirmar
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "confirmar");
        getRootPane().getActionMap().put("confirmar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblArticulos.isFocusOwner()) {
                    btnConfirmarPesada.requestFocus();
                }
            }
        });
        //agregado hugo
//        keyDispaycher = new KeyEventDispatcher() {
//
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent evt) {
//                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow().getClass().getSimpleName().equals("FrVentas")) {
//
//                    if (evt.getID() == KeyEvent.KEY_PRESSED) {
//                        if (evt.getKeyCode() == KeyEvent.VK_F3) {
//                            agregar();
//                        }
//                        if (evt.getKeyCode() == KeyEvent.VK_F7) {
//                            realizarVenta();
//                        }
//                        if (evt.getKeyCode() == KeyEvent.VK_F4) {
//                            cerrar();
//                        }
//                        if (evt.getKeyCode() == KeyEvent.VK_F10) {
//                            mostrardiagDescuento();
//                        }
//                        if (evt.getKeyCode() == KeyEvent.VK_DELETE && tblArticulos.getSelectedRow() != -1) {
//                            quitarArticuloLista();
//                        }
//                        if (evt.getKeyCode() == KeyEvent.VK_TAB && tblArticulos.isFocusOwner()) {
//                            btnConfirmarPesada.requestFocus();
//                        }
//                    }
//                }
//                return false;
//            }
//        };
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispaycher);
        //evento que actualiza subtotales de pesaje cuando cambia el contenido pesado en la balanza
        tfPesoBalanza.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                actualizarSubtotal();

            }

            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                actualizarSubtotal();

            }

            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                actualizarSubtotal();

            }

        });
    }

    private void cargarPromoEmpleado() {
        //probando comentar tamensaje para ver que pasa jajaj
        taMensaje.setText("Descuento Empleado " + frPrincipal.descuentoEmpleado + "%");
        calcularPorcentaje(frPrincipal.descuentoEmpleado);
    }

    private void verificarSiEsEmpleado() {
        if (cliente.getClass() == Persona.class) {
            if (EmpleadoFacade.getInstance().existeEmpleadoCliente(cliente)) {

                cargarPromoEmpleado();
            } else {

                cargarPrecioComun();
            }

        } else {

            cargarPrecioComun();

        }
    }

    private void actualizarSubtotal() {
        if (cliente != null) {
            if (articulo != null && articulo.getId() != null) {
                if (articulo.getUnidadMedida().isEsPeso()) {
                    if (!tfPesoBalanza.getText().isEmpty()) {
                        //cargarArticulo();

                        subTotalArticulo = precioPromocion.multiply(BigDecimal.valueOf(Double.parseDouble(tfPesoBalanza.getText())));
                        tfSubTotalArticulo.setText(new DecimalFormat("0.00").format(Double.parseDouble(subTotalArticulo.toString())));
                        //btnConfirmarPesada.requestFocus();
                    }

                }
            }
        }
    }

    private void abrirCobrarTicket() {
        Caja cajaAbierta = CajaFacade.getInstance().getCajaAbierta(sucursal, usuario);
        if (cajaAbierta != null) {
            DiagCobroTicket2 diagCobroTicket = new DiagCobroTicket2(this, true, usuario, sucursal, cajaAbierta);
            diagCobroTicket.setLocation(Comunes.centrarDialog(diagCobroTicket));
            diagCobroTicket.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No abrio la caja!");
        }
    }

    private void cargarSaldoCliente() {
        String saldo;
        List<Object[]> saldosClientes = CuentaCorrienteFacade.getInstance().getSaldosClientes(cliente);
        if (clienteTieneCuentaCorriente(cliente)) {
            for (Object[] objects : saldosClientes) {
                try {
                    saldo = String.valueOf(objects[0]);
                    BigDecimal valor = new BigDecimal(saldo);
                    BigDecimal cero = new BigDecimal(0);
                    if(valor.compareTo(cero)>=0){
                      JOptionPane.showMessageDialog(rootPane,"El cliente No posee saldo a favor para realizar \n "
                              + "compras en Cuenta Corriente", "Advertencia",JOptionPane.WARNING_MESSAGE);
                    }
                    tfSaldo.setText(String.valueOf(objects[0]));
                } catch (Exception e) {
                    System.err.println("errrorrrrrrr");
                    tfSaldo.setText("");
                }
            }
        } else {
            tfSaldo.setText("");
        }

    }

    private boolean clienteTieneCuentaCorriente(Cliente cliente) {
        List<CuentaCorriente> listaCuentas = CuentaCorrienteFacade.getInstance().getCuentasCCliente(cliente);
        return !listaCuentas.isEmpty();
    }

}
