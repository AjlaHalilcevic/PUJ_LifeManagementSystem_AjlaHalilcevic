package lifemanagement.trackers.study;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudyService {
    private final MongoCollection<Document> col =
            MongoDBConnection.getDatabase().getCollection("study_records");

    public void add(StudyRecord r) {
        col.insertOne(toDoc(r));
    }
    public List<StudyRecord> getForUser(String username) {
        List<StudyRecord> list = new ArrayList<>();
        for (Document d : col.find(Filters.eq("username", username))) {
            list.add(fromDoc(d));
        }
        return list;
    }
    public StudyRecord findById(String id, String username) {
       Document d = col.find(Filters.and(
               Filters.eq("id", id),
               Filters.eq("username", username)
       )).first();
       return d == null ? null : fromDoc(d);
    }
    public boolean deleteById(String id, String username) {
        return col.deleteOne(
                Filters.and(Filters.eq("id", id),
                        Filters.eq("username", username)
                )).getDeletedCount() > 0;
    }

    public boolean update(String id, String username, LocalDate newDate,
                          Integer newMinutes, String newSubject, String newNotes) {
        StudyRecord r = findById(id, username);
        if (r == null) return false;

        if (newDate != null) r.setDate(newDate);
        if (newMinutes != null) r.setMinutes(newMinutes);
        if (newSubject != null) r.setSubject(newSubject);
        if (newNotes != null) r.setNotes(newNotes);

        return col.replaceOne(
                Filters.and(Filters.eq("id", id),
                        Filters.eq("username", username)),
                toDoc(r),
                new ReplaceOptions().upsert(false)).getModifiedCount() > 0;
    }
    private Document toDoc(StudyRecord r) {
        return new Document()
                .append("id", r.getId())
                .append("username", r.getUsername())
                .append("subject", r.getSubject())
                .append("date", r.getDate() == null ? null : r.getDate().toString())
                .append("minutes", r.getMinutes())
                .append("notes", r.getNotes());
    }
    private StudyRecord fromDoc(Document d) {
        return new StudyRecord(
                d.getString("id"),
                d.getString("username"),
                readLocalDate(d,"date"),
                d.getInteger("minutes", 0),
                d.getString("subject"),
                d.getString("notes")
        );
    }
    private static LocalDate readLocalDate(Document d, String field) {
        Object val = d.get(field);
        if (val == null) return null;

        if (val instanceof String) {
            return LocalDate.parse((String) val);
        }
        if (val instanceof Date) {
            Date dt = (Date) val;
            return dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return LocalDate.parse(val.toString());
    }
}