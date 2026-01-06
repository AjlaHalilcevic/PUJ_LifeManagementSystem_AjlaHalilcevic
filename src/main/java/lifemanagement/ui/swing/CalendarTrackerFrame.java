package lifemanagement.ui.swing;

import lifemanagement.auth.Session;
import lifemanagement.trackers.calendar.CalendarEvent;
import lifemanagement.trackers.calendar.CalendarService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.List;

public class CalendarTrackerFrame extends JFrame {
    private final CalendarService calendarService;
    private final DefaultListModel<CalendarEvent> listModel = new DefaultListModel<>();
    private final JList<CalendarEvent> eventJList = new JList<>(listModel);

    private final JTextField titleField = new JTextField(15);
    private final JTextField dateField = new JTextField(10);
    private final JTextField timeField = new JTextField(5);
    private final JTextField descriptionField = new JTextField(15);

    public CalendarTrackerFrame(CalendarService calendarService) {
        this.calendarService = calendarService;

        setTitle("Calendar Tracker");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        eventJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(eventJList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Detalji o eventu"));

        form.add(new JLabel("Naslov:"));
        form.add(titleField);
        form.add(new JLabel("Datum (yyyy-mm-dd):"));
        form.add(dateField);
        form.add(new JLabel("Vrijem (HH:MM):"));
        form.add(timeField);
        form.add(new JLabel("Opis:"));
        form.add(descriptionField);

        add(form, BorderLayout.NORTH);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);

        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addEvent());
        updateBtn.addActionListener(e -> updateEvent());
        deleteBtn.addActionListener(e -> deleteEvent());

        loadEvents();
    }
    private void loadEvents() {
        listModel.clear();
        List<CalendarEvent> events = calendarService.getEventsForUser(Session.username);
        for (CalendarEvent e : events) {
            listModel.addElement(e);
        }
    }
    private void addEvent() {
        try {
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String title = titleField.getText().trim();
            String desc = descriptionField.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Potrebno je unijeti naslov.");
                return;
            }
            CalendarEvent ev = new CalendarEvent(UUID.randomUUID().toString().substring(0, 8),
                    Session.username, title, date, time, desc);
            calendarService.addEvent(ev);
            loadEvents();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos.");
        }
    }
    private void updateEvent() {
            CalendarEvent selected = eventJList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
                return;
            }
            JTextField timeField = new JTextField(selected.getTime().toString());
            JTextField dateField = new JTextField(selected.getDate().toString());
            JTextField titleField = new JTextField(selected.getTitle());
            JTextField descField = new JTextField(selected.getDescription());

            Object[] fields = {
                    "Datum (yyyy-mm-dd): ", dateField,
                    "Vrijeme (HH:MM): ", timeField,
                    "Naslov: ", titleField,
                    "Opis:", descField
            };
            int res = JOptionPane.showConfirmDialog(this, fields, "Ažuriraj event.",
                    JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;
            try {
                selected.setTime(LocalTime.parse(timeField.getText().trim()));
                selected.setDate(LocalDate.parse(dateField.getText().trim()));
                selected.setTitle(titleField.getText().trim());
                selected.setDescription(descField.getText().trim());

                loadEvents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Neispravan unos.");
            }
    }
    private void deleteEvent() {
        CalendarEvent selected = eventJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
            return;
        }

        int res = JOptionPane.showConfirmDialog(this, "Da li želite obrisati odabran event?", "Potvrdi",
                JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            calendarService.deleteEvent(selected.getId(), Session.username);
            loadEvents();
        }
    }
    private void clearForm() {
        titleField.setText("");
        dateField.setText("");
        timeField.setText("");
        descriptionField.setText("");
    }
}
