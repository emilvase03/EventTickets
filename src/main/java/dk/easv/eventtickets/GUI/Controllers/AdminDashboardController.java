package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.placeholder;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;


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
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminDashboardController implements Initializable {

    @FXML private MFXPaginatedTableView coordContainer;

    @FXML
    private MFXTableColumn<placeholder> colFirstName;
    @FXML
    private MFXTableColumn<placeholder> colEmail;
    @FXML
    private MFXTableColumn<placeholder> colLastName;
    @FXML
    private MFXTableColumn<placeholder> colOptions;


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
            //addCard();

        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to open NewCoordinatorView");
        }
    }

//    private void addCard(){
//        try{
//            FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/components/EventCoordCard.fxml"));
//            Parent cardRoot = cardLoader.load();
//
//            coordContainer.getItems().add(cardRoot);
//
//        } catch (IOException e) {
//            AlertHelper.showError("Error", "Failed to add Coordinator.");
//        }
//    }

    private void setupTable() {


        colFirstName.setRowCellFactory(name -> new MFXTableRowCell<>(placeholder::getFirstName));
        colLastName.setRowCellFactory(username -> new MFXTableRowCell<>(placeholder::getLastName));

        colEmail.setRowCellFactory(email -> new MFXTableRowCell<>(placeholder::getEmail));


        colOptions.setRowCellFactory(user -> new MFXTableRowCell<>(u -> "") {

            final MenuButton menu;

            {
                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");

                edit.setOnAction(e -> editUser(user));
                delete.setOnAction(e -> deleteUser(user));

                menu = new MenuButton("⋮");
                menu.getItems().addAll(edit, delete);

                setGraphic(menu);
            }
        });


        var available = coordContainer.widthProperty().subtract(colOptions.widthProperty());
        colFirstName.prefWidthProperty().bind(available.multiply(0.32));
        colLastName.prefWidthProperty().bind(available.multiply(0.35));
        colEmail.prefWidthProperty().bind(available.multiply(0.27));

        setupTableFilters();

        coordContainer.setItems(testData());
    }

    private void deleteUser(Object user) {
    }

    private void editUser(Object user) {
    }

    private void setupTableFilters() {

        coordContainer.getFilters().addAll(

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