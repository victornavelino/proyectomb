/**
 * Franco Zurita Perea 10 Feb 2015
 *
 * Transmitir archivos por un socket
 */
package Recursos.sincro;

import facade.SincronizaFacade;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Conecta con el servidor y sincroniza los archivos.
 *
 * @author
 */
public class SincronizaCliente {

    public final static int FILE_SIZE = 6022386;
    public InetAddress ip;
    public String host = "latradicioncentral.ddns.net";
    public int port = 4700;

    /**
     * Main del Cliente
     *
     * @param args de la l�nea de comandos. Se ignora.
     */
    public static void main(String[] args) {
//     Se crea el cliente y se le manda a sincronizar
        
        SincronizaCliente cliente = new SincronizaCliente();
        cliente.pedir();
        cliente.enviar();

    }

    /**
     * Establece comunicación con el servidor en el puerto indicado. Pide el
     * fichero. Cuando llega, lo escribe en pantalla y en disco duro.
     *
     * @param servidor host donde está el servidor
     * @param puerto Puerto de conexión
     * @param pedido Puerto de conexión
     * @return archivo
     * @throws java.io.IOException
     *
     */
    public File pedido(String servidor, int puerto, String pedido) throws IOException {
        int timeout = 30000;
        int bytesRead = 0;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket socket = null;
        socket = new Socket(servidor, puerto);

        System.out.println("Conectando con el servidor...");
        socket.setSoTimeout(timeout);

        // Se envía un mensaje de petición de archivo.
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(socket
                .getOutputStream());

        oos.writeObject(pedido);

        System.out.println("Solicitando archivos (" + pedido + ")"
                + "...");
        // Se abre un fichero para empezar a copiar lo que se reciba.

        File tempFile = null;

        // recibe archivo
        byte[] mybytearray = new byte[FILE_SIZE];
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        String nombre = dis.readUTF();
        int tamanio = dis.readInt();

        tempFile = File.createTempFile(nombre.substring(0, 10), null);

        tempFile.deleteOnExit();

        fos = new FileOutputStream(tempFile);

        bos = new BufferedOutputStream(fos);

        bytesRead = bis.read(mybytearray, 0, mybytearray.length);

        current = bytesRead;

        do {

            bytesRead
                    = bis.read(mybytearray, current, (mybytearray.length - current));

            if (bytesRead >= 0) {
                current += bytesRead;
            }
        } while (bytesRead > -1);

        bos.write(mybytearray, 0, current);

        bos.flush();

        System.out.println("Archivo " + tempFile
                + " descargado (" + current + " bytes leídos)");

        fos.close();

        bos.close();

        bis.close();
        socket.close();

        System.out.println(tempFile);
        if (current == tamanio) {
            System.out.println("El archivo se descargo correctamente");
            return tempFile;
        } else {
            System.out.println("El archivo se descargo con errores");
            return null;
        }

    }

    /**
     * Establece comunicación con el servidor en el puerto indicado. Pide el
     * fichero. Cuando llega, lo escribe en pantalla y en disco duro.
     *
     * @param servidor host donde está el servidor
     * @param puerto Puerto de conexión
     * @param envio Archivo
     * @throws java.io.IOException
     *
     */
    public void envio(String servidor, int puerto, File envio) throws IOException {
        ip= InetAddress.getByName(servidor);
        System.out.println("IP traducido: "+ip.getHostAddress());
        int timeout = 30000;
        Socket socket = null;
        socket = new Socket(ip.getHostAddress(), puerto);

        System.out.println("Conectando con el servidor...");
        socket.setSoTimeout(timeout);

        // Se envía un mensaje de petición de archivo.
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(socket
                .getOutputStream());
        FileInputStream ficheroStream = new FileInputStream(envio);
        byte contenido[] = new byte[(int) envio.length()];
        ficheroStream.read(contenido);

        oos.writeObject(contenido);

        System.out.println("Enviando archivos (" + envio + ")"
                + "...");

        oos.flush();

        System.out.println("Archivo " + envio
                + " enviado (" + envio.length() + " bytes leídos)");

        oos.close();

        socket.close();

    }

