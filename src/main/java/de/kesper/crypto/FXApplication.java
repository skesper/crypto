package de.kesper.crypto;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: kesper
 * Date: 12.07.13
 * Time: 11:22
 */
public class FXApplication extends javafx.application.Application {

    public static void main(String[] args) throws Exception {
        javafx.application.Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            primaryStage.setTitle("Crypto");
            URL url = ClassLoader.getSystemClassLoader().getResource("crypto-fx.fxml");
            System.out.println("DEBUG: url = "+url);
            Pane myPane = FXMLLoader.load(url);
            Scene myScene = new Scene(myPane);
            primaryStage.setScene(myScene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
