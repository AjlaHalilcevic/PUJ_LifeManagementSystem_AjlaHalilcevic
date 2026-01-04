package lifemanagement.trackers.study;

import java.util.ArrayList;
import java.util.List;

public class StudyService {
    private final List<StudyRecord> records = new ArrayList<>();

    public void add(StudyRecord r) {
        records.add(r);
    }
    public List<StudyRecord> getRecordsForUser(String username) {
        List<StudyRecord> result = new ArrayList<>();
        for (StudyRecord r : records) {
            if (r.getUsername().equals(username)) {
                result.add(r);
            }
        }
        return result;
    }
    public StudyRecord findById(String id) {
       for (StudyRecord r : records) {
           if (r.getId().equals(id)) return r;
       }
       return null;
    }
    public boolean deleteById(String id, String username) {
        StudyRecord r = findById(id);
        if (r == null) return false;
        if (!r.getUsername().equals(username)) return false;
        return records.remove(r);
    }
}