    public void envio2(String servidor, int puerto, File tempFile) throws IOException {
        int timeout = 30000;
        Socket socket = null;
        socket = new Socket(servidor, puerto);

        System.out.println("Conectando con el servidor...");
        socket.setSoTimeout(timeout);

        int tamañoArchivo = (int) tempFile.length();
        // Creamos el flujo de salida, este tipo de flujo nos permite 
        // hacer la escritura de diferentes tipos de datos tales como
        // Strings, boolean, caracteres y la familia de enteros, etc.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        System.out.println("Enviando Archivo: " + tempFile.getName());
        // Enviamos el nombre del archivo 
        dos.writeUTF(tempFile.getName());

        // Enviamos el tamaño del archivo
        dos.writeInt(tamañoArchivo);
        // Creamos flujo de entrada para realizar la lectura del archivo en bytes
        FileInputStream fis = new FileInputStream(tempFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        // Creamos el flujo de salida para enviar los datos del archivo en bytes
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

        // Creamos un array de tipo byte con el tamaño del archivo 
        byte[] buffer = new byte[tamañoArchivo];

        // Leemos el archivo y lo introducimos en el array de bytes 
        bis.read(buffer);

        // Realizamos el envio de los bytes que conforman el archivo
        for (int i = 0; i < buffer.length; i++) {
            bos.write(buffer[i]);
        }

        System.out.println("Archivo " + tempFile.getName()
                + " enviado (" + tempFile.length() + " bytes leídos)");
        // Cerramos socket y flujos
        bis.close();
        bos.close();
        socket.close();

    }

    private void abrirArchivo(File archivo) {
        if (Desktop.isDesktopSupported() == true) {
            Desktop desktop = Desktop.getDesktop();
            try {
                if (archivo.exists() == true) {
                    desktop.open(archivo);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontrar el archivo: " + archivo.getAbsolutePath(), "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede ejecutar el comando de apertura en este sistema operativo", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void readXLS(File xls) {
        try {
            Workbook workbook = Workbook.getWorkbook(xls);
            /* Si existen hojas */
            if (workbook.getNumberOfSheets() > 0) {
                Sheet hoja = workbook.getSheet(0); /* obtiene solo la primera hoja */
                /* forma el array para los nombres de las columnas del JTable */

                /* Forma la matriz para los datos */
                Object[][] data = new String[hoja.getRows()][hoja.getColumns()];
                /* Recorre todas las celdas*/
                for (int fila = 0; fila < hoja.getRows(); fila++) {
                    for (int columna = 0; columna < hoja.getColumns(); columna++) {

                        /* Lee celda y coloca en el array */
                        data[fila][columna] = hoja.getCell(columna, fila).getContents();
                    }
                }

                for (int i = 0; i < data.length; i++) {
                    System.out.print(Arrays.asList(data[i]) + " - ");
                }

            }

        } catch (IOException ex) {
            System.err.println(ex.toString());
        } catch (BiffException ex) {
            System.err.println(ex.toString());
        }
    }

    public void pedir() {
        int errorNro = 0;
        String errorDesc = "";
        try {
            recibirFichero(pedido(host, port, "pedidolistaprecios"));
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 1;
        }
        try {
            if (errorNro != 1) {
                recibirFichero(pedido(host, port, "pedidoclientesorganismo"));
            }
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 2;
        }
        try {
            if (errorNro != 2) {
                recibirFichero(pedido(host, port, "pedidoclientesorganismodomicilio"));
            }
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 3;
        }
        try {
            if (errorNro != 3) {
                recibirFichero(pedido(host, port, "pedidoclientesorganismomail"));
            }
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 4;
        }
        try {
            if (errorNro != 4) {
                recibirFichero(pedido(host, port, "pedidoclientesorganismotelefono"));
            }
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 5;
        }
        try {
            if (errorNro != 5) {
                recibirFichero(pedido(host, port, "pedidoclientespersona"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 6;
        }
        try {
            if (errorNro != 6) {
                recibirFichero(pedido(host, port, "pedidoclientespersonadomicilio"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 7;
        }
        try {
            if (errorNro != 7) {
                recibirFichero(pedido(host, port, "pedidoclientespersonatelefono"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 8;
        }
        try {
            if (errorNro != 8) {
                recibirFichero(pedido(host, port, "pedidoclientespersonamail"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 9;
        }
        try {
            if (errorNro != 9) {
                recibirFichero(pedido(host, port, "pedidosucursales"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 10;
        }
        //pedidosubcategorias
        try {
            if (errorNro != 10) {
                recibirFichero(pedido(host, port, "pedidosubcategorias"));
            }
        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 11;
        }
        try {
            if (errorNro != 11) {
                recibirFichero(pedido(host, port, "pedidoarticulos"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 12;
        }
        try {
            if (errorNro != 12) {
                recibirFichero(pedido(host, port, "pedidopromociones"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 13;
        }
        try {
            if (errorNro != 13) {
                recibirFichero(pedido(host, port, "pedidoprecioarticulos"));
//                recibirFichero(pedido(host, port, "pedidoarticulos"));
            }

        } catch (IOException ex) {
            errorDesc = errorDesc.concat(ex.toString());
            errorNro = 14;
        }
        try {
            if (errorNro != 14) {
                recibirFichero(pedido(host, port, "pedidopromociones"));
                recibirFichero(pedido(host, port, "pedidopromociones"));
            }

        } catch (IOException e) {
            errorNro = 15;
        }
        try {
            if (errorNro != 15) {
                recibirFichero(pedido(host, port, "pedidoprecioarticulos"));
            }

        } catch (IOException e) {
            errorNro = 16;
        }

        if (errorNro != 0) {
            System.out.println("Error sincronizando\n Error " + errorNro
                    + "\n " + errorDesc);
            // System.exit(1);

        } else {

            System.out.println("Recepcion exitosa");
            // System.exit(0);

        }
//        try {
//            Runtime.getRuntime().exec("rm -R /tmp/*.tmp");
//        } catch (IOException ex) {
//            Logger.getLogger(SincronizaCliente.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void recibirFichero(File file) {
        SincronizaFacade sincronizaFacade = new SincronizaFacade();
        //aca se hace el metodo para importar
        //Promocionofull
        if (file.getAbsolutePath().contains("promocion")) {
            System.out.println("Leyendo Promociones:");
            sincronizaFacade.importarPromociones(file);
        } else if (file.getAbsolutePath().contains("subcateg")) {
            System.out.println("Leyendo Subcategorias:");
            sincronizaFacade.importarSubcategorias(file);
        }  else {
            List<List<Object>> leerDatosExcel = sincronizaFacade.leerDatosExcel(file);
            try {
                if (leerDatosExcel.get(0).get(0).equals("Apellido")) {
                    System.out.println("Leyendo Personas:");
                    sincronizaFacade.importarClientesPersona(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("NombreSucursal")) {
                    System.out.println("Leyendo Sucursales:");

                    sincronizaFacade.importarSucursales(leerDatosExcel);

                }else if (leerDatosExcel.get(0).get(0).equals("PrecioArticulo")) {
                    System.out.println("Leyendo PreciosArticulos:");

                    sincronizaFacade.importarPreciosArticulos(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("DescripcionArticulo")) {
                    System.out.println("Leyendo Articulos:");

                    sincronizaFacade.importarArticulos(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("dniPersona")) {
                    System.out.println("Leyendo Domicilios Persona:");
                    sincronizaFacade.importarClientesPersonaDomicilio(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("dnitelefonopersona")) {
                    System.out.println("Leyendo Telefono Persona:");
                    sincronizaFacade.importarClientesPersonaTelefonos(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("dnimailpersona")) {
                    System.out.println("Leyendo Mail Persona:");
                    sincronizaFacade.importarClientesPersonaMail(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("Razon Social")) {
                    System.out.println("Leyendo Organismos:");
                    sincronizaFacade.importarClientesOrganismo(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(0).equals("Descripcion")) {
                    System.out.println("Leyendo Lista de P:");
                    sincronizaFacade.importarListaPrecios(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(1).equals("Barrio")) {
                    System.out.println("Leyendo Domicilio Organismo:");
                    sincronizaFacade.importarClientesOrganismoDomicilio(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(1).equals("Mail")) {
                    System.out.println("Leyendo Mails Organismo:");
                    sincronizaFacade.importarClientesOrganismoMail(leerDatosExcel);

                } else if (leerDatosExcel.get(0).get(2).equals("telefono")) {
                    System.out.println("Leyendo telefono Organismo:");
                    sincronizaFacade.importarClientesOrganismoTelefonosl(leerDatosExcel);

                }
            } catch (Exception e) {
                System.out.println("Archivo Invalido");
            }
        }
        file.delete();

        System.out.println("Importación Finalizada");
    }

    public void enviar() {
        //exportadores de cliente persona/domicilio/telefonos/mails
//        File exportarClientesPersona = new SincronizaFacade().exportarClientesPersona();
//        File exportarClientesPersonaDomicilio = new SincronizaFacade().exportarClientesPersonaDomicilio();
//        File exportarClientesPersonaTelefono = new SincronizaFacade().exportarClientesPersonasTelefono();
//        File exportaraClientesPersonaMail = new SincronizaFacade().exportarClientesPersonaMail();
//        //exportadores de organismo/domicilio/telefonos/mails
//        File exportarClientesOrganismos = new SincronizaFacade().exportarClientesOrganismos();
//        File exportarClientesOrganismosDomicilios = new SincronizaFacade().exportarClientesOrganismosDomicilio();
//        File exportarClientesOrganismosTelefonos = new SincronizaFacade().exportarClientesOrganismosTelefono();
//        File exportarClientesOrganismosMails = new SincronizaFacade().exportarClientesOrganismosMail();
        //exporta caja
        File exportarCajas = new SincronizaFacade().exportarCajas();
        //exporta ventas
        File exportarVentas = new SincronizaFacade().exportarVentas();
        //exporta cierreventas
        File exportarCierreVentas = new SincronizaFacade().exportarCierreVentas();
//        try {
//            envio(host, port, exportarClientesPersona);
//        } catch (IOException ex) {
//            System.out.println("Error enviando personas " + ex);
//        }
//        try {
//            envio(host, port, exportarClientesPersonaDomicilio);
//        } catch (IOException ex) {
//            System.out.println("Error enviando domicilios personas" + ex);
//        }
//        try {
//            envio(host, port, exportarClientesPersonaTelefono);
//        } catch (IOException ex) {
//            System.out.println("Error enviando telefonos personas " + ex);
//        }
//        try {
//            envio(host, port, exportaraClientesPersonaMail);
//        } catch (IOException ex) {
//            System.out.println("Error enviando mails personas " + ex);
//        }
//        try {
//            envio(host, port, exportarClientesOrganismos);
//        } catch (IOException ex) {
//            System.out.println("Error enviando organismos " + ex);
//        }
//        try {
//            envio(host, port, exportarClientesOrganismosDomicilios);
//        } catch (IOException ex) {
//            System.out.println("Error enviando Domicilios organismos " + ex);
//        }
//        try {
//            envio(host, port, exportarClientesOrganismosTelefonos);
//        } catch (IOException ex) {
//            System.out.println("Error enviando Telefonos organismos " + ex);
//        }
//        try {
//            envio(host, port, exportarClientesOrganismosMails);
//        } catch (IOException ex) {
//            System.out.println("Error enviando Mails organismos " + ex);
//        }
        try {
            envio(host, port, exportarCajas);
        } catch (IOException ex) {
            System.out.println("Error enviando Cajas " + ex);
        }
        try {
            envio(host, port, exportarVentas);
        } catch (IOException ex) {
            System.out.println("Error enviando Ventas " + ex);
        }
        try {
            envio(host, port, exportarCierreVentas);
        } catch (IOException ex) {
            System.out.println("Error enviando Cierres de Venta " + ex);
        }

    }

}
