package dk.easv.eventtickets.gui;

import io.github.palexdev.mfxcore.controls.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CardController {

    @FXML private Label lblTitle;
    @FXML private Label lblDateAndTime;
    @FXML private Label lblAddress;
    @FXML private Label lblDescription;
    @FXML private Label lblTicketsIssuedNum;

    @FXML
    private void handleDelete(ActionEvent event) {
        // Delete event
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        /*
         *
         * Open Tickets (update?) Window
         *
         */
    }

    @FXML
    private void handleTickets(ActionEvent event) {
        /*
        *
        * Open Tickets Window
        *
        */
    }

    private void setTitle(/*Event Object*/) {

    }

    private void setDateAndTime(/*Event Object ,Event Object*/) {

    }

    private void setAddress(/*Event Object*/) {

    }

    private void setDescription(/*Event Object*/) {

    }

    private void setTicketsIssued(/*Event Object*/) {

    }


}
