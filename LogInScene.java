import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogInScene {
	
	private Stage primaryStage;
	
	private Scene scene;
	
	public LogInScene(Stage primaryStage) {
		this.primaryStage = primaryStage;
		scene = null;
	}
	

	public String getTitle() {
		return "Log-In";
	}
	
	public Scene getScene() {
		
		if(scene != null) {
			return scene;
		}
		
		// load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInScene.fxml"));
				
		// load the FXML
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LogInSceneController controller = loader.getController();
		controller.setPrimaryStage(primaryStage);

		
		// create a scene
		Scene scene = new Scene(parentNode);
		
		return scene;
		
	}

}
