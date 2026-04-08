package org.example.storage;

import org.example.model.GovService;
import org.example.util.CsvUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServiceStorage {
    private final File file;

    public ServiceStorage(String path) {
        this.file = new File(path);
    }

    public List<GovService> loadAll() {
        List<GovService> list = new ArrayList<>();
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

                String[] parts = CsvUtils.parseLine(line, 8);

                GovService service = new GovService(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6],
                        parts[7]
                );

                list.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<GovService> list) {
        ensureParentExists();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("id;institutionId;name;description;documentsRequired;executionTime;cost;notes");
            writer.newLine();

            for (GovService s : list) {
                writer.write(s.getId() + ";"
                        + s.getInstitutionId() + ";"
                        + CsvUtils.escape(s.getName()) + ";"
                        + CsvUtils.escape(s.getDescription()) + ";"
                        + CsvUtils.escape(s.getDocumentsRequired()) + ";"
                        + CsvUtils.escape(s.getExecutionTime()) + ";"
                        + CsvUtils.escape(s.getCost()) + ";"
                        + CsvUtils.escape(s.getNotes()));
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
