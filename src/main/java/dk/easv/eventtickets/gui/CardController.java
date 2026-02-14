package dk.easv.eventtickets.gui;

import io.github.palexdev.mfxcore.controls.Label;
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
        /*
         *
         * Open Tickets (update?) Window --- Does it need to be editable? - Is this method needed?
         *
         */
    }

    @FXML
    private void handleTickets(ActionEvent event) {
        /*
        *
        * Open Tickets Window
        *
        */

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/NewTicketView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            // Show alert-box to user
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
