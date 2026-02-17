package dk.easv.eventtickets.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private VBox allCoordBox;

    @FXML
    private void onBtnAddCoord(ActionEvent actionEvent) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NewCoordinatorView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("New Coordinator");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            addTheCard();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public void addTheCard(){

        try{
        FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/components/EventCoordCard.fxml"));
        Parent cardRoot = cardLoader.load();

        // empty

        allCoordBox.getChildren().add(cardRoot);
    } catch (IOException e)

    {
        e.printStackTrace();

    }
    }
}
