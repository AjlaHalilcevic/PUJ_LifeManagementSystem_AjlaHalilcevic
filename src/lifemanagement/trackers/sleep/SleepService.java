package lifemanagement.trackers.sleep;

import java.util.ArrayList;
import java.util.List;

public class SleepService {
    private final List<SleepEntry> entries = new ArrayList<>();

    public void add(SleepEntry e) {
        entries.add(e);
    }
    public List<SleepEntry> getEntriesForUser(String username) {
        List<SleepEntry> result = new ArrayList<>();
        for (SleepEntry e : entries) {
            if (e.getUsername().equals(username)) {
                result.add(e);
            }
        }
        return result;
    }
    public SleepEntry findById(String id) {
        for (SleepEntry e : entries) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
    public boolean deleteById(String id, String username) {
        SleepEntry e = findById(id);
        if (e == null) return false;
        if (!e.getUsername().equals(username)) return false;
        return entries.remove(e);
    }
    public boolean updateById(String id, String username, SleepEntry updated) {
        SleepEntry e = findById(id);
        if (e == null) return false;
        if (!e.getUsername().equals(username)) return false;

        e.setDate(updated.getDate());
        e.setSleepTime(updated.getSleepTime());
        e.setWakeTime(updated.getWakeTime());
        e.setQuality(updated.getQuality());
        e.setNote(updated.getNote());
        return true;
    }
}