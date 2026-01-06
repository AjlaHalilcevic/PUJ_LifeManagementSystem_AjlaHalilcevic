package lifemanagement.trackers.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarEvent {
    private String id;
    private String username;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String description;

    public CalendarEvent(String id, String username, String title, LocalDate date, LocalTime time, String description) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getTitle() { return title; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getDescription() { return description; }

    public void setTitle(String title) {this.title = title;}
    public void setDate(LocalDate date) {this.date = date;}
    public void setTime(LocalTime time) {this.time = time;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return "[" + id + "]" + date + " " + time + " - " + title +
                (description == null || description.isBlank() ? "" : " (" + description + ")");
    }
}