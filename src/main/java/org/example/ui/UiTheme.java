package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UiTheme {

    public static final Color BG = new Color(15, 18, 26);
    public static final Color SIDEBAR = new Color(18, 22, 32);
    public static final Color CARD = new Color(27, 32, 44);
    public static final Color CARD_HOVER = new Color(35, 42, 58);
    public static final Color PRIMARY = new Color(75, 135, 255);
    public static final Color PRIMARY_HOVER = new Color(96, 154, 255);
    public static final Color TEXT = new Color(235, 239, 245);
    public static final Color MUTED = new Color(150, 160, 175);
    public static final Color DANGER = new Color(235, 80, 95);

    private UiTheme() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 18, 12, 18));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY);
            }
        });

        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(TEXT);
        button.setBackground(CARD);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 18, 12, 18));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(CARD_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(CARD);
            }
        });

        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = secondaryButton(text);
        button.setBackground(DANGER);
        return button;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(240, 42));
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        return field;
    }

    public static JTextArea textArea(int rows) {
        JTextArea area = new JTextArea(rows, 22);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
        return area;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(46);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(CARD);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(TEXT);
        header.setBackground(new Color(32, 38, 52));
        header.setPreferredSize(new Dimension(0, 46));
    }

    public static JScrollPane scroll(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD);
        return scrollPane;
    }
}