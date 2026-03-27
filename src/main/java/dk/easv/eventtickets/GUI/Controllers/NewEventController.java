package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Controllers.Components.CardController;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.Validator;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class NewEventController implements Initializable {

    private EventModel eventModel;

    @FXML private MFXTextField txtName;
    @FXML private MFXTextField txtTickets;
    @FXML private MFXDatePicker txtStartDate;
    @FXML private MFXDatePicker txtEndDate;
    @FXML private MFXTextField txtStartTime;
    @FXML private MFXTextField txtEndTime;
    @FXML private MFXTextField txtStreet;
    @FXML private MFXTextField txtPostalCode;
    @FXML private MFXTextField txtLocationGuidance;
    @FXML private MFXTextField txtDescription;

    public NewEventController() {
        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "EventModel failed");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Validator.numeric(txtTickets, null, 4);
        Validator.numeric(txtPostalCode, null, 10);
    }

    @FXML
    private void handleConfirmEvent(ActionEvent event) {
        try {
            if (
                txtName.getText().isBlank() ||
                txtTickets.getText().isBlank() ||
                txtStartDate.getText().isBlank() ||
                txtStartTime.getText().isBlank() ||
                txtStreet.getText().isBlank() ||
                txtPostalCode.getText().isBlank() ||
                txtDescription.getText().isBlank()
            ) {
                AlertHelper.showError("Error", "Please fill out all required fields.");
                return;
            }

            String title = txtName.getText().trim();
            int tickets = Integer.parseInt(txtTickets.getText().trim());

            LocalDate startDate = txtStartDate.getValue();

            String startTimeInput = txtStartTime.getText().trim();
            String[] startTimeParts = startTimeInput.split(":");
            int startHour = Integer.parseInt(startTimeParts[0]);
            int startMin = Integer.parseInt(startTimeParts[1]);
            LocalTime startTime = LocalTime.of(startHour, startMin);

            LocalDate endDate = txtEndDate.getValue();

            LocalTime endTime = null;
            if (!txtEndTime.getText().isBlank()) {
                String[] endTimeParts = txtEndTime.getText().trim().split(":");
                int endHour = Integer.parseInt(endTimeParts[0]);
                int endMin = Integer.parseInt(endTimeParts[1]);
                endTime = LocalTime.of(endHour, endMin);
            }

            String street = txtStreet.getText().trim();
            String zipCode = txtPostalCode.getText().trim();
            String locGuidance = txtLocationGuidance.getText().isBlank() ? "" : txtLocationGuidance.getText().trim();
            String description = txtDescription.getText().trim();

            int userId = UserSession.getInstance().getCurrentUser().getId();

            Event newEvent = new Event(userId, title, tickets, 0, startDate, startTime, endDate, endTime, street, zipCode, locGuidance, description);
            eventModel.createEvent(newEvent);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            loader.load();
            CardController cardController = loader.getController();

            CoordDashboardController.getInstance().getEventContainer().getChildren().add(cardController.createEventCard(newEvent));

            handleClose();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to create new event. " +e.getMessage());
        }
    }

    private void handleClose() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
