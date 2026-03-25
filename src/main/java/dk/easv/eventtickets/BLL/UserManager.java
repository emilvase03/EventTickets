package dk.easv.eventtickets.BLL;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BLL.UTIL.Encrypter;
import dk.easv.eventtickets.DAL.DAO.UserDAO;
import dk.easv.eventtickets.DAL.IUserDataAccess;

import java.util.List;

public class UserManager {

    private final IUserDataAccess userDAO;

    public UserManager() throws Exception {
        userDAO = new UserDAO();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.getAllUsers();
    }

    public List<User> getCoordinators() throws Exception {
        return userDAO.getUsersByRole(Role.EVENT);
    }

    public User loginUser(String email, String password) throws Exception {

        if (email == null || password == null)
            return null;

        User user = userDAO.getUserByEmail(email);

        if (user == null)
            return null;

        Encrypter.verifyPassword(password, user.getPassword());

        return user;
    }

    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           Role role) throws Exception {

        if (userDAO.emailExists(email))
            return null;

        String hashedPassword = Encrypter.hashPassword(password);

        User newUser = new User(
                -1,
                firstName,
                lastName,
                email,
                hashedPassword,
                role
        );

        return userDAO.createUser(newUser);
    }

    public void updateUser(User user) throws Exception {
        userDAO.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        userDAO.deleteUser(user);
    }

    public static void main(String[] args) {

        try {
            UserManager userManager = new UserManager();

            // Create Admin
            User admin = userManager.createUser(
                    "Test",
                    "Admin",
                    "admin@test.com",
                    "password123",
                    Role.ADMIN
            );

            if (admin != null)
                System.out.println("Admin created successfully!");
            else
                System.out.println("Admin already exists.");

            // Create Event user
            User eventUser = userManager.createUser(
                    "Test",
                    "Event",
                    "event@test.com",
                    "password123",
                    Role.EVENT
            );

            if (eventUser != null)
                System.out.println("Event user created successfully!");
            else
                System.out.println("Event user already exists.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}