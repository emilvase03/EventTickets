package dk.easv.eventtickets.gui;

// MFX imports
import dk.easv.eventtickets.gui.utils.AlertHelper;
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
            AlertHelper.showError("Error", "Failed to open tickets dialog.");
        }
    }

    public void lanchTicketsWindow(int ticketsAmount) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrintTicketsView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            // for loop for tickets
            for (int i = 0; i < ticketsAmount; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Ticket.fxml"));
                Parent ticketRoot = loader.load();
                PrintTicketController controller = fxmlLoader.getController();
                controller.getTicketsContainer().getChildren().add(ticketRoot);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display tickets.");
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
