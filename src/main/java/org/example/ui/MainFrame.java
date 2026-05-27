package org.example.ui;

import org.example.model.User;
import org.example.storage.DataStore;
import org.example.util.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final InstitutionPanel institutionPanel;
    private final ServicePanel servicePanel;

    private DashboardPanel dashboardPanel;
    private AdminPanel adminPanel;

    private boolean adminLoggedIn = false;

    public MainFrame() {
        DataStore.getInstance();

        setTitle("GovCRM | Довідник державних установ та послуг м. Ужгород");
        setSize(1450, 850);
        setMinimumSize(new Dimension(1200, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.BG);

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UiTheme.BG);

        dashboardPanel = new DashboardPanel();
        institutionPanel = new InstitutionPanel();
        servicePanel = new ServicePanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(institutionPanel, "institutions");
        contentPanel.add(servicePanel, "services");

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setBackground(UiTheme.SIDEBAR);

        JPanel logoPanel = new JPanel(new GridLayout(2, 1));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 25, 25));

        JLabel logo = new JLabel("GovCRM");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel subtitle = new JLabel("Municipal management system");
        subtitle.setForeground(UiTheme.MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        logoPanel.add(logo);
        logoPanel.add(subtitle);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new GridLayout(8, 1, 0, 14));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton dashboardBtn = sidebarButton("Dashboard");
        JButton institutionsBtn = sidebarButton("Установи");
        JButton servicesBtn = sidebarButton("Послуги");
        JButton reportBtn = sidebarButton("Звіт");
        JButton adminBtn = sidebarButton("Адмін-панель");
        JButton aboutBtn = sidebarButton("Про програму");
        JButton exitBtn = sidebarButton("Вихід");

        dashboardBtn.addActionListener(e -> cardLayout.show(contentPanel, "dashboard"));
        institutionsBtn.addActionListener(e -> cardLayout.show(contentPanel, "institutions"));
        servicesBtn.addActionListener(e -> cardLayout.show(contentPanel, "services"));
        reportBtn.addActionListener(e -> generateReport());
        adminBtn.addActionListener(e -> adminLogin());
        aboutBtn.addActionListener(e -> showAbout());
        exitBtn.addActionListener(e -> System.exit(0));

        menu.add(dashboardBtn);
        menu.add(institutionsBtn);
        menu.add(servicesBtn);
        menu.add(reportBtn);
        menu.add(adminBtn);
        menu.add(aboutBtn);
        menu.add(exitBtn);

        JLabel footer = new JLabel("<html><center>Uzhhorod<br>Government Services</center></html>", SwingConstants.CENTER);
        footer.setForeground(UiTheme.MUTED);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(20, 20, 25, 20));

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton sidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(UiTheme.TEXT);
        button.setBackground(new Color(27, 32, 44));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(UiTheme.PRIMARY);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(27, 32, 44));
            }
        });

        return button;
    }

    private void adminLogin() {
        if (adminLoggedIn) {
            cardLayout.show(contentPanel, "admin");
            return;
        }

        LoginDialog dialog = new LoginDialog(this);
        dialog.setVisible(true);

        User user = dialog.getAuthenticatedUser();

        if (user != null) {
            adminLoggedIn = true;
            adminPanel = new AdminPanel(this);
            contentPanel.add(adminPanel, "admin");
            cardLayout.show(contentPanel, "admin");
        }
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
                GovCRM System

                Інформаційна система:
                "Довідник державних установ та послуг м. Ужгород"

                Можливості:
                - перегляд установ
                - перегляд послуг
                - пошук та фільтрація
                - авторизація адміністратора
                - додавання, редагування, видалення даних
                - генерація звітів
                - збереження інформації у CSV-файлах
                """,
                "Про програму",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public void refreshAllData() {
        dashboardPanel.refreshData();
        institutionPanel.refreshData();
        servicePanel.refreshData();

        if (adminPanel != null) {
            adminPanel.repaint();
        }

        revalidate();
        repaint();
    }
}