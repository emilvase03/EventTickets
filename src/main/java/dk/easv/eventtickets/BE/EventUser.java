package dk.easv.eventtickets.BE;

public class EventUser {
    private int id = -1;
    private int userId = -1;
    private int eventId = -1;

    public EventUser(int id, int userId, int eventId) {
        setId(id);
        setUserId(userId);
        setEventId(eventId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id != -1)
            this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId != -1)
            this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        if (eventId != -1)
            this.eventId = eventId;
    }
}
