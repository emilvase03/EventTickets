package dk.easv.eventtickets.GUI.Controllers;

// MFX imports
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
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
         * Open Tickets (update?) Window --- Does it need to be editable? - Is this method needed?
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
