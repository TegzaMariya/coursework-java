package org.example.ui;

import org.example.storage.DataStore;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final JLabel institutionsCount;
    private final JLabel servicesCount;
    private final JLabel usersCount;

    public DashboardPanel() {
        setLayout(new BorderLayout(25, 25));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);

        titleBox.add(UiTheme.title("Dashboard"));
        titleBox.add(UiTheme.subtitle("Загальна панель керування інформаційною системою"));

        header.add(titleBox, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(25, 25));
        content.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1, 3, 25, 25));
        cards.setOpaque(false);

        institutionsCount = new JLabel();
        servicesCount = new JLabel();
        usersCount = new JLabel();

        cards.add(statCard("Установи", institutionsCount, "Кількість державних установ у системі"));
        cards.add(statCard("Послуги", servicesCount, "Загальна кількість доступних послуг"));
        cards.add(statCard("Користувачі", usersCount, "Адміністративні облікові записи"));

        JPanel info = UiTheme.card();
        info.setLayout(new BorderLayout(15, 15));

        JLabel infoTitle = new JLabel("Стан системи");
        infoTitle.setForeground(UiTheme.TEXT);
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setOpaque(false);
        text.setForeground(UiTheme.MUTED);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setText("""
                Система працює у режимі локальної CRM-панелі.
                Дані зберігаються у CSV-файлах, що дозволяє швидко переглядати, додавати, редагувати та видаляти інформацію без окремої бази даних.

                Інтерфейс має структуру професійної CRM:
                - бокове меню навігації;
                - dashboard зі статистикою;
                - сучасні таблиці;
                - адміністративну панель;
                - пошук і фільтрацію даних.
                """);

        info.add(infoTitle, BorderLayout.NORTH);
        info.add(text, BorderLayout.CENTER);

        content.add(cards, BorderLayout.NORTH);
        content.add(info, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel statCard(String title, JLabel valueLabel, String description) {
        JPanel panel = UiTheme.card();
        panel.setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(UiTheme.MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setForeground(UiTheme.MUTED);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(descLabel, BorderLayout.SOUTH);

        return panel;
    }

    public void refreshData() {
        DataStore store = DataStore.getInstance();

        institutionsCount.setText(String.valueOf(store.getInstitutions().size()));
        servicesCount.setText(String.valueOf(store.getServices().size()));
        usersCount.setText(String.valueOf(store.getUsers().size()));
    }
}