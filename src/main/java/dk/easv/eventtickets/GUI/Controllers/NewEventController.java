package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// Java imports
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewEventController {

    private CoordDashboardController dashboardController;

    @FXML private MFXTextField txtName;
    @FXML private MFXDatePicker txtStartDate;
    @FXML private MFXDatePicker txtEndDate;
    @FXML private MFXTextField txtStartTime;
    @FXML private MFXTextField txtEndTime;
    @FXML private MFXTextField txtLocation;
    @FXML private MFXTextField txtLocationGuidance;
    @FXML private MFXTextField txtNotes;

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
