package dk.easv.eventtickets.GUI.Controllers;

// MFX imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewTicketController implements Initializable {

    @FXML private TextField txtfieldName;
    @FXML private TextField txtfieldAmountTickets;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtfieldName.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-ZæøåÆØÅ ]*"))
                return change;
            return null;
        }));

        txtfieldAmountTickets.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,3}"))
                return change;
            return null;
        }));
    }

    @FXML
    private void handleConfirm(ActionEvent event) {

        if (txtfieldName.getText().isBlank()) {
            AlertHelper.showError("Error", "Please enter customer name");
            return;
        }

        if (txtfieldAmountTickets.getText().isBlank()) {
            AlertHelper.showError("Error", "Please enter number of tickets");
            return;
        }

        int ticketsAmount = Integer.parseInt(txtfieldAmountTickets.getText());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            fxmlLoader.load();
            CardController controller = fxmlLoader.getController();
            controller.lanchTicketsWindow(ticketsAmount);

            handleClose();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Confirmation error - could not create tickets.");
        }

    }

    private void handleClose() {
        Stage stage = (Stage) txtfieldName.getScene().getWindow();
        stage.close();
    }
}
