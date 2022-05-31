/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carlos
 */
public class ModeloTablaNoEditable extends DefaultTableModel {

    List<Integer> columnasNoEditables = new ArrayList<>();

    @Override
    public boolean isCellEditable(int row, int column) {
        // Aquí devolvemos true o false según queramos que una celda
        // identificada por fila,columna (row,column), sea o no editable

        if (!columnasNoEditables.isEmpty()) {
            return !columnasNoEditables.contains(column);
        }

        return false;
    }

    public ModeloTablaNoEditable() {
    }

    public ModeloTablaNoEditable(List<Integer> columnasNoEditables) {
        this.columnasNoEditables = columnasNoEditables;
    }

    public ModeloTablaNoEditable(Vector data, Vector headers) {
        super(data, headers);
    }

    public ModeloTablaNoEditable(Vector data, Vector headers, List<Integer> columnasNoEditables) {
        super(data, headers);
        this.columnasNoEditables = columnasNoEditables;
    }
}
