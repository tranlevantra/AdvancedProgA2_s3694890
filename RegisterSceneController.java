import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class RegisterSceneController {

	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	public void switchLogInScene(ActionEvent event) {
		
		LogInScene logInScene = new LogInScene(primaryStage);
		primaryStage.setTitle(logInScene.getTitle());
		primaryStage.setScene(logInScene.getScene());

		primaryStage.show();
		
	}
}
