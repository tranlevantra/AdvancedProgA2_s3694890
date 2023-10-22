import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NormalUserScene {
	
	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;
	

	
	public NormalUserScene(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap, HashMap<Integer, Post> postMap, User user, List<Post> userPosts) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
		this.user = user;
		this.userPosts = userPosts;
	}
	
	public String getTitle() {
		return "Normal User";
	}
	
	public Scene getScene() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NormalUserScene.fxml"));
				
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NormalUserSceneController controller = loader.getController();
		controller.setPrimaryStage(primaryStage, secondaryStage, userMap, postMap, user, userPosts);
		
		Scene scene = new Scene(parentNode);
		
		return scene;
		
	}

}
