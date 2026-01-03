package lifemanagement.trackers.habit;

import lifemanagement.auth.Session;

import java.util.List;
import java.util.Scanner;

public class HabitTrackerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final HabitService service = new HabitService();

    public void show() {
        while (true) {
            System.out.println("\n=== HABIT TRACKER ===");
            System.out.println("1) Add habit entry");
            System.out.println("2) View my habit entries");
            System.out.println("3) Update done status");
            System.out.println("4) Delete entry");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": add(); break;
                case "2": list(); break;
                case "3": update(); break;
                case "4": delete(); break;
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private void add() {
        System.out.print("Habit name: ");
        String habitName = sc.nextLine().trim();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = sc.nextLine().trim();

        System.out.print("Done? (y/n): ");
        boolean done = sc.nextLine().trim().equalsIgnoreCase("y");

        service.add(new HabitEntry(Session.username, habitName, date, done));
        System.out.println("Saved.");
    }
    private void list() {
        List<HabitEntry> all = service.getAllForUser(Session.username);
        if (all.isEmpty()) {
            System.out.println("No entries yet.");
            return;
        }
        System.out.println("\n--- Your Habit Entries ---");
        for (HabitEntry e : all) {
            System.out.println(e.getDate() + "|" + e.getHabitName() + " | done=" + e.isDone());
        }
    }
    private void update() {
        System.out.print("Habit name: ");
        String habitName = sc.nextLine().trim();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = sc.nextLine().trim();

        System.out.print("New done? (y/n): ");
        boolean done = sc.nextLine().trim().equalsIgnoreCase("y");

        boolean ok = service.updateDone(Session.username, habitName, date, done);
        System.out.println(ok ? "Updated." : "Entry not found.");
    }
    private void delete() {
        System.out.print("Habit name: ");
        String habitName = sc.nextLine().trim();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = sc.nextLine().trim();

        boolean ok = service.delete(Session.username, habitName, date);
        System.out.println(ok ? "Deleted." : "Entry not found.");
    }
}