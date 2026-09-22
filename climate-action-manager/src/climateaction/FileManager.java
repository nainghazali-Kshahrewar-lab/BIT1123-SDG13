package climateaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private final File dataDirectory;
    private final File usersFile;
    private final File activitiesFile;
    private final File greenActionsFile;

    public FileManager() {
        File firstChoice = new File("data");
        File secondChoice = new File("../data");
        if (firstChoice.exists() || firstChoice.mkdirs()) {
            dataDirectory = firstChoice;
        } else {
            dataDirectory = secondChoice;
            dataDirectory.mkdirs();
        }

        usersFile = new File(dataDirectory, "users.txt");
        activitiesFile = new File(dataDirectory, "activities.txt");
        greenActionsFile = new File(dataDirectory, "green_actions.txt");
    }

    public String getDataDirectoryPath() {
        return dataDirectory.getAbsolutePath();
    }

    public void saveAll() throws IOException {
        saveUsers();
        saveActivities();
        saveGreenActions();
    }

    private void saveUsers() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            for (User user : DataStore.getUsers()) {
                writer.write(user.getUserId() + "|" + sanitize(user.getName()));
                writer.newLine();
            }
        }
    }

    private void saveActivities() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(activitiesFile))) {
            for (Activity activity : DataStore.getActivities()) {
                writer.write(activity.toFileString());
                writer.newLine();
            }
        }
    }

    private void saveGreenActions() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(greenActionsFile))) {
            for (GreenAction action : DataStore.getGreenActions()) {
                writer.write(action.toFileString());
                writer.newLine();
            }
        }
    }

    public void loadAll() throws IOException {
        DataStore.clearAll();
        loadUsers();
        loadActivities();
        loadGreenActions();
    }

    private void loadUsers() throws IOException {
        if (!usersFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 2) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        DataStore.addUser(new User(id, parts[1]));
                    } catch (NumberFormatException ignored) {
                        // Skip malformed record.
                    }
                }
            }
        }
    }

    private void loadActivities() throws IOException {
        if (!activitiesFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(activitiesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                try {
                    if (p.length < 7) continue;
                    String type = p[0];
                    int activityId = Integer.parseInt(p[1]);
                    int userId = Integer.parseInt(p[2]);
                    String date = p[3];
                    String description = p[4];
                    double value = Double.parseDouble(p[6]);

                    switch (type) {
                        case "TRANSPORT" -> DataStore.addActivity(
                                new TransportActivity(activityId, userId, date, description, p[5], value));
                        case "ENERGY" -> DataStore.addActivity(
                                new EnergyActivity(activityId, userId, date, description, p[5], value));
                        case "WASTE" -> DataStore.addActivity(
                                new WasteActivity(activityId, userId, date, description, p[5], value));
                        default -> { }
                    }
                } catch (NumberFormatException ignored) {
                    // Skip malformed record.
                }
            }
        }
    }

    private void loadGreenActions() throws IOException {
        if (!greenActionsFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(greenActionsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length >= 6) {
                    try {
                        int actionId = Integer.parseInt(p[0]);
                        int userId = Integer.parseInt(p[1]);
                        DataStore.addGreenAction(new GreenAction(actionId, userId, p[2], p[3], p[4], p[5]));
                    } catch (NumberFormatException ignored) {
                        // Skip malformed record.
                    }
                }
            }
        }
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/").replace("\n", " ");
    }
}
