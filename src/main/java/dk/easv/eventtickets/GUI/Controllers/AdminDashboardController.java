package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.placeholder;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.filter.StringFilter;

// Java imports
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML private MFXPaginatedTableView coordContainer;

    private MFXTableColumn<placeholder> colName;
    private MFXTableColumn<placeholder> colEmail;
    private MFXTableColumn<placeholder> colUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }


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
            addCard();


        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to open NewCoordinatorView");
        }
    }

    private void addCard(){
        try{
            FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/components/EventCoordCard.fxml"));
            Parent cardRoot = cardLoader.load();

            // empty

            coordContainer.getItems().add(cardRoot);

        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to add Coordinator.");
        }
    }

    private void setupTable() {
        colName = new MFXTableColumn<>("Name");
        colEmail = new MFXTableColumn<>("Email");
        colUsername = new MFXTableColumn<>("Username");

        colName.setRowCellFactory(name -> new MFXTableRowCell<>(placeholder::getName));
        colEmail.setRowCellFactory(email -> new MFXTableRowCell<>(placeholder::getEmail));
        colUsername.setRowCellFactory(username -> new MFXTableRowCell<>(placeholder::getUsername));

        colName.prefWidthProperty().bind(coordContainer.widthProperty().divide(3));
        colEmail.prefWidthProperty().bind(coordContainer.widthProperty().divide(3));
        colUsername.prefWidthProperty().bind(coordContainer.widthProperty().divide(3));
        coordContainer.getTableColumns().addAll(colName, colEmail, colUsername);

        // If TableView needs to be dynamic, in height (max height)
        //coordContainer.setMaxHeight(Double.MAX_VALUE);

        setupTableFilters();

        coordContainer.setItems(testData());
    }

    private void setupTableFilters() {
        coordContainer.getFilters().addAll(
                new StringFilter<>("Name", placeholder::getName),
                new StringFilter<>("Email", placeholder::getEmail),
                new StringFilter<>("Username", placeholder::getUsername)
        );

        coordContainer.getFilters().addListener((ListChangeListener<Object>) change -> {
            Platform.runLater(() -> coordContainer.setCurrentPage(1));
        });

    }

    private ObservableList<placeholder> testData() {
        ObservableList<placeholder> test = FXCollections.observableArrayList();
        test.add(new placeholder("Jakob", "Jakob@Email.com", "UserJakob", "Jakob"));
        test.add(new placeholder("Zhovzan", "Zhovzan@Email.com", "UserZhovzan", "Zhovzan"));
        test.add(new placeholder("Emil", "Emil@Email.com", "UserEmil", "Emil"));
        test.add(new placeholder("Jakob", "Jakob@Email.com", "UserJakob", "Jakob"));
        test.add(new placeholder("Zhovzan", "Zhovzan@Email.com", "UserZhovzan", "Zhovzan"));
        test.add(new placeholder("Emil", "Emil@Email.com", "UserEmil", "Emil"));
        test.add(new placeholder("Jakob", "Jakob@Email.com", "UserJakob", "Jakob"));
        test.add(new placeholder("Zhovzan", "Zhovzan@Email.com", "UserZhovzan", "Zhovzan"));
        test.add(new placeholder("Emil", "Emil@Email.com", "UserEmil", "Emil"));

        return test;
    }

}
