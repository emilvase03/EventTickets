package dk.easv.eventtickets.BE;

public class placeholder {
    private String firstName;
    private String email;
    private String lastName;
    private String password;

    public placeholder(String name, String email, String username, String password) {
        this.firstName = name;
        this.email = email;
        this.lastName = username;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
