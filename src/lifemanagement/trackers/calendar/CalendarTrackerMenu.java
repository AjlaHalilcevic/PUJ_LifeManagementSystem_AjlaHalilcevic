package lifemanagement.trackers.calendar;

import lifemanagement.auth.Session;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.time.LocalTime;
import java.time.LocalDate;

public class CalendarTrackerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CalendarService calendarService = new CalendarService();

    public void show() {
        while (true) {
            System.out.println("\n=== CALENDAR TRACKER ===");
            System.out.println("1) Add event");
            System.out.println("2) View my events");
            System.out.println("3) Delete event");
            System.out.println("4) Update event");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addEvent();
                    break;
                case "2":
                    viewEvents();
                    break;
                case "3":
                    deleteEvent();
                    break;
                case "4":
                    updateEvent();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private void addEvent() {
       System.out.println("\n--- Add event ---");

       String title = prompt("Title: ");
       LocalDate date = readDate("Date (YYYY-MM-DD): ");
       LocalTime time = readTime("Time (HH:MM): ");
       String description = prompt("Description (optional): ");

       String id = UUID.randomUUID().toString().substring(0, 8);
       String username = Session.username;
       CalendarEvent event = new CalendarEvent( id, username, title, date, time, description);

       calendarService.addEvent(event);
       System.out.println("Saved. ID = " + id);
}
    private void viewEvents() {
    String username = Session.username;
    List<CalendarEvent> events = calendarService.getEventsForUser(username);

    if (events.isEmpty()) {
        System.out.println("No events yet.");
        return;
    }
    System.out.println("\n My events: ");
    for (CalendarEvent e : events) {
        System.out.println(e);
    }
}
    private void deleteEvent() {
        String id = prompt("Event ID to delete: ");
        boolean ok = calendarService.deleteEvent(id, Session.username);
        System.out.println(ok ? "Deleted." : "Event not found.");
    }
    private String prompt(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }
    private LocalDate readDate(String message) {
        while (true) {
            try {
                return LocalDate.parse(prompt(message));
                } catch (Exception e) {
            System.out.println("Invalid date format. Example: 2026-01-03");
        }
    }
}
    private LocalTime readTime(String message) {
        while (true) {
            try {
                return LocalTime.parse(prompt(message));
            } catch (Exception e) {
                System.out.println("Invalid time format. Example: 14:30");
            }
        }
    }

    private void updateEvent() {
        System.out.println();
        System.out.println("--- Update event ---");

        String id = prompt("Event id to update: ");
        String username = Session.username;

        CalendarEvent e = calendarService.findById(id);
        if (e == null) {
            System.out.println("Not found.");
            return;
        }
        if (!e.getUsername().equals(username)) {
            System.out.println("You can only update your own events.");
            return;
        }
        System.out.println("Current: " + e);

        String newTitle = prompt("New title (leave empty to keep): ");
        if (!newTitle.isBlank()) {
            e.setTitle(newTitle);
        }
        String dateInput = prompt("New date YYYY-MM-DD (leave empty to keep): ");
        if (!dateInput.isBlank()) {
            try {
                e.setDate(java.time.LocalDate.parse(dateInput));
            } catch (Exception ex) {
                System.out.println("Invalid date format. Keeping old date.");
            }
        }
        String timeInput = prompt("New time HH:MM (leave empty to keep): ");
        if (!timeInput.isBlank()) {
            try {
                e.setTime(java.time.LocalTime.parse(timeInput));
            } catch (Exception ex) {
                System.out.println("Invalid time format. Keepin old time.");
            }
        }
        String newDesc = prompt("New description (leave empty to keep): ");
        if (!newDesc.isBlank()) {
            e.setDescription(newDesc);
        }
        System.out.println("Updated.");
        System.out.println("Now: " + e);
    }
}