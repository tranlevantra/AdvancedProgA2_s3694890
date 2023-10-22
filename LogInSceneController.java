
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInSceneController {

    // Reference to primary and secondary stages, user map, and post map
    private Stage primaryStage;
    private Stage secondaryStage;
    private HashMap<String, User> userMap;
    private HashMap<Integer, Post> postMap;

    // FXML components
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label logInSceneExceptionLabel;

    // Method to set references to primary stage, secondary stage, and data maps
    public void setPrimaryStage(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap,
            HashMap<Integer, Post> postMap) {
        this.primaryStage = primaryStage;
        this.secondaryStage = secondaryStage;
        this.userMap = userMap;
        this.postMap = postMap;
    }

    // Event handler for switching to the Register Scene
    @FXML
    public void switchRegisterSceneButtonHandler(ActionEvent event) {
        // Create a RegisterScene and set it as the primary stage's scene
        RegisterScene registerScene = new RegisterScene(primaryStage, secondaryStage, userMap, postMap);
        primaryStage.setTitle(registerScene.getTitle());
        primaryStage.setScene(registerScene.getScene());
        primaryStage.show();
    }

    // Event handler for logging in
    @FXML
    public void logInButtonHandler(ActionEvent event) {
        logInSceneExceptionLabel.setText("");

        // Retrieve the username and password from input fields
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        try {
            // Find the user based on the provided username and password
            User user = findUser(username, password);
            
            // Retrieve the user's posts
            List<Post> userPosts = findUserPosts(user);

            // Clear input fields
            usernameTextField.clear();
            passwordTextField.clear();

            if (user instanceof VipUser) {
                // If the user is a VIP user, switch to the VIP user scene
                VipUserScene vipUserScene = new VipUserScene(primaryStage, secondaryStage, userMap, postMap, user, userPosts);
                primaryStage.setTitle(vipUserScene.getTitle());
                primaryStage.setScene(vipUserScene.getScene());
            } else {
                // If the user is a normal user, switch to the normal user scene
                NormalUserScene normalUserScene = new NormalUserScene(primaryStage, secondaryStage, userMap, postMap, user, userPosts);
                primaryStage.setTitle(normalUserScene.getTitle());
                primaryStage.setScene(normalUserScene.getScene());
            }

            primaryStage.show();
        } catch (ExistingException e) {
            logInSceneExceptionLabel.setText(e.getMessage());
        }
    }

    // Method to find a user based on username and password
    public User findUser(String username, String password) throws ExistingException {
        User user = userMap.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ExistingException("Incorrect Name or Password");
        }
        return user;
    }

    // Method to find posts authored by a specific user
    public List<Post> findUserPosts(User user) {
        List<Post> userPosts = new ArrayList<>();
        for (Post post : postMap.values()) {
            if (post.getAuthor().equals(user.getUsername())) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }
}

