import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class UpdateUserInforScene {

	private HashMap<String, User> userMap;
	private User user;
	private List<Post> userPosts;
	private Label welcomeMessageTextLabel;

	public UpdateUserInforScene(HashMap<String, User> userMap, User user, List<Post> userPosts, Label welcomeMessageTextLabel) {
		this.userMap = userMap;
		this.user = user;
		this.userPosts = userPosts;
		this.welcomeMessageTextLabel = welcomeMessageTextLabel;
	}

	public String getTitle() {
		return "Update User Information";
	}

	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateUserInforScene.fxml"));

		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		UpdateUserInforSceneController controller = loader.getController();
		controller.setSecondaryStage(userMap, user, userPosts, welcomeMessageTextLabel);

		Scene scene = new Scene(parentNode);

		return scene;
	}

}
