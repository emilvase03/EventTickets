package dk.easv.eventtickets.BE;

public class User {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Role role;

        public User(int id, String firstName, String lastName, String email, String password, Role role) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public Role getRole() {
            return role;
        }
}
