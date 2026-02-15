package dk.easv.eventtickets.gui;

// MFX imports
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class NewTicketController {

    @FXML private MFXTextField txtfieldName;
    @FXML private MFXTextField txtfieldAmountTickets;

    @FXML
    private void handleConfirm(ActionEvent event) {

        // Add safety check for data type
        int ticketsAmount = Integer.parseInt(txtfieldAmountTickets.getText());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            fxmlLoader.load();
            CardController controller = fxmlLoader.getController();
            controller.lanchTicketsWindow(ticketsAmount);

            handleClose();
        } catch (IOException e) {
            // Show user alert-box instead
            throw new RuntimeException(e);
        }

    }

    private void handleClose() {
        Stage stage = (Stage) txtfieldName.getScene().getWindow();
        stage.close();
    }
}
