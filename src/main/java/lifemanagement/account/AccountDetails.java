package lifemanagement.account;

public class AccountDetails {
    private String username;
    private String fullName;
    private String email;
    private Integer age;
    private Integer heightCm;
    private Integer weightKg;
    private String goal;

    public AccountDetails(String username, String fullName, String email, Integer age, Integer heightCm, Integer weightKg, String goal) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.goal = goal;
    }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Integer getAge() { return age; }
    public Integer getHeightCm() { return heightCm; }
    public Integer getWeightKg() { return weightKg; }
    public String getGoal() { return goal; }

    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setEmail(String email) {this.email = email;}
    public void setAge(Integer age) {this.age = age;}
    public void setHeightCm(Integer heightCm) {this.heightCm = heightCm;}
    public void setWeightKg(Integer weightKg) {this.weightKg = weightKg;}
    public void setGoal(String goal) {this.goal = goal;}
}
