package org.example.util;

import org.example.model.GovService;
import org.example.model.Institution;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ReportGenerator {
    private ReportGenerator() {
    }

    public static void generateReport(String filePath, List<Institution> institutions,
                                      List<GovService> services) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ЗВІТ\n");
            writer.write("Інформаційна система \"Довідник державних установ та послуг м. Ужгород\"\n");
            writer.write("Дата формування: " + LocalDateTime.now() + "\n");
            writer.write("============================================================\n\n");

            writer.write("1. УСТАНОВИ\n");
            writer.write("------------------------------------------------------------\n");
            for (Institution i : institutions) {
                writer.write("ID: " + i.getId() + "\n");
                writer.write("Назва: " + i.getName() + "\n");
                writer.write("Категорія: " + i.getCategory() + "\n");
                writer.write("Адреса: " + i.getAddress() + "\n");
                writer.write("Телефон: " + i.getPhone() + "\n");
                writer.write("Email: " + i.getEmail() + "\n");
                writer.write("Сайт: " + i.getWebsite() + "\n");
                writer.write("Графік роботи: " + i.getWorkingHours() + "\n");
                writer.write("Опис: " + i.getDescription() + "\n");
                writer.write("------------------------------------------------------------\n");
            }

            writer.write("\n2. ПОСЛУГИ\n");
            writer.write("------------------------------------------------------------\n");
            for (GovService s : services) {
                writer.write("ID: " + s.getId() + "\n");
                writer.write("ID установи: " + s.getInstitutionId() + "\n");
                writer.write("Назва: " + s.getName() + "\n");
                writer.write("Опис: " + s.getDescription() + "\n");
                writer.write("Документи: " + s.getDocumentsRequired() + "\n");
                writer.write("Термін виконання: " + s.getExecutionTime() + "\n");
                writer.write("Вартість: " + s.getCost() + "\n");
                writer.write("Примітки: " + s.getNotes() + "\n");
                writer.write("------------------------------------------------------------\n");
            }

            writer.write("\n3. СТАТИСТИКА\n");
            writer.write("------------------------------------------------------------\n");
            writer.write("Кількість установ: " + institutions.size() + "\n");
            writer.write("Кількість послуг: " + services.size() + "\n");
        }
    }
}