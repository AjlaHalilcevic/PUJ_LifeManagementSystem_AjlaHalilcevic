package lifemanagement.ui.swing;

import financeapp.FinanceTrackerForm;

import javax.swing.*;
import java.awt.*;

public class FinanceTrackerFrame extends JFrame {
    public FinanceTrackerFrame() {
        setTitle("Finance Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        FinanceTrackerForm form = new FinanceTrackerForm();
        setContentPane(form.getMainPanel());

        getContentPane().setPreferredSize(new Dimension(800, 600));
        pack();
    }
}
