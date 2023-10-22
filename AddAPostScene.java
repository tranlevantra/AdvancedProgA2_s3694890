import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AddAPostScene {
	

	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;
	
	
	public AddAPostScene(HashMap<Integer, Post> postMap, User user, List<Post> userPosts) {
		this.postMap = postMap;
		this.user = user;
		this.userPosts = userPosts;
	}
	

	public String getTitle() {
		return "Add A Post";
	}
	
	public Scene getScene() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAPost.fxml"));
				
		Parent parentNode = null;
		try {
			parentNode = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AddAPostSceneController controller = loader.getController();
		controller.setSecondaryStage(postMap, user, userPosts);

		Scene scene = new Scene(parentNode);
		
		return scene;
		
	}

}

