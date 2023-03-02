package includes;

import entidades.articulo.Articulo;
import entidades.articulo.TipoIva;
import entidades.caja.Caja;
import entidades.caja.CobranzaCtaCte;
import entidades.caja.CuentaCorriente;
import entidades.caja.CuponTarjeta;
import entidades.caja.MovimientoCaja;
import entidades.cliente.Cliente;
import entidades.cliente.Persona;
import entidades.inventario.MovimientoInterno;
import entidades.venta.CierreVentas;
import entidades.venta.Entrega;
import entidades.venta.Venta;
import entidades.venta.VentaArticulo;
import facade.ArticuloFacade;
import facade.CajaFacade;
import facade.CierreVentasFacade;
import facade.ClienteFacade;
import facade.ConfiguracionFacade;
import facade.CuentaCorrienteFacade;
import facade.CuponTarjetaFacade;
import facade.EmpleadoFacade;
import facade.EntregaFacade;
import facade.TipoIvaFacade;
import facade.VentaArticuloFacade;
import facade.VentaFacade;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * **************************
 *
 * Clase para manejar la impresora
 *
 * *******************************************************
 */
public class Impresora {

    PrintJob pj = Toolkit.getDefaultToolkit().getPrintJob(new Frame(), "Comprobante", null);
    Graphics pagina;
    ConfiguracionFacade configFacade = ConfiguracionFacade.getInstance();
    private String tipoIvaViejo;

    /**
     * ******************************************************************
     * Metodo "imprimir(String)", se encargado de * colocar en el objeto gráfico
     * la cadena que se le pasa como * parámetro y se imprime.	*
     * ******************************************************************
     */
    public void imprimir(String Cadena) {
        //LO COLOCO EN UN try/catch PORQUE PUEDEN CANCELAR LA IMPRESION
        try {
            Font fuente = new Font("Dialog", Font.PLAIN, 10);
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);

            pagina.drawString(Cadena, 60, 60);

            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA...");
        }
    }//FIN DEL PROCEDIMIENTO imprimir(String...)

    public void imprimir(String Cadena, int x, int y) {
        //LO COLOCO EN UN try/catch PORQUE PUEDEN CANCELAR LA IMPRESION
        try {
            Font fuente = new Font("Dialog", Font.PLAIN, 10);

            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);

            pagina.drawString(Cadena, x, y);

            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA...");
        }
    }//FIN DEL

    @SuppressWarnings("empty-statement")
    public void imprimir(Venta venta) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
