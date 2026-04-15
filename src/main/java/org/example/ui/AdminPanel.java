package org.example.ui;

import org.example.model.GovService;
import org.example.model.Institution;
import org.example.storage.DataStore;
import org.example.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private final MainFrame mainFrame;
    private final DataStore store;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.store = DataStore.getInstance();

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Панель адміністратора", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addInstitutionButton = new JButton("Додати установу");
        JButton editInstitutionButton = new JButton("Редагувати установу");
        JButton deleteInstitutionButton = new JButton("Видалити установу");
        JButton addServiceButton = new JButton("Додати послугу");
        JButton editServiceButton = new JButton("Редагувати послугу");
        JButton deleteServiceButton = new JButton("Видалити послугу");

        buttonPanel.add(addInstitutionButton);
        buttonPanel.add(editInstitutionButton);
        buttonPanel.add(deleteInstitutionButton);
        buttonPanel.add(addServiceButton);
        buttonPanel.add(editServiceButton);
        buttonPanel.add(deleteServiceButton);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        addInstitutionButton.addActionListener(e -> addInstitution());
        editInstitutionButton.addActionListener(e -> editInstitution());
        deleteInstitutionButton.addActionListener(e -> deleteInstitution());
        addServiceButton.addActionListener(e -> addService());
        editServiceButton.addActionListener(e -> editService());
        deleteServiceButton.addActionListener(e -> deleteService());
    }

    private void addInstitution() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField websiteField = new JTextField();
        JTextField hoursField = new JTextField();
        JTextArea descriptionArea = new JTextArea(4, 20);

        Object[] message = {
                "Назва:", nameField,
                "Категорія:", categoryField,
                "Адреса:", addressField,
                "Телефон:", phoneField,
                "Email:", emailField,
                "Сайт:", websiteField,
                "Графік роботи:", hoursField,
                "Опис:", new JScrollPane(descriptionArea)
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Додати установу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (Validator.isEmpty(nameField.getText())
                    || Validator.isEmpty(categoryField.getText())
                    || Validator.isEmpty(addressField.getText())) {
                JOptionPane.showMessageDialog(this, "Назва, категорія та адреса є обов'язковими.");
                return;
            }

            if (!Validator.isValidEmail(emailField.getText())
                    || !Validator.isValidPhone(phoneField.getText())
                    || !Validator.isValidUrl(websiteField.getText())) {
                JOptionPane.showMessageDialog(this, "Перевірте правильність email, телефону або сайту.");
                return;
            }

            Institution institution = new Institution(
                    store.nextInstitutionId(),
                    nameField.getText().trim(),
                    categoryField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    websiteField.getText().trim(),
                    hoursField.getText().trim(),
                    descriptionArea.getText().trim()
            );

            store.getInstitutions().add(institution);
            store.saveInstitutions();
            JOptionPane.showMessageDialog(this, "Установу додано.");
            mainFrame.refreshAllData();
        }
    }

    private void editInstitution() {
        List<Institution> institutions = store.getInstitutions();
        if (institutions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Список установ порожній.");
            return;
        }

        Institution selected = (Institution) JOptionPane.showInputDialog(
                this,
                "Оберіть установу:",
                "Редагування установи",
                JOptionPane.PLAIN_MESSAGE,
                null,
                institutions.toArray(),
                null
        );

        if (selected == null) return;

        JTextField nameField = new JTextField(selected.getName());
        JTextField categoryField = new JTextField(selected.getCategory());
        JTextField addressField = new JTextField(selected.getAddress());
        JTextField phoneField = new JTextField(selected.getPhone());
        JTextField emailField = new JTextField(selected.getEmail());
        JTextField websiteField = new JTextField(selected.getWebsite());
        JTextField hoursField = new JTextField(selected.getWorkingHours());
        JTextArea descriptionArea = new JTextArea(selected.getDescription(), 4, 20);

        Object[] message = {
                "Назва:", nameField,
                "Категорія:", categoryField,
                "Адреса:", addressField,
                "Телефон:", phoneField,
                "Email:", emailField,
                "Сайт:", websiteField,
                "Графік роботи:", hoursField,
                "Опис:", new JScrollPane(descriptionArea)
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Редагувати установу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (Validator.isEmpty(nameField.getText())
                    || Validator.isEmpty(categoryField.getText())
                    || Validator.isEmpty(addressField.getText())) {
                JOptionPane.showMessageDialog(this, "Назва, категорія та адреса є обов'язковими.");
                return;
            }

            selected.setName(nameField.getText().trim());
            selected.setCategory(categoryField.getText().trim());
            selected.setAddress(addressField.getText().trim());
            selected.setPhone(phoneField.getText().trim());
            selected.setEmail(emailField.getText().trim());
            selected.setWebsite(websiteField.getText().trim());
            selected.setWorkingHours(hoursField.getText().trim());
            selected.setDescription(descriptionArea.getText().trim());

            store.saveInstitutions();
            JOptionPane.showMessageDialog(this, "Установу оновлено.");
            mainFrame.refreshAllData();
        }
    }

    private void deleteInstitution() {
        List<Institution> institutions = store.getInstitutions();
        if (institutions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Список установ порожній.");
            return;
        }

        Institution selected = (Institution) JOptionPane.showInputDialog(
                this,
                "Оберіть установу:",
                "Видалення установи",
                JOptionPane.WARNING_MESSAGE,
                null,
                institutions.toArray(),
                null
        );

        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Видалити \"" + selected.getName() + "\"?\nПов'язані послуги також буде видалено.",
                "Підтвердження",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            store.deleteInstitution(selected.getId());
            JOptionPane.showMessageDialog(this, "Установу видалено.");
            mainFrame.refreshAllData();
        }
    }

    private void addService() {
        List<Institution> institutions = store.getInstitutions();
        if (institutions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Спочатку додайте хоча б одну установу.");
            return;
        }

        JComboBox<Institution> institutionBox = new JComboBox<>(institutions.toArray(new Institution[0]));
        JTextField nameField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        JTextArea docsArea = new JTextArea(3, 20);
        JTextField timeField = new JTextField();
        JTextField costField = new JTextField();
        JTextArea notesArea = new JTextArea(3, 20);

        Object[] message = {
                "Установа:", institutionBox,
                "Назва послуги:", nameField,
                "Опис:", new JScrollPane(descriptionArea),
                "Необхідні документи:", new JScrollPane(docsArea),
                "Термін виконання:", timeField,
                "Вартість:", costField,
                "Примітки:", new JScrollPane(notesArea)
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Додати послугу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Institution institution = (Institution) institutionBox.getSelectedItem();
            if (institution == null || Validator.isEmpty(nameField.getText())) {
                JOptionPane.showMessageDialog(this, "Назва послуги є обов'язковою.");
                return;
            }

            GovService service = new GovService(
                    store.nextServiceId(),
                    institution.getId(),
                    nameField.getText().trim(),
                    descriptionArea.getText().trim(),
                    docsArea.getText().trim(),
                    timeField.getText().trim(),
                    costField.getText().trim(),
                    notesArea.getText().trim()
            );

            store.getServices().add(service);
            store.saveServices();
            JOptionPane.showMessageDialog(this, "Послугу додано.");
            mainFrame.refreshAllData();
        }
    }

    private void editService() {
        List<GovService> services = store.getServices();
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Список послуг порожній.");
            return;
        }

        GovService selected = (GovService) JOptionPane.showInputDialog(
                this,
                "Оберіть послугу:",
                "Редагування послуги",
                JOptionPane.PLAIN_MESSAGE,
                null,
                services.toArray(),
                null
        );

        if (selected == null) return;

        List<Institution> institutions = store.getInstitutions();
        JComboBox<Institution> institutionBox = new JComboBox<>(institutions.toArray(new Institution[0]));

        for (Institution i : institutions) {
            if (i.getId() == selected.getInstitutionId()) {
                institutionBox.setSelectedItem(i);
                break;
            }
        }

        JTextField nameField = new JTextField(selected.getName());
        JTextArea descriptionArea = new JTextArea(selected.getDescription(), 3, 20);
        JTextArea docsArea = new JTextArea(selected.getDocumentsRequired(), 3, 20);
        JTextField timeField = new JTextField(selected.getExecutionTime());
        JTextField costField = new JTextField(selected.getCost());
        JTextArea notesArea = new JTextArea(selected.getNotes(), 3, 20);

        Object[] message = {
                "Установа:", institutionBox,
                "Назва послуги:", nameField,
                "Опис:", new JScrollPane(descriptionArea),
                "Необхідні документи:", new JScrollPane(docsArea),
                "Термін виконання:", timeField,
                "Вартість:", costField,
                "Примітки:", new JScrollPane(notesArea)
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Редагувати послугу",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Institution institution = (Institution) institutionBox.getSelectedItem();
            if (institution == null || Validator.isEmpty(nameField.getText())) {
                JOptionPane.showMessageDialog(this, "Назва послуги є обов'язковою.");
                return;
            }

            selected.setInstitutionId(institution.getId());
            selected.setName(nameField.getText().trim());
            selected.setDescription(descriptionArea.getText().trim());
            selected.setDocumentsRequired(docsArea.getText().trim());
            selected.setExecutionTime(timeField.getText().trim());
            selected.setCost(costField.getText().trim());
            selected.setNotes(notesArea.getText().trim());

            store.saveServices();
            JOptionPane.showMessageDialog(this, "Послугу оновлено.");
            mainFrame.refreshAllData();
        }
    }

    private void deleteService() {
        List<GovService> services = store.getServices();
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Список послуг порожній.");
            return;
        }

        GovService selected = (GovService) JOptionPane.showInputDialog(
                this,
                "Оберіть послугу:",
                "Видалення послуги",
                JOptionPane.WARNING_MESSAGE,
                null,
                services.toArray(),
                null
        );

        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Видалити послугу \"" + selected.getName() + "\"?",
                "Підтвердження",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            store.deleteService(selected.getId());
            JOptionPane.showMessageDialog(this, "Послугу видалено.");
            mainFrame.refreshAllData();
        }
    }
}
