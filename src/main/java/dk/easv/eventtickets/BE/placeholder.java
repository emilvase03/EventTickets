package dk.easv.eventtickets.BE;

public class placeholder {
    private String name;
    private String email;
    private String username;
    private String password;

    public placeholder(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
