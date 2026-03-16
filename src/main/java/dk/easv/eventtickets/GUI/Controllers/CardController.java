package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MFX imports
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CardController {

    @FXML private Label lblTitle;
    @FXML private Label lblStartDateAndTime;
    @FXML private Label lblEndDateAndTime;
    @FXML private Label lblAddress;
    @FXML private Label lblDescription;
    @FXML private Label lblTicketsIssuedNum;

    public VBox createEvent(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
        VBox root = loader.load();
        CardController controller  = loader.getController();
        controller.setData(event);

        return root;
    }

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
            AlertHelper.showError("Error", "Unable to open EditEventView");
            throw new RuntimeException(e);
        }
    }

    private void setData(Event event) {
        lblTitle.setText(event.getTitle());
        lblStartDateAndTime.setText(event.getStartTime().toString()+", "+event.getStartDate().toString());

        String endDateTime = (event.getEndTime() != null && event.getEndDate() != null)
                ? event.getEndTime().toString() + ", " + event.getEndDate().toString()
                : "";

        lblEndDateAndTime.setText(endDateTime);
        lblAddress.setText(event.getStreet()+", "+event.getZipCode());
        lblDescription.setText(event.getEventDescription());
        lblTicketsIssuedNum.setText(event.getTicketsIssued()+"/"+ event.getTotalTickets());
    }

}
