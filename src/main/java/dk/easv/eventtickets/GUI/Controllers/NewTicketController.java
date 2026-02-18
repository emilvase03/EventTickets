package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.Validator;

// Java imports
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewTicketController implements Initializable {
    @FXML private MFXTextField txtTitle;
    @FXML private MFXTextField txtCustomerName;
    @FXML private MFXComboBox comboBoxEvent;
    @FXML private MFXComboBox comboBoxDiscount;
    @FXML private MFXTextField txtAmountTickets;
    @FXML private MFXToggleButton toggleSpecialTicket;

    private int ticketsAmount;
    ObservableList<String> discountOptions = FXCollections.observableArrayList(
            "10%", "15%", "20%", "25%", "30%", "35%", "40%", "45%", "50%", "Free"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxEvent.getItems().add("No Event");

        comboBoxDiscount.setItems(discountOptions);


        txtTitle.managedProperty().bind(txtTitle.visibleProperty());
        txtCustomerName.managedProperty().bind(txtCustomerName.visibleProperty());
        comboBoxDiscount.managedProperty().bind(comboBoxDiscount.visibleProperty());

        updateTicketState(toggleSpecialTicket.isSelected());

        toggleSpecialTicket.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateTicketState(newVal);
        });

        Validator.numeric(txtAmountTickets, null, 3);
        Validator.nonNumeric(txtTitle);
        Validator.nonNumeric(txtCustomerName);
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        if (comboBoxEvent.getSelectionModel().getSelectedItem() == null) {
            AlertHelper.showError("Error", "Please choose an event");
            return;
        }

        if (txtAmountTickets.getText().isBlank()) {
            AlertHelper.showError("Error", "Please enter number of tickets");
            return;
        }
        ticketsAmount = Integer.parseInt(txtAmountTickets.getText());

        if (toggleSpecialTicket.isSelected()) {
            handleSpecialTicket();
        }
        else {
            handleNormalTicket();
        }

    }

    private void printTickets(int ticketsAmount, boolean specialTicket) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CoordDashboardView.fxml"));
            fxmlLoader.load();
            CoordDashboardController controller = fxmlLoader.getController();
            controller.launchTicketsWindow(ticketsAmount, specialTicket);

            handleClose();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Confirmation error - could not create tickets.");
        }
    }

    private void handleNormalTicket() {
        if (txtCustomerName.getText().isBlank()) {
            AlertHelper.showError("Error", "Please enter customer name");
            return;
        }
        printTickets(ticketsAmount, false);

    }

    private void handleSpecialTicket() {
        if (txtTitle.getText().isBlank()) {
            AlertHelper.showError("Error", "Please enter ticket title");
            return;
        }

        if (comboBoxDiscount.getSelectionModel().getSelectedItem() == null) {
            AlertHelper.showError("Error", "Please choose an discount");
            return;
        }

        printTickets(ticketsAmount, true);
    }

    private void updateTicketState(boolean isSelected) {
        if (isSelected) {
            txtTitle.setVisible(true);
            txtCustomerName.setVisible(false);
            comboBoxDiscount.setVisible(true);
        } else {
            txtTitle.setVisible(false);
            txtCustomerName.setVisible(true);
            comboBoxDiscount.setVisible(false);
        }
    }

    private void handleClose() {
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }


}
