package lifemanagement.trackers.calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarService {
    private final List<CalendarEvent> events = new ArrayList<>();

    public void addEvent(CalendarEvent e) {
        events.add(e);
    }

    public List<CalendarEvent> getEventsForUser(String username) {
        List<CalendarEvent> result = new ArrayList<>();
        for (CalendarEvent e : events) {
            if (e.getUsername().equals(username)) {
                result.add(e);
            }
        }
        return result;
    }
    public CalendarEvent findById(String id) {
        for (CalendarEvent e : events) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
    public boolean deleteEvent(String id, String username) {
        CalendarEvent e = findById(id);
        if (e == null) return false;
        if (!e.getUsername().equals(username)) return false;
        return events.remove(e);
    }
}