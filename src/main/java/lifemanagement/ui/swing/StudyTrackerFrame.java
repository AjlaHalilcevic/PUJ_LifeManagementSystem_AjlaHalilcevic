package lifemanagement.ui.swing;

import lifemanagement.auth.Session;
import lifemanagement.trackers.study.StudyRecord;
import lifemanagement.trackers.study.StudyService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public class StudyTrackerFrame extends JFrame {
    private final StudyService studyService;
    private final DefaultListModel<StudyRecord> model = new DefaultListModel<>();
    private final JList<StudyRecord> list = new JList<>(model);

    public StudyTrackerFrame(StudyService studyService) {
        this.studyService = studyService;

        setTitle("Study Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(list);
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        refreshBtn.addActionListener(e -> refresh());

        refresh();
    }
    private void refresh() {
        model.clear();
        List<StudyRecord> records = studyService.getForUser(Session.username);
        for (StudyRecord r : records) model.addElement(r);
    }
    private void onAdd() {
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JSpinner minutesSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 1440, 1));
        JTextField subjectField = new JTextField();
        JTextField notesField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Datum (yyyy-mm-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Minute (1-1440):"));
        panel.add(minutesSpinner);
        panel.add(new JLabel("Predmet:"));
        panel.add(subjectField);
        panel.add(new JLabel("Bilješke:"));
        panel.add(notesField);

        int ok = JOptionPane.showConfirmDialog(this, panel, "AddStudy record",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos. Provjerite datum(yyyy-mm-dd).");
            return;
        }
        int minutes = (int) minutesSpinner.getValue();
        String subject = subjectField.getText().trim();
        String notes = notesField.getText().trim();
        String id = UUID.randomUUID().toString().substring(0, 8);
        StudyRecord r = new StudyRecord(id, Session.username, date, minutes, subject.isBlank() ? null : subject,
                notes.isBlank() ? null : notes);
        studyService.add(r);
        refresh();
        JOptionPane.showMessageDialog(this, "Sačuvano! ID:" + id);
    }
    private void onUpdate() {
        StudyRecord selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
            return;
        }
        JTextField dateField = new JTextField(selected.getDate().toString());
        JSpinner minutesSpinner = new JSpinner(new SpinnerNumberModel(selected.getMinutes(), 1,  1440, 1));
        JTextField subjectField = new JTextField(selected.getSubject() == null ? "" : selected.getSubject());
        JTextField notesField = new JTextField(selected.getNotes() == null ? "" : selected.getNotes());

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Datum (yyyy-mm-dd): "));
        panel.add(dateField);
        panel.add(new JLabel("Minute (1-1440): "));
        panel.add(minutesSpinner);
        panel.add(new JLabel("Predmet: "));
        panel.add(subjectField);
        panel.add(new JLabel("Bilješke: "));
        panel.add(notesField);

        int ok = JOptionPane.showConfirmDialog(this, panel, "Update study record",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos. Provjerite datum(yyyy-mm-dd)");
            return;
        }
        int minutes = (int) minutesSpinner.getValue();
        String subject = subjectField.getText().trim();
        String notes = notesField.getText().trim();

        boolean updated = studyService.update(selected.getId(), Session.username, date, minutes,
                subject.isBlank() ? null : subject,
                notes.isBlank() ? null : notes
        );
        if (!updated) {
            JOptionPane.showMessageDialog(this, "Nije pronađeno.");
            return;
        }
        refresh();
        JOptionPane.showMessageDialog(this, "Ažurirano!");
    }

    private void onDelete() {
        StudyRecord selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Obrisano!" + selected.getId() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean deleted = studyService.deleteById(selected.getId(), Session.username);
        if (!deleted) {
            JOptionPane.showMessageDialog(this, "Nije pronađeno.");
            return;
        }
        refresh();
        JOptionPane.showMessageDialog(this, "Obrisano!");
    }
}
