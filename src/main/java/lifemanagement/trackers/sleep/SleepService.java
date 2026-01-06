package lifemanagement.trackers.sleep;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SleepService {
    private final MongoCollection<Document> col =
            MongoDBConnection.getDatabase().getCollection("sleep_entries");

    public void add(SleepEntry s) {
        col.insertOne(toDoc(s));
    }
    public List<SleepEntry> getForUser(String username) {
        List<SleepEntry> list = new ArrayList<>();
        for (Document d : col.find(Filters.eq("username", username))) {
            list.add(fromDoc(d));
            }
        return list;
    }
    public SleepEntry findById(String id, String username) {
        Document d = col.find(Filters.and(
                Filters.eq("id", id),
                Filters.eq("username", username)
        )).first();
        return d == null ? null : fromDoc(d);
    }
    public boolean deleteById(String id, String username) {
        return col.deleteOne(Filters.and(
                Filters.eq("id", id),
                Filters.eq("username", username)
        )).getDeletedCount() > 0;
    }
    public boolean update(String id, String username, LocalDate newDate, Double newHours, Boolean newGood) {
        SleepEntry s = findById(id, username);
        if (newDate != null) return false;

        if (newDate != null) s.setDate(newDate);
        if (newHours != null) s.setHours(newHours);
        if (newGood != null) s.setGood(newGood);

        return updateEntry(s);
    }
    public boolean updateEntry(SleepEntry updated) {
        return col.replaceOne(
                Filters.and(
                        Filters.eq("id", updated.getId()),
                        Filters.eq("username", updated.getUsername())
                ),
                toDoc(updated),
                new ReplaceOptions().upsert(false)
        ).getModifiedCount() > 0;
    }
    private Document toDoc(SleepEntry s) {
        return new Document()
                .append("id", s.getId())
                .append("username", s.getUsername())
                .append("date", s.getDate().toString())
                .append("hours", s.getHours())
                .append("good", s.isGood());
    }
    private SleepEntry fromDoc(Document d) {
        return new SleepEntry(
                d.getString("id"),
                d.getString("username"),
                LocalDate.parse(d.getString("date")),
                d.getDouble("hours"),
                d.getBoolean("good")
        );
    }
}