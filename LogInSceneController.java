import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LogInSceneController {
	
	private Stage primaryStage;
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	@FXML
	public void goToRegisterSceneHandler(ActionEvent event) {
		
		RegisterScene secondScene = new RegisterScene(primaryStage);
		primaryStage.setTitle(RegisterScene.getTitle());
		primaryStage.setScene(RegisterScene.getScene());

		primaryStage.show();

}
