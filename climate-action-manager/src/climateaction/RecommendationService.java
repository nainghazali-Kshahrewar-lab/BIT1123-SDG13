package climateaction;

import java.util.ArrayList;
import java.util.List;

public class RecommendationService {

    public List<String> generateRecommendations(int userId) {
        List<String> recommendations = new ArrayList<>();
        double transport = 0;
        double energy = 0;
        double waste = 0;

        for (Activity activity : DataStore.getActivitiesForUser(userId)) {
            double emission = activity.calculateEmission();
            if (activity instanceof TransportActivity) {
                transport += emission;
            } else if (activity instanceof EnergyActivity) {
                energy += emission;
            } else if (activity instanceof WasteActivity) {
                waste += emission;
            }
        }

        double total = transport + energy + waste;

        if (total == 0) {
            recommendations.add("Start recording daily activities to receive personalized recommendations.");
            return recommendations;
        }

        if (transport >= energy && transport >= waste && transport > 0) {
            recommendations.add("Transportation is your largest recorded emission source. Consider public transport, walking, cycling or carpooling.");
        }

        if (energy >= transport && energy >= waste && energy > 0) {
            recommendations.add("Energy use is your largest recorded emission source. Consider reducing unnecessary electricity use and switching off unused devices.");
        }

        if (waste >= transport && waste >= energy && waste > 0) {
            recommendations.add("Waste is your largest recorded emission source. Consider reducing single-use items, reusing products and separating recyclable materials.");
        }

        if (transport > 5) {
            recommendations.add("Try combining errands into one trip or choosing lower-emission transport for short journeys.");
        }

        if (energy > 5) {
            recommendations.add("Use energy-efficient appliances where practical and avoid leaving devices on standby unnecessarily.");
        }

        if (waste > 3) {
            recommendations.add("Plan purchases carefully and reduce avoidable food and packaging waste.");
        }

        recommendations.add("Keep recording green actions so you can monitor positive environmental habits over time.");
        return recommendations;
    }
}
