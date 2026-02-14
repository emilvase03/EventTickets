package dk.easv.eventtickets.gui;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewTicketController {

    @FXML private MFXTextField txtfieldName;
    @FXML private MFXTextField txtfieldAmountTickets;

    @FXML
    private void handleConfirm(ActionEvent event) {

        // Add safety check for data type
        int ticketsAmount = Integer.parseInt(txtfieldAmountTickets.getText());
        System.out.println(ticketsAmount);

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
                System.out.println("tickets added: "+i+1);
            }


        } catch (Exception e) {
            // Show alert-box to user
            throw new RuntimeException("HandlePrintTickets() failed");
        }

    }
}
