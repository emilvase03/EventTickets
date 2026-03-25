package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.GUI.Models.EventModel;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EditEventController {
    private EventModel eventModel;
    private Event eventToEdit;

    @FXML
    private MFXTextField txtName;
    @FXML
    private MFXDatePicker txtStartDate;
    @FXML
    private MFXTextField txtStartTime;
    @FXML
    private MFXDatePicker txtEndDate;
    @FXML
    private MFXTextField txtEndTime;
    @FXML
    private MFXTextField txtStreet;
    @FXML
    private MFXTextField txtPostalCode;
    @FXML
    private MFXTextField txtLocationGuidance;
    @FXML
    private MFXTextField txtNotes;
    @FXML
    private Label editEventSubtitle;

    public void init(EventModel eventModel, Event event) {
        this.eventModel  = eventModel;
        this.eventToEdit = event;

        editEventSubtitle.setText("Edit information for " + event.getTitle());

        txtName.setText(event.getTitle());
        txtStartDate.setText(String.valueOf(event.getStartDate()));
        txtStartTime.setText(String.valueOf(event.getStartTime()));
        txtEndDate.setText(event.getEndDate() != null ? event.getEndDate().toString() : "");
        txtEndTime.setText(event.getEndTime() != null ? event.getEndTime().toString() : "");
        txtStreet.setText(event.getStreet());
        txtPostalCode.setText(event.getZipCode());
        txtLocationGuidance.setText(event.getLocationGuidance());
        txtNotes.setText(event.getEventDescription());

    }
    @FXML
    private void handleEditEvent(ActionEvent actionEvent) {
        // handle the edit logic later here and send feedback to the card view or the coordview
        // we should figure out a way to avoid making the components handle too much data.
    }
}
