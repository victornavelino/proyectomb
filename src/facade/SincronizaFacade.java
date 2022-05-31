/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.CajaJpaController;
import controladores.ClienteJpaController;
import controladores.LocalidadJpaController;
import controladores.OrganismoJpaController;
import controladores.PersonaJpaController;
import controladores.PromocionJpaController;
import entidades.Configuracion;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.articulo.PrecioArticulo;
import entidades.articulo.SubCategoria;
import entidades.articulo.TipoIva;
import entidades.caja.Caja;
import entidades.cliente.Organismo;
import entidades.cliente.Persona;
import entidades.persona.CorreoElectronico;
import entidades.persona.DocumentoIdentidad;
import entidades.persona.Domicilio;
import entidades.persona.Sexo;
import entidades.persona.Telefono;
import entidades.persona.TipoDocumento;
import entidades.persona.TipoTelefono;
import entidades.promocion.DiaSemana;
import entidades.promocion.Promocion;
import entidades.venta.CierreVentas;
import entidades.venta.Venta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author hugo
 */
public class SincronizaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static SincronizaFacade instance = null;
    SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
    private Domicilio domicilio;

    public SincronizaFacade() {
    }

    /**
     * Genera el Excel con los clientes (personas)
     *
     *
     * @return Archivo que exportar clientes
     */
    public File exportarClientesPersona() {

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

        return crearExcel(tabla, "persona");
    }

    public File exportarClientesPersonaDomicilio() {

        PersonaJpaController controller = new PersonaJpaController(emf);
        List<Persona> listadoPersonas = controller.findPersonaEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("dniPersona");
        fila.add("BarrioPersona");
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

        for (Persona persona : listadoPersonas) {
            fila = new ArrayList<>();

            fila.add(persona.getDocumentoIdentidad().getNumero());
            try {
                fila.add(persona.getDomicilio().getBarrio());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getCalle());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getCodigoPostal());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getDpto());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getEntreCalles());
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(persona.getDomicilio().getLocalidad().getId());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getNumero());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getPiso());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(persona.getDomicilio().getReferencia());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }
        return crearExcel(tabla, "PersonaDomicilio");
    }

    public File exportarClientesPersonasTelefono() {
//        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
//        PersonaJpaController controller = new PersonaJpaController(emfa);
        List<Persona> listadoPersonas = ClienteFacade.getInstance().getPersonas();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("dnitelefonopersona");
        fila.add("tipo telefono");
        fila.add("telefonopersona");
        //add
        tabla.add(fila);

        for (Persona persona : listadoPersonas) {
            for (Telefono telefono : persona.getTelefonos()) {
                fila = new ArrayList<>();
                fila.add(persona.getDocumentoIdentidad().getNumero());
                fila.add(telefono.getTipoTelefono().getDescripcion());
                fila.add(telefono.getNumero());
                tabla.add(fila);
            }

        }
        return crearExcel(tabla, "PersonaTelefono");
    }

    public File exportarClientesPersonaMail() {
//        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
//        PersonaJpaController controller = new PersonaJpaController(emfa);
        List<Persona> listadoPersonas = ClienteFacade.getInstance().getPersonas();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("dnimailpersona");
        fila.add("Mail");
        //add
        tabla.add(fila);

        for (Persona persona : listadoPersonas) {
            for (CorreoElectronico ce : persona.getCorreosElectronicos()) {
                fila = new ArrayList<>();
                fila.add(persona.getDocumentoIdentidad().getNumero());
                fila.add(ce.getDireccion());
                tabla.add(fila);
            }

        }
        return crearExcel(tabla, "PersonaMail");
    }

    /**
     * Genera el Excel con los clientes (organismos)
     *
     *
     * @return archivo que exporta organismos
     */
    public File exportarClientesOrganismos() {

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
        return crearExcel(tabla, "organismo");
    }

    public File exportarClientesOrganismosDomicilio() {

        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("cuitorganismodomicilio");
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

            fila.add(organismo.getCUIT());
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
        return crearExcel(tabla, "organismoDomicilio");
    }

    public File exportarClientesOrganismosMail() {
        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("cuitorganismomail");
        fila.add("Mail");
        //add
        tabla.add(fila);

        for (Organismo organismo : listadoOrganismos) {
            for (CorreoElectronico ce : organismo.getCorreosElectronicos()) {
                fila = new ArrayList<>();
                fila.add(organismo.getCUIT());
                fila.add(ce.getDireccion());
                tabla.add(fila);
            }

        }
        return crearExcel(tabla, "organismoMail");
    }

    public File exportarClientesOrganismosTelefono() {
        OrganismoJpaController controller = new OrganismoJpaController(emf);
        List<Organismo> listadoOrganismos = controller.findOrganismoEntities();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("cuitorganismotelefono");
        fila.add("tipo telefono");
        fila.add("telefono");
        //add
        tabla.add(fila);

        for (Organismo organismo : listadoOrganismos) {
            for (Telefono telefono : organismo.getTelefonos()) {
                fila = new ArrayList<>();
                fila.add(organismo.getCUIT());
                fila.add(telefono.getTipoTelefono().getDescripcion());
                fila.add(telefono.getNumero());
                tabla.add(fila);
            }

        }
        return crearExcel(tabla, "organismoTelefono");
    }

    /**
     * Crea una hoja Excel con el contenido especificado.
     *
     * @param lista List con los datos a escribir en la hoja.
     * @param nombre nombre de la hoja.
     */
    private File crearExcel(List lista, String nombre) {
        try {
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
            File tempFile = File.createTempFile(nombre, null);
            tempFile.deleteOnExit();
            try (FileOutputStream archivo = new FileOutputStream(tempFile)) {
                libro.write(archivo);
            }
            System.out.println("Se exportaron los " + nombre);
            return tempFile;
        } catch (IOException ex) {
            System.out.println("Error exportando " + nombre + ": " + ex);
            return null;
        }

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

    /**
     * Para leer un excel y ponerlo en una lista
     *
     * @param file
     * @param File Archivo a importar.
     * @return
     *
     */
    public List<List<Object>> leerDatosExcel(File file) {
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getColumns(); i++) {
                jxl.Cell cell1 = sheet.getCell(i, 0);
                fila.add(cell1.getContents());
            }
            tabla.add(fila);
            for (int j = 1; j < sheet.getRows(); j++) {
                fila = new ArrayList<>();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    jxl.Cell cell = sheet.getCell(i, j);
                    fila.add(cell.getContents());
                }
                tabla.add(fila);
            }
            return tabla;
        } catch (BiffException | IOException e) {
            System.out.println("Error: " + e);
            return null;

        }
    }

    public void importarClientesPersona(List<List<Object>> lista) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        ClienteJpaController clienteJpaController = new ClienteJpaController(entityManagerFactory);
        try {
            for (int i = 1; i < lista.size(); i++) {

                if (!ClienteFacade.getInstance().getPersonaDni(lista.get(i).get(3).toString())) {

                    Persona persona = new Persona();
                    DocumentoIdentidad documento = new DocumentoIdentidad();
                    TipoDocumento tipoDoc = new TipoDocumento();
                    persona.setApellido(lista.get(i).get(0).toString());
                    persona.setNombre(lista.get(i).get(1).toString());

//                    if (lista.get(i).get(2).toString().equals("DNI")) {
                    tipoDoc = (TipoDocumento) TipoDocumentoFacade.getInstance().buscarPorTipo(lista.get(i).get(2).toString());
//                    }

                    documento.setNumero(lista.get(i).get(3).toString());
                    documento.setTipoDocumento(tipoDoc);
                    persona.setDocumentoIdentidad(documento);
                    try {
                        persona.setSexo(Sexo.valueOf(lista.get(i).get(5).toString()));
                    } catch (Exception e) {
                        persona.setSexo(null);
                    }

                    try {
                        persona.setFechaNacimiento(formatFecha.parse(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        ListaPrecio listaPrecio = ListaPrecioFacade.getInstance().getPorDescripcion(lista.get(i).get(6).toString());
                        persona.setListaPrecio(listaPrecio);
                    } catch (Exception e) {
                    }
                    clienteJpaController.create(persona);
                }
            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            System.out.println("Error :" + e);
        }
    }

    public void importarClientesPersonaDomicilio(List<List<Object>> lista) throws Exception {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        ClienteJpaController clienteJpaController = new ClienteJpaController(entityManagerFactory);
        for (int i = 1; i < lista.size(); i++) {
            Persona persona = ClienteFacade.getInstance().getPersonaXDni(lista.get(i).get(0).toString());
            Domicilio domicilio = new Domicilio();
            domicilio.setBarrio(lista.get(i).get(1).toString());
            domicilio.setCalle(lista.get(i).get(2).toString());
            domicilio.setCodigoPostal(lista.get(i).get(3).toString());
            domicilio.setDpto(lista.get(i).get(4).toString());
            domicilio.setEntreCalles(lista.get(i).get(5).toString());
            try {
                domicilio.setLocalidad(LocalidadFacade.getInstance().buscar(Long.parseLong(lista.get(i).get(6).toString())));
            } catch (Exception e) {
            }
            domicilio.setNumero(lista.get(i).get(7).toString());
            domicilio.setPiso(lista.get(i).get(8).toString());
            domicilio.setReferencia(lista.get(i).get(9).toString());
            persona.setDomicilio(domicilio);
            clienteJpaController.edit(persona);
        }
    }

    public void importarClientesPersonaTelefonos(List<List<Object>> lista) throws Exception {
        System.out.println("Persona Telefno entro");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        ClienteJpaController clienteJpaController = new ClienteJpaController(entityManagerFactory);
        for (int i = 1; i < lista.size(); i++) {
            Persona persona = ClienteFacade.getInstance().getPersonaXDni(lista.get(i).get(0).toString());
            if (persona.getCorreosElectronicos().isEmpty()) {
                Telefono telefono = new Telefono();
                telefono.setTipoTelefono(TipoTelefonoFacade.getInstance().buscarPorTipo(lista.get(i).get(1).toString()));
                telefono.setNumero(lista.get(i).get(2).toString());
                persona.getTelefonos().add(telefono);
                clienteJpaController.edit(persona);
            }

        }
    }

    public void importarClientesOrganismo(List<List<Object>> lista) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        ClienteJpaController clienteJpaController = new ClienteJpaController(entityManagerFactory);
        EntityManager em = emf.createEntityManager();
        for (int i = 1; i < lista.size(); i++) {
            String cuitStr = lista.get(i).get(1).toString();
            if (ClienteFacade.getInstance().buscarCuitEmpresaObjeto(cuitStr, em) == null) {
                Organismo organismo = new Organismo();
                organismo.setRazonSocial(lista.get(i).get(0).toString());
                organismo.setCUIT(lista.get(i).get(1).toString());
                try {
                    ListaPrecio listaPrecio = ListaPrecioFacade.getInstance().getPorDescripcion(lista.get(i).get(2).toString(), em);
                    organismo.setListaPrecio(listaPrecio);
                } catch (Exception e) {
                }
                clienteJpaController.create(organismo);
            }

        }
        System.out.println("Importación Exitosa");

//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "" + e);
//        }
    }

    public File exportarCajas() {
        //Buscamos la sucursal
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
        String codSucursal = configSucursal.getValor();
        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
        List<Caja> listaCajas = CajaFacade.getInstance().getCajasCerradas(sucursal);
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("cajasucursal");
        fila.add("Usuario");
        fila.add("Fecha Apertura");
        fila.add("Fecha Cierre");
        fila.add("Caja Inicial");
        fila.add("Caja Final");

        //add
        tabla.add(fila);

        for (Caja caja : listaCajas) {
            fila = new ArrayList<>();
            try {
                fila.add(caja.getSucursal().getCodigo());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(caja.getUsuario().getNombreUsuario());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(formats.format(caja.getFechaInicio()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(formats.format(caja.getFechaFin()));
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(caja.getCajaInicial());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(caja.getCajaFinal());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }
        return crearExcel(tabla, "caja");
    }

    public void importarCajas(List<List<Object>> lista) {
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI LA CAJA YA FUE IMPORTADA ANTERIORMENTE
                if (!CajaFacade.getInstance().existeCajaSucursal(formats.parse(lista.get(i).get(2).toString()), lista.get(i).get(0).toString(), lista.get(i).get(1).toString())) {

                    Caja caja = new Caja();
                    try {
                        caja.setSucursal(SucursalFacade.getInstance().buscarPorCodigo(lista.get(i).get(0).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        caja.setUsuario(new UsuarioFacade().buscarPorNombre(lista.get(i).get(1).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        caja.setFechaInicio(formats.parse(lista.get(i).get(2).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        caja.setFechaFin(formats.parse(lista.get(i).get(3).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        caja.setCajaInicial(new BigDecimal(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        caja.setCajaFinal(new BigDecimal(lista.get(i).get(5).toString()));
                    } catch (Exception e) {
                    }

                    CajaFacade.getInstance().alta(caja);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarCierreVentas() {
        //Buscamos la sucursal
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
        String codSucursal = configSucursal.getValor();
        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
        List<CierreVentas> listaCierreVentas = CierreVentasFacade.getInstance().listarCierreVentas(sucursal);
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

        return crearExcel(tabla, "cierreventas");
    }

    public void importarCierreVentas(List<List<Object>> lista) {
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI LA CAJA YA FUE IMPORTADA ANTERIORMENTE
                if (!CierreVentasFacade.getInstance().existeCierreVentaSucursal(Integer.valueOf(lista.get(i).get(4).toString()), lista.get(i).get(0).toString())) {
                    CierreVentas cierreVentas = new CierreVentas();
                    try {
                        cierreVentas.setSucursal(SucursalFacade.getInstance().buscarPorCodigo(lista.get(i).get(0).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setCantidad(Integer.valueOf(lista.get(i).get(1).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setFecha(formats.parse(lista.get(i).get(2).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setImporte(new BigDecimal(lista.get(i).get(3).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setNumeroCierre(Integer.valueOf(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setTicketDesde(Integer.valueOf(lista.get(i).get(5).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        cierreVentas.setTicketDesde(Integer.valueOf(lista.get(i).get(6).toString()));
                    } catch (Exception e) {
                    }

                    CierreVentasFacade.getInstance().alta(cierreVentas);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarVentas() {
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

        return crearExcel(tabla, "ventas");
    }

    public void importarVentas(List<List<Object>> lista) {
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI LA VENTA YA FUE IMPORTADA ANTERIORMENTE
                if (!VentaFacade.getInstance().getVentaNumeroTicketSucursal(lista.get(i).get(5).toString(), lista.get(i).get(0).toString())) {

                    Venta venta = new Venta();
                    try {
                        venta.setSucursal(SucursalFacade.getInstance().buscarPorCodigo(lista.get(i).get(0).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setAnulado(Boolean.getBoolean(lista.get(i).get(1).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setDescuento(new BigDecimal(lista.get(i).get(2).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setFecha(formats.parse(lista.get(i).get(3).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setMonto(new BigDecimal(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setNumeroTicket(Integer.valueOf(lista.get(i).get(5).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        Persona persona = ClienteFacade.getInstance().getPersonaXDni(lista.get(i).get(6).toString());
                        if (persona != null) {
                            venta.setCliente(persona.toString());
                        } else {
                            Organismo organismo = ClienteFacade.getInstance().getOrganismo(lista.get(i).get(6).toString());
                            if (organismo != null) {
                                venta.setCliente(organismo.toString());
                            }
                        }

                    } catch (Exception e) {
                    }
                    try {
                        venta.setUsuario(new UsuarioFacade().buscarPorNombre(lista.get(i).get(7).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        venta.setCierreVentas(CierreVentasFacade.getInstance().listarCierreVentas(Integer.valueOf(lista.get(i).get(8).toString())));
                    } catch (Exception e) {
                    }

                    VentaFacade.getInstance().alta(venta);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarArticulos() {
        //Buscamos la sucursal

        List<Articulo> articulos = ArticuloFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Descripcion");
        fila.add("DescripcionReducida");
        fila.add("CodigoBarra");
        fila.add("SubcategoriaId");
        fila.add("UnidadDeMedidaId");
        //add
        tabla.add(fila);
        for (Articulo articulo : articulos) {
            fila = new ArrayList<>();
            try {
                fila.add(articulo.getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(articulo.getDescripcionReducida());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(articulo.getCodigoBarra());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(articulo.getSubCategoria().getId());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(articulo.getUnidadMedida().getId());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }

        return crearExcel(tabla, "articulos");
    }

    public void importarArticulos(List<List<Object>> lista) {
        Articulo articulo = new Articulo();
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI EL ARTICULO  YA FUE IMPORTADO ANTERIORMENTE
                if (ArticuloFacade.getInstance().buscarCodigoBarra(lista.get(i).get(2).toString())) {
                    //MODIFICAR ARTICULO

                    articulo = ArticuloFacade.getInstance().buscarPorCodigo(lista.get(i).get(2).toString());

                    try {
                        articulo.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setDescripcionReducida(lista.get(i).get(1).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setCodigoBarra(lista.get(i).get(2).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setSubCategoria(SubCategoriaFacade.getInstance().buscarPorDescripcion(lista.get(i).get(3).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setUnidadMedida(UnidadMedidaFacade.getInstance().buscaDescripcion(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    ArticuloFacade.getInstance().modificar(articulo);

                } else {
                    //nuevo articulo
                    articulo = new Articulo();

                    try {
                        articulo.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setDescripcionReducida(lista.get(i).get(1).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setCodigoBarra(lista.get(i).get(2).toString());
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setSubCategoria(SubCategoriaFacade.getInstance().buscarPorDescripcion(lista.get(i).get(3).toString()));
                    } catch (Exception e) {
                    }
                    try {
                        articulo.setUnidadMedida(UnidadMedidaFacade.getInstance().buscaDescripcion(lista.get(i).get(4).toString()));
                    } catch (Exception e) {
                    }
                    ArticuloFacade.getInstance().alta(articulo);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarSubcategoria() {
        //Buscamos la sucursal

        List<SubCategoria> subcategorias = SubCategoriaFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Descripcion");
        fila.add("Categoria");
        //add
        tabla.add(fila);
        for (SubCategoria subCategoria : subcategorias) {
            fila = new ArrayList<>();
            try {
                fila.add(subCategoria.getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(subCategoria.getCategoria().getId());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }

        return crearExcel(tabla, "subcategoria");
    }

    public void importarSubcategorias(List<List<Object>> lista) {
        SubCategoria subCategoria = new SubCategoria();
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI la SubCATEGORIA  YA FUE IMPORTADA ANTERIORMENTE
                if (SubCategoriaFacade.getInstance().buscarPorDescripcion(lista.get(i).get(0).toString()) != null) {
                    //MODIFICAR ARTICULO

                    subCategoria = SubCategoriaFacade.getInstance().buscarPorDescripcion(lista.get(i).get(0).toString());

                    try {
                        subCategoria.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        subCategoria.setCategoria(CategoriaFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(1).toString())));
                    } catch (Exception e) {
                    }

                    SubCategoriaFacade.getInstance().modificar(subCategoria);

                } else {
                    //nueva subcategoria
                    subCategoria = new SubCategoria();

                    try {
                        subCategoria.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        subCategoria.setCategoria(CategoriaFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(1).toString())));
                    } catch (Exception e) {
                    }
                    SubCategoriaFacade.getInstance().alta(subCategoria);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarCategoria() {
        //Buscamos la sucursal

        List<Categoria> categorias = CategoriaFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Descripcion");
        fila.add("TipoIva");
        //add
        tabla.add(fila);
        for (Categoria categoria : categorias) {
            fila = new ArrayList<>();
            try {
                fila.add(categoria.getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(categoria.getTipoIva().getId());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }

        return crearExcel(tabla, "categoria");
    }

    public void importarCategorias(List<List<Object>> lista) {
        Categoria categoria = new Categoria();
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI la CATEGORIA  YA FUE IMPORTADA ANTERIORMENTE
                if (CategoriaFacade.getInstance().buscarPorDescripcionIgualUnico(lista.get(i).get(0).toString()) != null) {
                    //MODIFICAR ARTICULO

                    categoria = CategoriaFacade.getInstance().buscarPorDescripcionIgualUnico(lista.get(i).get(0).toString());

                    try {
                        categoria.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        categoria.setTipoIva(TipoIvaFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(1).toString())));
                    } catch (Exception e) {
                    }

                    CategoriaFacade.getInstance().modificar(categoria);

                } else {
                    //nueva subcategoria
                    categoria = new Categoria();

                    try {
                        categoria.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        categoria.setTipoIva(TipoIvaFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(1).toString())));
                    } catch (Exception e) {
                    }
                    CategoriaFacade.getInstance().alta(categoria);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarTipoIva() {
        //Buscamos la sucursal

        List<TipoIva> tipoIvas = TipoIvaFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Descripcion");
        fila.add("Porcentaje");
        //add
        tabla.add(fila);
        for (TipoIva tipoIva : tipoIvas) {
            fila = new ArrayList<>();
            try {
                fila.add(tipoIva.getDescripcion());
            } catch (Exception e) {
                fila.add("");
            }
            try {
                fila.add(tipoIva.getPorcentaje());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }

        return crearExcel(tabla, "tipoiva");
    }

    public void importarTipoIva(List<List<Object>> lista) {
        TipoIva tipoIva = new TipoIva();
        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VERIFICAMOS SI ELTIPOIVA YA FUE IMPORTADA ANTERIORMENTE
                if (TipoIvaFacade.getInstance().getXDescripcion(lista.get(i).get(0).toString()) != null) {
                    //MODIFICAR ARTICULO

                    tipoIva = TipoIvaFacade.getInstance().getXDescripcion(lista.get(i).get(0).toString());

                    try {
                        tipoIva.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        tipoIva.setPorcentaje(new BigDecimal(lista.get(i).get(1).toString()));
                    } catch (Exception e) {
                    }

                    TipoIvaFacade.getInstance().modificar(tipoIva);

                } else {
                    //nueva subcategoria
                    tipoIva = new TipoIva();

                    try {
                        tipoIva.setDescripcion(lista.get(i).get(0).toString());
                    } catch (Exception e) {
                    }
                    try {
                        tipoIva.setPorcentaje(new BigDecimal(lista.get(i).get(1).toString()));
                    } catch (Exception e) {
                    }
                    TipoIvaFacade.getInstance().alta(tipoIva);
                }

            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }
    }

    public File exportarPromociones() {
        String backPromocion = "pg_dump -i -h localhost -p 5432 -U postgres -f /promocion.sql carniceria -t promocion_articulo -t promocion -t promocion_promocion_articulo";
        try {
            Runtime.getRuntime().exec(backPromocion);
        } catch (IOException ex) {
            Logger.getLogger(SincronizaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crearArchivoSql("/promocion.sql");
    }

    public void importarPromociones(File file) {
        String rutaSql = file.getAbsolutePath();
        System.out.println("contenido Archivo: " + file.getAbsolutePath());
        PromocionFacade.getInstance().borrarPromociones();
        try {

            Process p = Runtime.getRuntime().exec("psql -h localhost -U postgres -d carniceria -f " + rutaSql);

            // Se obtiene el stream de salida del programa 
            InputStream is = p.getInputStream();

            /* Se prepara un bufferedReader para poder leer la salida más comodamente. */
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Se lee la primera linea 
            String aux = br.readLine();

            // Mientras se haya leido alguna linea 
            while (aux != null) {
                // Se escribe la linea en pantalla 
                System.out.println(aux);

                // y se lee la siguiente. 
                aux = br.readLine();
            }

            System.out.println("Promociones Importadas");
        } catch (Exception ex) {
            System.out.println("Error Importando Articulos: " + ex);
        }
    }

    public void importarSubcategorias(File file) {
        String rutaSql = file.getAbsolutePath();
        System.out.println("ruta articuloss: " + rutaSql);
        ArticuloFacade.getInstance().borrarArticulos();
        try {

            Process p = Runtime.getRuntime().exec("psql -h localhost -U postgres -d carniceria -f " + rutaSql);

            // Se obtiene el stream de salida del programa 
            InputStream is = p.getInputStream();

            /* Se prepara un bufferedReader para poder leer la salida más comodamente. */
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Se lee la primera linea 
            String aux = br.readLine();

            // Mientras se haya leido alguna linea 
            while (aux != null) {
                // Se escribe la linea en pantalla 
                System.out.println(aux);

                // y se lee la siguiente. 
                aux = br.readLine();
            }

        } catch (IOException ex) {
            System.out.println("Error Importando Subcategorias: " + ex);
        }
    }

//    public void importarPromociones(List<List<Object>> lista) {
//        Promocion promocion = new Promocion();
//        SimpleDateFormat formats = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//        //Buscamos la sucursal
//        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
//        String codSucursal = configSucursal.getValor();
//        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);
//        try {
//            for (int i = 1; i < lista.size(); i++) {
//                //PRIMERO VERIFICAMOS si la promocion YA FUE IMPORTADA ANTERIORMENTE
//
//                if (PromocionFacade.getInstance().buscarPorSucursalyDescripcion(sucursal, lista.get(i).get(0).toString()) != null) {
//                    //MODIFICAR Promocion
//
//                    promocion = PromocionFacade.getInstance().buscarPorSucursalyDescripcion(sucursal, lista.get(i).get(0).toString());
//
//                    try {
//                        promocion.setFechaInicio(formats.parse(lista.get(i).get(1).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setFechaFin(formats.parse(lista.get(i).get(2).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setEsPorPrecio(Boolean.getBoolean(lista.get(i).get(3).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setDiasSemanas((List<DiaSemana>) lista.get(i).get(4));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setPorcentajeATodos(new BigDecimal(lista.get(i).get(5).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setHabilitada(Boolean.getBoolean(lista.get(i).get(6).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setPrioridad(Integer.parseInt(lista.get(i).get(7).toString()));
//                    } catch (Exception e) {
//                    }
//
//                    PromocionFacade.getInstance().modificar(promocion);
//
//                } else {
//                    //nueva promocion
//                    promocion = new Promocion();
//                    try {
//                        promocion.setNombre(lista.get(i).get(0).toString());
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setFechaInicio(formats.parse(lista.get(i).get(1).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setFechaFin(formats.parse(lista.get(i).get(2).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setEsPorPrecio(Boolean.getBoolean(lista.get(i).get(3).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setDiasSemanas((List<DiaSemana>) lista.get(i).get(4));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setPorcentajeATodos(new BigDecimal(lista.get(i).get(5).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setHabilitada(Boolean.getBoolean(lista.get(i).get(6).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//
//                        promocion.setPrioridad(Integer.parseInt(lista.get(i).get(7).toString()));
//                    } catch (Exception e) {
//                    }
//                    try {
//                        promocion.setSucursal(sucursal);
//                    } catch (Exception e) {
//                    }
//                    PromocionFacade.getInstance().alta(promocion);
//                }
//
//            }
//            System.out.println("Importación Exitosa");
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "" + e);
//        }
//    }
    public File exportarListaPrecios() {

        List<ListaPrecio> listaPrecio = ListaPrecioFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Descripcion");

        //add
        tabla.add(fila);
        for (ListaPrecio lista : listaPrecio) {
            fila = new ArrayList<>();
            try {
                fila.add(lista.getDescripcion());
            } catch (Exception e) {
                fila.add("");
                tabla.add(fila);

            }

        }
        return crearExcel(tabla, "ListaPrecios");
    }

    public void importarListaPrecios(List<List<Object>> lista) {
        try {
            for (int i = 1; i < lista.size(); i++) {
                if (!ListaPrecioFacade.getInstance().getExisteDescripcion(lista.get(i).get(0).toString())) {
                    ListaPrecio listaPrecio = new ListaPrecio();
                    listaPrecio.setDescripcion(lista.get(i).get(0).toString());
                    ListaPrecioFacade.getInstance().alta(listaPrecio);
                }
            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            System.out.println("Error :" + e);
        }
    }

    public File exportarPreciosArticulo() {

        List<PrecioArticulo> precioArticulo = PrecioArticuloFacade.getInstance().getTodos();
        List<List<Object>> tabla = new ArrayList<>();
        List<Object> fila = new ArrayList<>();
        fila.add("Precio");
        fila.add("Sucursal");
        fila.add("Articulo");
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
                fila.add(precio.getSucursal());
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(precio.getArticulo());
            } catch (Exception e) {
                fila.add("");
            }

            try {
                fila.add(precio.getListaPrecio());
            } catch (Exception e) {
                fila.add("");
            }

            tabla.add(fila);

        }
        return crearExcel(tabla, "PrecioArticulo");
    }

//    public void importarPreciosArticulos(File file) {
//        String rutaSql = file.getAbsolutePath();
//        System.out.println("ruta Precioarticuloss: " + rutaSql);
//        PrecioArticuloFacade.getInstance().borrarPrecioArticulos();
//        try {
//
//            Process p = Runtime.getRuntime().exec("psql -h localhost -U postgres -d carniceria -f " + rutaSql);
//
//            // Se obtiene el stream de salida del programa 
//            InputStream is = p.getInputStream();
//
//            /* Se prepara un bufferedReader para poder leer la salida más comodamente. */
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            // Se lee la primera linea 
//            String aux = br.readLine();
//
//            // Mientras se haya leido alguna linea 
//            while (aux != null) {
//                // Se escribe la linea en pantalla 
//                System.out.println(aux);
//
//                // y se lee la siguiente. 
//                aux = br.readLine();
//            }
//
//        } catch (IOException ex) {
//            System.out.println("Error Importando PrecioArticulos: " + ex);
//        }
//    }
    public void importarPreciosArticulos(List<List<Object>> lista) {
        PrecioArticulo precioArticulo = new PrecioArticulo();
        //Buscamos la sucursal
        Configuracion configSucursal = ConfiguracionFacade.getInstance().buscar("sucursal");
        String codSucursal = configSucursal.getValor();
        Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(codSucursal);

        try {
            for (int i = 1; i < lista.size(); i++) {
                //PRIMERO VEMOS SI EL PRECIO ES PARA ESTA SUCURSAL
                if (lista.get(i).get(1).toString().equals(sucursal.getCodigo())) {

                    //SEGUNDO VERIFICAMOS SI EL PRECIOARTICULO YA FUE IMPORTADO ANTERIORMENTE
                    String listaPrecio = lista.get(i).get(3).toString();
                    String codigoSucursal = sucursal.getCodigo();
                    String codigoArticulo = lista.get(i).get(2).toString();
                    if (PrecioArticuloFacade.getInstance().existePrecioArticulo(listaPrecio, codigoSucursal, codigoArticulo)) {

                        precioArticulo = PrecioArticuloFacade.getInstance().getPrecioArticuloPorCodigo(lista.get(i).get(3).toString(), sucursal.getCodigo(), lista.get(i).get(2).toString());

                        try {
                            precioArticulo.setPrecio(new BigDecimal(lista.get(i).get(0).toString()));
                        } catch (Exception e) {
                        }
                        PrecioArticuloFacade.getInstance().modificar(precioArticulo);

                    } else {
                        try {
                            //nuevo Precio Articulo
                            precioArticulo = new PrecioArticulo();

                            precioArticulo.setPrecio(new BigDecimal(lista.get(i).get(0).toString()));

                            precioArticulo.setSucursal(sucursal);

                            precioArticulo.setArticulo(ArticuloFacade.getInstance().buscarPorCodigo(lista.get(i).get(2).toString()));

                            precioArticulo.setListaPrecio(ListaPrecioFacade.getInstance().getPorDescripcion(lista.get(i).get(3).toString()));

                            PrecioArticuloFacade.getInstance().alta(precioArticulo);
                        } catch (Exception e) {
                            System.out.println("Error:" + e);
                        }
                    }
                }
            }
            System.out.println("Importación Exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "fdfdf" + e);
        }
    }

    private File crearArchivoSql(String backPromocionbackup) {

        File file = new File(backPromocionbackup);
        System.out.println(file.exists());
        return file;

    }

    public void importarClientesOrganismoDomicilio(List<List<Object>> lista) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        LocalidadJpaController ljc = new LocalidadJpaController(emfa);
        ClienteJpaController cjc = new ClienteJpaController(emfa);
        for (int i = 1; i < lista.size(); i++) {
            Organismo organismo = ClienteFacade.getInstance().buscarCuitEmpresaObjeto(lista.get(i).get(0).toString(), ema);
            if (organismo != null) {
                Domicilio domicilio = new Domicilio();
                domicilio.setBarrio(lista.get(i).get(1).toString());
                domicilio.setCalle(lista.get(i).get(2).toString());
                domicilio.setCodigoPostal(lista.get(i).get(3).toString());
                domicilio.setDpto(lista.get(i).get(4).toString());
                domicilio.setEntreCalles(lista.get(i).get(5).toString());
                try {
                    domicilio.setLocalidad(ljc.findLocalidad(Long.parseLong(lista.get(i).get(6).toString())));
                } catch (Exception e) {
                }
                domicilio.setNumero(lista.get(i).get(7).toString());
                domicilio.setPiso(lista.get(i).get(8).toString());
                domicilio.setReferencia(lista.get(i).get(9).toString());
                try {
                    organismo.setDomicilio(domicilio);
                    cjc.edit(organismo);
                } catch (Exception e) {
                }
            }
        }
    }

    public void importarClientesOrganismoMail(List<List<Object>> lista) throws Exception {
        System.out.println("Organismo mail entro");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emf.createEntityManager();
        ClienteJpaController cjc = new ClienteJpaController(emf);
        for (int i = 1; i < lista.size(); i++) {
            Organismo organismo = ClienteFacade.getInstance().buscarCuitEmpresaObjeto(lista.get(i).get(0).toString(), em);
            if (organismo.getCorreosElectronicos().isEmpty()) {
                CorreoElectronico correoElectronico = new CorreoElectronico();
                correoElectronico.setDireccion(lista.get(i).get(1).toString());
                organismo.getCorreosElectronicos().add(correoElectronico);
                cjc.edit(organismo);
            }

        }

    }

    public void importarClientesPersonaMail(List<List<Object>> lista) throws Exception {
        System.out.println("Persona mail entro");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        ClienteJpaController cjc = new ClienteJpaController(emf);
        for (int i = 1; i < lista.size(); i++) {
            Persona persona = ClienteFacade.getInstance().getPersonaXDni(lista.get(i).get(0).toString());
            if (persona.getCorreosElectronicos().isEmpty()) {
                CorreoElectronico correoElectronico = new CorreoElectronico();
                correoElectronico.setDireccion(lista.get(i).get(1).toString());
                persona.getCorreosElectronicos().add(correoElectronico);
                cjc.edit(persona);
            }

        }

    }

    public void importarClientesOrganismoTelefonosl(List<List<Object>> lista) {
        System.out.println("Organismo Telefno entro");
        for (int i = 1; i < lista.size(); i++) {
            Organismo organismo = ClienteFacade.getInstance().buscarCuitEmpresaObjeto(lista.get(i).get(0).toString());
            if (organismo.getTelefonos().isEmpty()) {
                Telefono telefono = new Telefono();
                telefono.setTipoTelefono(TipoTelefonoFacade.getInstance().buscarPorTipo(lista.get(i).get(1).toString()));
                telefono.setNumero(lista.get(i).get(2).toString());
                organismo.getTelefonos().add(telefono);
                ClienteFacade.getInstance().modificar(organismo);
            }

        }
    }

    public void importarSucursales(List<List<Object>> lista) {
        System.out.println("Importando Sucursales");
        for (int i = 1; i < lista.size(); i++) {
            Sucursal sucursal = SucursalFacade.getInstance().buscarPorCodigo(lista.get(i).get(1).toString());
            if (sucursal == null) {
                sucursal = new Sucursal();
                sucursal.setCodigo(lista.get(i).get(1).toString());
                sucursal.setNombre(lista.get(i).get(0).toString());
                domicilio = new Domicilio();
                try {
                    domicilio.setBarrio(lista.get(i).get(2).toString());

                } catch (Exception e) {
                }
                try {
                    domicilio.setCalle(lista.get(i).get(3).toString());
                } catch (Exception e) {
                }
                try {
                    domicilio.setEntreCalles(lista.get(i).get(4).toString());
                } catch (Exception e) {
                }
                try {
                    domicilio.setLocalidad(LocalidadFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(5).toString())));
                } catch (Exception e) {
                }
                sucursal.setDomicilio(domicilio);
                SucursalFacade.getInstance().alta(sucursal);
            }
            sucursal.setCodigo(lista.get(i).get(1).toString());
            sucursal.setNombre(lista.get(i).get(0).toString());
            domicilio = new Domicilio();
            try {
                domicilio.setBarrio(lista.get(i).get(2).toString());

            } catch (Exception e) {
            }
            try {
                domicilio.setCalle(lista.get(i).get(3).toString());
            } catch (Exception e) {
            }
            try {
                domicilio.setEntreCalles(lista.get(i).get(4).toString());
            } catch (Exception e) {
            }
            try {
                domicilio.setLocalidad(LocalidadFacade.getInstance().buscar(Long.valueOf(lista.get(i).get(5).toString())));
            } catch (Exception e) {
            }
            sucursal.setDomicilio(domicilio);
            SucursalFacade.getInstance().modificar(sucursal);
        }
    }
}
