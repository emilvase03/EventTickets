package dk.easv.eventtickets.GUI.Models;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BLL.UserManager;

// Java imports
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// JavaFX imports
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public class UserModel {

    private final UserManager userManager;

    private final ObjectProperty<User> loggedInUser = new SimpleObjectProperty<>();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final BooleanProperty loginFailed = new SimpleBooleanProperty(false);

    private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public UserModel() {
        try {
            userManager = new UserManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UserManager", e);
        }
    }

    public void loginUser(String email, String password) {
        loginFailed.set(false);

        Task<User> task = new Task<>() {
            @Override
            protected User call() throws Exception {
                return userManager.loginUser(email, password);
            }
        };

        task.setOnRunning(e -> loading.set(true));

        task.setOnSucceeded(e -> {
            loading.set(false);
            User user = task.getValue();
            if (user == null) {
                loginFailed.set(true);
            } else {
                loggedInUser.set(user);
            }
        });

        task.setOnFailed(e -> {
            loading.set(false);
            loginFailed.set(true);
        });

        executor.submit(task);
    }

    public List<User> getAllUsers() throws Exception {
        return userManager.getAllUsers();
    }

    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           Role role) throws Exception {
        return userManager.createUser(firstName, lastName, email, password, role);
    }

    public void updateUser(User user) throws Exception {
        userManager.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        userManager.deleteUser(user);
    }

    public ObjectProperty<User> loggedInUserProperty() { return loggedInUser; }
    public BooleanProperty loadingProperty() { return loading; }
    public BooleanProperty loginFailedProperty() { return loginFailed; }
}