import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateUserInforSceneController {
	
	private Resources records;
	
	private HashMap<String, User> userMap = new HashMap<>();
	
	private Stage secondaryStage;
	
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
	
	
	@FXML
	public void updateUserInforButtonHandler(ActionEvent event) {
		//
	}
	

	
	
	
	public void updateUserInfor(User user) {
		// if findUser(username) != null, throw ExisitingException
		// if password already there?
		
		// try create User with current field, throw UserRegistrationException
	}
}
