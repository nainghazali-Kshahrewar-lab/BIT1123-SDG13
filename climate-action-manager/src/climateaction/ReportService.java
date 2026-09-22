package climateaction;

import java.util.List;

public class ReportService {

    public double getTotalEmission(int userId) {
        double total = 0;
        for (Activity activity : DataStore.getActivitiesForUser(userId)) {
            total += activity.calculateEmission();
        }
        return total;
    }

    public double getTransportEmission(int userId) {
        return sumByType(userId, TransportActivity.class);
    }

    public double getEnergyEmission(int userId) {
        return sumByType(userId, EnergyActivity.class);
    }

    public double getWasteEmission(int userId) {
        return sumByType(userId, WasteActivity.class);
    }

    private double sumByType(int userId, Class<?> type) {
        double total = 0;
        for (Activity activity : DataStore.getActivitiesForUser(userId)) {
            if (type.isInstance(activity)) total += activity.calculateEmission();
        }
        return total;
    }

    public String generateReport(int userId) {
        User user = DataStore.findUserById(userId);
        if (user == null) return "User not found.";

        List<Activity> activities = DataStore.getActivitiesForUser(userId);
        List<GreenAction> actions = DataStore.getGreenActionsForUser(userId);

        StringBuilder report = new StringBuilder();
        report.append("===============================================\n");
        report.append("           CLIMATE ACTION REPORT\n");
        report.append("===============================================\n");
        report.append("User ID: ").append(user.getUserId()).append("\n");
        report.append("Name   : ").append(user.getName()).append("\n\n");
        report.append("ACTIVITY SUMMARY\n");
        report.append("-----------------------------------------------\n");
        report.append("Total activities : ").append(activities.size()).append("\n");
        report.append(String.format("Transport        : %.2f kg CO2%n", getTransportEmission(userId)));
        report.append(String.format("Energy           : %.2f kg CO2%n", getEnergyEmission(userId)));
        report.append(String.format("Waste            : %.2f kg CO2%n", getWasteEmission(userId)));
        report.append(String.format("TOTAL            : %.2f kg CO2%n", getTotalEmission(userId)));
        report.append("\nGREEN ACTIONS\n");
        report.append("-----------------------------------------------\n");
        report.append("Completed actions: ").append(actions.size()).append("\n");

        if (!actions.isEmpty()) {
            for (GreenAction action : actions) {
                report.append("- ").append(action.getActionName())
                        .append(" (Impact: ").append(action.getImpact()).append(")\n");
            }
        }

        report.append("\n===============================================\n");
        report.append("Note: CO2 values are estimates based on the\n");
        report.append("illustrative emission factors used in this\n");
        report.append("academic project.\n");
        report.append("===============================================\n");
        return report.toString();
    }
}
