package dk.easv.eventtickets.GUI.Models;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.EventManager;
import dk.easv.eventtickets.GUI.Utils.BackgroundExecutor;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class EventModel {

    private final EventManager eventManager;
    private final ObservableList<Event> allEvents = FXCollections.observableArrayList();

    public EventModel() throws Exception {
        eventManager = new EventManager();
    }

    public void loadAllEvents() {
        BackgroundExecutor.execute(
                () -> eventManager.getAllEvents(),
                result -> allEvents.setAll(result),
                e -> { throw new RuntimeException("Failed to load events", e); },
                ignored -> {}
        );
    }

    public ObservableList<Event> getEvents() {
        return allEvents;
    }

    public List<Event> getMyEvents(User currentUser) throws Exception {
        return eventManager.getMyEvents(currentUser);
    }

    public Event createEvent(Event newEvent) throws Exception {
        return eventManager.createEvent(newEvent);
    }

    public void updateEvent(Event event) throws Exception {
        eventManager.updateEvent(event);
    }

    public void deleteEvent(Event event) throws Exception {
        eventManager.deleteEvent(event);
    }
}