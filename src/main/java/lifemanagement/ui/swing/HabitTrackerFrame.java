package lifemanagement.ui.swing;

import lifemanagement.auth.Session;
import lifemanagement.trackers.habit.HabitEntry;
import lifemanagement.trackers.habit.HabitService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class HabitTrackerFrame extends JFrame {
    private final HabitService habitService;
    private final JTable table = new JTable();

    public HabitTrackerFrame(HabitService habitService) {
        this.habitService = habitService;

        setTitle("Habit Tracker");
        setSize(800,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> addHabit());
        updateBtn.addActionListener(e -> updateHabit());
        deleteBtn.addActionListener(e -> deleteHabit());
        refreshBtn.addActionListener(e -> refreshTable());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(addBtn);
        top.add(updateBtn);
        top.add(deleteBtn);
        top.add(refreshBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }
    private void refreshTable() {
        List<HabitEntry> list = habitService.getForUser(Session.username);

        String[] cols = {"ID", "Name", "Frequency", "Active", "Created"};
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            HabitEntry h = list.get(i);
            data[i][0] = h.getId();
            data[i][1] = h.getName();
            data[i][2] = h.getFrequency();
            data[i][3] = h.isActive();
            data[i][4] = h.getCreatedAt();
        }
        table.setModel(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
    }
    private void addHabit() {
        JTextField nameField = new JTextField();
        JTextField freqField = new JTextField();
        JCheckBox activeBox = new JCheckBox("Active", true);

        Object[] msg = {
                "Name:", nameField,
                "Frequency (daily/weekly/...):", freqField,
                activeBox
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Add Habit", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String freq = freqField.getText().trim();
        boolean active = activeBox.isSelected();

        if (name.isEmpty() || freq.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name i Frequency ne smiju biti prazni.");
            return;
        }

        String id = UUID.randomUUID().toString().substring(0, 8);
        habitService.add(new HabitEntry(id, Session.username, name, freq, active, LocalDate.now()));
        refreshTable();
    }
    private void updateHabit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selektuj habit u tabeli.");
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        HabitEntry existing = habitService.findById(id, Session.username);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Habit nije pronađen.");
            return;
        }
        JTextField nameField = new JTextField(existing.getName());
        JTextField freqField = new JTextField(existing.getFrequency());
        JCheckBox activeBox = new JCheckBox("Active", existing.isActive());

        Object[] msg = {
                "Name:", nameField,
                "Frequency", freqField,
                activeBox
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Update Habit", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String newName = nameField.getText().trim();
        String newFreq = freqField.getText().trim();
        Boolean newActive = activeBox.isSelected();

        if (newName.isEmpty() || newFreq.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name i Frequency ne smiju biti prazni.");
            return;
        }
        habitService.update(id, Session.username, newName, newFreq, newActive);
        refreshTable();
    }

    private void deleteHabit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selektuj habit u tabeli.");
            return;
        }

        String id = table.getValueAt(row, 0).toString();

        int res = JOptionPane.showConfirmDialog(this, "Obrisati habit ID: " + id + "?", "Delete", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) return;

        boolean ok = habitService.deleteById(id, Session.username);
        if (!ok) JOptionPane.showMessageDialog(this, "Nije pronađen/nije obrisan.");
        refreshTable();
    }
}