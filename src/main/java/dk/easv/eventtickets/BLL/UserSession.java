package dk.easv.eventtickets.BLL;

import dk.easv.eventtickets.BE.User;

public class UserSession {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}