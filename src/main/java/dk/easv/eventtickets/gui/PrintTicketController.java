package dk.easv.eventtickets.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class PrintTicketController {

    @FXML private FlowPane ticketsContainer;

    @FXML
    private void handlePrint(ActionEvent event) {
    }

    public FlowPane getTicketsContainer() {
        return ticketsContainer;
    }
}
