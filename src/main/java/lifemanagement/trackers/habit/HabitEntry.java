package lifemanagement.trackers.habit;

import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;

public class HabitEntry {
    private String id;
    private String username;
    private String name;
    private String frequency;
    private boolean active;
    private LocalDate createdAt;

    public HabitEntry(String id, String username, String name, String frequency, boolean active, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.frequency = frequency;
        this.active = active;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getFrequency() { return frequency; }
    public boolean isActive() { return active; }
    public LocalDate getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
    return "[" + id + "]" + name + " | freq=" + frequency + " | active=" + active + " | created=" + createdAt;
    }
}