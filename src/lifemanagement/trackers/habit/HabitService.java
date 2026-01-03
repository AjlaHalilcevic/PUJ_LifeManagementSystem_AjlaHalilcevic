package lifemanagement.trackers.habit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class HabitService {
    private final MongoCollection<Document> habits;

    public HabitService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        habits = db.getCollection("habits");
    }
    public void add(HabitEntry entry) {
        habits.insertOne(entry.toDocument());
    }
    public List<HabitEntry> getAllForUser(String username) {
        List<HabitEntry> list = new ArrayList<>();
        for (Document d : habits.find(eq("username", username))) {
            list.add(HabitEntry.fromDocument(d));
        }
        return list;
    }
    public boolean updateDone(String username, String habitName, String date, boolean done) {
        Document found = habits.find(and(eq("username", username), eq("habitName", habitName), eq("date", date))).first();
        if (found == null) return false;

        habits.updateOne(and(eq("username", username), eq("habitName", habitName), eq("date", date)), set("done", done));
        return true;
    }
    public  boolean delete(String username, String habitName, String date) {
        Document found = habits.find(and(eq("username", username), eq("habitName", habitName), eq("date", date))).first();
        if (found == null) return false;

        habits.deleteOne(and(eq("username", username), eq("habitName", habitName), eq("date", date)));
        return true;
    }
}