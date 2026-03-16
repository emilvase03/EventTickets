package dk.easv.eventtickets.GUI.Models;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BLL.UserManager;

// Java imports
import java.util.List;

public class UserModel {

    private final UserManager userManager;

    public UserModel() {
        try {
            userManager = new UserManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UserManager", e);
        }
    }

    public List<User> getAllUsers() throws Exception {
        return userManager.getAllUsers();
    }

    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           Role role) throws Exception {

        return userManager.createUser(
                firstName,
                lastName,
                email,
                password,
                role
        );
    }

    public User loginUser(String email, String password) throws Exception {
        return userManager.loginUser(email, password);
    }

    public void updateUser(User user) throws Exception {
        userManager.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        userManager.deleteUser(user);
    }
}