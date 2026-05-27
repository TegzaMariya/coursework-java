package org.example.ui;

import org.example.model.GovService;
import org.example.model.Institution;
import org.example.storage.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InstitutionPanel extends JPanel {

    private final DataStore store;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextArea detailsArea;
    private final JTextField searchField;
    private final JComboBox<String> categoryCombo;

    public InstitutionPanel() {
        store = DataStore.getInstance();

        setLayout(new BorderLayout(25, 25));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout(20, 20));
        header.setOpaque(false);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(UiTheme.title("Установи"));
        titleBox.add(UiTheme.subtitle("Перегляд, пошук та фільтрація державних установ міста Ужгород"));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        filters.setOpaque(false);

        searchField = UiTheme.textField();
        searchField.setPreferredSize(new Dimension(270, 42));

        categoryCombo = new JComboBox<>();
        categoryCombo.setPreferredSize(new Dimension(180, 42));

        JButton searchButton = UiTheme.primaryButton("Пошук");
        JButton refreshButton = UiTheme.secondaryButton("Оновити");

        filters.add(searchField);
        filters.add(categoryCombo);
        filters.add(searchButton);
        filters.add(refreshButton);

        header.add(titleBox, BorderLayout.WEST);
        header.add(filters, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.setOpaque(false);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Назва", "Категорія", "Адреса", "Телефон"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiTheme.styleTable(table);

        detailsArea = UiTheme.textArea(8);
        detailsArea.setEditable(false);
        detailsArea.setForeground(UiTheme.TEXT);
        detailsArea.setBackground(UiTheme.CARD);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                UiTheme.scroll(table),
                UiTheme.scroll(detailsArea)
        );

        splitPane.setDividerLocation(430);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.72);

        center.add(splitPane, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        searchButton.addActionListener(e -> loadInstitutions());
        refreshButton.addActionListener(e -> refreshData());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedInstitution();
            }
        });

        refreshData();
    }

    public void refreshData() {
        loadCategories();
        loadInstitutions();
    }

    private void loadCategories() {
        categoryCombo.removeAllItems();
        categoryCombo.addItem("Усі");

        Set<String> categories = new LinkedHashSet<>();

        for (Institution institution : store.getInstitutions()) {
            categories.add(institution.getCategory());
        }

        for (String category : categories) {
            categoryCombo.addItem(category);
        }
    }

    private void loadInstitutions() {
        tableModel.setRowCount(0);

        String keyword = searchField.getText().trim().toLowerCase();
        String category = String.valueOf(categoryCombo.getSelectedItem());

        for (Institution institution : store.getInstitutions()) {
            boolean matchesKeyword =
                    keyword.isEmpty()
                            || institution.getName().toLowerCase().contains(keyword)
                            || institution.getAddress().toLowerCase().contains(keyword)
                            || institution.getDescription().toLowerCase().contains(keyword);

            boolean matchesCategory =
                    category == null
                            || category.equals("Усі")
                            || institution.getCategory().equals(category);

            if (matchesKeyword && matchesCategory) {
                tableModel.addRow(new Object[]{
                        institution.getId(),
                        institution.getName(),
                        institution.getCategory(),
                        institution.getAddress(),
                        institution.getPhone()
                });
            }
        }

        detailsArea.setText("Оберіть установу в таблиці, щоб переглянути детальну інформацію.");
    }

    private void showSelectedInstitution() {
        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        Institution institution = store.findInstitutionById(id);

        if (institution == null) {
            return;
        }

        List<GovService> services = store.getServicesByInstitutionId(id);

        StringBuilder sb = new StringBuilder();

        sb.append("Назва: ").append(institution.getName()).append("\n");
        sb.append("Категорія: ").append(institution.getCategory()).append("\n");
        sb.append("Адреса: ").append(institution.getAddress()).append("\n");
        sb.append("Телефон: ").append(institution.getPhone()).append("\n");
        sb.append("Email: ").append(institution.getEmail()).append("\n");
        sb.append("Сайт: ").append(institution.getWebsite()).append("\n");
        sb.append("Графік роботи: ").append(institution.getWorkingHours()).append("\n\n");

        sb.append("Опис:\n").append(institution.getDescription()).append("\n\n");

        sb.append("Послуги:\n");

        if (services.isEmpty()) {
            sb.append("Немає послуг.\n");
        } else {
            for (GovService service : services) {
                sb.append("• ")
                        .append(service.getName())
                        .append(" — ")
                        .append(service.getExecutionTime())
                        .append("\n");
            }
        }

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }
}