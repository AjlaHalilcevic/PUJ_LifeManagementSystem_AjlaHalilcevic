package lifemanagement.trackers.habit;

import lifemanagement.auth.Session;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.UUID;

public class HabitTrackerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final HabitService habitService = new HabitService();

    public void show() {
        while (true) {
            System.out.println("\n=== HABIT TRACKER ===");
            System.out.println("1) Add habit");
            System.out.println("2) View my habits");
            System.out.println("3) Update habit");
            System.out.println("4) Delete habit");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": addHabit(); break;
                case "2": viewHabits(); break;
                case "3": updateHabit(); break;
                case "4": deleteHabit(); break;
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private void addHabit() {
        System.out.println("\n--- Add habit ---");

        String name = prompt("Habit name: ");
        String frequency = prompt("Frequency (daily/weekly/3x-week...): ");
        boolean active = readBoolean("Active? (yes/no): ");
        String id = UUID.randomUUID().toString().substring(0, 8);
        String username = Session.username;

        HabitEntry h = new HabitEntry(id, username, name, frequency, active, LocalDate.now());
        habitService.add(h);

        System.out.println("Saved. ID =" + id);
    }

    private void viewHabits() {
        String username = Session.username;
        List<HabitEntry> list = habitService.getForUser(username);

        if (list.isEmpty()) {
            System.out.println("No habits yet.");
            return;
        }
        System.out.println("\nMy habits:");
        for (HabitEntry h : list) {
            System.out.println(h);
        }
    }
        private void updateHabit () {
            System.out.println("\n--- Update habit ---");
            String id = prompt("Habit ID:");
            String username = Session.username;

            HabitEntry existing = habitService.findById(id, username);
            if (existing == null) {
                System.out.println("Not found.");
                return;
            }
            System.out.println("Current: " + existing);

            String newName = promptOptional("New name (enter to skip): ");
            String newFreq = promptOptional("New frequency (enter to skip): ");
            Boolean newActive = readBooleanOptional("Change active? (yes/no/enter to skip): ");

            boolean ok = habitService.update(id, username, newName.isEmpty() ? null : newName,
                    newFreq.isEmpty() ? null : newFreq, newActive);
            System.out.println(ok ? "Updated." : "Update failed.");
        }

        private void deleteHabit () {
            System.out.println("\n--- Delete habit ---");
            String id = prompt("Habit ID: ");
            String username = Session.username;

            boolean ok = habitService.deleteById(id, username);
            System.out.println(ok ? "Deleted." : "Not found.");
        }
        private String prompt (String msg){
            System.out.print(msg);
            return sc.nextLine().trim();
        }
        private String promptOptional (String msg){
            System.out.println(msg);
            return sc.nextLine().trim();
        }
        private boolean readBoolean(String msg) {
            while (true) {
                System.out.print(msg);
                String s = sc.nextLine().trim().toLowerCase();
                if (s.equals("yes") || s.equals("y")) return true;
                if (s.equals("no") || s.equals("n")) return false;
                System.out.print("Please type yes/no.");
            }
        }
        private Boolean readBooleanOptional(String msg) {
            while (true) {
                System.out.print(msg);
                String s = sc.nextLine().trim().toLowerCase();
                if (s.isEmpty()) return null;
                if (s.equals("yes") || s.equals("y")) return true;
                if (s.equals("no") || s.equals("n")) return false;
                System.out.println("Type yes/no or press Enter to skip.");
            }
        }
    }
