package lifemanagement.trackers.sleep;

import lifemanagement.auth.Session;

import java.security.Key;
import java.security.spec.ECField;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Locale;
import java.util.Scanner;

public class SleepTrackerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final SleepService sleepService = new SleepService();

    public void show() {
        while (true) {
            System.out.println("\n=== SLEEP TRACKER ===");
            System.out.println("1) Add sleep record");
            System.out.println("2) View my sleep records");
            System.out.println("3) Update my sleep record");
            System.out.println("4) Delete my sleep record");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    addRecord();
                    break;
                case "2":
                    viewRecords();
                    break;
                case "3":
                    updateRecord();
                    break;
                case "4":
                    deleteRecord();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addRecord() {
        System.out.println("\n--- Add sleep record ---");
        String username = Session.username;

        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime sleepTime = readTime("Sleep time (HH:MM): ");
        LocalTime wakeTime = readTime("Wake time (HH:MM): ");
        int quality = readIntRange("Quality (1-5): ", 1, 5);

        String note = prompt("Note (optional): ");
        if (note.isBlank()) note = "";

        String id = UUID.randomUUID().toString().substring(0, 8);

        SleepEntry e = new SleepEntry(id, username, date, sleepTime, wakeTime, quality, note);
        sleepService.add(e);

        System.out.println("Saved. ID = " + id);
    }

    private void viewRecords() {
        String username = Session.username;
        List<SleepEntry> list = sleepService.getEntriesForUser(username);

        if (list.isEmpty()) {
            System.out.println("No sleep records yet.");
            return;
        }
        System.out.println("\nMy sleep records:");
        for (SleepEntry e : list) {
            System.out.println(e);
        }
    }

    private void updateRecord() {
        System.out.println("\n--- Update sleep record ---");
        String username = Session.username;

        String id = prompt("Record ID to update: ");
        SleepEntry current = sleepService.findById(id);

        if (current == null) {
            System.out.println("Not found.");
            return;
        }
        if (!current.getUsername().equals(username)) {
            System.out.println("You can only update your own records.");
            return;
        }
        System.out.println("Current: " + current);
        System.out.println("Press Enter to keep existing values.");

        LocalDate date = readDateOptional("New date (YYYY-MM-DD): ", current.getDate());
        LocalTime sleepTime = readTimeOptional("New sleep time (HH:MM): ", current.getSleepTime());
        LocalTime wakeTime = readTimeOptional("New wake time (HH:MM): ", current.getWakeTime());
        int quality = readIntRangeOptional("New quality (1-5): ", 1, 5, current.getQuality());

        String note = prompt("New note (optional): ");
        if (note.isBlank()) note = current.getNote();

        SleepEntry updated = new SleepEntry(id, username, date, sleepTime, wakeTime, quality, note);
        boolean ok = sleepService.updateById(id, username, updated);

        System.out.println(ok ? "Updated." : "Update failed.");
    }

    private void deleteRecord() {
        System.out.println("\n--- Delete sleep record ---");
        String username = Session.username;

        String id = prompt("Record ID to delete: ");
        boolean ok = sleepService.deleteById(id, username);

        System.out.println(ok ? "Deleted." : "Not found.");
    }

    private String prompt(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private LocalDate readDate(String message) {
        while (true) {
            String s = prompt(message);
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date format. Example: 2026-01-03");
            }
        }
    }
    private LocalTime readTime(String message) {
        while (true) {
            String s = prompt(message);
            try {
                return LocalTime.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid time format. Example: 15:30");
            }
        }
    }
    private int readIntRange(String message, int min, int max) {
        while (true) {
            String s = prompt(message);
            try {
                int val = Integer.parseInt(s);
                if (val < min || val > max) {
                    System.out.println("Enter a number between" + min + "and" + max + ".");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }
    private LocalDate readDateOptional(String message, LocalDate current) {
        while (true) {
            String s = prompt(message);
            if (s.isBlank()) return current;
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date format. Example: 2026-01-03");
            }
        }
    }
    private LocalTime readTimeOptional(String message, LocalTime current) {
        while (true) {
            String s = prompt(message);
            if (s.isBlank()) return current;
            try {
                return LocalTime.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid time format. Example: 17:30");
            }
        }
    }
    private int readIntRangeOptional(String message, int min, int max, int current) {
        while (true) {
            String s = prompt(message);
            if (s.isBlank()) return current;
            try {
                int val = Integer.parseInt(s);
                if (val < min || val > max) {
                    System.out.println("Enter a number between " + min + "and" + max + ".");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }
}