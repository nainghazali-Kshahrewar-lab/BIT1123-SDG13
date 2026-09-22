package climateaction;

import java.util.HashMap;
import java.util.Map;

public class CarbonCalculator {
    private static final Map<String, Double> TRANSPORT_FACTORS = new HashMap<>();
    private static final Map<String, Double> ENERGY_FACTORS = new HashMap<>();
    private static final Map<String, Double> WASTE_FACTORS = new HashMap<>();

    static {
        // Illustrative academic-project emission factors, not direct measurements.
        TRANSPORT_FACTORS.put("Car", 0.21);
        TRANSPORT_FACTORS.put("Motorcycle", 0.10);
        TRANSPORT_FACTORS.put("Bus", 0.08);
        TRANSPORT_FACTORS.put("Train", 0.04);
        TRANSPORT_FACTORS.put("Bicycle", 0.00);
        TRANSPORT_FACTORS.put("Walking", 0.00);

        ENERGY_FACTORS.put("Electricity", 0.50);
        ENERGY_FACTORS.put("Natural Gas", 0.20);
        ENERGY_FACTORS.put("LPG", 3.00);

        WASTE_FACTORS.put("Plastic", 2.50);
        WASTE_FACTORS.put("Paper", 1.00);
        WASTE_FACTORS.put("Food", 1.50);
        WASTE_FACTORS.put("Glass", 0.80);
        WASTE_FACTORS.put("General", 1.20);
    }

    public static double getTransportFactor(String type) {
        return TRANSPORT_FACTORS.getOrDefault(type, 0.10);
    }

    public static double getEnergyFactor(String type) {
        return ENERGY_FACTORS.getOrDefault(type, 0.50);
    }

    public static double getWasteFactor(String type) {
        return WASTE_FACTORS.getOrDefault(type, 1.20);
    }

    private CarbonCalculator() {
        // Utility class.
    }
}
