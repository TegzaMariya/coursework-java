package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());

            UIManager.put("Button.arc", 18);
            UIManager.put("Component.arc", 18);
            UIManager.put("TextComponent.arc", 18);
            UIManager.put("Table.showHorizontalLines", false);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 12);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}