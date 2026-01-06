package lifemanagement.ui.swing;

import lifemanagement.account.AccountDetails;
import lifemanagement.account.AccountDetailsService;
import lifemanagement.auth.Session;

import javax.swing.*;
import java.awt.*;

public class AccountDetailsFrame extends JFrame {
    private final AccountDetailsService service = new AccountDetailsService();

    private final JTextField fullNameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField ageField = new JTextField(5);
    private final JTextField heightField = new JTextField(5);
    private final JTextField weightField = new JTextField(5);
    private final JTextArea goalArea = new JTextArea(4, 20);

    public AccountDetailsFrame() {
        setTitle("Account Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 420);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.add(new JLabel("Puno ime i prezime:"));
        form.add(fullNameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Visicna (cm):"));
        form.add(heightField);
        form.add(new JLabel("Težina (kg):"));
        form.add(weightField);
        form.add(new JLabel("Cilj:"));
        form.add(new JScrollPane(goalArea));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loadBtn = new JButton("Load");
        JButton saveBtn = new JButton("Save");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(loadBtn);
        buttons.add(saveBtn);
        buttons.add(deleteBtn);

        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);

        loadBtn.addActionListener(e -> load());
        saveBtn.addActionListener(e -> save());
        deleteBtn.addActionListener(e -> delete());

        load();
    }

    private void load() {
        if (Session.username == null) {
            JOptionPane.showMessageDialog(this, "Niste logovani.");
            return;
        }
        AccountDetails d = service.getByUsername(Session.username);
        if (d == null) {
            fullNameField.setText("");
            emailField.setText("");
            ageField.setText("");
            heightField.setText("");
            weightField.setText("");
            goalArea.setText("");
            return;
        }
        fullNameField.setText(nullToEmpty(d.getFullName()));
        emailField.setText(nullToEmpty(d.getEmail()));
        ageField.setText(intToEmpty(d.getAge()));
        heightField.setText(intToEmpty(d.getHeightCm()));
        weightField.setText(intToEmpty(d.getWeightKg()));
        goalArea.setText(nullToEmpty(d.getGoal()));
    }

    private void save() {
        if (Session.username == null) {
            JOptionPane.showMessageDialog(this, "Niste logovani.");
            return;
        }
        AccountDetails d = new AccountDetails(
                Session.username,
                fullNameField.getText().trim(),
                emailField.getText().trim(),
                parseIntOrNull(ageField.getText()),
                parseIntOrNull(heightField.getText()),
                parseIntOrNull(weightField.getText()),
                goalArea.getText().trim()
        );
        service.upsert(d);
        JOptionPane.showMessageDialog(this, "Snimljeno!");
    }

    private void delete() {
        if (Session.username == null) {
            JOptionPane.showMessageDialog(this, "Niste logovani.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Da li želite obrisati svoje profilne detalje?", "Potvrdi",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        boolean deleted = service.deleteByUsername(Session.username);
        JOptionPane.showMessageDialog(this, deleted ? "Obrisano!" : "Nema ništa za obrisati.");
        load();
    }
    private static Integer parseIntOrNull(String s) {
        try {
            s = s.trim();
            if (s.isEmpty()) return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }
    private static String nullToEmpty(String s) {return s == null ? "" : s;}
    private static String intToEmpty(Integer i) {return i == null ? "" : String.valueOf(i);}
}
