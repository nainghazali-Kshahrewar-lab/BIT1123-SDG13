package climateaction;

public class EnergyActivity extends Activity {
    private String energyType;
    private double consumption;

    public EnergyActivity(int activityId, int userId, String date, String description,
                          String energyType, double consumption) {
        super(activityId, userId, date, description);
        this.energyType = energyType;
        this.consumption = consumption;
    }

    public String getEnergyType() { return energyType; }
    public void setEnergyType(String energyType) { this.energyType = energyType; }

    public double getConsumption() { return consumption; }
    public void setConsumption(double consumption) { this.consumption = consumption; }

    @Override
    public double calculateEmission() {
        return consumption * CarbonCalculator.getEnergyFactor(energyType);
    }

    @Override
    public String getActivityType() { return "Energy"; }

    @Override
    public String toFileString() {
        return String.join("|",
                "ENERGY", String.valueOf(getActivityId()), String.valueOf(getUserId()),
                getDate(), sanitize(getDescription()), sanitize(energyType), String.valueOf(consumption));
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/");
    }
}
