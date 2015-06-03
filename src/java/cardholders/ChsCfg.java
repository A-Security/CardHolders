/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardholders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class ChsCfg {
    private String GRhost;
    private String AShost;
    
    public ChsCfg(){
        FileInputStream fileInput = null;
        Properties prop = new Properties();
        try {
            File file = new File("/opt/wso2/appconf/cardholders.cfg");
            if (!file.exists()){
                file = new File("C:/opt/wso2/appconf/cardholders.cfg");
                if (!file.exists()){
                    return;
                }
            }
            fileInput = new FileInputStream(file);
            prop.loadFromXML(fileInput);
            this.GRhost = prop.getProperty("GRhost");
            this.AShost = prop.getProperty("AShost");
        } catch (IOException ex) {
            Logger.getLogger(ChsCfg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInput.close();
            } catch (IOException ex) {
                Logger.getLogger(ChsCfg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * @return the GRhost
     */
    public String getGRhost() {
        return GRhost;
    }

    /**
     * @return the AShost
     */
    public String getAShost() {
        return AShost;
    }
}
