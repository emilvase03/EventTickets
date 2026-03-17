package dk.easv.eventtickets.BLL;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.DAL.DAO.EventDAO;
import dk.easv.eventtickets.DAL.IEventDataAccess;

import java.util.List;

public class EventManager {

    IEventDataAccess eventDAO;

    public EventManager() throws Exception {
        eventDAO = new EventDAO();
    }

    public List<Event> getAllEvents() throws Exception {
        return eventDAO.getAllEvents();
    }

    public Event createEvent(Event newEvent) throws Exception {
        return eventDAO.createEvent(newEvent);
    }

    public void updateEvent(Event event) throws Exception {
        eventDAO.updateEvent(event);
    }

    public void deleteEvent(Event event) throws Exception {
        eventDAO.deleteEvent(event);
    }

}