//los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);

        String fecha = Comunes.calcularFechaHora();
        String total = new DecimalFormat("0.00").format(venta.getMonto());
        String subTotal = total;
        String descuento = "0.00";
        String saldo = "0";
        String nroCliente = "";
        if (venta.getDescuento() != null) {
            descuento = new DecimalFormat("0.00").format(venta.getDescuento());
            //subTotal = venta.getMonto().subtract(venta.getDescuento()).toString();
        }
        try {
            Persona persona = ClienteFacade.getInstance().getPersonaXDni(venta.getDniCliente());
            nroCliente = persona.getDocumentoIdentidad().getNumero();
        } catch (Exception e) {
            nroCliente ="";
        }
        
        // saldo de cliente
        try {

            Cliente cliente = ClienteFacade.getInstance().getPersonaXDni(venta.getDniCliente());
            List<Object[]> saldosClientes = CuentaCorrienteFacade.getInstance().getSaldosClientes(cliente);
            for (Object[] objects : saldosClientes) {
                try {
                    saldo = String.valueOf(objects[0]);
                } catch (Exception e) {
                    saldo = "0";
                }
            }
        } catch (Exception e) {
        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString(venta.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString(venta.getSucursal().getDomicilio().toString(), 10, 40 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    venta.getFecha()), 10, 55 + suma); //FECHA
            pagina.drawString("Nro: " + venta.getNumeroTicket() + "", 10, 70 + suma); //NUMERO DE RECIBO
            pagina.drawString("Vendedor: " + venta.getUsuario().getNombreCompleto(), 10, 85 + suma); //VENDEDOR
            pagina.drawString("Nro de Ficha: " + nroCliente, 10, 100 + suma); //nro cliente 
            pagina.drawString("Cliente: " + venta.getCliente(), 10, 115 + suma); //VENDEDOR
            pagina.drawString("SALDO C.C CLIENTE: $ " + saldo, 10, 130 + suma);
            pagina.drawString("______________________________", 10, 135 + suma); //SEPARADOR          
            int salto = 135 + suma;
            BigDecimal totalComun = new BigDecimal("0.00");
            BigDecimal descuentoPromo = new BigDecimal("0.00");
            //LO NUEVO
            for (TipoIva tipoIva : TipoIvaFacade.getInstance().getTodos()) {
                BigDecimal totalIva = new BigDecimal("0.00");

                List<Object[]> articulos = VentaFacade.getInstance().listadoArticulosVendidos(venta, tipoIva);
                for (Object[] articulo : articulos) {
                    pagina.drawString(articulo[0]
                            + " x " + articulo[1] + " $ " + new DecimalFormat("0.00").format(articulo[2]), 10, salto += 10);
                    pagina.drawString("Precio Unit.: $ " + new DecimalFormat("0.00").format(articulo[3]), 10, salto += 10);
                    pagina.drawString("Precio Promo: $ " + new DecimalFormat("0.00").format(articulo[4]), 10, salto += 10);
                    pagina.drawString("                ", 10, salto += 10);
//calculamos descuento gral para mostrar al final en descuento promo
                    totalComun = totalComun.add((BigDecimal) articulo[6]);
                    totalIva = totalIva.add((BigDecimal) articulo[2]);
                }
                if (!articulos.isEmpty()) {
                    pagina.drawString("______________________________", 10, salto); //SEPARADOR
                    pagina.drawString("SUBTOTAL " + tipoIva.getDescripcion() + "% : $ " + new DecimalFormat("0.00").format(totalIva), 10, salto += 10);
                    pagina.drawString("                              ", 10, salto += 10); //SEPARADOR

                }
            }
//            //FIN DE LO NUEVO

            descuentoPromo = totalComun.subtract(venta.getMonto());
//
            pagina.drawString("MONTO TOTAL : $ " + new DecimalFormat("0.00").format(totalComun), 10, salto += 20);
            pagina.drawString("DESCUENTO: $ " + descuento, 10, salto += 10);
            pagina.drawString("DESC. PROMO: $ " + new DecimalFormat("0.00").format(descuentoPromo), 10, salto += 10);
            pagina.drawString("TOTAL: $ " + total, 10, salto += 10);
            // pagina.drawString("TOTAL:", 300, salto + 10);
            //pagina.drawString("$ " + recibimos, 430, 780);
            //pagina.drawString("$ " + vuelto, 430, 790);
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    @SuppressWarnings("empty-statement")
    public void imprimir2(Venta venta) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
//los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);

        String fecha = Comunes.calcularFechaHora();
        String total = new DecimalFormat("0.00").format(venta.getMonto());
        String subTotal = total;
        String descuento = "0.00";
        if (venta.getDescuento() != null) {
            descuento = new DecimalFormat("0.00").format(venta.getDescuento());
            //subTotal = venta.getMonto().subtract(venta.getDescuento()).toString();
        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString(venta.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString(venta.getSucursal().getDomicilio().toString(), 10, 40 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    venta.getFecha()), 10, 55 + suma); //FECHA
            pagina.drawString("Nro: " + venta.getNumeroTicket() + "", 10, 70 + suma); //NUMERO DE RECIBO
            pagina.drawString("Vendedor: " + venta.getUsuario().getNombreCompleto(), 10, 85 + suma); //VENDEDOR

            //pagina.drawString("Cliente: " + venta.getCliente().toString(), 10, 100 + suma); //CLIENTE
            pagina.drawString("______________________________", 10, 115 + suma); //SEPARADOR

            int salto = 115 + suma;
            BigDecimal totalComun = new BigDecimal("0.00");
            BigDecimal subtotalPromo = new BigDecimal("0.00");
            BigDecimal descuentoPromo = new BigDecimal("0.00");
            //Recorremos el vector de articulos
            List<Object[]> ventasArticulos = VentaFacade.getInstance().getVentasArticulos(venta);
            //VARIABLE AUXILIAR CORTE DE CONTROL
            Object[] ventaArticuloAux = ventasArticulos.get(0);
            String tipoIvaAux = (String) ventaArticuloAux[6];
            for (Object[] ventaArticulo : ventasArticulos) {
                //SGUIENTE
                if (tipoIvaAux.equals(ventaArticulo[6])) {
                    pagina.drawString(ventaArticulo[0]
                            + " x " + ventaArticulo[1] + " $ " + ventaArticulo[2], 10, salto);
                    //pagina.drawString("$ " + new DecimalFormat("0.00").format(articulo.getPrecio()), 10, salto);
                    pagina.drawString("Precio Unit.: $ " + ventaArticulo[4], 10, salto + 10);
                    pagina.drawString("Precio Promo: $ " + ventaArticulo[5], 10, salto + 20);
                    //calculamos descuento gral para mostrar al final en descuento promo
                    subtotalPromo = subtotalPromo.add(new BigDecimal(ventaArticulo[1].toString()).multiply(new BigDecimal(ventaArticulo[5].toString())));
                    totalComun = totalComun.add(new BigDecimal(ventaArticulo[1].toString()).multiply(new BigDecimal(ventaArticulo[4].toString())));
                    salto = salto + 40;
                } else {
                    pagina.drawString("______________________________", 10, salto + 30); //SEPARADOR
                    pagina.drawString("SUB TOTAL %" + tipoIvaAux + " : $ " + subtotalPromo, 10, salto + 30);
                    //reiniciamos subtotal
                    subtotalPromo = new BigDecimal("0.00");
                    //IMPRIMIMOS EL OTRO
                    tipoIvaAux = (String) ventaArticulo[6];
                    pagina.drawString(ventaArticulo[0]
                            + " x " + ventaArticulo[1] + " $ " + ventaArticulo[2], 10, salto + 40);
                    //pagina.drawString("$ " + new DecimalFormat("0.00").format(articulo.getPrecio()), 10, salto);
                    pagina.drawString("Precio Unit.: $ " + ventaArticulo[4], 10, salto + 50);
                    pagina.drawString("Precio Promo: $ " + ventaArticulo[5], 10, salto + 60);
                    //calculamos descuento gral para mostrar al final en descuento promo
                    subtotalPromo = subtotalPromo.add(new BigDecimal(ventaArticulo[1].toString()).multiply(new BigDecimal(ventaArticulo[5].toString())));
                    totalComun = totalComun.add(new BigDecimal(ventaArticulo[1].toString()).multiply(new BigDecimal(ventaArticulo[4].toString())));
                    salto = salto + 40;

                }

            }

            pagina.drawString("______________________________", 10, salto + 30); //SEPARADOR
            pagina.drawString("SUB TOTAL %" + tipoIvaAux + " : $ " + subtotalPromo, 10, salto + 30);
            // restamos subtotal real cobrado del totalComun para ver cuanto se le descontó en promociones
            descuentoPromo = totalComun.subtract(venta.getMonto());
//            pagina.drawString("______________________________", 10, salto); //SEPARADOR
//
            pagina.drawString("SUB TOTAL : $ " + subTotal, 10, salto += 20);
            pagina.drawString("DESCUENTO: $ " + descuento, 10, salto += 10);
            pagina.drawString("DESC. PROMO: $ " + new DecimalFormat("0.00").format(descuentoPromo), 10, salto += 10);
            pagina.drawString("TOTAL: $ " + total, 10, salto += 10);
//            pagina.drawString("Gracias por su compra ", 10, salto += 10);

            // pagina.drawString("TOTAL:", 300, salto + 10);
            //pagina.drawString("$ " + recibimos, 430, 780);
            //pagina.drawString("$ " + vuelto, 430, 790);
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    @SuppressWarnings("empty-statement")
    public void imprimir(CierreVentas cierreVentas) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
        //los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);
        //creamos la fecha
        BigDecimal total = VentaFacade.getInstance().getTotalVentasEventuales(cierreVentas);
        List<Venta> listaVenta = new ArrayList<>();
        BigDecimal descuentos = new BigDecimal("0.00");
        BigDecimal promedio = new BigDecimal("0.00");
        BigDecimal totalComun = new BigDecimal("0.00");
        BigDecimal totalMediaRes = new BigDecimal("0.00");
        BigDecimal totalEventual = new BigDecimal("0.00");
        BigDecimal totalEmpleado = new BigDecimal("0.00");
        BigDecimal totalPromoEmpleado = new BigDecimal("0.00");
        BigDecimal totalDescuentoManualEmpleado = new BigDecimal("0.00");
        BigDecimal totalPromo = new BigDecimal("0.00");
        BigDecimal totalOtros = new BigDecimal("0.00");
        int cantTicketsEntregas = 0;
        BigDecimal totalEntregaSpromo = new BigDecimal("0.00");
        BigDecimal totalEntrega = new BigDecimal("0.00");
        int cantTicketsAnulados = 0;
        int cantTicketsOtros = 0;
        int cantTicketsEventuales = 0;
        BigDecimal totalTicketsAnulados = new BigDecimal("0.00");
        // calculamos total de descuentos aplicados y monto promedio de ticket

        for (Venta venta : VentaFacade.getInstance().getVentasNoEntrega(cierreVentas)) {

            //total sin promo
            for (VentaArticulo ventaArticulo : venta.getVentasArticulos()) {
                totalPromo = totalPromo.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioPromocion()));
            }
            //cliente

            if (venta.isEsPersona()) {
                //Eventual
                //        System.out.println("clinetexxx " + venta.getCliente());
                if (venta.getCliente().contains("EVENTUAL")) {
                    for (VentaArticulo ventaArticulo : venta.getVentasArticulos()) {
                        totalEventual = totalEventual.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioUnitario()));
                    }
                    cantTicketsEventuales++;
                }
                //Empleado
                if (EmpleadoFacade.getInstance().existeEmpleadoCliente(getClienteDeLaVenta(venta))) {
                    totalDescuentoManualEmpleado = totalDescuentoManualEmpleado.add(venta.getDescuento());
                    for (VentaArticulo ventaArticulo : venta.getVentasArticulos()) {
                        totalEmpleado = totalEmpleado.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioUnitario()));
                        totalPromoEmpleado = totalPromoEmpleado.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioPromocion()));

                    }
                }
            }

            //descuentos
            try {
                descuentos = descuentos.add(venta.getDescuento());
            } catch (Exception e) {
            }
