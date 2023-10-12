import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterScene {
	
	private Stage primaryStage;
	
	
	public RegisterScene(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
	}
	
	public String getTitle() {
		return "Register";
	}
	
	public Scene getScene() {
		//load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterScene.fxml"));
		
		
		// load the FXML
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		RegisterScene controller = loader.getController();
		controller.setPrimaryStage(primaryStage);
		
		Scene scene = new Scene(parentNode);
		
		return scene;
		}

	


}
