package lifemanagement.trackers.study;

import java.util.ArrayList;
import java.util.List;

public class StudyService {
    private final List<StudyRecord> records = new ArrayList<>();

    public void add(StudyRecord r) {
        records.add(r);
    }
    public List<StudyRecord> getForUser(String username) {
        List<StudyRecord> result = new ArrayList<>();
        for (StudyRecord r : records) {
            if (r.getUsername().equals(username)) {result.add(r);}
        }
        return result;
    }
    public StudyRecord findById(String id, String username) {
       for (StudyRecord r : records) {
           if (r.getId().equals(id) && r.getUsername().equals(username)) return r;
       }
       return null;
    }
    public boolean update(String id, String username, java.time.LocalDate newDate,
                          Integer newMinutes, String newSubject, String newNotes) {
        StudyRecord r = findById(id, username);
        if (r == null) return false;

        if (newDate != null) r.setDate(newDate);
        if (newMinutes != null) r.setMinutes(newMinutes);
        if (newSubject != null) r.setSubject(newSubject);
        if (newNotes != null) r.setNotes(newNotes);

        return true;
    }
    public boolean deleteById(String id, String username) {
        StudyRecord r = findById(id, username);
        if (r == null) return false;
        return records.remove(r);
    }
}