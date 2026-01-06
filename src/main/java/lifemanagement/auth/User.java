package lifemanagement.auth;

import org.bson.Document;

public class User {
    private final String username;
    private final String password;
    private final String theme;

    public User(String username, String password, String theme) {
        this.username = username;
        this.password = password;
        this.theme = (theme == null || theme.isBlank()) ? "default" : theme;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTheme() { return theme; }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password)
                .append("theme", theme);
    }
}