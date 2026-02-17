package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.TicketPrinter;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class PrintTicketController {

    @FXML private FlowPane ticketsContainer;

    @FXML
    private void handlePrint(ActionEvent event) {
        TicketPrinter.print(ticketsContainer);
    }

    public FlowPane getTicketsContainer() {
        return ticketsContainer;
    }
}
