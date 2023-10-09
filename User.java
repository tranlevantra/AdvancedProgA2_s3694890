public class User {
    private String username;
    private String userID;
    private String password;
    private String firstname;
    private String lastname;

    public static final String usernameFormatter = "dd/MM/yyyyHH:mm";
    public static final String passwordFormatter = "[A-Z0-9]{6}";

    public User(String username, String userID, String password, String firstname, String lastname) throws UserRegistrationException {
        setUsername(username);
        setUserID(userID);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws UserRegistrationException {
        if (password == null || password.isEmpty()) {
            throw new UserRegistrationException("Password cannot be empty");
        }

        if (password.matches(passwordFormatter)) {
            this.password = password;
        } else {
            throw new UserRegistrationException("Invalid password format");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws UserRegistrationException {
        if (username == null || username.isEmpty()) {
            throw new UserRegistrationException("Username cannot be empty");
        }

        if (username.matches(usernameFormatter)) {
            this.username = username;
        } else {
            throw new UserRegistrationException("Invalid username format");
        }
    }

    public String getFirstname() {
        return firstname;
    }


    public String getLastname() {
        return lastname;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) throws UserRegistrationException {
        if (userID == null || userID.isEmpty()) {
            throw new UserRegistrationException("UserID cannot be empty");
        }

        if (userID.matches(usernameFormatter)) {
            this.userID = userID;
        } else {
            throw new UserRegistrationException("Invalid userID format");
        }
    }
    
    public void setFirstname(String firstname) throws UserRegistrationException {
        this.firstname = formatAndTrimName(firstname);
    }

    public void setLastname(String lastname) throws UserRegistrationException {
        this.lastname = formatAndTrimName(lastname);
    }

    private String formatAndTrimName(String name) throws UserRegistrationException {
        if (name == null || name.isEmpty()) {
            throw new UserRegistrationException("Name cannot be empty");
        }

        String[] nameParts = name.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                formattedName.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    formattedName.append(part.substring(1).toLowerCase());
                }
                formattedName.append(" ");
            }
        }

        return formattedName.toString().trim(); // Trim whitespace
    }

}
