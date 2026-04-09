package dk.easv.eventtickets.DAL.DAO;

import dk.easv.eventtickets.BE.EventUser;
import dk.easv.eventtickets.DAL.IUserEventDataAccess;
import dk.easv.eventtickets.DAL.DB.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEventDAO implements IUserEventDataAccess {

    private final DBConnector dbConnector;

    public UserEventDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    @Override
    public List<EventUser> getCoordinatorsForEvent(int eventId) throws Exception {
        List<EventUser> list = new ArrayList<>();
        String sql = "SELECT * FROM UserEvent WHERE EventId = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new EventUser(
                        rs.getInt("Id"),
                        rs.getInt("UserId"),
                        rs.getInt("EventId")
                ));
            }
        }
        return list;
    }

    @Override
    public void assignCoordinator(int userId, int eventId) throws Exception {
        String sql = "INSERT INTO UserEvent (UserId, EventId) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.executeUpdate();
        }
    }

    @Override
    public void removeCoordinator(int userId, int eventId) throws Exception {
        String sql = "DELETE FROM UserEvent WHERE UserId = ? AND EventId = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.executeUpdate();
        }
    }
}