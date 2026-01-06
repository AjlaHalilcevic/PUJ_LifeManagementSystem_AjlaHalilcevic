package lifemanagement.analytics;

import lifemanagement.auth.Session;
import lifemanagement.trackers.calendar.CalendarEvent;
import lifemanagement.trackers.habit.HabitEntry;
import lifemanagement.trackers.habit.HabitService;
import lifemanagement.trackers.sleep.SleepEntry;
import lifemanagement.trackers.study.StudyRecord;
import lifemanagement.trackers.study.StudyService;
import lifemanagement.trackers.sleep.SleepService;
import lifemanagement.trackers.calendar.CalendarService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class AnalyticsFrame extends JFrame {
    private final HabitService habitService;
    private final SleepService sleepService;
    private final StudyService studyService;
    private final CalendarService calendarService;

    private final JTextArea output = new JTextArea();

    public AnalyticsFrame(HabitService habitService, SleepService sleepService, StudyService studyService,
                          CalendarService calendarService) {
        this.habitService = habitService;
        this.sleepService = sleepService;
        this.studyService = studyService;
        this.calendarService = calendarService;

        setTitle("Analytics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        output.setEnabled(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton sleepBtn = new JButton("Sleep");
        JButton habitBtn = new JButton("Habit");
        JButton studyBtn = new JButton("Study");
        JButton calBtn = new JButton("Calendar");

        top.add(sleepBtn);
        top.add(habitBtn);
        top.add(studyBtn);
        top.add(calBtn);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        sleepBtn.addActionListener(e -> showSleep());
        habitBtn.addActionListener(e -> showHabit());
        studyBtn.addActionListener(e -> showStudy());
        calBtn.addActionListener(e -> showCalendar());

        output.setText("Izaberite module analytics. \nUser: " + Session.username);
    }
    private void showSleep() {
        String username = Session.username;
        List<SleepEntry> list = sleepService.getForUser(username);

        if (list.isEmpty()) {
            output.setText("SLEEP ANALYTICS\n\nNema unosa za korisnika:" + username);
            return;
        }
        double totalHours = 0.0;
        int goodCount = 0;
        LocalDate from7 = LocalDate.now().minusDays(6);
        double totalHours7 = 0.0;
        int count7 = 0;

        for (SleepEntry s : list) {
            totalHours += s.getHours();
            if (s.isGood()) goodCount++;

            if (s.getDate() != null && !s.getDate().isBefore(from7)) {
                totalHours7 += s.getHours();
                count7++;
            }
        }
        double avg = totalHours / list.size();
        double goodPct = (goodCount * 100.0) / list.size();
        double avg7 = count7 == 0 ? 0.0 : totalHours7 / count7;

        StringBuilder sb = new StringBuilder();
        sb.append("SLEEP ANALYTICS\n\n");
        sb.append("Korisnik:").append(username).append("\n");
        sb.append("Broj unosa:").append(list.size()).append("\n");
        sb.append(String.format(Locale.US, "Prosjek sati: %.2f\n", avg));
        sb.append(String.format(Locale.US, "Kvalitetni snovi: %d (%.1f%%)\n", goodCount, goodPct));
        sb.append(String.format(Locale.US, "Zadnjih 7 dana (prosjek): %.2f (unos: &d)\n", avg7, count7));
        output.setText(sb.toString());
    }

    private void showHabit() {
        String username = Session.username;
        List<HabitEntry> list = habitService.getForUser(username);

        if (list.isEmpty()) {
            output.setText("HABIT ANALYTICS\n\nNema unosa za korisnika:" + username);
            return;
        }
        int active = 0;
        Map<String, Integer> freqCount = new HashMap<>();

        for (HabitEntry h : list) {
            if (h.isActive()) active++;
            String freq = h.getFrequency();
            if (freq == null || freq.isBlank()) freq = "(no frequency)";
            freqCount.put(freq, freqCount.getOrDefault(freq, 0) + 1);
        }
        List<Map.Entry<String, Integer>> freqList = new ArrayList<>(freqCount.entrySet());
        freqList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        StringBuilder sb = new StringBuilder();
        sb.append("HABIT ANALYTICS\n\n");
        sb.append("Korisnik:").append(username).append("\n");
        sb.append("Ukupno navika:").append(list.size()).append("\n");
        sb.append("Aktivne:").append(active).append("\n");
        sb.append("Neaktivne:").append(list.size() - active).append("\n\n");
        sb.append("Raspodjela po frequency:\n");

        for (Map.Entry<String, Integer> e : freqList) {
            sb.append("-").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        output.setText(sb.toString());
    }

    private void showStudy() {
        String username = Session.username;
        List<StudyRecord> list = studyService.getForUser(username);

        if (list.isEmpty()) {
            output.setText("STUDY ANALYTICS");
            return;
        }
        int totalMin = 0;
        int totalMin7 = 0;
        LocalDate from7 = LocalDate.now().minusDays(6);

        Map<String, Integer> minutesBySubject = new HashMap<>();

        for (StudyRecord r : list) {
            totalMin += r.getMinutes();

            if (r.getDate() != null && !r.getDate().isBefore(from7)) {
                totalMin7 += r.getMinutes();
            }
            String subject = r.getSubject();
            if (subject == null || subject.isBlank()) subject = "(no subject)";
            minutesBySubject.put(subject, minutesBySubject.getOrDefault(subject, 0) + r.getMinutes());
        }
        List<Map.Entry<String, Integer>> top = new ArrayList<>(minutesBySubject.entrySet());
        top.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        StringBuilder sb = new StringBuilder();
        sb.append("STUDY ANALYTICS\n\n");
        sb.append("Korisnik:").append(username).append("\n");
        sb.append("Broj unosa:").append(list.size()).append("\n");
        sb.append("Ukupno minuta:").append(totalMin).append("\n");
        sb.append(String.format(Locale.US, "Prosjek po unosu: %.1f min\n", (double)totalMin / list.size()));
        sb.append("Zadnjih 7 dana ukupno:").append(totalMin7).append("min\n\n");
        sb.append("Top predmeti: \n");

        int limit = Math.min(3, top.size());
        for (int i = 0; i < limit; i++) {
            sb.append(" ").append(i  + 1).append(") ")
                    .append(top.get(i).getKey()).append("-")
                    .append(top.get(i).getValue()).append("min\n");
        }
        output.setText(sb.toString());
    }

    private void showCalendar() {
        String username = Session.username;
        List<CalendarEvent> list = calendarService.getEventsForUser(username);

        if (list.isEmpty()) {
            output.setText("CALENDAR ANALYTICS\n\nNema događaja za korisnika: " + username);
            return;
        }
        LocalDate today = LocalDate.now();
        int upcoming = 0;

        List<CalendarEvent> sorted = new ArrayList<>(list);
        sorted.sort((a, b) -> {
            LocalDate da = a.getDate();
            LocalDate db = b.getDate();
            if (da == null && db == null) return 0;
            if (da == null) return 1;
            if (db == null) return -1;

            int c = da.compareTo(db);
            if (c != 0) return c;

            if (a.getTime() == null && b.getTime() == null) return 0;
            if (a.getTime() == null) return 1;
            if (b.getTime() == null) return -1;
            return a.getTime().compareTo(b.getTime());
        });
        for (CalendarEvent e : list) {
            if (e.getDate() != null && !e.getDate().isBefore(today)) upcoming++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CALENDAR ANALYTICS\n\n");
        sb.append("Korisnik:").append(username).append("\n");
        sb.append("Ukupno događaja:").append(list.size()).append("\n");
        sb.append("Upcoming (od danas):").append(upcoming).append("\n\n");

        sb.append("Sljedećih 5 događaja:\n");
        int limit = Math.min(5, sorted.size());
        for (int i = 0; i < limit; i++) {
            sb.append("-").append(sorted.get(i).toString()).append("\n");
        }
        output.setText(sb.toString());
    }
}
