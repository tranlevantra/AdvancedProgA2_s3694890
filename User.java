

public class User {
    private String username;
    private String password;
    private String firstname;
    private String lastname;

    // >= 5 characters long and no spaces
    public static final String usernameFormatter = "^[\\S]{5,}$";
    
    // >= 5 characters long and contains both letters and numbers
    public static final String passwordFormatter = "^(?=.*[a-zA-Z])(?=.*[0-9]).{5,}$";

    public User(String username, String password, String firstname, String lastname) throws UserRegistrationException {
        setUsername(username);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
    }
    
    public void setUsername(String username) throws UserRegistrationException {
        if (username == null || username.isEmpty()) {
            throw new UserRegistrationException("Username cannot be empty");
        }

        if (username.matches(usernameFormatter)) {
            this.username = username;
        } else {
            throw new UserRegistrationException("Username format: ^[\\\\S]{5,}$");
        }
    }

    public void setPassword(String password) throws UserRegistrationException {
        if (password == null || password.isEmpty()) {
            throw new UserRegistrationException("Password cannot be empty");
        }

        if (password.matches(passwordFormatter)) {
            this.password = password;
        } else {
            throw new UserRegistrationException("Password format: ^(?=.*[a-zA-Z])(?=.*[0-9]).{5,}$");
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

        return formattedName.toString().trim();
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public String getPassword() {
        return password;
    }

    public String getLastname() {
        return lastname;
    }


}
