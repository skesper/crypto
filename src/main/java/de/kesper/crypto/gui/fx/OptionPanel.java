package de.kesper.crypto.gui.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * User: kesper
 * Date: 12.07.13
 * Time: 14:32
 */
public class OptionPanel {

    public static void showMessageDialog(String title, String message, Image icon, MessageDialogListener listener) throws IOException {

        URL url = ClassLoader.getSystemClassLoader().getResource("message-dialog.fxml");
        FXMLLoader loader = new FXMLLoader();
        Pane aboutPane = loader.load(url);
        MessageDialogController controller = loader.getController();

        controller.setText(message);
        controller.setIcon(icon);
        controller.setListener(listener);

        Scene aboutScene = new Scene(aboutPane);
        Stage s = new Stage();
        s.setTitle(title);
        s.setScene(aboutScene);
        s.show();

    }

    public static interface MessageDialogListener {
        public static final int OK_OPTION = 1;
        public static final int CANCEL_OPTION = 2;

        public void buttonPressed(int button);
    }
}
