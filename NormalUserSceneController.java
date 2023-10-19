import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NormalUserSceneController implements Initializable {

	private Stage primaryStage;
	private Stage secondaryStage;
	private User user;
	private List<Post> userPosts;
	private Resources resources;

	public void setPrimaryStage(Stage primaryStage, Stage secondaryStage, User user, List<Post> userPosts,
			HashMap<Integer, Post> postMap, HashMap<String, User> userMap) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.user = user;
		this.userPosts = userPosts;

		updateTableView(userPosts);
		setWelcomeMessage();
	}

	@FXML
	private Label welcomeMessageTextLabel;

	@FXML
	private TableView<Post> table;

	@FXML
	private TableColumn<Post, Integer> postID;

	@FXML
	private TableColumn<Post, String> content;

	@FXML
	private TableColumn<Post, Integer> likes;

	@FXML
	private TableColumn<Post, Integer> shares;

	@FXML
	private TableColumn<Post, String> datetime;

	@FXML
	private TextField quantityTextField;

	@FXML
	private Label quantityExceptionLabel;

	@FXML
	private TextField postIDTextField;

	@FXML
	private Label postIDExceptionLabel;
	
	
	@FXML
	public void showSpecificPostHandler() {
	    int targetPostID;

	    try {
	        targetPostID = Integer.parseInt(postIDTextField.getText());
	    } catch (NumberFormatException e) {
	        postIDExceptionLabel.setText("Invalid post ID: Please enter a valid number.");
	        return;
	    }

	    Post specificPost;

	    try {
	        specificPost = findPost(targetPostID);
	    } catch (ExistingException e) {
	        postIDExceptionLabel.setText(e.getMessage());
	        return;
	    }

	    ObservableList<Post> specificPostList = FXCollections.observableArrayList(specificPost);
	    updateTableView(specificPostList);
	}
	
	
	@FXML
	public void deletePostHandler() {
	    int targetPostID;

	    try {
	        targetPostID = Integer.parseInt(postIDTextField.getText());
	    } catch (NumberFormatException e) {
	        postIDExceptionLabel.setText("Invalid post ID: Please enter a valid number.");
	        return;
	    }

	    Post specificPost;

	    try {
	        specificPost = findPost(targetPostID);
	    } catch (ExistingException e) {
	        postIDExceptionLabel.setText(e.getMessage());
	        return;
	    }

	    userPosts.remove(specificPost);
	    
	    resources.deletePostDB(specificPost);


	    updateTableView(userPosts);
	}


	public Post findPost(int targetPostID) throws ExistingException {
		Post specificPost = null;

		for (Post post : userPosts) {
			if (post.getPostID() == targetPostID) {
				specificPost = post;
				break;
			}
		}

		if (specificPost == null) {
			throw new ExistingException("Post not found.");
		}
		return specificPost;
	}

	@FXML
	public void showSubUserPostsHandler() {
	    int wantedQuantity;

	    try {
	        wantedQuantity = Integer.parseInt(quantityTextField.getText());
	    } catch (NumberFormatException e) {
	        quantityExceptionLabel.setText("Invalid quantity: Please enter a valid number.");
	        return;  
	    }

	    if (wantedQuantity < 0) {
	        quantityExceptionLabel.setText("Invalid quantity: Please enter a non-negative number.");
	    } else {
	        // Calculate the actual quantity to display
	        int actualQuantity = Math.min(userPosts.size(), wantedQuantity);

	        // Create a sublist of userPosts to display
	        List<Post> toDisplayList = userPosts.subList(0, actualQuantity);

	        // Update the TableView with the posts to display
	        updateTableView(toDisplayList);
	    }
	}


	private ObservableList<Post> observableUserPosts = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		postID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPostID()).asObject());
		content.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContent()));
		likes.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLikes()).asObject());
		shares.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShares()).asObject());
		datetime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatetime()));

		// Bind the TableView to the ObservableList
		table.setItems(observableUserPosts);

	}

	@FXML
	public void exportAPostHandler(ActionEvent event) {
		int targetPostID;
		try {
			targetPostID = Integer.parseInt(postIDTextField.getText());
		} catch (NumberFormatException e) {
			postIDExceptionLabel.setText("Invalid post ID: Please enter a valid number.");
			return;
		}

		Post specificPost;
		try {
			specificPost = findPost(targetPostID);
		} catch (ExistingException e) {
			postIDExceptionLabel.setText(e.getMessage());
			return;
		}

		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Export Post");
	    fileChooser.setInitialFileName("post_" + specificPost.getPostID() + ".csv");

	    FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
	    fileChooser.getExtensionFilters().add(csvFilter);


		File file = fileChooser.showSaveDialog(secondaryStage);
		if (file != null) {
	        // Continue with the export process if the user selected a file
	        resources.exportPostToFile(specificPost, file);
	    } else {
	        // Handle the case where the user canceled the export
	        postIDExceptionLabel.setText("Export canceled by the user.");
	    }	
	}

	@FXML
	public void switchUpgradeUserStageHandler(ActionEvent event) {
		// Switch to UpgradeUserScene on secondary stage
	}

	@FXML
	public void switchLogInScene(ActionEvent event) {

		LogInScene logInScene = new LogInScene(primaryStage);
		primaryStage.setTitle(logInScene.getTitle());
		primaryStage.setScene(logInScene.getScene());

		primaryStage.show();
	}

	@FXML
	public void switchUpdateUserInforStageHandler(ActionEvent event) {
		// Switch to UpdateScene on secondaryStage
	}

	@FXML
	public void switchAddAPostStage(ActionEvent event) {

		// Pass in secondary stage, for Add A post Scene.

		// show secondary stage.

	}


	private void updateTableView(List<Post> userPosts) {
		observableUserPosts.setAll(userPosts);
	}

	private void setWelcomeMessage() {
		if (user != null) {
			String fullName = user.getFirstname() + " " + user.getLastname();
			welcomeMessageTextLabel.setText("Welcome, " + fullName);
		}
	}

}
