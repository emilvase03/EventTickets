package dk.easv.eventtickets.DAL.DAO;

import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.DAL.DB.DBConnector;
import dk.easv.eventtickets.DAL.IUserDataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDataAccess {

    private final DBConnector databaseConnector;

    public UserDAO() throws Exception {
        databaseConnector = new DBConnector();
    }

    @Override
    public List<User> getAllUsers() throws Exception {

        List<User> users = new ArrayList<>();

        String sql = "SELECT id, firstName, lastName, email, password, role FROM [User]";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }

        return users;
    }

    @Override
    public User createUser(User newUser) throws Exception {

        String sql = """
                INSERT INTO [User] (firstName, lastName, email, password, role)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newUser.getFirstName());
            stmt.setString(2, newUser.getLastName());
            stmt.setString(3, newUser.getEmail());
            stmt.setString(4, newUser.getPassword());
            stmt.setString(5, newUser.getRole().name()); // ENUM → String

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = -1;

            if (rs.next())
                id = rs.getInt(1);

            return new User(
                    id,
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getEmail(),
                    newUser.getPassword(),
                    newUser.getRole()
            );
        }
    }

    @Override
    public void updateUser(User user) throws Exception {

        String sql = """
                UPDATE [User]
                SET firstName = ?, lastName = ?, email = ?, password = ?, role = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole().name()); // ENUM → String
            stmt.setInt(6, user.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(User user) throws Exception {

        String sql = "DELETE FROM [User] WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean emailExists(String email) throws Exception {

        String sql = "SELECT COUNT(*) FROM [User] WHERE LOWER(email) = LOWER(?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());

            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public User getUserByEmail(String email) throws Exception {

        String sql = "SELECT id, firstName, lastName, email, password, role FROM [User] WHERE email = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return mapUser(rs);

            return null;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {

        return new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role").toUpperCase()) // String → ENUM
        );
    }
}