package lifemanagement.trackers.habit;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class HabitService {
    private final List<HabitEntry> habits = new ArrayList<>();

    public void add(HabitEntry h) {
        habits.add(h);
    }
    public List<HabitEntry> getForUser(String username) {
        List<HabitEntry> result = new ArrayList<>();
        for (HabitEntry h : habits) {
            if (h.getUsername().equals(username)) result.add(h);
        }
        return result;
    }
    public HabitEntry findById(String id, String username) {
    for (HabitEntry h : habits) {
        if (h.getId().equals(id) && h.getUsername().equals(username)) return h;
    }
    return null;
    }
    public boolean update(String id, String username, String newName, String newFrequency, Boolean newActive) {
        HabitEntry h = findById(id, username);
        if (h == null) return false;

        if (newName != null) h.setName(newName);
        if (newFrequency != null) h.setFrequency(newFrequency);
        if (newActive != null) h.setActive(newActive);

        return true;
    }

    public boolean deleteById(String id, String username) {
        HabitEntry h = findById(id, username);
        if (h == null) return false;
        return habits.remove(h);
    }
}