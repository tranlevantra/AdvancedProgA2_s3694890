import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterSceneController {

	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;
	private Resources resources = new Resources();
	
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

	
	public void setPrimaryStage(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap,
			HashMap<Integer, Post> postMap) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
	}

	// Handle the event when the 'Log-in' button is clicked
	@FXML
	public void switchLogInScene(ActionEvent event) {

		LogInScene logInScene = new LogInScene(primaryStage, secondaryStage, userMap, postMap);
		primaryStage.setTitle(logInScene.getTitle());
		primaryStage.setScene(logInScene.getScene());

		primaryStage.show();
	}

	// Handle the event when the 'Register' button is clicked
	@FXML
	public void registerButtonHandle(ActionEvent event) {
		String newUsername = userNameTextField.getText();
		String newPassword = passwordTextField.getText();
		String newFirstName = firstNameTextField.getText();
		String newLastName = lastNameTextField.getText();

		try {// Prevent duplicate username
			if (userMap.containsKey(newUsername)) {
				exceptionTextLabel.setText("Username already exists.");
				return;
			}

			//Create new user
			User newUser = new User(newUsername, newPassword, newFirstName, newLastName);

			//Add newly created user to userMap
			userMap.put(newUsername, newUser);

			//Update DB
			resources.insertUserDB(newUser);

			userNameTextField.clear();
			passwordTextField.clear();
			firstNameTextField.clear();
			lastNameTextField.clear();

			exceptionTextLabel.setText("User created successfully. 'Log In' to proceed");

		} catch (UserRegistrationException e) {
			exceptionTextLabel.setText(e.getMessage());
		}
	}

}
