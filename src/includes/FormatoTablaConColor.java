/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.el.impl.lang.ELArithmetic;

/**
 *
 * @author vicky
 */
public class FormatoTablaConColor extends DefaultTableCellRenderer{

    private int columna_patron ;

    public FormatoTablaConColor(int Colpatron)
    {
        this.columna_patron = Colpatron;
    }
   
    
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean selected, boolean focused, int row, int column )
    {  
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        table.setFont(new Font("Verdana", Font.PLAIN, 10));
        try{
            //System.out.println("fila: " + table.getValueAt(row,columna_patron));
            BigDecimal bdAux = (BigDecimal)table.getValueAt(row,columna_patron);
            //Double.parseDouble((String) table.getValueAt(row,columna_patron));
            setBackground(new Color(153,204,255));
            table.setFont(new Font("Verdana", Font.BOLD, 14));
        }catch(Exception e){
            
        }
        
        if( table.getValueAt(row,columna_patron)==" * EFECTIVO")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }
         if( table.getValueAt(row,columna_patron)==" * GASTOS")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }
        if( table.getValueAt(row,columna_patron)==" * CTA. CTE.")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }
        if( table.getValueAt(row,columna_patron)==" * PAGO DE SUELDO")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }       
        if( table.getValueAt(row,columna_patron)==" * RETIRO EFECTIVO")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }    
        
        if( table.getValueAt(row,columna_patron)==" * TARJETAS")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        } 
        
        if( table.getValueAt(row,columna_patron)==" * VALES")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }        
        if( table.getValueAt(row,columna_patron)==" * INGRESOS")
        {
            setBackground(new Color(204,255,255));
            table.setFont(new Font("Verdana", Font.BOLD, 10));
        }  
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
 }
    
}
