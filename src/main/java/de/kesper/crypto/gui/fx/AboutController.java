/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.kesper.crypto.gui.fx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

/**
 *
 * @author kesper
 */
public class AboutController implements Initializable {

    @FXML
    private WebView view;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        view.getEngine().loadContent(readCopyright("copyright.html"));
        
//        view.getEngine().loadContent(
//                "<html>"
//                + "<head><style>body {background-color: #333; color: #fff; font-family: sans-serif; font-size: 10pt;}</style></head>"
//                + "<body><h3>(c) 2013 Stephan Kesper</h3>"
//                + "<p>Crypto is an encryption/decryption tool based on 8192 bit RSA and 256 bit AES technology.</p>"
//                + "<p>Use it with care, do not let NSA read your mails and do <b>not</b> destroy your key ring!</p>"
//                + "</body></html>"
//                );
    }
    
    private String readCopyright(String source) {
        try {
            Path p = Paths.get(source);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Files.copy(p, baos);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
