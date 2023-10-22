import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UpdateUserInforSceneController {

	private HashMap<String, User> userMap;
	private User user;
	private List<Post> userPosts;
	private Resources resources = new Resources();
	private Label welcomeMessageTextLabel;
	
	@FXML
	private TextField userNameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField lastNameTextField;
	@FXML
	private Label exceptionTextLabel;
	

	public void setSecondaryStage(HashMap<String, User> userMap, User user,
			List<Post> userPosts, Label welcomeMessageTextLabel) {
		this.userMap = userMap;
		this.user = user;
		this.userPosts = userPosts;
		this.welcomeMessageTextLabel = welcomeMessageTextLabel;
	}
	

	@FXML
	public void updateUserInforButtonHandler(ActionEvent event) {
		String newUsername = userNameTextField.getText();
		String newPassword = passwordTextField.getText();
		String newFirstName = firstNameTextField.getText();
		String newLastName = lastNameTextField.getText();
        
		//Fields to update are optional, empty TextField indicate such parameter will not be changed
		try {
			if (!newUsername.isEmpty()) {
                // If username is changed, check if the new username already exits elsewhere
				if (!newUsername.equals(user.getUsername()) && userMap.containsKey(newUsername)) {
					throw new ExistingException("Username already exists.");
				}

				if (!newUsername.equals(user.getUsername())) {
                    // Validate if username is in the correct format
					new User(newUsername, user.getPassword(), user.getFirstname(), user.getLastname());

					// Update Post table
					resources.updatePostAuthorDB(newUsername, user.getUsername());
					// Update User table
					resources.updateUserInforDB(newUsername, user);
					// Update PostMap
					for (Post post : userPosts) {
						if (user.getUsername().equals(post.getAuthor())) {
							post.setAuthor(newUsername);
						}
					}
					// Update UserMap and User object
					userMap.remove(user.getUsername());
					user.setUsername(newUsername);
					userMap.put(newUsername, user);
				}
			}

			if (!newPassword.isEmpty()) {
				// Validate if new password is in the correct format
				new User(user.getUsername(), newPassword, user.getFirstname(), user.getLastname());
				user.setPassword(newPassword);
			}

			if (!newFirstName.isEmpty()) {
				user.setFirstname(newFirstName);
			}

			if (!newLastName.isEmpty()) {
				user.setLastname(newLastName);
			}

			// Update User table
			resources.updateUserInforDB(user.getUsername(), user);

			exceptionTextLabel.setText("Okie!!! Exit");
			
			String fullName = user.getFirstname() + " " + user.getLastname();
			
			welcomeMessageTextLabel.setText("Welcome, " + fullName);

		} catch (ExistingException e) {
			exceptionTextLabel.setText(e.getMessage());
		} catch (UserRegistrationException e) {
			exceptionTextLabel.setText(e.getMessage());
		}
	}
}
