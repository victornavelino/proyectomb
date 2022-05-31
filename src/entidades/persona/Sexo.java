/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.persona;

/**
 *
 * @author Carlos
 */

public enum Sexo  {

  MASCULINO("MASCULINO"),
    FEMENINO("FEMENINO");

    private String name;

    private Sexo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
//para llenar combo box
        return name;
    }
}
