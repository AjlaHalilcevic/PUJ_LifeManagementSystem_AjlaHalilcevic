package lifemanagement.trackers.sleep;

import lifemanagement.auth.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
                    addSleep();
                    break;
                case "2":
                    viewSleep();
                    break;
                case "3":
                    updateSleep();
                    break;
                case "4":
                    deleteSleep();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addSleep() {
        System.out.println("\n--- Add sleep record ---");

        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        double hours = readDouble("Sleep time (npr. 7:5): ");
        boolean good = readBoolean("Good sleep (yes/no): ");

        String id = UUID.randomUUID().toString().substring(0, 8);
        String username = Session.username;

        SleepEntry s = new SleepEntry(id, username, date, hours, good);
        sleepService.add(s);

        System.out.println("Saved. ID = " + id);
    }

    private void viewSleep() {
        String username = Session.username;
        List<SleepEntry> list = sleepService.getForUser(username);

        if (list.isEmpty()) {
            System.out.println("No sleep records yet.");
            return;
        }
        System.out.println("\nMy sleep records:");
        for (SleepEntry s : list) {
            System.out.println(s);
        }
    }

    private void updateSleep() {
        System.out.println("\n--- Update sleep record ---");
        String username = Session.username;

        String id = prompt("Record ID to update: ");
        SleepEntry existing = sleepService.findById(id, username);
        if (existing == null) {
            System.out.println("Not found.");
            return;
        }
        if (!existing.getUsername().equals(username)) {
            System.out.println("You can only update your own records.");
            return;
        }
        System.out.println("Current: " + existing);
        System.out.println("Press Enter to keep existing values.");

        String dateTxt = promptOptional("New date (Enter to skip): ");
        String hoursTxt = promptOptional("New sleep time (Enter to skip):");
        String goodTxt = promptOptional("Good sleep? (yes/no/Enter to skip):");

        LocalDate newDate = dateTxt.isEmpty() ? null : LocalDate.parse(dateTxt);
        Double newHours = hoursTxt.isEmpty() ? null : Double.parseDouble(hoursTxt);
        Boolean newGood = goodTxt.isEmpty() ? null : parseYesNo(goodTxt);

        boolean ok = sleepService.update(id, username, newDate, newHours, newGood);
        System.out.println(ok ? "Updated." : "Update failed.");
    }
    private void deleteSleep() {
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
    private String promptOptional(String message) {
        System.out.println(message);
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
    private double readDouble(String message) {
        while (true) {
            String s = prompt(message);
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.println("Invalid time format. Example: 15:30");
            }
        }
    }
    private boolean readBoolean(String message) {
        while (true) {
            String s = prompt(message).toLowerCase();
            if (s.equals("yes") || s.equals("y")) return true;
            if (s.equals("no") || s.equals("n")) return false;
            System.out.println("Enter: yes or no.");
        }
    }
    private Boolean parseYesNo(String s) {
        s = s.toLowerCase();
        if (s.equals("yes") || s.equals("y")) return true;
        if (s.equals("no") || s.equals("n")) return false;
        return null;
        }
    }