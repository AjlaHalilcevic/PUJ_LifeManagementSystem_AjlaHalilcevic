package lifemanagement.trackers.sleep;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SleepService {
    private final List<SleepEntry> sleeps = new ArrayList<>();

    public void add(SleepEntry s) {
        sleeps.add(s);
    }
    public List<SleepEntry> getForUser(String username) {
        List<SleepEntry> result = new ArrayList<>();
        for (SleepEntry s : sleeps) {
            if (s.getUsername().equals(username))
                result.add(s);
            }
        return result;
    }
    public SleepEntry findById(String id, String username) {
        for (SleepEntry s : sleeps) {
            if (s.getId().equals(id) && s.getUsername().equals(username)) return s;
        }
        return null;
    }
    public boolean deleteById(String id, String username) {
        SleepEntry s = findById(id, username);
        if (s == null) return false;
        return sleeps.remove(s);
    }
    public boolean update(String id, String username, LocalDate newDate, Double newHours, Boolean newGood) {
        SleepEntry s = findById(id, username);
        if (s == null) return false;
        if (newDate != null) s.setDate(newDate);
        if (newHours != null) s.setHours(newHours);
        if (newGood != null) s.setGood(newGood);

        return true;
    }
}