package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MFX imports
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CardController {

    private EventModel eventModel;
    private Event currentEvent;
    private CardController cardController;

    @FXML private VBox card;
    @FXML private Label lblTitle;
    @FXML private Label lblStartDateAndTime;
    @FXML private Label lblEndDateAndTime;
    @FXML private Label lblAddress;
    @FXML private Label lblDescription;
    @FXML private Label lblTicketsIssuedNum;
    @FXML private MFXProgressBar ticketBar;

    public CardController() {
        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "EventModel failed in CardController.");
        }
    }

    public VBox createEventCard(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
        VBox root = loader.load();
        cardController  = loader.getController();
        cardController.setData(event);
        cardController.currentEvent = event;

        return root;
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        event.consume(); // stops the click from bubbling to the VBox
        if (AlertHelper.showConfirmation("Are you sure?", "Are you sure you want to delete this event?")) {
            try {
                eventModel.deleteEvent(currentEvent);
                CoordDashboardController.getInstance().getEventContainer().getChildren().remove(card);

            } catch (Exception e) {
                AlertHelper.showError("Error", "Failed to delete event.");
            }
        }
    }

    private void setData(Event event) {
        lblTitle.setText(event.getTitle());
        lblStartDateAndTime.setText(event.getStartTime().toString() + ", " + event.getStartDate().toString());

        String endDateTime = (event.getEndTime() != null && event.getEndDate() != null)
                ? event.getEndTime().toString() + ", " + event.getEndDate().toString()
                : "";

        lblEndDateAndTime.setText(endDateTime);
        lblAddress.setText(event.getStreet() + ", " + event.getZipCode());
        lblDescription.setText(event.getEventDescription());

        int issued = event.getTicketsIssued();
        int total  = event.getTotalTickets();
        lblTicketsIssuedNum.setText(issued + "/" + total);

        if (total > 0) {
            double progress = (double) issued / total;
            ticketBar.setProgress(progress);

            ticketBar.getStyleClass().removeAll("bar-green", "bar-yellow", "bar-red");

            if (progress < 0.5)
                ticketBar.getStyleClass().add("bar-green");
            else if (progress < 0.8)
                ticketBar.getStyleClass().add("bar-yellow");
            else
                ticketBar.getStyleClass().add("bar-red");
        } else {
            ticketBar.setProgress(0);
            ticketBar.getStyleClass().removeAll("bar-green", "bar-yellow", "bar-red");
            ticketBar.getStyleClass().add("bar-green");
        }
    }

    @FXML
    private void onEditEventClick(MouseEvent mouseEvent) {
        try {
            ViewHandler.EDIT_EVENT.reset();
            ViewHandler.EDIT_EVENT.preLoad();

            EditEventController controller = ViewHandler.EDIT_EVENT.getController();
            controller.init(eventModel, currentEvent);

            ViewHandler.EDIT_EVENT.showAndWait(false);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Unable to open EditEventView.");
        }
    }
}
