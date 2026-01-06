package lifemanagement.ui.swing;

import lifemanagement.account.AccountDetailsService;
import lifemanagement.trackers.habit.HabitService;
import lifemanagement.trackers.sleep.SleepService;
import lifemanagement.trackers.study.StudyService;
import lifemanagement.trackers.calendar.CalendarService;
import lifemanagement.ui.swing.SleepTrackerFrame;
import lifemanagement.ui.swing.HabitTrackerFrame;
import lifemanagement.ui.swing.StudyTrackerFrame;
import lifemanagement.ui.swing.CalendarTrackerFrame;
import lifemanagement.ui.swing.FinanceTrackerFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private final HabitService habitService;
    private final SleepService sleepService;
    private final StudyService studyService;
    private final CalendarService calendarService;

    public MainMenuFrame(HabitService habitService, SleepService sleepService, StudyService studyService, CalendarService calendarService) {
        this.habitService = habitService;
        this.sleepService = sleepService;
        this.studyService = studyService;
        this.calendarService = calendarService;

        setTitle("Life Management System - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel grid = new JPanel(new GridLayout(2, 3, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton accountBtn = new JButton("Account Details");
        JButton financeBtn = new JButton("Finance tracker");
        JButton sleepBtn = new JButton("Sleep tracker");
        JButton calendarBtn = new JButton("Calendar tracker");
        JButton studyBtn = new JButton("Study tracker");
        JButton habitBtn = new JButton("Habit tracker");

        grid.add(accountBtn);
        grid.add(financeBtn);
        grid.add(sleepBtn);
        grid.add(calendarBtn);
        grid.add(studyBtn);
        grid.add(habitBtn);

        financeBtn.addActionListener(e -> new FinanceTrackerFrame().setVisible(true));
        sleepBtn.addActionListener(e -> new SleepTrackerFrame(sleepService).setVisible(true));
        studyBtn.addActionListener(e -> new StudyTrackerFrame(studyService).setVisible(true));
        habitBtn.addActionListener(e -> new HabitTrackerFrame(habitService).setVisible(true));
        accountBtn.addActionListener(e -> new AccountDetailsFrame().setVisible(true));
        calendarBtn.addActionListener(e -> new CalendarTrackerFrame(calendarService).setVisible(true));

        setContentPane(grid);
    }
}
