import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Stage secondaryStage = new Stage();
		Resources resources = new Resources();
		
		HashMap<String, User> userMap = resources.loadUserData();
		HashMap<Integer, Post> postMap = resources.loadPostData();
	
		LogInScene logInScene = new LogInScene(primaryStage, secondaryStage, userMap, postMap);
		
		primaryStage.setTitle(logInScene.getTitle());
		primaryStage.setScene(logInScene.getScene());
		
		
		primaryStage.show();
		
	}

	
	
	public static void main(String[] args) {
		launch(args);
	}


}
