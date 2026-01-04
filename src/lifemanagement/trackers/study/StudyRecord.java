package lifemanagement.trackers.study;

import java.time.LocalDate;

public class StudyRecord {
    private String id;
    private String username;
    private String subject;
    private LocalDate date;
    private int minutes;
    private String notes;

    public StudyRecord(String id, String username, LocalDate date, int minutes, String subject, String notes) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.minutes = minutes;
        this.subject = subject;
        this.notes = notes;
    }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public LocalDate getDate() { return date; }
    public int getMinutes() { return minutes; }
    public String getSubject() { return subject; }
    public String getNotes() { return notes; }

    public void setSubject(String subject) { this.subject = subject; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        String sub = (subject == null || subject.isBlank()) ? "" : " | Subject: " + subject;
        String note = (notes == null || notes.isBlank()) ? "" : " | Notes: " + notes;
        return "[" + id + "]" + date + "-" + minutes + "min" + sub + note;
    }
}