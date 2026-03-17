package dk.easv.eventtickets.DAL;

import dk.easv.eventtickets.BE.Event;

import java.util.List;

public interface IEventDataAccess {

    public List<Event> getAllEvents() throws Exception;

    public Event createEvent(Event newEvent) throws Exception;

    public void updateEvent(Event event) throws Exception;

    public void deleteEvent(Event event) throws Exception;

}
