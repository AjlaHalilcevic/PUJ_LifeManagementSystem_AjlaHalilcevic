package lifemanagement.ui.swing;

import lifemanagement.auth.UserManager;
import lifemanagement.trackers.calendar.CalendarService;
import lifemanagement.trackers.habit.HabitService;
import lifemanagement.trackers.sleep.SleepService;
import lifemanagement.trackers.study.StudyService;

import javax.swing.*;
import java.awt.*;

public class AuthFrame extends JFrame {
    private final UserManager userManager = new UserManager();
    private final HabitService habitService = new HabitService();
    private final SleepService sleepService = new SleepService();
    private final StudyService studyService = new StudyService();
    private final CalendarService calendarService = new CalendarService();

    private final JTextField usernameField = new JTextField(16);
    private final JPasswordField passwordField = new JPasswordField(16);
    private final JComboBox<String> themeCombo = new JComboBox<>(new String[]{
            "default",
            "dark",
            "light"
    });
    public AuthFrame() {
        setTitle("Life Management System - Login/Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Login / Register", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel("Theme:"));
        form.add(themeCombo);

        root.add(form, BorderLayout.CENTER);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(loginBtn);
        buttons.add(registerBtn);

        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> doRegister());
    }
    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }
        boolean ok = userManager.login(username, password);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new MainMenuFrame(habitService, sleepService, studyService, calendarService).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Wrong username/password.");
        }
    }
    private void doRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String theme = (String) themeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }
            boolean ok = userManager.register(username, password, theme);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Registered successfully. Now login.");
            } else {
            JOptionPane.showMessageDialog(this, "User already exists.");
        }
    }
}
