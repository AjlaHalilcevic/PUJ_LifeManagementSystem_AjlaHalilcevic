package lifemanagement.ui.swing;

import lifemanagement.trackers.habit.HabitService;
import lifemanagement.trackers.sleep.SleepService;
import lifemanagement.ui.swing.SleepTrackerFrame;
import lifemanagement.ui.swing.HabitTrackerFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private final HabitService habitService;
    private final SleepService sleepService;

    public MainMenuFrame(HabitService habitService, SleepService sleepService) {
        this.habitService = habitService;
        this.sleepService = sleepService;

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

        // TODO:
        financeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Finance UI (Project 1 integration)"));
        sleepBtn.addActionListener(e -> new SleepTrackerFrame(sleepService).setVisible(true));
        studyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Study Tracker UI"));
        habitBtn.addActionListener(e -> new HabitTrackerFrame(habitService).setVisible(true));
        accountBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Account Details UI"));
        calendarBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open Calendar Tracker UI"));

        setContentPane(grid);
    }
}
