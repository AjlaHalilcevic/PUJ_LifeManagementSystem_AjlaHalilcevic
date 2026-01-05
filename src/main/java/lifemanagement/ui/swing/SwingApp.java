package lifemanagement.ui.swing;

import lifemanagement.trackers.habit.HabitService;
import lifemanagement.trackers.sleep.SleepService;
import lifemanagement.trackers.study.StudyService;

import javax.swing.SwingUtilities;

public class SwingApp {
    public static void main(String [] args) {
        SwingUtilities.invokeLater(() -> {
            HabitService habitService = new HabitService();
            SleepService sleepService = new SleepService();
            StudyService studyService = new StudyService();
            new AuthFrame().setVisible(true);
        });
    }
}
