package dk.easv.eventtickets.BE;

// Java imports
import java.time.LocalDate;
import java.time.LocalTime;

public record TicketData(
        boolean isSpecial,
        String optionText,
        String customerName,
        String customerEmail,
        String eventTitle,
        LocalDate startDate,
        LocalTime startTime,
        String address,
        String ticketTitle,
        String discount
) {}
