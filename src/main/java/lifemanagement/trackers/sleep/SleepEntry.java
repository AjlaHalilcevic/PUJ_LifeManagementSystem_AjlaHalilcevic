package lifemanagement.trackers.sleep;

import java.time.LocalDate;
import java.time.LocalTime;

public class SleepEntry {
    private String id;
    private String username;
    private LocalDate date;
    private double hours;
    private boolean good;

    public SleepEntry(String id, String username, LocalDate date, double hours, boolean good) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.hours = hours;
        this.good = good;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public LocalDate getDate() { return date; }
    public double getHours() { return hours; }
    public boolean isGood() { return good; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setHours(double hours) { this.hours = hours; }
    public void setGood(boolean good) { this.good = good; }

    @Override
    public String toString() {
    return "[" + id + "] datum=" + date + " | sati=" + hours + " | good= " + good;
    }
}