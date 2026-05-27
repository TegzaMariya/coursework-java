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

        super(parent, "Авторизація адміністратора", true);

        setSize(680, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        setLayout(new BorderLayout());

        getContentPane().setBackground(UiTheme.BG);

        JPanel main = UiTheme.card();

        main.setLayout(new BorderLayout(20, 30));

        main.setBorder(BorderFactory.createEmptyBorder(
                40,
                45,
                40,
                45
        ));

        JLabel title = new JLabel("Вхід адміністратора");

        title.setForeground(Color.WHITE);

        title.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                28
        ));

        JLabel subtitle = new JLabel(
                "Увійдіть, щоб керувати установами та послугами"
        );

        subtitle.setForeground(UiTheme.MUTED);

        subtitle.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                16
        ));

        JPanel header = new JPanel(
                new GridLayout(2, 1, 0, 10)
        );

        header.setOpaque(false);

        header.add(title);
        header.add(subtitle);

        JPanel form = new JPanel(
                new GridLayout(4, 1, 0, 10)
        );

        form.setOpaque(false);

        usernameField = new JTextField();

        usernameField.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                16
        ));

        usernameField.setBackground(
                new Color(55, 60, 65)
        );

        usernameField.setForeground(Color.WHITE);

        usernameField.setCaretColor(Color.WHITE);

        usernameField.setPreferredSize(
                new Dimension(420, 42)
        );

        usernameField.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        14,
                        8,
                        14
                )
        );

        passwordField = new JPasswordField();

        passwordField.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                16
        ));

        passwordField.setBackground(
                new Color(55, 60, 65)
        );

        passwordField.setForeground(Color.WHITE);

        passwordField.setCaretColor(Color.WHITE);

        passwordField.setPreferredSize(
                new Dimension(420, 42)
        );

        passwordField.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        14,
                        8,
                        14
                )
        );

        JLabel loginLabel = new JLabel("Логін");

        loginLabel.setForeground(UiTheme.MUTED);

        loginLabel.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                16
        ));

        JLabel passwordLabel = new JLabel("Пароль");

        passwordLabel.setForeground(UiTheme.MUTED);

        passwordLabel.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                16
        ));

        form.add(loginLabel);
        form.add(usernameField);
        form.add(passwordLabel);
        form.add(passwordField);

        JPanel buttons = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        buttons.setOpaque(false);

        JButton loginButton =
                UiTheme.primaryButton("Увійти");

        JButton cancelButton =
                UiTheme.secondaryButton("Скасувати");

        loginButton.addActionListener(
                e -> authenticate()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        buttons.add(loginButton);
        buttons.add(cancelButton);

        main.add(header, BorderLayout.NORTH);

        main.add(form, BorderLayout.CENTER);

        main.add(buttons, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        getRootPane().setDefaultButton(loginButton);

        SwingUtilities.invokeLater(() ->
                usernameField.requestFocusInWindow()
        );
    }

    private void authenticate() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        User user =
                DataStore.getInstance()
                        .authenticate(username, password);

        if (user != null
                && "ADMIN".equalsIgnoreCase(
                user.getRole()
        )) {

            authenticatedUser = user;

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Невірний логін або пароль."
            );
        }
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}