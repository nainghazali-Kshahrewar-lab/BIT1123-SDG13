package climateaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataStore {
    private static final List<User> users = new ArrayList<>();
    private static final List<Activity> activities = new ArrayList<>();
    private static final List<GreenAction> greenActions = new ArrayList<>();

    private DataStore() {
        // Utility class.
    }

    public static List<User> getUsers() { return users; }
    public static List<Activity> getActivities() { return activities; }
    public static List<GreenAction> getGreenActions() { return greenActions; }

    public static void addUser(User user) { users.add(user); }
    public static void addActivity(Activity activity) { activities.add(activity); }
    public static void addGreenAction(GreenAction action) { greenActions.add(action); }

    public static int nextUserId() {
        return users.stream().mapToInt(User::getUserId).max().orElse(1000) + 1;
    }

    public static int nextActivityId() {
        return activities.stream().mapToInt(Activity::getActivityId).max().orElse(0) + 1;
    }

    public static int nextActionId() {
        return greenActions.stream().mapToInt(GreenAction::getActionId).max().orElse(0) + 1;
    }

    public static User findUserById(int userId) {
        return users.stream().filter(u -> u.getUserId() == userId).findFirst().orElse(null);
    }

    public static List<Activity> getActivitiesForUser(int userId) {
        List<Activity> result = new ArrayList<>();
        for (Activity a : activities) {
            if (a.getUserId() == userId) result.add(a);
        }
        result.sort(Comparator.comparing(Activity::getDate).reversed());
        return result;
    }

    public static List<GreenAction> getGreenActionsForUser(int userId) {
        List<GreenAction> result = new ArrayList<>();
        for (GreenAction a : greenActions) {
            if (a.getUserId() == userId) result.add(a);
        }
        result.sort(Comparator.comparing(GreenAction::getDate).reversed());
        return result;
    }

    public static void clearAll() {
        users.clear();
        activities.clear();
        greenActions.clear();
    }
}
