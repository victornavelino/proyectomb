package ExportadoreExcel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controladores.OrganismoJpaController;
import controladores.PersonaJpaController;
import entidades.Configuracion;
import entidades.Sucursal;
import entidades.articulo.PrecioArticulo;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.persona.CorreoElectronico;
import entidades.venta.CierreVentas;
import entidades.venta.Venta;
import facade.CierreVentasFacade;
import facade.ConexionFacade;
import facade.ConfiguracionFacade;
import facade.PrecioArticuloFacade;
import facade.SincronizaFacade;
import facade.SucursalFacade;
import facade.VentaFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class TestExportador {

    SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    public TestExportador() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    public void exportarClientesOrganismosDomicilio() {

        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Razon Social");
        fila.add("Barrio");
        fila.add("Calle");
        fila.add("CodigoPostal");
        fila.add("Dpto");
        fila.add("EntreCalles");
        fila.add("LocalidadId");
        fila.add("nroCalle");
        fila.add("Piso");
        fila.add("Referencia");

        //add
        tabla.add(fila);

        for (Organismo organismo : listadoOrganismos) {
            fila = new ArrayList<>();

            fila.add(organismo.getRazonSocial());
            try {
                fila.add(organismo.getDomicilio().getBarrio());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getCalle());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getCodigoPostal());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getDpto());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getEntreCalles());
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(organismo.getDomicilio().getLocalidad().getId());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getNumero());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getPiso());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(organismo.getDomicilio().getReferencia());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }
        crearExcel(tabla, "organismoDomicilio");
    }

    public void exportarClientesOrganismosMail() {
        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Razon Social");
        fila.add("Mail");
        //add
        tabla.add(fila);

        for (Organismo organismo : listadoOrganismos) {
            for (CorreoElectronico ce : organismo.getCorreosElectronicos()) {
                fila = new ArrayList<>();
                fila.add(organismo.getRazonSocial());
                fila.add(ce.getDireccion());
                tabla.add(fila);
            }

        }
        crearExcel(tabla, "organismoMail");
    }

    public void exportarClientesPersona() {

        PersonaJpaController controller = new PersonaJpaController(emf);
        List<Persona> listadoPersonas = controller.findPersonaEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Apellido");
        fila.add("Nombre");
        fila.add("tipo Documento dni");
        fila.add("Nro Dni");
        fila.add("Fecha Nacimiento");
        fila.add("Sexo");
        fila.add("Lista de Precios");
        //add
        tabla.add(fila);

        for (Persona persona : listadoPersonas) {
            fila = new ArrayList<>();
            fila.add(persona.getApellido());
            fila.add(persona.getNombre());
            try {
                fila.add(persona.getDocumentoIdentidad().getTipoDocumento().toString());
            } catch (Exception e) {
                fila.add("");

            }
            try {
                fila.add(persona.getDocumentoIdentidad().getNumero());
            } catch (Exception e) {
                fila.add("");

            }
            try {
                fila.add(formatFecha.format(persona.getFechaNacimiento()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getSexo().toString());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getListaPrecio().getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            tabla.add(fila);
        }

        crearExcel(tabla, "persona");
    }

    public void exportarClientesOrganismos() {

        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Razon Social");
        fila.add("cuit");
        fila.add("Lista de Precios");

        //add
        tabla.add(fila);

        for (Organismo organismo : listadoOrganismos) {
            fila = new ArrayList<>();

            fila.add(organismo.getRazonSocial());
            fila.add(organismo.getCUIT());
            try {
                fila.add(organismo.getListaPrecio().getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            tabla.add(fila);

        }
        crearExcel(tabla, "organismo");
    }

    public void exportarSucursales() {

        List<Sucursal> listaSucursales = SucursalFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("NombreSucursal");
        fila.add("CodigoSucursal");
        fila.add("Barrio");
        fila.add("Calle");
        fila.add("EntreCalles");
        fila.add("Localidad");

        //add
        tabla.add(fila);
        for (Sucursal sucursal : listaSucursales) {
            fila = new ArrayList<>();
            try {
                fila.add(sucursal.getNombre());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(sucursal.getCodigo());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(sucursal.getDomicilio().getBarrio());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(sucursal.getDomicilio().getCalle());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(sucursal.getDomicilio().getEntreCalles());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(sucursal.getDomicilio().getLocalidad().getId());
            } catch (Exception e) {
                fila.add("");
            }
            tabla.add(fila);

        }
        crearExcel(tabla, "Sucursales");
    }

    public void exportarArticulo() {
        String backArticulo = "pg_dump -i -h localhost -p 5432 -U postgres -f /articulo.sql carniceria -t tipo_iva -t categoria -t subcategoria -t articulo";

        try {
            Runtime.getRuntime().exec(backArticulo);

        } catch (IOException ex) {
            Logger.getLogger(SincronizaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        crearArchivoSql("/tmp/articulo.sql");
    }

//    public void exportarPrecioArticulo() {
//        String backArticulo = "pg_dump -i -h localhost -p 5432 -U postgres -f /precart.sql carniceria -t articulo_precio";
//
//        try {
//            Runtime.getRuntime().exec(backArticulo);
//
//        } catch (IOException ex) {
//            Logger.getLogger(SincronizaFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        crearArchivoSql("/precart.sql");
//    }

    private void crearArchivoSql(String backPromocionbackup) {

        File file = new File(backPromocionbackup);
        System.out.println(file.exists());

    }

    private void crearArchivoSqltemp(String backPromocionbackup) {

        try {
            File tempFile = File.createTempFile(backPromocionbackup, null);
            tempFile.deleteOnExit();
            FileOutputStream archivo = new FileOutputStream(tempFile);
            System.out.println("Entro temppfile");
        } catch (IOException ex) {

        }

    }
    public void exportarPreciosArticulo() {

        List<PrecioArticulo> precioArticulo = PrecioArticuloFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("PrecioArticulo");
        fila.add("CodigoSucursal");
        fila.add("CodigoArticulo");
        fila.add("ListaPrecio");

        //add
        tabla.add(fila);
        for (PrecioArticulo precio : precioArticulo) {
            fila = new ArrayList<>();
            try {
                fila.add(precio.getPrecio());
            } catch (Exception e) {
                fila.add("");

            }
            try {
                fila.add(precio.getSucursal().getCodigo());
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(precio.getArticulo().getCodigoBarra());//mod
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(precio.getListaPrecio().getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }
        crearExcel(tabla, "PrecioArticulo");
    }
   public void exportarVentas() {
        //Buscamos la sucursal
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
        String codSucursal = configSucursal.getValor();
        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
        List<Venta> listaVentas = VentaFacade.getInstance().getVentasSucursal(sucursal);
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("SucursalVentas");
        fila.add("Anulado");
        fila.add("Descuento");
        fila.add("Fecha");
        fila.add("Monto");
        fila.add("NroTicket");
        fila.add("Cliente");
        fila.add("DNICliente");
        fila.add("Es Persona");
        fila.add("Usuario");
        fila.add("NroCierreVenta");
        //add
        tabla.add(fila);
        for (Venta venta : listaVentas) {
            fila = new ArrayList<>();
            try {
                fila.add(venta.getSucursal().getCodigo());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(String.valueOf(venta.isAnulado()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getDescuento());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(formats.format(venta.getFecha()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getMonto());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getNumeroTicket());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getCliente());

            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getDniCliente());

            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(String.valueOf(venta.isEsPersona()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getUsuario().getNombreUsuario());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(venta.getCierreVentas().getNumeroCierre());
            } catch (Exception e) {
                fila.add("");
            }
            tabla.add(fila);

        }

    crearExcel(tabla, "ventas");
    }
     public void exportarCierreVentas() {
        //Buscamos la sucursal
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
//        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
//        String codSucursal = configSucursal.getValor();
//        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
        List<CierreVentas> listaCierreVentas = CierreVentasFacade.getInstance().listarCierreVentas();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("SucursalCierre");
        fila.add("Cantidad");
        fila.add("Fecha");
        fila.add("Importe");
        fila.add("NroCierre");
        fila.add("TicketDesde");
        fila.add("TicketHasta");

        //add
        tabla.add(fila);
        for (CierreVentas cierreVentas : listaCierreVentas) {
            fila = new ArrayList<>();
            try {
                fila.add(cierreVentas.getSucursal().getCodigo());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(cierreVentas.getCantidad());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(formats.format(cierreVentas.getFecha()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(cierreVentas.getImporte());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(cierreVentas.getNumeroCierre());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(cierreVentas.getTicketDesde());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(cierreVentas.getTicketHasta());
            } catch (Exception e) {
                fila.add("");
            }
            tabla.add(fila);

        }

        crearExcel(tabla, "cierreventas");
    }

    /**
     * Crea una hoja Excel con el contenido especificado.
     *
     * @param lista List con los datos a escribir en la hoja.
     * @param nombre nombre de la hoja.
     */
    private void crearExcel(List lista, String nombre) {
//        try {
        HSSFWorkbook libro = new HSSFWorkbook();
        // Se crea una hoja dentro del libro
        HSSFSheet hoja = libro.createSheet();
        int filas = lista.size();
        for (int i = 0; i < filas; i++) {
            List<Object> fila = (List<Object>) lista.get(i);
            Row row = hoja.createRow((short) i);
            int j = 0;
            for (Object token : fila) {
                createCell(row, j, token);
                j = j + 1;
            }
        }
        //////
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel", "xls");
        fc.setFileFilter(filter);
        fc.showSaveDialog(null);//Muestra el diálogo
        File guardar = fc.getSelectedFile();
        if (guardar != null) {
            try {
                FileOutputStream archivo = new FileOutputStream(guardar + ".xls");
                libro.write(archivo);
                archivo.close();
                JOptionPane.showMessageDialog(null, "Exportado Correctamente");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al Exportar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }/////////
//            File tempFile = File.createTempFile(nombre, null);
//            //tempFile.deleteOnExit();
//            try (FileOutputStream archivo = new FileOutputStream(tempFile)) {
//                libro.write(archivo);
//            }
//            System.out.println("Se exportaron los " + nombre);
//            return tempFile;
//        } catch (IOException ex) {
//            System.out.println("Error exportando " + nombre + ": " + ex);
//            return null;
//        }

    }

    /**
     * Para escribir el contenido de una celda.
     *
     * @param row Row.
     * @param i posicion en la fila.
     * @param value texto a escribir.
     */
    public void createCell(Row row, int i, Object value) {
        Cell cell = row.createCell(i);
        if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    @Test
    public void exportarOrg() {
        //exportarClientesOrganismos();
        //exportarClientesPersona();
        // exportarClientesOrganismosMail();
        //exportarClientesOrganismosDomicilio();
        //exportarArticulo();
        //exportarPreciosArticulo();
        //exportarSucursales();
        exportarVentas();
        //exportarCierreVentas();
    }
}
