package dk.easv.eventtickets.GUI.Models;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BLL.UserManager;
import dk.easv.eventtickets.GUI.Utils.BackgroundExecutor;

// Java imports
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {

    private final UserManager userManager;

    private final ObjectProperty<User> loggedInUser = new SimpleObjectProperty<>();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final BooleanProperty loginFailed = new SimpleBooleanProperty(false);
    private final ObservableList<User> users = FXCollections.observableArrayList();

    public UserModel() {
        try {
            userManager = new UserManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UserManager", e);
        }
    }

    public void loginUser(String email, String password) {
        loginFailed.set(false);

        BackgroundExecutor.execute(
                () -> userManager.loginUser(email, password),
                user -> {
                    if (user == null) {
                        loginFailed.set(true);
                    } else {
                        loggedInUser.set(user);
                    }
                },
                e -> loginFailed.set(true),
                loading::set
        );
    }

    public void loadAllUsers() {
        BackgroundExecutor.execute(
                () -> userManager.getAllUsers(),
                users::setAll,
                e -> { throw new RuntimeException("Failed to load users", e); },
                loading::set
        );
    }

    public void createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           Role role) {
        BackgroundExecutor.execute(
                () -> userManager.createUser(firstName, lastName, email, password, role),
                users::add,
                e -> { throw new RuntimeException("Failed to create user", e); },
                loading::set
        );
    }

    public void updateUser(User user) {
        BackgroundExecutor.execute(
                () -> { userManager.updateUser(user); return null; },
                result -> {
                    int index = users.indexOf(user);
                    if (index >= 0) users.set(index, user);
                },
                e -> { throw new RuntimeException("Failed to update user", e); },
                loading::set
        );
    }

    public void deleteUser(User user) {
        BackgroundExecutor.execute(
                () -> { userManager.deleteUser(user); return null; },
                result -> users.remove(user),
                e -> { throw new RuntimeException("Failed to delete user", e); },
                loading::set
        );
    }

    public ObservableList<User> getUsers()                  { return users; }
    public ObjectProperty<User> loggedInUserProperty()      { return loggedInUser; }
    public BooleanProperty loadingProperty()                { return loading; }
    public BooleanProperty loginFailedProperty()            { return loginFailed; }
}