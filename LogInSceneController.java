import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LogInSceneController {

	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	public void switchRegisterScene(ActionEvent event) {
		
		RegisterScene registerScene = new RegisterScene(primaryStage);
		primaryStage.setTitle(registerScene.getTitle());
		primaryStage.setScene(registerScene.getScene());

		primaryStage.show();
		
	}
	
}
