package dk.easv.eventtickets.DAL.DAO;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.DAL.DB.DBConnector;
import dk.easv.eventtickets.DAL.IEventDataAccess;

// Java imports
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements IEventDataAccess {

    private DBConnector databaseConnector;

    public EventDAO() throws Exception {
        databaseConnector = new DBConnector();
    }

    @Override
    public List<Event> getAllEvents() throws Exception {
        ArrayList<Event> allEvents = new ArrayList<>();

        String sql = "SELECT * FROM Event;";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                int totalTickets = rs.getInt("TotalTickets");
                int ticketsIssued = rs.getInt("TicketsIssued");
                LocalDate startDate = rs.getDate("StartDate").toLocalDate();
                LocalTime startTime = rs.getTime("StartTime").toLocalTime();
                LocalDate endDate = rs.getDate("EndDate") != null ? rs.getDate("EndDate").toLocalDate() : null;
                LocalTime endTime = rs.getTime("EndTime") != null ? rs.getTime("EndTime").toLocalTime() : null;
                String street = rs.getString("Street");
                String zipCode = rs.getString("ZipCode");
                String locationGuidance = rs.getString("LocationGuidance");
                String description = rs.getString("Description");
                int userId = rs.getInt("UserId");

                Event event = new Event(id, userId, title, totalTickets, ticketsIssued, startDate, startTime, endDate, endTime, street, zipCode, locationGuidance, description);
                allEvents.add(event);
            }
            return allEvents;
        }
    }

    @Override
    public List<Event> getMyEvents(User currentUser) throws Exception {
        ArrayList<Event> allEvents = new ArrayList<>();

        String sql = "SELECT * FROM Event WHERE UserId=?;";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentUser.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                int totalTickets = rs.getInt("TotalTickets");
                int ticketsIssued = rs.getInt("TicketsIssued");
                LocalDate startDate = rs.getDate("StartDate").toLocalDate();
                LocalTime startTime = rs.getTime("StartTime").toLocalTime();
                LocalDate endDate = rs.getDate("EndDate") != null ? rs.getDate("EndDate").toLocalDate() : null;
                LocalTime endTime = rs.getTime("EndTime") != null ? rs.getTime("EndTime").toLocalTime() : null;
                String street = rs.getString("Street");
                String zipCode = rs.getString("ZipCode");
                String locationGuidance = rs.getString("LocationGuidance");
                String description = rs.getString("Description");
                int userId = rs.getInt("UserId");

                Event event = new Event(id, userId, title, totalTickets, ticketsIssued, startDate, startTime, endDate, endTime, street, zipCode, locationGuidance, description);
                allEvents.add(event);
            }
            return allEvents;
        }
    }

    @Override
    public Event createEvent(Event newEvent) throws Exception {
        String sql = "INSERT INTO Event (Title, StartDate, StartTime, EndDate, EndTime, Street, ZipCode, LocationGuidance, Description, TotalTickets, TicketsIssued, UserId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, newEvent.getTitle());
            stmt.setDate(2, Date.valueOf(newEvent.getStartDate()));
            stmt.setTime(3, Time.valueOf(newEvent.getStartTime()));
            stmt.setDate(4, newEvent.getEndDate() != null ? Date.valueOf(newEvent.getEndDate()) : null);
            stmt.setTime(5, newEvent.getEndTime() != null ? Time.valueOf(newEvent.getEndTime()) : null);
            stmt.setString(6, newEvent.getStreet());
            stmt.setString(7, newEvent.getZipCode());
            stmt.setString(8, newEvent.getLocationGuidance());
            stmt.setString(9, newEvent.getEventDescription());
            stmt.setInt(10, newEvent.getTotalTickets());
            stmt.setInt(11, newEvent.getTicketsIssued());
            stmt.setInt(12, newEvent.getUserId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            Event createdEvent = new Event(id, newEvent.getUserId(), newEvent.getTitle(), newEvent.getTotalTickets(), newEvent.getTicketsIssued(), newEvent.getStartDate(), newEvent.getStartTime(), newEvent.getEndDate(), newEvent.getEndTime(), newEvent.getStreet(), newEvent.getZipCode(), newEvent.getLocationGuidance(), newEvent.getEventDescription());
            return createdEvent;
        }
    }


    @Override
    public void updateEvent(Event event) throws Exception {
        String sql = "UPDATE Event (Title, StartDate, StartTime, EndDate, EndTime, Street, ZipCode, LocationGuidance, Description, TotalTickets, TicketsIssued) VALUES (?,?,?,?,?,?,?,?,?,?,?) WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getTitle());
            stmt.setDate(2, Date.valueOf(event.getStartDate()));
            stmt.setTime(3, Time.valueOf(event.getStartTime()));
            stmt.setDate(4, event.getEndDate() != null ? Date.valueOf(event.getEndDate()) : null);
            stmt.setTime(5, event.getEndTime() != null ? Time.valueOf(event.getEndTime()) : null);
            stmt.setString(6, event.getStreet());
            stmt.setString(7, event.getZipCode());
            stmt.setString(8, event.getLocationGuidance());
            stmt.setString(9, event.getEventDescription());
            stmt.setInt(10, event.getTotalTickets());
            stmt.setInt(11, event.getTicketsIssued());

            stmt.setInt(12, event.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteEvent(Event event) throws Exception {
        String sql = "DELETE FROM Event WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event.getId());

            stmt.executeUpdate();
        }

    }
}
