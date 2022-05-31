/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package includes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author AFerSor
 */
public class Encrypter {
    
   public static String encriptar(String password) throws Exception {
        try {
            
            if(password.isEmpty()){
                return "";
            }//fin if
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

          
            //convert the byte to hex format 
            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                    String hex=Integer.toHexString(0xff & byteData[i]);
                        if(hex.length()==1) hexString.append('0');
                        hexString.append(hex);
            }

            return hexString.toString();

             } catch (NoSuchAlgorithmException ex) {
                 throw new Exception("Se ha producido un error al guardar el usuario");
             }
    }//fin encriptar
}//FIN CLASE
