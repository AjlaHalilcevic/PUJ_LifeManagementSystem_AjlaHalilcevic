package lifemanagement.ui.swing;

import lifemanagement.auth.Session;
import lifemanagement.trackers.sleep.SleepEntry;
import lifemanagement.trackers.sleep.SleepService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.UUID;

public class SleepTrackerFrame extends JFrame {
    private final SleepService sleepService;

    private final DefaultListModel<SleepEntry>listModel = new DefaultListModel<>();
    private final JList<SleepEntry> list = new JList<>(listModel);

    private final JTextField dateField = new JTextField(10);
    private final JTextField hoursField = new JTextField(5);
    private final JCheckBox goodCheck = new JCheckBox("Kvalitetan san");

    public SleepTrackerFrame(SleepService sleepService) {
        this.sleepService = sleepService;

        setTitle("Sleep tracker:");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Datum (yyyy-mm-dd):"));
        form.add(dateField);

        form.add(new JLabel("Sati spavanja:"));
        form.add(hoursField);

        form.add(new JLabel("Status:"));
        form.add(goodCheck);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(addBtn);
        btns.add(updateBtn);
        btns.add(deleteBtn);
        btns.add(refreshBtn);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(btns,  BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addSleep());
        updateBtn.addActionListener(e -> updateSleep());
        deleteBtn.addActionListener(e -> deleteSleep());
        refreshBtn.addActionListener(e -> refreshList());

        refreshList();
    }
    private void refreshList() {
        listModel.clear();
        String username = Session.username;
        for (SleepEntry s : sleepService.getForUser(username)) {
            listModel.addElement(s);
        }
    }
    private void addSleep() {
        try {
            String username = Session.username;
            String id = UUID.randomUUID().toString().substring(0, 8);

            LocalDate date = LocalDate.parse(dateField.getText().trim());
            double hours = Double.parseDouble(hoursField.getText().trim());
            boolean good = goodCheck.isSelected();

            SleepEntry entry = new SleepEntry(id, username, date, hours, good);
            sleepService.add(entry);

            JOptionPane.showMessageDialog(this, "Sačuvano!. ID: " + id);
            refreshList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos. Provjerite datum (yyyy-mm-dd) i sate (npr. 7.5)" + ex.getMessage());
        }
    }
    private void updateSleep() {
        SleepEntry selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
            return;
        }
        try {
            String id = selected.getId();
            String username = Session.username;

            LocalDate newDate = dateField.getText().trim().isEmpty() ? null : LocalDate.parse(dateField.getText().trim());
            Double newHours = hoursField.getText().trim().isEmpty() ? null : Double.parseDouble(hoursField.getText().trim());
            Boolean newGood = goodCheck.isSelected();

            boolean ok = sleepService.update(id, username, newDate, newHours, newGood);
            JOptionPane.showMessageDialog(this, ok ? "Ažurirano!" : "Ažuriranje nije uspjelo.");
            refreshList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Neispravan unos. Provjerite datum i sate." + ex.getMessage());
        }
    }
    private void deleteSleep() {
        SleepEntry selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Prvo odaberite unos iz liste.");
            return;
        }
        boolean ok = sleepService.deleteById(selected.getId(), Session.username);
        JOptionPane.showMessageDialog(this, ok ? "Obrisano!" : "Nije pronađeno.");
        refreshList();
    }
}
