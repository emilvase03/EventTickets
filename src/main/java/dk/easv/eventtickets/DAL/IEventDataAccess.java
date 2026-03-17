package dk.easv.eventtickets.DAL;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;

// Java imports
import java.util.List;

public interface IEventDataAccess {

    public List<Event> getAllEvents() throws Exception;

    public List<Event> getMyEvents(User currentUser) throws Exception;

    public Event createEvent(Event newEvent) throws Exception;

    public void updateEvent(Event event) throws Exception;

    public void deleteEvent(Event event) throws Exception;
}
