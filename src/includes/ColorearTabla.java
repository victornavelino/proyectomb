/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author franco
 */
public class ColorearTabla extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean Selected, boolean hasFocus, int row, int col) {

        super.getTableCellRendererComponent(table, value, Selected, hasFocus, row, col);

        try {
            switch (table.getValueAt(row, 2).toString()) {
                case "Venta CC":
                    setBackground(Color.RED);
                    break;
                case "Cobranza":
                    setBackground(Color.GREEN);
                    break;
            }
        } catch (Exception e) {
            setBackground(Color.WHITE);

        }

        return this;

    }

}
