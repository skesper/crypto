package de.kesper.crypto.gui;

import javax.swing.*;
import java.awt.*;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:04
 */
public class CryptoMainFrame extends javax.swing.JFrame {
    private JTextPane editorPane;
    private javax.swing.JMenuBar theMenu;
    private javax.swing.JScrollPane scrollPane;


    public CryptoMainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        editorPane = new JTextPane();
        editorPane.setFont(Font.getFont("Courier New"));
        theMenu = MenuCreator.create();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        scrollPane.setViewportView(editorPane);

        setJMenuBar(theMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();
    }

    public String getTextContent() {
        return this.editorPane.getText();
    }

    public void setTextContent(String text) {
        this.editorPane.setText(text);
        this.editorPane.setCaretPosition(editorPane.getDocument().getLength());
    }
}
