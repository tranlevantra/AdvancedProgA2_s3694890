import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VipUserScene {
	
	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;
	
	
	
	public VipUserScene(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap, HashMap<Integer, Post> postMap, User vipUser, List<Post> userPosts) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
		this.user = vipUser;
		this.userPosts = userPosts;
	}
	
	public String getTitle() {
		return "Vip User";
	}
	
	public Scene getScene() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VipUserScene.fxml"));
				
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		VipUserSceneController controller = loader.getController();
		controller.setPrimaryStage(primaryStage, secondaryStage, userMap, postMap, user, userPosts);
		
		Scene scene = new Scene(parentNode);
		
		return scene;
		
	}

}
