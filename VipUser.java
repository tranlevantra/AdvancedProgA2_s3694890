public class VipUser extends User {
	
    private boolean isVip;

    public VipUser(String username, String password, String firstname, String lastname) throws UserRegistrationException {
        super(username, password, firstname, lastname);
        this.isVip = true; // Set a VIP-specific attribute
    }
	
}
