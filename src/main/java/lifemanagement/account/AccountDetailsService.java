package lifemanagement.account;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lifemanagement.db.MongoDBConnection;
import org.bson.Document;

public class AccountDetailsService {
    private final MongoCollection<Document> col;

    public AccountDetailsService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.col = db.getCollection("account_details");
    }
    public AccountDetails getByUsername(String username) {
        Document d = col.find(Filters.eq("username", username)).first();
        if (d == null) return null;

        return new AccountDetails(
                d.getString("username"),
                d.getString("fullName"),
                d.getString("email"),
                d.getInteger("age"),
                d.getInteger("heightCm"),
                d.getInteger("weightKg"),
                d.getString("goal")
        );
    }
    public void upsert(AccountDetails details) {
        Document d = new Document("username", details.getUsername())
        .append("fullName", details.getFullName())
        .append("email", details.getEmail())
        .append("age", details.getAge())
        .append("heightCm", details.getHeightCm())
        .append("weightKg", details.getWeightKg())
        .append("goal", details.getGoal());
    col.replaceOne(
            Filters.eq("username", details.getUsername()),
            d, new ReplaceOptions().upsert(true)
    );
    }
    public boolean deleteByUsername(String username) {
        return col.deleteOne(Filters.eq("username", username)).getDeletedCount() > 0;
    }
}
