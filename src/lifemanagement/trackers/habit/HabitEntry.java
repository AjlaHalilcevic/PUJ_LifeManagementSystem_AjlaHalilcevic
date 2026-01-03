package lifemanagement.trackers.habit;

import org.bson.Document;

public class HabitEntry {
    private final String username;
    private final String habitName;
    private final String date;
    private final boolean done;

    public HabitEntry(String username, String habitName, String date, boolean done) {
        this.username = username;
        this.habitName = habitName;
        this.date = date;
        this.done = done;
    }

    public String getUsername() { return username; }
    public String getHabitName() { return habitName; }
    public String getDate() { return date; }
    public boolean isDone() { return done; }

    public Document toDocument() {
        return new Document("username", username)
                .append("habitName", habitName)
                .append("date", date)
                .append("done", done);
    }
    public static HabitEntry fromDocument(Document d) {
        return new HabitEntry(
                d.getString("username"),
                d.getString("habitName"),
                d.getString("date"),
                d.getBoolean("done", false)
        );
    }
}