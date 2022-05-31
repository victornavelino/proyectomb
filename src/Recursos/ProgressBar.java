/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import javax.swing.JProgressBar;

/**
 *
 * @author vicky
 */
public class ProgressBar implements Runnable {

    private JProgressBar jProgressBar;
    private int i = 1;
    private int tamanio = 50;//retardo en milisegundos

    public ProgressBar(JProgressBar jProgressBar, int tamanio ) {
        this.jProgressBar = jProgressBar;
        this.tamanio = tamanio;
    }
    
    
    @Override
    public void run() {
        i=1;        
        //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        //while( !Job.band )
        //{
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1
            i = (i > tamanio) ? 1 : i+1;
            System.out.println("valor progreso: " + i);
            jProgressBar.setValue(i);
            jProgressBar.repaint();  
            //retardo en milisegundos
            try{
                Thread.sleep( 50 );
            }        
            catch (InterruptedException e){ 
                System.err.println( e.getMessage() ); 
            }            
            //si el trabajo en paralelo a terminado
            /*if( Job.band )
            {
                jProgressBar.setValue(100);
                System.out.println("Trabajo finalizado...");
                break;//rompe ciclo 
            }*/            
        }
    
    
}
