package climateaction;

public class TransportActivity extends Activity {
    private String transportType;
    private double distance;

    public TransportActivity(int activityId, int userId, String date, String description,
                             String transportType, double distance) {
        super(activityId, userId, date, description);
        this.transportType = transportType;
        this.distance = distance;
    }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    @Override
    public double calculateEmission() {
        return distance * CarbonCalculator.getTransportFactor(transportType);
    }

    @Override
    public String getActivityType() { return "Transport"; }

    @Override
    public String toFileString() {
        return String.join("|",
                "TRANSPORT", String.valueOf(getActivityId()), String.valueOf(getUserId()),
                getDate(), sanitize(getDescription()), sanitize(transportType), String.valueOf(distance));
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/");
    }
}