//            //promedio ticket valor
//            if (!venta.isAnulado()) {
//                try {
//                    promedio = promedio.add(venta.getMonto());
//                } catch (Exception e) {
//                }
//            }

            //calculamos precio comun
            for (VentaArticulo ventaArticulo : venta.getVentasArticulos()) {
                Articulo arti = ArticuloFacade.getInstance().buscarPorCodigo(ventaArticulo.getArticuloCodigo());
                //calculamos media res
                if (arti.getSubCategoria().getCategoria().getDescripcion().equals("VACUNO")) {
                    totalMediaRes = totalMediaRes.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioUnitario()));
                }
                //total comun
                totalComun = totalComun.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioUnitario()));

                //CALCULAMOS OTROS ARTICULOS
                if (arti.getSubCategoria().getCategoria().getDescripcion().equals("OTROS")) {
                    totalOtros = totalOtros.add(ventaArticulo.getPrecio());
                }
            }

        }
        for (Venta venta : VentaFacade.getInstance().getVentasEntrega(cierreVentas)) {
            cantTicketsEntregas++;
            totalEntrega = totalEntrega.add(venta.getMonto());
            for (VentaArticulo ventaArticulo : venta.getVentasArticulos()) {
                totalEntregaSpromo = totalEntregaSpromo.add(ventaArticulo.getCantidadPeso().multiply(ventaArticulo.getPrecioUnitario()));
            }
        }
        cantTicketsAnulados = VentaFacade.getInstance().cantidadVentasAnuladas(cierreVentas);
        totalTicketsAnulados = VentaFacade.getInstance().getTotalVentasAnuladas(cierreVentas);
        if (totalTicketsAnulados == null) {
            totalTicketsAnulados = BigDecimal.ZERO;
        }

        cantTicketsOtros = Integer.parseInt(VentaFacade.getInstance().getCantVentaOtrosArticulos(cierreVentas));
        cantTicketsEventuales = Integer.parseInt(VentaFacade.getInstance().getCantVentaEventualesNoAnuladosNoEntregas(cierreVentas));
        // ticket promedio
        try {
            promedio = totalPromo.divide(new BigDecimal(cantTicketsEventuales), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString(cierreVentas.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            //pagina.drawString(cierreVentas.getSucursal().getDomicilio().toString(), 90, 25 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha Cierre: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    cierreVentas.getFecha()), 10, 40 + suma); //FECHA
            pagina.drawString("Nro. de cierre: " + cierreVentas.getNumeroCierre() + "", 10, 50 + suma); //NUMERO DE RECIBO
            pagina.drawString("Ticket Desde: " + cierreVentas.getTicketDesde()
                    + " Hasta: " + cierreVentas.getTicketHasta(), 10, 60 + suma); //VENDEDOR

            pagina.drawString("Cantidad de Tickets: " + cantTicketsEventuales, 10, 70 + suma); //CLIENTE
            pagina.drawString("_____________________________", 10, 80 + suma); //SEPARADOR

            int salto = 100 + suma;

            pagina.drawString("Importe Total Tickets: $ " + new DecimalFormat("0.00").format(totalPromo), 10, salto += 10);
            pagina.drawString("Total de Descuentos: $ " + new DecimalFormat("0.00").format(descuentos), 10, salto += 10);
            pagina.drawString("Importe Final Ticket: $ " + total, 10, salto += 10);
            pagina.drawString("Ticket Promedio: $ " + new DecimalFormat("0.00").format(promedio), 10, salto += 10);
            // Total tickets S/promo
            pagina.drawString("Total tickets (S/promo): $ " + new DecimalFormat("0.00").format(totalComun), 10, salto += 10);
            //Tickets de ordenes de compras (pedidos)
            pagina.drawString("Cant.tickets ped.: " + new DecimalFormat("0.00").format(cantTicketsEntregas), 10, salto += 10);
            pagina.drawString("Imp.total ped.: $ " + new DecimalFormat("0.00").format(totalEntrega), 10, salto += 10);
            pagina.drawString("Total tickets (S/promo)ped.: $ " + new DecimalFormat("0.00").format(totalEntregaSpromo), 10, salto += 10);
            pagina.drawString("Cant.tickets anulados: " + cantTicketsAnulados, 10, salto += 10);
            pagina.drawString("Imp.total tickets anulados: $ " + new DecimalFormat("0.00").format(totalTicketsAnulados), 10, salto += 10);
            //otros articulos
//            pagina.drawString("Cant.tickets Otros: " + cantTicketsOtros, 10, salto += 10);
//            pagina.drawString("Imp.total tickets Otros: $ " + new DecimalFormat("0.00").format(totalOtros), 10, salto += 10);
            //Cliente Eventual
            pagina.drawString("------- Cliente Eventual -------", 10, salto += 20);
            pagina.drawString("Importe Total Ticket: $ " + new DecimalFormat("0.00").format(totalEventual), 10, salto += 20);
            pagina.drawString("Total de Descuentos: $ 0.00", 10, salto += 10);
            pagina.drawString("Importe Final Ticket: $ " + new DecimalFormat("0.00").format(totalEventual), 10, salto += 10);
            //Empleados
            pagina.drawString("------- Empleados 20% -------", 10, salto += 20);
            pagina.drawString("Importe comun Ticket: $ " + new DecimalFormat("0.00").format(totalEmpleado), 10, salto += 20);
            pagina.drawString("Importe Promo Ticket: $ " + new DecimalFormat("0.00").format(totalPromoEmpleado), 10, salto += 10);
            pagina.drawString("Importe Descuento: $ " + new DecimalFormat("0.00").format(totalDescuentoManualEmpleado), 10, salto += 10);
            pagina.drawString("Importe Final Ticket: $ " + new DecimalFormat("0.00").format(totalPromoEmpleado.subtract(totalDescuentoManualEmpleado)), 10, salto += 10);

            //AGRUPADOS POR CATEGORIA
            List<Object[]> listaVentaCat = VentaFacade.getInstance().getResumenVentasPorCategoria(cierreVentas);
            for (Object[] venta : listaVentaCat) {
                if ((salto % 815 == 0) || (salto % 830 == 0)) {
                    pagina.dispose();
                    pagina = pj.getGraphics();
                    pagina.setFont(fuente);
                    pagina.setColor(Color.black);
                    salto = 0;
                    pagina.drawString("Salto: --------------- ", 10, salto += 10);
                }
                pagina.drawString("------- " + venta[0] + " -------", 10, salto += 20);
                pagina.drawString("Importe comun Ticket: $ " + new DecimalFormat("0.00").format(venta[2]), 10, salto += 20);
                pagina.drawString("Importe Promo Ticket: $ " + venta[1], 10, salto += 10);
                pagina.drawString("Importe Descuento: $ " + venta[3], 10, salto += 10);
                pagina.drawString("Importe Final Ticket: $ " + venta[4], 10, salto += 10);

            }

            //ORGANISMOS -EMPRESAS
            List<Object[]> listaVentaOrg = VentaFacade.getInstance().getResumenVentasOrganismos(cierreVentas);
            for (Object[] venta : listaVentaOrg) {
                if ((salto % 815 == 0) || (salto % 830 == 0)) {
                    pagina.dispose();
                    pagina = pj.getGraphics();
                    pagina.setFont(fuente);
                    pagina.setColor(Color.black);
                    salto = 0;
                    pagina.drawString("Salto: --------------- ", 10, salto += 10);
                }
                pagina.drawString("------- " + venta[0] + " -------", 10, salto += 20);
                pagina.drawString("Importe comun Ticket: $ " + new DecimalFormat("0.00").format(venta[2]), 10, salto += 20);
                pagina.drawString("Importe Promo Ticket: $ " + venta[1], 10, salto += 10);
                pagina.drawString("Importe Descuento: $ " + venta[3], 10, salto += 10);
                pagina.drawString("Importe Final Ticket: $ " + venta[4], 10, salto += 10);

            }
            //LO NUEVO
            List<Object[]> listaventa = VentaFacade.getInstance().getResumenVentasEntrega(cierreVentas);
            for (Object[] venta : listaventa) {
                if ((salto % 815 == 0) || (salto % 830 == 0)) {
                    pagina.dispose();
                    pagina = pj.getGraphics();
                    pagina.setFont(fuente);
                    pagina.setColor(Color.black);
                    salto = 0;
                    pagina.drawString("Salto: --------------- ", 10, salto += 10);
                }
                pagina.drawString("------- " + venta[2] + " -------", 10, salto += 20);
                pagina.drawString("Importe Total Ticket: $ " + venta[1], 10, salto += 20);
                pagina.drawString("Total de Descuentos: $ " + venta[0], 10, salto += 10);
                pagina.drawString("Importe Final Ticket: $ " + venta[1], 10, salto += 10);
            }
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    @SuppressWarnings("empty-statement")
    public void imprimirEntrega(Venta venta) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
//los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);

        String fecha = Comunes.calcularFechaHora();
        String total = new DecimalFormat("0.00").format(venta.getMonto());
        String subTotal = total;
        String descuento = "0.00";
        if (venta.getDescuento() != null) {
            descuento = new DecimalFormat("0.00").format(venta.getDescuento());
            //subTotal = venta.getMonto().subtract(venta.getDescuento()).toString();
        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString(venta.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString(venta.getSucursal().getDomicilio().toString(), 90, 25 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    venta.getFecha()), 1, 40 + suma); //FECHA
            pagina.drawString("Nro: " + venta.getNumeroTicket() + "", 1, 50 + suma); //NUMERO DE RECIBO
            pagina.drawString("Vendedor: " + venta.getUsuario().getNombreCompleto(), 1, 60 + suma); //VENDEDOR

            pagina.drawString("Cliente: " + venta.getCliente().toString(), 1, 70 + suma); //CLIENTE
            pagina.drawString("______________________________", 1, 80 + suma); //SEPARADOR

            int salto = 100 + suma;
            BigDecimal totalComun = new BigDecimal("0.00");
            BigDecimal descuentoPromo = new BigDecimal("0.00");
            //Recorremos el vector de articulos
            for (VentaArticulo articulo : venta.getVentasArticulos()) {
                pagina.drawString(articulo.getArticuloDescripcion()
                        + " x " + articulo.getCantidadPeso().toString(), 1, salto);
                pagina.drawString("$ " + new DecimalFormat("0.00").format(articulo.getPrecio()), 120, salto);
                pagina.drawString("Precio Unit.: $ " + articulo.getPrecioUnitario(), 10, salto + 10);
                pagina.drawString("Precio Promo: $ " + new DecimalFormat("0.00").format(articulo.getPrecioPromocion()), 10, salto + 20);
                //calculamos descuento gral para mostrar al final en descuento promo
                totalComun = totalComun.add(articulo.getCantidadPeso().multiply(articulo.getPrecioUnitario()));
                System.out.println("total comun: " + totalComun.toString());

                //damos el espacio para el siguiente
                salto = salto + 40;
            }
            // restamos subtotal real cobrado del totalComun para ver cuanto se le descontó en promociones
            descuentoPromo = totalComun.subtract(venta.getMonto());
            pagina.drawString("______________________________", 1, salto); //SEPARADOR

            pagina.drawString("SUB TOTAL 21% : $ " + subTotal, 10, salto += 10);
            pagina.drawString("DESCUENTO: $ " + descuento, 10, salto += 10);
            pagina.drawString("DESC. PROMO: $ " + new DecimalFormat("0.00").format(descuentoPromo), 10, salto += 10);
            pagina.drawString("TOTAL: $ " + total, 10, salto += 10);
            //COMENTAMOS GRACIAS POR SU COMPRA POR QUE ES UNA ENTREGA
            //pagina.drawString("Gracias por su compra ", 10, salto += 10);

            // pagina.drawString("TOTAL:", 300, salto + 10);
            //pagina.drawString("$ " + recibimos, 430, 780);
            //pagina.drawString("$ " + vuelto, 430, 790);
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    public void imprimir(Caja caja) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
        //los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);
        //creamos la fecha
//        String total = caja.getImporte().toString();

        BigDecimal cobranzaCtaCte = new BigDecimal("0.00");
        BigDecimal cobroVenta = new BigDecimal("0.00");
        BigDecimal gasto = new BigDecimal("0.00");
        BigDecimal ingreso = new BigDecimal("0.00");
        BigDecimal sueldo = new BigDecimal("0.00");
        BigDecimal saldoFinalCaja = new BigDecimal("0.00");
        BigDecimal totalIngresosCaja = new BigDecimal("0.00");
        BigDecimal totalEgresosCaja = new BigDecimal("0.00");
        BigDecimal retiro = new BigDecimal("0.00");

        BigDecimal totalCreditoCtaCte = new BigDecimal("0.00");
        BigDecimal totalCuponesTarjeta = new BigDecimal("0.00");
        BigDecimal totalVales = new BigDecimal("0.00");

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
                    retiro = retiro.add(movimientoCaja.getImporte());
                    break;
                case "Sueldo":
                    egresosCaja.add(movimientoCaja);
                    sueldo = sueldo.add(movimientoCaja.getImporte());
                    break;
            }

        }
        //AQUI RESTAMOS DE LAAS COBRANZAS DE CTA CTE.. LAS COBRANZA EN CTA CTE COBRADAS CON TARJETA
        // YA QUE ESTAS ULTIMAS NO REPRESENTAN INGRESO DE CAJA
        BigDecimal cuponesCtaCteConTarjeta = new BigDecimal("0.000");
        for (CuponTarjeta cupon : CuponTarjetaFacade.getInstance().getCuponesTarjetaCtaCteCaja(caja.getFechaInicio())) {
            cuponesCtaCteConTarjeta = cuponesCtaCteConTarjeta.add(cupon.getImporteCuponConRecargo());
        }
        //aqui le restamos las cobranzas con tarjeta porque ya que no es ingreso de caja
        cobranzaCtaCte = cobranzaCtaCte.subtract(cuponesCtaCteConTarjeta);
        totalIngresosCaja = cobranzaCtaCte.add(cobroVenta).add(ingreso);
        totalEgresosCaja = sueldo.add(gasto).add(retiro);
        //como ahora el saldo final lo pones a mano desde la vista previa
        saldoFinalCaja = caja.getCajaFinal();
        //saldoFinalCaja = caja.getCajaInicial().add(totalIngresosCaja).subtract(totalEgresosCaja);

        // CUENTA CORRIENTE
        for (CuentaCorriente cc : caja.getMovimientosCtaCte()) {
            totalCreditoCtaCte = totalCreditoCtaCte.add(cc.getImporteCtaCte());
        }
        // TARJETA DE CREDITO y VALES(NUEVO)
        for (CuponTarjeta cuponTarjeta : caja.getMovimientosCuponesTarjeta()) {
            if (!cuponTarjeta.getPlanTarjeta().getEsVale()) {
                totalCuponesTarjeta = totalCuponesTarjeta.add(cuponTarjeta.getImporteCuponConRecargo());
            } else {
                totalVales = totalVales.add(cuponTarjeta.getImporteCuponConRecargo());
            }

        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString("SUCURSAL: " + caja.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString("USUARIO: " + caja.getUsuario().toString(), 10, 40 + suma);//USUario
            //pagina.drawString(cierreVentas.getSucursal().getDomicilio().toString(), 90, 25 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha Apertura: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    caja.getFechaInicio()), 10, 55 + suma); //FECHA
            pagina.drawString("Fecha Cierre: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    caja.getFechaFin()), 10, 70 + suma); //FECHA
            pagina.drawString("CAJA INICIAL: " + caja.getCajaInicial(), 10, 85 + suma);
            pagina.drawString("_____________________________", 10, 100 + suma); //SEPARADOR
            pagina.drawString("------- INGRESOS DE CAJA -------- ", 10, 115 + suma);
            pagina.drawString("Ventas: " + new DecimalFormat("0.00").format(cobroVenta), 10, 130 + suma);
            pagina.drawString("Cobranzas Cta. Corriente: " + new DecimalFormat("0.00").format(cobranzaCtaCte), 10, 145 + suma);
            pagina.drawString("Ingresos varios: " + new DecimalFormat("0.00").format(ingreso), 10, 160 + suma);
            pagina.drawString("TOTAL DE INGRESOS CAJA: " + new DecimalFormat("0.00").format(totalIngresosCaja), 10, 175 + suma);
            pagina.drawString("------- EGRESOS DE CAJA -------- ", 10, 190 + suma);
            pagina.drawString("Sueldos y Adelantos: " + new DecimalFormat("0.00").format(sueldo), 10, 205 + suma);
            pagina.drawString("Gastos: " + new DecimalFormat("0.00").format(gasto), 10, 220 + suma);
            pagina.drawString("Retiros en Efectivo: " + new DecimalFormat("0.00").format(retiro), 10, 235 + suma);
            pagina.drawString("TOTAL DE EGRESOS CAJA: " + new DecimalFormat("0.00").format(totalEgresosCaja), 10, 250 + suma);
            pagina.drawString("_____________________________", 10, 265 + suma); //SEPARADOR
            pagina.drawString("------- CREDITO CUENTA CORRIENTE -------- ", 10, 280 + suma);
            pagina.drawString("Total cuenta corriente: " + new DecimalFormat("0.00").format(totalCreditoCtaCte), 10, 295 + suma);
            pagina.drawString("------- TARJETA DE CREDITO -------- ", 10, 310 + suma);
            pagina.drawString("Total tarjeta de credito: " + new DecimalFormat("0.00").format(totalCuponesTarjeta), 10, 325 + suma);
            pagina.drawString("------- VALE -------- ", 10, 340 + suma);
            pagina.drawString("Total Vales: " + new DecimalFormat("0.00").format(totalVales), 10, 355 + suma);
            pagina.drawString("_____________________________", 10, 370 + suma); //SEPARADOR
            pagina.drawString("SALDO DE CAJA: " + new DecimalFormat("0.00").format(saldoFinalCaja), 10, 390 + suma);

            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    public Cliente getClienteDeLaVenta(Venta venta) {
        Cliente cliente;
        if (venta.isEsPersona()) {
            System.out.println("dni del cliente: " + venta.getDniCliente());
            cliente = ClienteFacade.getInstance().getPersonaXDni(venta.getDniCliente());
        } else {
            cliente = ClienteFacade.getInstance().buscarCuitEmpresaObjeto(venta.getDniCliente());
        }
        //System.out.println("CLIEUNTE DE LA VENTNAAA: "+ cliente);
        return cliente;
    }

    @SuppressWarnings("empty-statement")
    public void imprimir(List<MovimientoInterno> movimientos, String tipo) {

        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }

        //los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);
        String fecha = Comunes.calcularFechaHora();

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("  MB CARNICERIA", 13, suma += 10); //LEER DE BASE NOMBRE DEL NEGOCIO    
            pagina.drawString("               -- " + tipo + " --", 13, suma += 10); //LEER DE BASE NOMBRE DEL NEGOCIO    
            //pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES")).format(
            //fecha), 10, 55 + suma); //FECHA          
            pagina.drawString("______________________________", 10, suma += 10); //SEPARADOR

            int salto = 10 + suma;
            BigDecimal totalComun = new BigDecimal("0.00");

            // Uso esta variable para que imprimi solo la primera vez que recorre
            // el ciclo
            boolean primeraPasada = true;

            for (MovimientoInterno movimiento : movimientos) {
                if (primeraPasada) {
                    pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy").format(movimiento.getFecha()), 13, salto += 10);
                    pagina.drawString("                ", 13, salto += 10);
                    pagina.drawString("Numero Lote: " + movimiento.getNumeroLote(), 13, salto += 10);
                    pagina.drawString("Sucursal Envío:  " + movimiento.getSucursal(), 13, salto += 10);
                    pagina.drawString("Sucursal Destino:  " + movimiento.getSucursalDestino(), 13, salto += 10);
                    pagina.drawString("                ", 13, salto += 10);
                    pagina.drawString("          - DETALLE -      ", 13, salto += 10);
                    pagina.drawString("                ", 13, salto += 10);
                    primeraPasada = false;
                }

                pagina.drawString("Tipo de Movimiento: " + movimiento.getTipoDeMovimiento(), 13, salto += 10);
                pagina.drawString("Artículo:           " + movimiento.getArticuloDescripcion(), 13, salto += 10);
                pagina.drawString("Cantidad:           " + movimiento.getCantidad(), 13, salto += 10);
                BigDecimal totalReg = movimiento.getCantidad().
                        multiply(movimiento.getMonto()).setScale(2, BigDecimal.ROUND_CEILING);
                pagina.drawString("                ", 13, salto += 10);

                //totalComun = totalComun.add(totalReg);
            }

            /*if (!movimientos.isEmpty()) {
                pagina.drawString("______________________________", 10, salto); //SEPARADOR
                pagina.drawString("TOTAL : $ " + totalComun, 10, salto += 10);
                pagina.drawString("                              ", 10, salto += 10); //SEPARADOR
            }*/
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    @SuppressWarnings("empty-statement")
    public void imprimirTicketSandwicheria(Venta venta) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (Exception e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (Exception e) {
        }
//los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);
        Font fuenteGrande = new Font(tipoFuente, Font.PLAIN, 15);

        String fecha = Comunes.calcularFechaHora();
        String total = new DecimalFormat("0.00").format(venta.getMonto());
        String subTotal = total;
        String descuento = "0.00";
        if (venta.getDescuento() != null) {
            descuento = new DecimalFormat("0.00").format(venta.getDescuento());
            //subTotal = venta.getMonto().subtract(venta.getDescuento()).toString();
        }

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.setFont(fuenteGrande);
            pagina.drawString("Nro: " + venta.getNumeroTicket() + "", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.setFont(fuente);
            pagina.drawString(venta.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString(venta.getSucursal().getDomicilio().toString(), 10, 40 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    venta.getFecha()), 10, 55 + suma); //FECHA
            pagina.drawString("Vendedor: " + venta.getUsuario().getNombreCompleto(), 10, 70 + suma); //VENDEDOR
            pagina.drawString("Cliente: " + venta.getCliente(), 10, 85 + suma); //VENDEDOR
            pagina.drawString("-------------------------------", 10, 105 + suma); //SEPARADOR

            int salto = 110 + suma;
            BigDecimal totalComun = new BigDecimal("0.00");
            BigDecimal descuentoPromo = new BigDecimal("0.00");
            //LO NUEVO
            for (TipoIva tipoIva : TipoIvaFacade.getInstance().getTodos()) {
                BigDecimal totalIva = new BigDecimal("0.00");

                List<Object[]> articulos = VentaFacade.getInstance().listadoArticulosVendidos(venta, tipoIva);
                for (Object[] articulo : articulos) {
                    pagina.drawString(articulo[0]
                            + " x " + articulo[1] + " $ " + new DecimalFormat("0.00").format(articulo[2]), 10, salto += 10);
                    pagina.drawString("                ", 10, salto += 10);
//calculamos descuento gral para mostrar al final en descuento promo
                    totalComun = totalComun.add((BigDecimal) articulo[6]);
                    totalIva = totalIva.add((BigDecimal) articulo[2]);
                }

            }
            pagina.drawString("------------------------------", 10, salto); //SEPARADOR
//            //FIN DE LO NUEVO

            descuentoPromo = totalComun.subtract(venta.getMonto());
//
            pagina.drawString("MONTO TOTAL : $ " + new DecimalFormat("0.00").format(totalComun), 10, salto += 20);
            pagina.drawString("DESCUENTO: $ " + descuento, 10, salto += 10);
            pagina.drawString("DESC. PROMO: $ " + new DecimalFormat("0.00").format(descuentoPromo), 10, salto += 10);
            pagina.setFont(fuenteGrande);
            pagina.drawString("TOTAL: $ " + total, 10, salto += 20);
//            pagina.drawString("Gracias por su compra ", 10, salto += 10);

            // pagina.drawString("TOTAL:", 300, salto + 10);
            //pagina.drawString("$ " + recibimos, 430, 780);
            //pagina.drawString("$ " + vuelto, 430, 790);
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }

    }

    @SuppressWarnings("empty-statement")
    public void imprimir(CobranzaCtaCte cobranza) {
        //leemos los valores
        String tipoFuente = "Dialog";
        int tamanioFuente = 10;
        int suma = 5;
        try {
            tipoFuente = configFacade.buscar("fuente").getValor();
        } catch (Exception e) {
        }
        try {
            tamanioFuente = Integer.parseInt(configFacade.buscar("tamanio").getValor());
        } catch (NumberFormatException e) {
        }
        try {
            suma = Integer.parseInt(configFacade.buscar("margensuperior").getValor());
        } catch (NumberFormatException e) {
        }
        //los asignamos
        Font fuente = new Font(tipoFuente, Font.PLAIN, tamanioFuente);
        Persona persona = ClienteFacade.getInstance().getPersonasXId(cobranza.getCliente().getId());

        try {
            //preparamos la pagina
            pagina = pj.getGraphics();
            pagina.setFont(fuente);
            pagina.setColor(Color.black);
            //ORIGINAL
            pagina.drawString("MB CARNICERIA", 10, 10 + suma); //LEER DE BASE NOMBRE DEL NEGOCIO
            pagina.drawString(cobranza.getSucursal().getNombre(), 10, 25 + suma);//SUCURSAL
            pagina.drawString("COBRANZA C. CORRIENTE", 10, 40 + suma);//SUCURSAL DOMICILIO
            pagina.drawString("Fecha: " + new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH:mm:ss", new Locale("es_ES")).format(
                    cobranza.getFecha()), 10, 55 + suma); //FECHA
            pagina.drawString("Nro de ticket: " + cobranza.getNumero() + "", 10, 70 + suma); //NUMERO DE RECIBO
            pagina.drawString("Vendedor: " + cobranza.getUsuario().getNombreCompleto(), 10, 85 + suma); //VENDEDOR
            pagina.drawString("Nro de Ficha: " + persona.getDocumentoIdentidad().getNumero(), 10, 100 + suma); //nro cliente
            pagina.drawString("Cliente: " + cobranza.getCliente(), 10, 115 + suma); //cliente
            pagina.drawString("_________________________", 10, 120 + suma); //SEPARADOR
            pagina.drawString("SU PAGO: $ " + cobranza.getImporte(), 10, 135 + suma);
            pagina.dispose();
            pj.end();
        } catch (Exception e) {
            System.out.println("LA IMPRESION HA SIDO CANCELADA..." + e);
        }
    }
}
