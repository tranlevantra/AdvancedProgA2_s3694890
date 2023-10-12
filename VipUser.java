public class VipUser extends User {
	public VipUser(String username, String userID, String password, String firstname, String lastname)
			throws UserRegistrationException {
		super(username, userID, password, firstname, lastname);
	}

	public static VipUser castToVipUser(User user) throws UserRegistrationException {
		// You can add a "V" to the user's userID
		String vipUserID = "V" + user.getUserID();

		// Create a new VipUser with the modified userID
		return new VipUser(user.getUsername(), vipUserID, user.getPassword(), user.getFirstname(), user.getLastname());
	}
}
