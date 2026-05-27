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

        setLayout(new BorderLayout(25, 25));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);

        JButton backButton = UiTheme.secondaryButton("← Назад");
        backButton.setPreferredSize(new Dimension(135, 42));

        backButton.addActionListener(e -> {
            mainFrame.dispose();

            MainFrame newFrame = new MainFrame();
            newFrame.setVisible(true);
        });

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);

        header.add(UiTheme.title("Адмін-панель"));
        header.add(UiTheme.subtitle("Керування установами та послугами системи"));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
        backPanel.add(backButton);        backPanel.setOpaque(false);
        backPanel.add(backButton);

        headerWrapper.add(backPanel, BorderLayout.WEST);
        headerWrapper.add(header, BorderLayout.CENTER);

        add(headerWrapper, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 25, 25));
        cards.setOpaque(false);

        cards.add(actionCard("Додати установу", "Створення нового запису установи", this::addInstitution));
        cards.add(actionCard("Редагувати установу", "Оновлення інформації про установу", this::editInstitution));
        cards.add(actionCard("Видалити установу", "Видалення установи та її послуг", this::deleteInstitution));
        cards.add(actionCard("Додати послугу", "Створення нової державної послуги", this::addService));
        cards.add(actionCard("Редагувати послугу", "Оновлення даних про послугу", this::editService));
        cards.add(actionCard("Видалити послугу", "Видалення послуги із системи", this::deleteService));

        add(cards, BorderLayout.CENTER);
    }

    private JPanel actionCard(String title, String description, Runnable action) {
        JPanel card = UiTheme.card();
        card.setLayout(new BorderLayout(10, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(UiTheme.TEXT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setForeground(UiTheme.MUTED);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel arrow = new JLabel("→");
        arrow.setForeground(UiTheme.PRIMARY);
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 32));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(arrow, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(UiTheme.CARD_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(UiTheme.CARD);
            }
        });

        return card;
    }

    private void addInstitution() {
        JTextField nameField = UiTheme.textField();
        JTextField categoryField = UiTheme.textField();
        JTextField addressField = UiTheme.textField();
        JTextField phoneField = UiTheme.textField();
        JTextField emailField = UiTheme.textField();
        JTextField websiteField = UiTheme.textField();
        JTextField hoursField = UiTheme.textField();
        JTextArea descriptionArea = UiTheme.textArea(4);

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

        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                "Додати установу",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

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

        if (selected == null) {
            return;
        }

        JTextField nameField = UiTheme.textField();
        JTextField categoryField = UiTheme.textField();
        JTextField addressField = UiTheme.textField();
        JTextField phoneField = UiTheme.textField();
        JTextField emailField = UiTheme.textField();
        JTextField websiteField = UiTheme.textField();
        JTextField hoursField = UiTheme.textField();
        JTextArea descriptionArea = UiTheme.textArea(4);

        nameField.setText(selected.getName());
        categoryField.setText(selected.getCategory());
        addressField.setText(selected.getAddress());
        phoneField.setText(selected.getPhone());
        emailField.setText(selected.getEmail());
        websiteField.setText(selected.getWebsite());
        hoursField.setText(selected.getWorkingHours());
        descriptionArea.setText(selected.getDescription());

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

        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                "Редагувати установу",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

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

        if (selected == null) {
            return;
        }

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
        JTextField nameField = UiTheme.textField();
        JTextArea descriptionArea = UiTheme.textArea(3);
        JTextArea docsArea = UiTheme.textArea(3);
        JTextField timeField = UiTheme.textField();
        JTextField costField = UiTheme.textField();
        JTextArea notesArea = UiTheme.textArea(3);

        Object[] message = {
                "Установа:", institutionBox,
                "Назва послуги:", nameField,
                "Опис:", new JScrollPane(descriptionArea),
                "Необхідні документи:", new JScrollPane(docsArea),
                "Термін виконання:", timeField,
                "Вартість:", costField,
                "Примітки:", new JScrollPane(notesArea)
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                "Додати послугу",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

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

        if (selected == null) {
            return;
        }

        List<Institution> institutions = store.getInstitutions();

        JComboBox<Institution> institutionBox = new JComboBox<>(institutions.toArray(new Institution[0]));

        for (Institution i : institutions) {
            if (i.getId() == selected.getInstitutionId()) {
                institutionBox.setSelectedItem(i);
                break;
            }
        }

        JTextField nameField = UiTheme.textField();
        JTextArea descriptionArea = UiTheme.textArea(3);
        JTextArea docsArea = UiTheme.textArea(3);
        JTextField timeField = UiTheme.textField();
        JTextField costField = UiTheme.textField();
        JTextArea notesArea = UiTheme.textArea(3);

        nameField.setText(selected.getName());
        descriptionArea.setText(selected.getDescription());
        docsArea.setText(selected.getDocumentsRequired());
        timeField.setText(selected.getExecutionTime());
        costField.setText(selected.getCost());
        notesArea.setText(selected.getNotes());

        Object[] message = {
                "Установа:", institutionBox,
                "Назва послуги:", nameField,
                "Опис:", new JScrollPane(descriptionArea),
                "Необхідні документи:", new JScrollPane(docsArea),
                "Термін виконання:", timeField,
                "Вартість:", costField,
                "Примітки:", new JScrollPane(notesArea)
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                "Редагувати послугу",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

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

        if (selected == null) {
            return;
        }

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