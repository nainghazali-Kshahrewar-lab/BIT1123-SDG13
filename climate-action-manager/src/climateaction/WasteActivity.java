package climateaction;

public class WasteActivity extends Activity {
    private String wasteType;
    private double weight;

    public WasteActivity(int activityId, int userId, String date, String description,
                         String wasteType, double weight) {
        super(activityId, userId, date, description);
        this.wasteType = wasteType;
        this.weight = weight;
    }

    public String getWasteType() { return wasteType; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public double calculateEmission() {
        return weight * CarbonCalculator.getWasteFactor(wasteType);
    }

    @Override
    public String getActivityType() { return "Waste"; }

    @Override
    public String toFileString() {
        return String.join("|",
                "WASTE", String.valueOf(getActivityId()), String.valueOf(getUserId()),
                getDate(), sanitize(getDescription()), sanitize(wasteType), String.valueOf(weight));
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/");
    }
}
