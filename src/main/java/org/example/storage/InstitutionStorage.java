package org.example.storage;

import org.example.model.Institution;
import org.example.util.CsvUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InstitutionStorage {
    private final File file;

    public InstitutionStorage(String path) {
        this.file = new File(path);
    }

    public List<Institution> loadAll() {
        List<Institution> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = CsvUtils.parseLine(line, 9);

                Institution institution = new Institution(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6],
                        parts[7],
                        parts[8]
                );

                list.add(institution);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<Institution> list) {
        ensureParentExists();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("id;name;category;address;phone;email;website;workingHours;description");
            writer.newLine();

            for (Institution i : list) {
                writer.write(i.getId() + ";"
                        + CsvUtils.escape(i.getName()) + ";"
                        + CsvUtils.escape(i.getCategory()) + ";"
                        + CsvUtils.escape(i.getAddress()) + ";"
                        + CsvUtils.escape(i.getPhone()) + ";"
                        + CsvUtils.escape(i.getEmail()) + ";"
                        + CsvUtils.escape(i.getWebsite()) + ";"
                        + CsvUtils.escape(i.getWorkingHours()) + ";"
                        + CsvUtils.escape(i.getDescription()));
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureParentExists() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}