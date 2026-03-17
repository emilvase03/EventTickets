package dk.easv.eventtickets.GUI.Models;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BLL.EventManager;

import java.util.List;

public class EventModel {
    EventManager eventManager;

    public EventModel() throws Exception {
        eventManager = new EventManager();
    }

    public List<Event> getAllEvents() throws Exception {
        return eventManager.getAllEvents();
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
