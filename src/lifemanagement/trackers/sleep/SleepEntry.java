package lifemanagement.trackers.sleep;

import java.time.LocalDate;
import java.time.LocalTime;

public class SleepEntry {
    private String id;
    private String username;
    private LocalDate date;
    private LocalTime sleepTime;
    private LocalTime wakeTime;
    private int quality;
    private String note;

    public SleepEntry(String id, String username, LocalDate date, LocalTime sleepTime, LocalTime wakeTime, int quality, String note) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.quality = quality;
        this.note = note;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public LocalDate getDate() { return date; }
    public LocalTime getSleepTime() { return sleepTime; }
    public LocalTime getWakeTime() { return wakeTime; }
    public int getQuality() { return quality; }
    public String getNote() { return note; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setSleepTime(LocalTime sleepTime) { this.sleepTime = sleepTime; }
    public void setWakeTime(LocalTime wakeTime) { this.wakeTime = wakeTime; }
    public void setQuality(int quality) { this.quality = quality; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
    String extra = (note == null || note.isBlank()) ? "" : "(" + note + ")";
    return "[" + id + "]" + date + " " + sleepTime + "-" + wakeTime + " | quality: " + quality + extra;
    }
}