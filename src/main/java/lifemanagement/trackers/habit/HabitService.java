package lifemanagement.trackers.habit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitService {
    private final MongoCollection<Document> col =
            MongoDBConnection.getDatabase().getCollection("habit_entries");

    public void add(HabitEntry h) {
        col.insertOne(toDoc(h));
    }
    public List<HabitEntry> getForUser(String username) {
        List<HabitEntry> list = new ArrayList<>();
        for (Document d : col.find(Filters.eq("username", username))) {
            list.add(fromDoc(d));
        }
        return list;
    }
    public HabitEntry findById(String id, String username) {
        Document d = col.find(Filters.and(
                Filters.eq("id", id),
                Filters.eq("username", username)
        )).first();
        return d == null ? null : fromDoc(d);
    }
    public boolean update(String id, String username, String newName, String newFrequency, Boolean newActive) {
        HabitEntry h = findById(id, username);
        if (h == null) return false;

        if (newName != null) h.setName(newName);
        if (newFrequency != null) h.setFrequency(newFrequency);
        if (newActive != null) h.setActive(newActive);

        return col.replaceOne(
                Filters.and(Filters.eq("id", id),
                        Filters.eq("username", username)),
                toDoc(h),
                new ReplaceOptions().upsert(false)).getModifiedCount() > 0;
    }

    public boolean deleteById(String id, String username) {
       return col.deleteOne(Filters.and(
               Filters.eq("id", id),
               Filters.eq("username", username)
       )).getDeletedCount() > 0;
    }
    private Document toDoc(HabitEntry h) {
        return new Document()
                .append("id", h.getId())
                .append("username", h.getUsername())
                .append("name", h.getName())
                .append("frequency", h.getFrequency())
                .append("active", h.isActive())
                .append("createdAt",h.getCreatedAt().toString());
    }
    private HabitEntry fromDoc(Document d) {
        return new HabitEntry(
                d.getString("id"),
                d.getString("username"),
                d.getString("name"),
                d.getString("frequency"),
                d.getBoolean("active", true),
                LocalDate.parse(d.getString("createdAt"))
        );
    }
}