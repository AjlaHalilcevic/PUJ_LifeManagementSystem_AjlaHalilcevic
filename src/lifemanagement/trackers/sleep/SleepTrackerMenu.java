package lifemanagement.trackers.sleep;

import java.util.Locale;
import java.util.Scanner;

public class SleepTrackerMenu {
    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n--- SLEEP TRACKER ---");
            System.out.println("1) Add");
            System.out.println("2) View");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            String c = sc.nextLine().trim();
            if (c.equals("0")) return;

            System.out.println("TODO...");
        }
    }
}