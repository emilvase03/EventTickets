package dk.easv.eventtickets.BE;

// Java imports
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int id = -1;
    private int createdByUserId = -1;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String street;
    private String zipCode;
    private String locationGuidance;
    private String eventDescription;
    private int totalTickets = -1;
    private int ticketsIssued;

    public Event(int createdByUserId, String title, int totalTickets, int ticketsIssued, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String street, String zipCode, String locationGuidance, String eventDescription) {
        setCreatedByUserId(createdByUserId);
        setTitle(title);
        setTotalTickets(totalTickets);
        setTicketsIssued(ticketsIssued);
        setStartDate(startDate);
        setStartTime(startTime);
        setEndDate(endDate);
        setEndTime(endTime);
        setStreet(street);
        setZipCode(zipCode);
        setLocationGuidance(locationGuidance);
        setEventDescription(eventDescription);
    }

    public Event(int id, int createdByUserId, String title, int totalTickets, int ticketsIssued, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String street, String zipCode, String locationGuidance, String eventDescription) {
        setId(id);
        setCreatedByUserId(createdByUserId);
        setTitle(title);
        setTotalTickets(totalTickets);
        setTicketsIssued(ticketsIssued);
        setStartDate(startDate);
        setStartTime(startTime);
        setEndDate(endDate);
        setEndTime(endTime);
        setStreet(street);
        setZipCode(zipCode);
        setLocationGuidance(locationGuidance);
        setEventDescription(eventDescription);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id != -1)
            this.id = id;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    private void setCreatedByUserId(int createdByUserId) {
        if (createdByUserId != -1)
            this.createdByUserId = createdByUserId;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        if (!title.isBlank())
            this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    private void setStartDate(LocalDate startDate) {
        if (startDate != null)
            this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    private void setStartTime(LocalTime startTime) {
        if (startTime != null)
            this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    private void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) {
        if (!street.isBlank())
            this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    private void setZipCode(String zipCode) {
        if (!zipCode.isBlank())
            this.zipCode = zipCode;
    }

    public String getLocationGuidance() {
        return locationGuidance;
    }

    private void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    private void setEventDescription(String eventDescription) {
        if (!eventDescription.isBlank())
            this.eventDescription = eventDescription;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    private void setTotalTickets(int totalTickets) {
        if (totalTickets != -1)
            this.totalTickets = totalTickets;
    }

    public int getTicketsIssued() {
        return ticketsIssued;
    }

    private void setTicketsIssued(int ticketsIssued) {
            this.ticketsIssued = ticketsIssued;
    }
}
