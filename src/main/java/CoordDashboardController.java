
// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXScrollPane;

import java.io.IOException;

public class CoordDashboardController {

    @FXML private MFXScrollPane scrollPane;
    @FXML private FlowPane eventContainer;

    @FXML
    private void onCreateEvent(ActionEvent event) {

        /*
        *  Open 'create event'-window
        */

        try {
            // Load the Card.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            Parent cardRoot = loader.load();

            // Optionally, get the controller if you need to pass data
            CardController cardController = loader.getController();
            // cardController.setEventData(...);

            // Add the card to the FlowPane
            eventContainer.getChildren().add(cardRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
