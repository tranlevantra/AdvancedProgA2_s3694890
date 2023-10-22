import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterScene {

	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;

	public RegisterScene(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap,
			HashMap<Integer, Post> postMap) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
	}

	public String getTitle() {
		return "Register";
	}

	public Scene getScene() {
		// load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterScene.fxml"));

		// load the FXML
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		RegisterSceneController controller = loader.getController();
		controller.setPrimaryStage(primaryStage, secondaryStage, userMap, postMap);

		Scene scene = new Scene(parentNode);

		return scene;
	}

}
