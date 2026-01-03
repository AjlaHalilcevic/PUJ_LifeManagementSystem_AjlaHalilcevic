package lifemanagement.mainmenu;

import lifemanagement.auth.AuthMenu;
import lifemanagement.auth.Session;

import java.util.Calendar;
import java.util.Scanner;

//TODO:
import lifemanagement.trackers.habit.HabitTrackerMenu;
import lifemanagement.trackers.calendar.CalendarTrackerMenu;
import lifemanagement.trackers.sleep.SleepTrackerMenu;
import lifemanagement.trackers.study.StudyTrackerMenu;

public class MainMenu {

    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("Logged in as: " + Session.username);
            System.out.println("Theme: " + Session.theme);
            System.out.println("1) Calendar tracker");
            System.out.println("2) Habit tracker");
            System.out.println("3) Sleep tracker");
            System.out.println("4) Study tracker");
            System.out.println("5) Logout");
            System.out.println("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                   new CalendarTrackerMenu().show();
                   break;

                case "2":
                   new HabitTrackerMenu().show();
                    break;

                case "3":
                    new SleepTrackerMenu().show();
                    break;

                case "4":
                    new StudyTrackerMenu().show();
                    break;

                case "5":
                    Session.username = null;
                    Session.theme = "default";
                    System.out.println("Logged out.");
                    new AuthMenu().show();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}