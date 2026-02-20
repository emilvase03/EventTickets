package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MFX imports
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditEventView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Unable to open SpecialTicketView");
            throw new RuntimeException(e);
        }
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
