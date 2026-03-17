package dk.easv.eventtickets.BE;

public class EventCoordinator {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    private void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    private EventCoordinator(int id, String firstName, String lastName, String email, String hashedPassword){

    }
}
