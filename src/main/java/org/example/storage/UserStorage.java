package org.example.storage;

import org.example.model.User;
import org.example.util.CsvUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    private final File file;

    public UserStorage(String path) {
        this.file = new File(path);
    }

    public List<User> loadAll() {
        List<User> list = new ArrayList<>();
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

                String[] parts = CsvUtils.parseLine(line, 4);

                User user = new User(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3]
                );

                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<User> list) {
        ensureParentExists();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("id;username;password;role");
            writer.newLine();

            for (User u : list) {
                writer.write(u.getId() + ";"
                        + CsvUtils.escape(u.getUsername()) + ";"
                        + CsvUtils.escape(u.getPassword()) + ";"
                        + CsvUtils.escape(u.getRole()));
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