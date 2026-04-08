package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.GUI.Models.EventModel;

// MaterialFX imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalTime;

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
        try {
            if (
                txtName.getText().isBlank() ||
                txtStartDate.getText().isBlank() ||
                txtStartTime.getText().isBlank() ||
                txtStreet.getText().isBlank() ||
                txtPostalCode.getText().isBlank() ||
                txtNotes.getText().isBlank()
            ) {
                AlertHelper.showError("Error", "Please fill out all required fields.");
                return;
            }

            String title = txtName.getText().trim();

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
            String locGuidance = txtLocationGuidance.getText().isBlank()
                    ? ""
                    : txtLocationGuidance.getText().trim();
            String description = txtNotes.getText().trim();

            eventToEdit.setTitle(title);
            eventToEdit.setStartDate(startDate);
            eventToEdit.setStartTime(startTime);
            eventToEdit.setEndDate(endDate);
            eventToEdit.setEndTime(endTime);
            eventToEdit.setStreet(street);
            eventToEdit.setZipCode(zipCode);
            eventToEdit.setLocationGuidance(locGuidance);
            eventToEdit.setEventDescription(description);

            eventModel.updateEvent(eventToEdit);
            CoordDashboardController.getInstance().refreshEvents();
            ViewHandler.EDIT_EVENT.close();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to update event: " + e.getMessage());
            System.out.println("Failed to update event: " + e.getMessage());
        }
    }
}
