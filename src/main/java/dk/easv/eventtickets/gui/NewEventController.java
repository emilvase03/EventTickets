package dk.easv.eventtickets.gui;

// Project imports
import dk.easv.eventtickets.GUI.Controllers.CoordDashboardController;
import dk.easv.eventtickets.GUI.Controllers.CardController;

// Java imports
import dk.easv.eventtickets.gui.Utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewEventController {

    private CoordDashboardController dashboardController;

    @FXML private TextField txtName;
    @FXML private DatePicker txtStartDate;
    @FXML private TextField txtStartTime;
    @FXML private DatePicker txtEndDate;
    @FXML private TextField txtEndTime;
    @FXML private TextField txtLocation;
    @FXML private TextField txtLocationGuidance;
    @FXML private TextField txtNotes;

    @FXML
    private void handleConfirmEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            Parent cardRoot = loader.load();
            dk.easv.eventtickets.GUI.Controllers.CardController cardController = loader.getController();

            dashboardController.getEventContainer().getChildren().add(cardRoot);

            handleClose();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to create new event. " +e.getMessage());
        }
    }

    public void setDashboardController(CoordDashboardController controller) {
        this.dashboardController = controller;
    }

    private void handleClose() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
