package climateaction;

public class GreenAction {
    private int actionId;
    private int userId;
    private String date;
    private String actionName;
    private String description;
    private String impact;

    public GreenAction(int actionId, int userId, String date, String actionName,
                       String description, String impact) {
        this.actionId = actionId;
        this.userId = userId;
        this.date = date;
        this.actionName = actionName;
        this.description = description;
        this.impact = impact;
    }

    public int getActionId() { return actionId; }
    public void setActionId(int actionId) { this.actionId = actionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }

    public String toFileString() {
        return String.join("|",
                String.valueOf(actionId), String.valueOf(userId), date,
                sanitize(actionName), sanitize(description), sanitize(impact));
    }

    public String getSummary() {
        return String.format("#%d | %s | %s | Impact: %s", actionId, date, actionName, impact);
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/");
    }
}
