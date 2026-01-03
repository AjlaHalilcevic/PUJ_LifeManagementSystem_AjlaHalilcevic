package lifemanagement.auth;

import lifemanagement.mainmenu.MainMenu;
import java.util.Scanner;
import lifemanagement.auth.Session;

public class AuthMenu {
    private final Scanner sc = new Scanner(System.in);
    private final UserManager userManager = new UserManager();

    public void show() {
        while (true) {
            System.out.println("\n=== AUTH MENU ===");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("0) Exit");
            System.out.println("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegister();
                    break;

                case "2":
                    boolean ok = handleLogin();
                    if (ok) {
                        new MainMenu().show();
                    }
                    break;

                case "0":
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void handleRegister() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        System.out.print("Theme (default/dark/green/blue...): ");
        String theme = sc.nextLine().trim();
        if (theme.isEmpty()) theme = "default";

        boolean ok = userManager.register(new User(username, password, theme));
        System.out.println(ok ? "Registered." : "Username already exists.");
    }

    private boolean handleLogin() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        boolean ok = userManager.login(username, password);
        System.out.println(ok ? "Login OK." : "Wrong username/password.");
        return ok;
    }
}