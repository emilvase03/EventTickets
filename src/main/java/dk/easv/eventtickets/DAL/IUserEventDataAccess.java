package dk.easv.eventtickets.DAL;

import dk.easv.eventtickets.BE.EventUser;

import java.util.List;

public interface IUserEventDataAccess {
    List<EventUser> getCoordinatorsForEvent(int eventId) throws Exception;
    void assignCoordinator(int userId, int eventId) throws Exception;
    void removeCoordinator(int userId, int eventId) throws Exception;
}