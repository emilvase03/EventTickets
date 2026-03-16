package dk.easv.eventtickets.DAL;

// Project imports
import dk.easv.eventtickets.BE.User;

// Java imports
import java.util.List;

public interface IUserDataAccess {

    List<User> getAllUsers() throws Exception;

    User createUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    void deleteUser(User user) throws Exception;

    boolean emailExists(String email) throws Exception;

    User getUserByEmail(String email) throws Exception;
}