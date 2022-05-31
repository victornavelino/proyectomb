/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author AFerSor
 */
public class JButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{

    
    Boolean currentValue;
    JButton button;
    JButton buttonEliminar;
    protected static final String EDIT = "edit";
    private JTable jTable1;
    
     public JButtonEditor(JTable jTable1, String texto, ActionListener al, ImageIcon ic) {
        button = new JButton();
        buttonEliminar = new JButton();
        //button.setActionCommand(EDIT);
        button.addActionListener(al);
        button.setToolTipText(texto);
        button.setIcon(ic);
        button.setBorderPainted(false);
        this.jTable1 = jTable1;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // mymodel t = (mymodel) jTable1.getModel();
        // t.addNewRecord();
        fireEditingStopped();
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Va a mostrar el botón solo en la última fila.
        // de otra forma muestra un espacio en blanco.
 
            return button;
       // }
       // return new JLabel();
    }
    
}
