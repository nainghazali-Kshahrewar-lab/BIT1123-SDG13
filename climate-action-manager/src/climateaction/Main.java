package climateaction;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main extends JFrame {
    private final FileManager fileManager = new FileManager();
    private final RecommendationService recommendationService = new RecommendationService();
    private final ReportService reportService = new ReportService();

    private final JComboBox<String> userCombo = new JComboBox<>();
    private final JLabel totalEmissionLabel = new JLabel("Total CO2: 0.00 kg");

    private final DefaultListModel<String> activityListModel = new DefaultListModel<>();
    private final JList<String> activityList = new JList<>(activityListModel);

    private final DefaultListModel<String> greenListModel = new DefaultListModel<>();
    private final JList<String> greenList = new JList<>(greenListModel);

    private final JTextField userIdField = new JTextField();
    private final JTextField userNameField = new JTextField();

    private final JComboBox<String> activityTypeCombo = new JComboBox<>(new String[]{"Transport", "Energy", "Waste"});
    private final CardLayout activityCardLayout = new CardLayout();
    private final JPanel activityDynamicPanel = new JPanel(activityCardLayout);
    private final JComboBox<String> transportTypeCombo = new JComboBox<>(new String[]{"Car", "Motorcycle", "Bus", "Train", "Bicycle", "Walking"});
    private final JSpinner transportDistanceSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 100000.0, 0.5));
    private final JComboBox<String> energyTypeCombo = new JComboBox<>(new String[]{"Electricity", "Natural Gas", "LPG"});
    private final JSpinner energyConsumptionSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 100000.0, 0.5));
    private final JComboBox<String> wasteTypeCombo = new JComboBox<>(new String[]{"Plastic", "Paper", "Food", "Glass", "General"});
    private final JSpinner wasteWeightSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 100000.0, 0.5));
    private final JTextField activityDescriptionField = new JTextField();

    private final JTextField greenActionNameField = new JTextField();
    private final JTextField greenDescriptionField = new JTextField();
    private final JComboBox<String> greenImpactCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});

    private final JTextArea reportArea = new JTextArea();
    private final JTextArea recommendationArea = new JTextArea();

    public Main() {
        setTitle("Climate Action Manager - SDG 13");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 620));

        buildUi();
        loadDataSilently();
        refreshAll();
    }

    private void buildUi() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel title = new JLabel("Climate Action Manager");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(title, BorderLayout.WEST);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.add(new JLabel("User:"));
        userCombo.setPreferredSize(new Dimension(200, 28));
        userCombo.addActionListener(e -> refreshUserViews());
        headerRight.add(userCombo);
        headerRight.add(totalEmissionLabel);
        header.add(headerRight, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", buildDashboardTab());
        tabs.addTab("Users", buildUserTab());
        tabs.addTab("Activities", buildActivityTab());
        tabs.addTab("Green Actions", buildGreenActionTab());
        tabs.addTab("Report", buildReportTab());
        tabs.addTab("Recommendations", buildRecommendationTab());

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel intro = new JLabel("SDG 13 - Climate Action", SwingConstants.CENTER);
        intro.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(intro, BorderLayout.NORTH);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setFont(new Font("SansSerif", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setText(
                "This application helps users record daily transport, energy and waste activities, " +
                "estimate carbon emissions, store data, record green actions and generate recommendations.\n\n" +
                "OOP concepts demonstrated:\n" +
                "• Encapsulation\n" +
                "• Inheritance\n" +
                "• Runtime polymorphism\n" +
                "• Abstraction\n" +
                "• Collections\n" +
                "• File handling\n\n" +
                "Use the tabs above to manage your climate-related data.");
        info.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(info, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = new JButton("Save All Data");
        saveButton.addActionListener(e -> saveData());
        JButton reloadButton = new JButton("Reload From Files");
        reloadButton.addActionListener(e -> reloadData());
        bottom.add(saveButton);
        bottom.add(reloadButton);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildUserTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Register User"));
        form.add(new JLabel("User ID:"));
        userIdField.setText(String.valueOf(DataStore.nextUserId()));
        form.add(userIdField);
        form.add(new JLabel("Name:"));
        form.add(userNameField);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> addUser());
        form.add(new JLabel());
        form.add(addUserButton);

        panel.add(form, BorderLayout.NORTH);

        DefaultListModel<String> usersModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(usersModel);
        JScrollPane scroll = new JScrollPane(userList);
        scroll.setBorder(BorderFactory.createTitledBorder("Registered Users"));
        panel.add(scroll, BorderLayout.CENTER);

        panel.add(new JLabel("Tip: Select a user from the top-right dropdown before adding activities."), BorderLayout.SOUTH);

        panel.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override public void ancestorAdded(javax.swing.event.AncestorEvent event) { refreshUsersModel(usersModel); }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent event) { }
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent event) { }
        });

        // Keep list refreshed whenever user data changes.
        JButton refresh = new JButton("Refresh User List");
        refresh.addActionListener(e -> refreshUsersModel(usersModel));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildActivityTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Activity Type:"));
        activityTypeCombo.addActionListener(e -> switchActivityCard());
        top.add(activityTypeCombo);
        top.add(new JLabel("Date:"));
        JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
        top.add(dateField);
        panel.add(top, BorderLayout.NORTH);

        activityDynamicPanel.add(buildTransportPanel(), "Transport");
        activityDynamicPanel.add(buildEnergyPanel(), "Energy");
        activityDynamicPanel.add(buildWastePanel(), "Waste");
        panel.add(activityDynamicPanel, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(10, 10));
        JPanel desc = new JPanel(new BorderLayout(5, 5));
        desc.add(new JLabel("Description:"), BorderLayout.WEST);
        desc.add(activityDescriptionField, BorderLayout.CENTER);
        south.add(desc, BorderLayout.NORTH);

        JButton addActivityButton = new JButton("Add Activity");
        addActivityButton.addActionListener(e -> addActivity(dateField.getText()));
        south.add(addActivityButton, BorderLayout.WEST);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Selected User Activities"));
        listPanel.add(new JScrollPane(activityList), BorderLayout.CENTER);
        south.add(listPanel, BorderLayout.CENTER);

        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildTransportPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Transport Details"));
        p.add(new JLabel("Transport type:"));
        p.add(transportTypeCombo);
        p.add(new JLabel("Distance (km):"));
        p.add(transportDistanceSpinner);
        return p;
    }

    private JPanel buildEnergyPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Energy Details"));
        p.add(new JLabel("Energy type:"));
        p.add(energyTypeCombo);
        p.add(new JLabel("Consumption (unit):"));
        p.add(energyConsumptionSpinner);
        return p;
    }

    private JPanel buildWastePanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Waste Details"));
        p.add(new JLabel("Waste type:"));
        p.add(wasteTypeCombo);
        p.add(new JLabel("Weight (kg):"));
        p.add(wasteWeightSpinner);
        return p;
    }

    private JPanel buildGreenActionTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Record Green Action"));
        form.add(new JLabel("Action name:"));
        form.add(greenActionNameField);
        form.add(new JLabel("Description:"));
        form.add(greenDescriptionField);
        form.add(new JLabel("Impact:"));
        form.add(greenImpactCombo);
        JButton add = new JButton("Add Green Action");
        add.addActionListener(e -> addGreenAction());
        form.add(new JLabel());
        form.add(add);
        panel.add(form, BorderLayout.NORTH);

        panel.add(new JScrollPane(greenList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildReportTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setLineWrap(false);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton generate = new JButton("Generate Report");
        generate.addActionListener(e -> generateReport());
        panel.add(generate, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRecommendationTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        recommendationArea.setEditable(false);
        recommendationArea.setLineWrap(true);
        recommendationArea.setWrapStyleWord(true);
        recommendationArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(new JScrollPane(recommendationArea), BorderLayout.CENTER);

        JButton generate = new JButton("Generate Recommendations");
        generate.addActionListener(e -> generateRecommendations());
        panel.add(generate, BorderLayout.SOUTH);
        return panel;
    }

    private void switchActivityCard() {
        activityCardLayout.show(activityDynamicPanel, (String) activityTypeCombo.getSelectedItem());
    }

    private void addUser() {
        String idText = userIdField.getText().trim();
        String name = userNameField.getText().trim();

        if (name.isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }
        if (!name.matches("[A-Za-z .'-]+")) {
            showError("Name can contain letters, spaces, apostrophes, periods and hyphens only.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            if (id <= 0) throw new NumberFormatException();
            if (DataStore.findUserById(id) != null) {
                showError("User ID already exists.");
                return;
            }

            DataStore.addUser(new User(id, name));
            userNameField.setText("");
            userIdField.setText(String.valueOf(DataStore.nextUserId()));
            refreshAll();
            JOptionPane.showMessageDialog(this, "User added successfully.");
        } catch (NumberFormatException ex) {
            showError("User ID must be a positive integer.");
        }
    }

    private void addActivity(String date) {
        User user = getSelectedUser();
        if (user == null) {
            showError("Please register and select a user first.");
            return;
        }
        if (date == null || date.isBlank()) {
            showError("Date cannot be empty.");
            return;
        }

        String type = (String) activityTypeCombo.getSelectedItem();
        String description = activityDescriptionField.getText().trim();
        if (description.isEmpty()) description = "Daily activity";

        int activityId = DataStore.nextActivityId();
        Activity activity;

        if ("Transport".equals(type)) {
            double distance = ((Number) transportDistanceSpinner.getValue()).doubleValue();
            if (distance <= 0) {
                showError("Distance must be greater than 0.");
                return;
            }
            activity = new TransportActivity(activityId, user.getUserId(), date, description,
                    (String) transportTypeCombo.getSelectedItem(), distance);
        } else if ("Energy".equals(type)) {
            double consumption = ((Number) energyConsumptionSpinner.getValue()).doubleValue();
            if (consumption <= 0) {
                showError("Energy consumption must be greater than 0.");
                return;
            }
            activity = new EnergyActivity(activityId, user.getUserId(), date, description,
                    (String) energyTypeCombo.getSelectedItem(), consumption);
        } else {
            double weight = ((Number) wasteWeightSpinner.getValue()).doubleValue();
            if (weight <= 0) {
                showError("Waste weight must be greater than 0.");
                return;
            }
            activity = new WasteActivity(activityId, user.getUserId(), date, description,
                    (String) wasteTypeCombo.getSelectedItem(), weight);
        }

        DataStore.addActivity(activity);
        activityDescriptionField.setText("");
        refreshAll();
        JOptionPane.showMessageDialog(this,
                String.format("Activity added. Estimated emission: %.2f kg CO2", activity.calculateEmission()));
    }

    private void addGreenAction() {
        User user = getSelectedUser();
        if (user == null) {
            showError("Please register and select a user first.");
            return;
        }

        String name = greenActionNameField.getText().trim();
        String description = greenDescriptionField.getText().trim();
        if (name.isEmpty()) {
            showError("Action name cannot be empty.");
            return;
        }
        if (description.isEmpty()) description = "Green action completed by user.";

        GreenAction action = new GreenAction(
                DataStore.nextActionId(),
                user.getUserId(),
                LocalDate.now().toString(),
                name,
                description,
                (String) greenImpactCombo.getSelectedItem());

        DataStore.addGreenAction(action);
        greenActionNameField.setText("");
        greenDescriptionField.setText("");
        refreshAll();
        JOptionPane.showMessageDialog(this, "Green action recorded successfully.");
    }

    private void refreshAll() {
        refreshUserCombo();
        refreshUserViews();
    }

    private void refreshUserCombo() {
        Integer selectedId = getSelectedUserId();
        userCombo.removeAllItems();
        for (User user : DataStore.getUsers()) {
            userCombo.addItem(user.getUserId() + " - " + user.getName());
        }
        if (selectedId != null) {
            for (int i = 0; i < userCombo.getItemCount(); i++) {
                String item = userCombo.getItemAt(i);
                if (item.startsWith(selectedId + " -")) {
                    userCombo.setSelectedIndex(i);
                    return;
                }
            }
        }
        if (userCombo.getItemCount() > 0) userCombo.setSelectedIndex(0);
    }

    private Integer getSelectedUserId() {
        String value = (String) userCombo.getSelectedItem();
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value.split(" - ")[0]);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private User getSelectedUser() {
        Integer id = getSelectedUserId();
        return id == null ? null : DataStore.findUserById(id);
    }

    private void refreshUserViews() {
        Integer userId = getSelectedUserId();
        activityListModel.clear();
        greenListModel.clear();
        if (userId == null) {
            totalEmissionLabel.setText("Total CO2: 0.00 kg");
            reportArea.setText("");
            recommendationArea.setText("");
            return;
        }

        List<Activity> activities = DataStore.getActivitiesForUser(userId);
        for (Activity activity : activities) activityListModel.addElement(activity.getSummary());

        List<GreenAction> actions = DataStore.getGreenActionsForUser(userId);
        for (GreenAction action : actions) greenListModel.addElement(action.getSummary());

        totalEmissionLabel.setText(String.format("Total CO2: %.2f kg", reportService.getTotalEmission(userId)));
        reportArea.setText(reportService.generateReport(userId));
        showRecommendationsInArea(userId);
    }

    private void refreshUsersModel(DefaultListModel<String> model) {
        model.clear();
        for (User user : DataStore.getUsers()) model.addElement(user.toString());
    }

    private void generateReport() {
        User user = getSelectedUser();
        if (user == null) {
            showError("Please select a user.");
            return;
        }
        reportArea.setText(reportService.generateReport(user.getUserId()));
        reportArea.setCaretPosition(0);
    }

    private void generateRecommendations() {
        User user = getSelectedUser();
        if (user == null) {
            showError("Please select a user.");
            return;
        }
        showRecommendationsInArea(user.getUserId());
    }

    private void showRecommendationsInArea(int userId) {
        List<String> recommendations = recommendationService.generateRecommendations(userId);
        StringBuilder sb = new StringBuilder("CLIMATE ACTION RECOMMENDATIONS\n\n");
        int i = 1;
        for (String recommendation : recommendations) {
            sb.append(i++).append(". ").append(recommendation).append("\n\n");
        }
        recommendationArea.setText(sb.toString());
        recommendationArea.setCaretPosition(0);
    }

    private void saveData() {
        try {
            fileManager.saveAll();
            JOptionPane.showMessageDialog(this,
                    "All data saved successfully.\nLocation: " + fileManager.getDataDirectoryPath());
        } catch (IOException ex) {
            showError("Unable to save data: " + ex.getMessage());
        }
    }

    private void loadDataSilently() {
        try {
            fileManager.loadAll();
        } catch (IOException ex) {
            // First run may have no files. The application can still continue.
        }
    }

    private void reloadData() {
        try {
            fileManager.loadAll();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Data reloaded from files successfully.");
        } catch (IOException ex) {
            showError("Unable to load data: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
