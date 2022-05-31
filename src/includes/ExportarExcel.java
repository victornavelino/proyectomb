package includes;

/**
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 * Para generar una hoja Excel Simple
 *
 * @author Franco
 *
 */
public class ExportarExcel {

    /**
     * Para escribir el contenido de una celda.
     *
     * @param row Row.
     * @param i posicion en la fila.
     * @param value texto a escribir.
     * @param style estilo de la celda.
     */
    public void createCell(Row row, int i, String value, HSSFCellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellValue(value);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }
    //-------------

    public void crearExcelJtable(JTable jtable, String nombre) {
        List<String> lista = new ArrayList<>();
        int numFilas = jtable.getRowCount();
        int numColumnas = jtable.getColumnCount();
        StringBuilder stringBuider = new StringBuilder();
        for (int colIndex = 0; colIndex < numColumnas; colIndex++) {
            stringBuider.append(jtable.getColumnName(colIndex));
            stringBuider.append("|");
        }
        lista.add(stringBuider.toString());
        for (int rowIndex = 0; rowIndex < numFilas; rowIndex++) {
            stringBuider = new StringBuilder();
            for (int colIndex = 0; colIndex < numColumnas; colIndex++) {
                stringBuider.append(jtable.getValueAt(rowIndex, colIndex));
                stringBuider.append("|");
            }
            lista.add(stringBuider.toString());
        }
        crearExcel(lista, nombre);
    }

    /**
     * Crea una hoja Excel con el contenido especificado.
     *
     * @param lista List con los datos a escribir en la hoja.
     * @param nombre nombre de la hoja.
     */
    public void crearExcel(List lista, String nombre) {
        HSSFWorkbook libro = new HSSFWorkbook();
        // Se crea una hoja dentro del libro
        HSSFSheet hoja = libro.createSheet();
        CreationHelper createHelper = libro.getCreationHelper();
        int filas = lista.size();
        for (int i = 0; i < filas; i++) {
            String fila = (String) lista.get(i);
            StringTokenizer st = new StringTokenizer(fila, "|");
            Row row = hoja.createRow((short) i);
            int j = 0;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                // para la cabecera, la primera fila, aplicamos un estilo (negrita y color de fondo azul)
                if (i == 0) {
                    HSSFCellStyle style = libro.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    Font font = libro.createFont();
                    // font.setFontHeightInPoints((short)10);
                    font.setFontName("Courier New");
                    // font.setItalic(true);
                    // font.setStrikeout(true);
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    font.setColor(IndexedColors.WHITE.getIndex());
                    style.setFont(font);
                    createCell(row, j, token, (HSSFCellStyle) style);
                } else {
                    createCell(row, j, token, null);
                }

                j = j + 1;

            }

        }

        // Asignar automaticamente el tamaño de las celdas en funcion del contenido
        for (int i = 0; i < filas; i++) {
            hoja.autoSizeColumn((short) i);
        }

        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel", "xls");
        fc.setFileFilter(filter);
        fc.showSaveDialog(null); //Muestra el diálogo
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
        }

    }
    // ------------------
}
