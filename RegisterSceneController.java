import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class RegisterSceneController {
	
	
private Stage primaryStage;
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void goBackButtonHandler(ActionEvent event) {
		
		
		LogInScene imageViewerScene = new LogInScene(primaryStage);
		
		primaryStage.setTitle(imageViewerScene.getTitle());
		primaryStage.setScene(imageViewerScene.getScene());
		
		primaryStage.show();

}
