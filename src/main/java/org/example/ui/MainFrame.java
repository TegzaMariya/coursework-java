package org.example.ui;

import org.example.model.User;
import org.example.storage.DataStore;
import org.example.util.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
    private final InstitutionPanel institutionPanel;
    private final ServicePanel servicePanel;
    private final JTabbedPane tabbedPane;

    private AdminPanel adminPanel;
    private boolean adminLoggedIn = false;

    public MainFrame() {
        DataStore.getInstance();

        setTitle("Довідник державних установ та послуг м. Ужгород");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        institutionPanel = new InstitutionPanel();
        servicePanel = new ServicePanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Установи", institutionPanel);
        tabbedPane.addTab("Послуги", servicePanel);

        setJMenuBar(createMenuBar());
        add(tabbedPane, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "Інформаційна система «Довідник державних установ та послуг м. Ужгород»",
                SwingConstants.CENTER
        );
        footer.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        add(footer, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem reportItem = new JMenuItem("Згенерувати звіт");
        JMenuItem exitItem = new JMenuItem("Вихід");

        reportItem.addActionListener(e -> generateReport());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(reportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu adminMenu = new JMenu("Адміністрування");
        JMenuItem loginItem = new JMenuItem("Увійти як адміністратор");
        JMenuItem logoutItem = new JMenuItem("Вийти з режиму адміністратора");

        loginItem.addActionListener(e -> adminLogin());
        logoutItem.addActionListener(e -> adminLogout());

        adminMenu.add(loginItem);
        adminMenu.add(logoutItem);

        JMenu helpMenu = new JMenu("Довідка");
        JMenuItem aboutItem = new JMenuItem("Про програму");

        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(adminMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void adminLogin() {
        if (adminLoggedIn) {
            JOptionPane.showMessageDialog(this, "Адміністратор уже увійшов.");
            return;
        }

        LoginDialog dialog = new LoginDialog(this);
        dialog.setVisible(true);

        User user = dialog.getAuthenticatedUser();
        if (user != null) {
            adminLoggedIn = true;
            adminPanel = new AdminPanel(this);
            tabbedPane.addTab("Адмін-панель", adminPanel);
            tabbedPane.setSelectedComponent(adminPanel);
        }
    }

    private void adminLogout() {
        if (!adminLoggedIn) {
            JOptionPane.showMessageDialog(this, "Режим адміністратора не активний.");
            return;
        }

        int adminTab = tabbedPane.indexOfTab("Адмін-панель");
        if (adminTab >= 0) {
            tabbedPane.remove(adminTab);
        }

        adminLoggedIn = false;
        adminPanel = null;
        JOptionPane.showMessageDialog(this, "Ви вийшли з режиму адміністратора.");
    }

    private void generateReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Зберегти звіт");
        chooser.setSelectedFile(new File("report_uzhhorod_guide.txt"));

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                ReportGenerator.generateReport(
                        chooser.getSelectedFile().getAbsolutePath(),
                        DataStore.getInstance().getInstitutions(),
                        DataStore.getInstance().getServices()
                );
                JOptionPane.showMessageDialog(this, "Звіт збережено успішно.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Помилка при формуванні звіту.");
                e.printStackTrace();
            }
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                """
                Інформаційна система
                "Довідник державних установ та послуг м. Ужгород"

                Можливості:
                - перегляд установ
                - перегляд послуг
                - пошук та фільтрація
                - авторизація адміністратора
                - додавання, редагування, видалення даних
                - генерація текстового звіту
                - збереження інформації у CSV-файлах

                Розроблено на Java Swing без використання СУБД.
                """,
                "Про програму",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void refreshAllData() {
        institutionPanel.refreshData();
        servicePanel.refreshData();
        revalidate();
        repaint();
    }
}