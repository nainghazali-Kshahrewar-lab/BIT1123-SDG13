package climateaction;

public abstract class Activity {
    private int activityId;
    private int userId;
    private String date;
    private String description;

    public Activity(int activityId, int userId, String date, String description) {
        this.activityId = activityId;
        this.userId = userId;
        this.date = date;
        this.description = description;
    }

    public int getActivityId() { return activityId; }
    public void setActivityId(int activityId) { this.activityId = activityId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public abstract double calculateEmission();
    public abstract String getActivityType();
    public abstract String toFileString();

    public String getSummary() {
        return String.format("#%d | %s | %s | %.2f kg CO2 | %s",
                activityId, getActivityType(), date, calculateEmission(), description);
    }
}
