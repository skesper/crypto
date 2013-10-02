package de.kesper.crypto.gui.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: kesper
 * Date: 12.07.13
 * Time: 14:27
 */
public class MessageDialogController implements Initializable {

    @FXML
    private TextArea textArea;

    @FXML
    private ImageView imageView;

    private OptionPanel.MessageDialogListener listener;

    public void setText(String text) {
        textArea.setText(text);
    }

    public void setIcon(Image icon) {
        imageView.setImage(icon);
    }

    public void setListener(OptionPanel.MessageDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void cancelPressed() {
        if (listener==null) return;
        listener.buttonPressed(OptionPanel.MessageDialogListener.CANCEL_OPTION);
    }

    public void okPressed() {
        if (listener==null) return;
        listener.buttonPressed(OptionPanel.MessageDialogListener.OK_OPTION);
    }


}
