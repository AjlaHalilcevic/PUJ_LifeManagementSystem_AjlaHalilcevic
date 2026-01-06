package lifemanagement.trackers.calendar;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarService {
    private final MongoCollection<Document> col =
            MongoDBConnection.getDatabase().getCollection("calendar_events");

    public void addEvent(CalendarEvent e) {
        col.insertOne(toDoc(e));
    }

    public List<CalendarEvent> getEventsForUser(String username) {
        List<CalendarEvent> list = new ArrayList<>();
        for (Document d : col.find(Filters.eq("username", username))) {
            list.add(fromDoc(d));
        }
        return list;
    }

    public CalendarEvent findById(String id, String username) {
        Document d = col.find(Filters.and(
                Filters.eq("id", id),
                Filters.eq("username", username)
        )).first();
        return d == null ? null : fromDoc(d);
    }

    public boolean deleteEvent(String id, String username) {
        return col.deleteOne(Filters.and(
                Filters.eq("id", id),
                Filters.eq("username", username)
        )).getDeletedCount() > 0;
    }

    public boolean update(CalendarEvent e) {
        return col.replaceOne(
                Filters.and(
                        Filters.eq("id", e.getId()),
                        Filters.eq("username", e.getUsername())
                ),
                toDoc(e),
                new ReplaceOptions().upsert(false)
        ).getModifiedCount() > 0;
    }

    private Document toDoc(CalendarEvent e) {
        return new Document()
                .append("id", e.getId())
                .append("username", e.getUsername())
                .append("title", e.getTitle())
                .append("date", e.getDate() == null ? null : e.getDate().toString())
                .append("time", e.getTime() == null ? null : e.getTime().toString())
                .append("description", e.getDescription());
    }

    private CalendarEvent fromDoc(Document d) {
        return new CalendarEvent(
                d.getString("id"),
                d.getString("username"),
                d.getString("title"),
                readLocalDate(d, "date"),
                readLocalTime(d, "time"),
                d.getString("description")
        );
    }

    private static java.time.LocalDate readLocalDate(org.bson.Document d, String field) {
        Object val = d.get(field);
        if (val == null) return null;

        if (val instanceof String) {
            return java.time.LocalDate.parse((String) val);
        }
        if (val instanceof java.util.Date) {
            java.util.Date dt = (java.util.Date) val;
            return dt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        return java.time.LocalDate.parse(val.toString());
    }

    private static java.time.LocalTime readLocalTime(org.bson.Document d, String field) {
        Object val = d.get(field);
        if (val == null) return null;

        if (val instanceof String) {
            return LocalTime.parse((String) val);
        }
        if (val instanceof java.util.Date) {
            java.util.Date dt = (java.util.Date) val;
            return dt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
        }
        return java.time.LocalTime.parse(val.toString());
    }
}