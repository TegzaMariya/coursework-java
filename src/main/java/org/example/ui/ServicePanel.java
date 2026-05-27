package org.example.ui;

import org.example.model.GovService;
import org.example.model.Institution;
import org.example.storage.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServicePanel extends JPanel {

    private final DataStore store;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextArea detailsArea;
    private final JTextField searchField;

    public ServicePanel() {
        store = DataStore.getInstance();

        setLayout(new BorderLayout(25, 25));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout(20, 20));
        header.setOpaque(false);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(UiTheme.title("Послуги"));
        titleBox.add(UiTheme.subtitle("Каталог державних послуг з описом, документами, термінами та вартістю"));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        filters.setOpaque(false);

        searchField = UiTheme.textField();
        searchField.setPreferredSize(new Dimension(300, 42));

        JButton searchButton = UiTheme.primaryButton("Пошук");
        JButton refreshButton = UiTheme.secondaryButton("Оновити");

        filters.add(searchField);
        filters.add(searchButton);
        filters.add(refreshButton);

        header.add(titleBox, BorderLayout.WEST);
        header.add(filters, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Назва послуги", "Установа", "Термін", "Вартість"}, 0
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

        add(splitPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> loadServices());
        refreshButton.addActionListener(e -> refreshData());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedService();
            }
        });

        refreshData();
    }

    public void refreshData() {
        loadServices();
    }

    private void loadServices() {
        tableModel.setRowCount(0);

        String keyword = searchField.getText().trim().toLowerCase();

        for (GovService service : store.getServices()) {
            Institution institution = store.findInstitutionById(service.getInstitutionId());
            String institutionName = institution != null ? institution.getName() : "Невідома установа";

            boolean matches =
                    keyword.isEmpty()
                            || service.getName().toLowerCase().contains(keyword)
                            || service.getDescription().toLowerCase().contains(keyword)
                            || institutionName.toLowerCase().contains(keyword);

            if (matches) {
                tableModel.addRow(new Object[]{
                        service.getId(),
                        service.getName(),
                        institutionName,
                        service.getExecutionTime(),
                        service.getCost()
                });
            }
        }

        detailsArea.setText("Оберіть послугу в таблиці, щоб переглянути детальну інформацію.");
    }

    private void showSelectedService() {
        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        GovService found = null;

        for (GovService service : store.getServices()) {
            if (service.getId() == id) {
                found = service;
                break;
            }
        }

        if (found == null) {
            return;
        }

        Institution institution = store.findInstitutionById(found.getInstitutionId());
        String institutionName = institution != null ? institution.getName() : "Невідома установа";

        StringBuilder sb = new StringBuilder();

        sb.append("Назва послуги: ").append(found.getName()).append("\n");
        sb.append("Установа: ").append(institutionName).append("\n");
        sb.append("Термін виконання: ").append(found.getExecutionTime()).append("\n");
        sb.append("Вартість: ").append(found.getCost()).append("\n\n");

        sb.append("Опис:\n").append(found.getDescription()).append("\n\n");
        sb.append("Необхідні документи:\n").append(found.getDocumentsRequired()).append("\n\n");
        sb.append("Примітки:\n").append(found.getNotes()).append("\n");

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }
}