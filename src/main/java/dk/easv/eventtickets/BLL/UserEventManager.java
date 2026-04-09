package dk.easv.eventtickets.BLL;

import dk.easv.eventtickets.BE.EventUser;
import dk.easv.eventtickets.DAL.DAO.UserEventDAO;
import dk.easv.eventtickets.DAL.IUserEventDataAccess;

import java.util.List;

public class UserEventManager {

    private final IUserEventDataAccess userEventDAO;

    public UserEventManager() throws Exception {
        userEventDAO = new UserEventDAO();
    }

    public List<EventUser> getCoordinatorsForEvent(int eventId) throws Exception {
        return userEventDAO.getCoordinatorsForEvent(eventId);
    }

    public void assignCoordinator(int userId, int eventId) throws Exception {
        userEventDAO.assignCoordinator(userId, eventId);
    }

    public void removeCoordinator(int userId, int eventId) throws Exception {
        userEventDAO.removeCoordinator(userId, eventId);
    }
}