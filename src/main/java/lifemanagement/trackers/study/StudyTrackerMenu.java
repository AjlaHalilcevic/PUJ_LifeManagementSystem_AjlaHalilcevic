package lifemanagement.trackers.study;

import lifemanagement.auth.Session;

import java.util.List;
import java.util.UUID;
import java.util.Scanner;
import java.time.LocalDate;

public class StudyTrackerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final StudyService studyService = new StudyService();

    public void show() {
        while (true) {
            System.out.println("\n=== STUDY TRACKER ===");
            System.out.println("1) Add study record");
            System.out.println("2) View my study records");
            System.out.println("3) Update my study record");
            System.out.println("4) Delete my study record");
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
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private void addRecord() {
        System.out.println("\n--- Add study record ---");

        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        int minutes = readInt("Minutes studied (1-1440): ", 1, 1440);

        String subject = prompt("Subject (optional): ");
        String notes = prompt("Notes (optional): ");

        String id = UUID.randomUUID().toString().substring(0, 8);

        StudyRecord record = new StudyRecord(id, Session.username, date, minutes, subject, notes);
        studyService.add(record);
        System.out.println("Saved. ID = " + id);
    }
    private void viewRecords() {
        List<StudyRecord> records = studyService.getForUser(Session.username);

        if (records.isEmpty()) {
            System.out.println("No study records yet.");
            return;
        }
        System.out.println("\nMy study records:");
        for (StudyRecord r : records) {
            System.out.println(r);
        }
    }
    private void updateRecord() {
        System.out.println("\n--- Update study record ---");
        String id = prompt("Record id to update: ");

        StudyRecord r = studyService.findById(id, Session.username);
        if (r == null || !r.getUsername().equals(Session.username)) {
            System.out.println("Not found.");
            return;
        }
        String newDateStr = prompt("New date (YYYY-MM-DD) [Enter to keep " + r.getDate() + "]: ");
        if (!newDateStr.isBlank()) {
            try {
                r.setDate(LocalDate.parse(newDateStr.trim()));
            } catch (Exception e) {
                System.out.println("Invalid date, keeping old value.");
            }
        }
        String newMinStr = prompt("New minutes (1-1440) [Enter to keep " + r.getMinutes() + "]: ");
        if (!newMinStr.isBlank()) {
            try {
                int m = Integer.parseInt(newMinStr.trim());
                if (m >= 1 && m <= 1440) r.setMinutes(m);
                else System.out.println("Minutes out of range, keeping old value.");
            } catch (Exception e) {
                System.out.println("Invalid number, keeping old value.");
            }
        }
        String newSubject = prompt("New subject [Enter to keep]: ");
        if (!newSubject.isBlank()) r.setSubject(newSubject);

        String newNotes = prompt("New notes [Enter to keep]: ");
        if (!newNotes.isBlank()) r.setNotes(newNotes);

        System.out.println("Updated.");
    }
    private void deleteRecord() {
        System.out.println("\n--- Delete study record ---");
        String id = prompt("Record id to delete: ");

        boolean ok = studyService.deleteById(id, Session.username);
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
                System.out.println("Invalid date format. Example: 2026-01-04");
            }
        }
    }
    private int readInt(String message, int min, int max) {
        while (true) {
            String s = prompt(message);
            try {
                int n = Integer.parseInt(s);
                if (n < min || n > max) {
                    System.out.println("Enter a number between" + min + "and" + max + ".");
                    continue;
                }
                return n;
            }catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }
}