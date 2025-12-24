package lifemanagement.auth;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

import javax.print.Doc;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class UserManager {
    private final MongoCollection<Document> users;

    public UserManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        users = db.getCollection("users");
    }
    public boolean register(User u) {
        if (users.find(eq("username", u.getUsername())).first() !=null) {
            return false;
        }
        users.insertOne(u.toDocument());
        return true;
    }
    public boolean login(String username, String password) {
        Document d = users.find(eq("username", username)).first();
        if (d == null) return false;

        String dbPass = d.getString("password");
        if (!password.equals(dbPass)) return false;

        Session.username = username;
        Session.theme = d.getString("theme");
        return true;
    }

    public void updateTheme(String username, String theme) {
        users.updateOne(eq("username", username), set("theme", theme));
        Session.theme = theme;
    }
    public void deleteUser(String username) {
        users.deleteOne(eq("username", username));
        if (username.equals(Session.username)) {
            Session.username = null;
            Session.theme = "default";
        }
    }
}