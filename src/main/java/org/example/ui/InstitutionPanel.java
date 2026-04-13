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
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(20);
        categoryCombo = new JComboBox<>();
        JButton searchButton = new JButton("Пошук");
        JButton refreshButton = new JButton("Оновити");

        topPanel.add(new JLabel("Ключове слово:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Категорія:"));
        topPanel.add(categoryCombo);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Назва", "Категорія", "Адреса", "Телефон"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table),
                new JScrollPane(detailsArea)
        );
        splitPane.setDividerLocation(280);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

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

        detailsArea.setText("");
    }

    private void showSelectedInstitution() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        Institution institution = store.findInstitutionById(id);
        if (institution == null) return;

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
                sb.append("• ").append(service.getName())
                        .append(" (").append(service.getExecutionTime()).append(")\n");
            }
        }

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }
}