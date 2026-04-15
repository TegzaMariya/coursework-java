package org.example.ui;

import org.example.model.User;
import org.example.storage.DataStore;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private User authenticatedUser;

    public LoginDialog(Frame parent) {
        super(parent, "Вхід адміністратора", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("Логін:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Увійти");
        JButton cancelButton = new JButton("Скасувати");

        loginButton.addActionListener(e -> authenticate());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = DataStore.getInstance().authenticate(username, password);

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            authenticatedUser = user;
            JOptionPane.showMessageDialog(this, "Вхід успішний.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Невірний логін або пароль.");
        }
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}