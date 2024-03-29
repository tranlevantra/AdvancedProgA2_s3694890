import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NormalUserSceneController implements Initializable {

	private Resources resources = new Resources();

	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;

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

	private ObservableList<Post> observableUserPosts = FXCollections.observableArrayList();

	public void setPrimaryStage(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap,
			HashMap<Integer, Post> postMap, User user, List<Post> userPosts) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
		this.user = user;
		this.userPosts = userPosts;

		updateTableView(userPosts);

		welcomeMessageTextLabel.setText("Welcome " + user.getFirstname() + " " + user.getLastname());

	}

	// Dynamic show posts in tableView
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		postID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPostID()).asObject());
		content.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContent()));
		likes.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLikes()).asObject());
		shares.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShares()).asObject());
		datetime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatetime()));

		table.setItems(observableUserPosts);

	}

	// Show ONE post on TableView using PostID from postIDTextfield 
	@FXML
	public void showSpecificPostHandler() throws ExistingException {
		if (validatePostID(postIDTextField, postIDExceptionLabel)) {
			int targetPostID = Integer.parseInt(postIDTextField.getText());
			Post specificPost;
			specificPost = findPost(targetPostID);

			updateTableView(FXCollections.observableArrayList(specificPost));

		}
	}
	// Show a subset of UserPosts, quantity to subset is specified with Quantity Text field input
	@FXML
	public void showSubUserPostsHandler() {
		
		quantityExceptionLabel.setText("");
		String quantityText = quantityTextField.getText();

		if (quantityText.isEmpty()) {
			quantityExceptionLabel.setText("Quantity is empty. Please enter a valid number.");
			return;
		}

		int wantedQuantity;
		try {
			wantedQuantity = Integer.parseInt(quantityText);

			if (wantedQuantity < 0) {
				throw new NumberFormatException();
			}

			int actualQuantity = Math.min(userPosts.size(), wantedQuantity);
			List<Post> toDisplayList = userPosts.subList(0, actualQuantity);
			updateTableView(toDisplayList);
		} catch (NumberFormatException e) {
			quantityExceptionLabel.setText("Invalid quantity: Please enter a non-negative number.");
		}
	}
	

	// Delete ONE POST using postID obtained from postIDTextField
	@FXML
	public void deletePostHandler() throws ExistingException {
		if (validatePostID(postIDTextField, postIDExceptionLabel)) {
			int targetPostID = Integer.parseInt(postIDTextField.getText());
			Post specificPost = findPost(targetPostID);

			resources.deletePostDB(specificPost); // Delete from Database 
			postMap.remove(specificPost.getPostID());// Remove from PostMap
			userPosts.remove(specificPost);// Remove from UserPost list

			updateTableView(userPosts);
		}
	}
	

	// Export ONE Post using postID obtained from postIDTextField
	@FXML
	public void exportAPostHandler(ActionEvent event) throws ExistingException, IOException {
		if (validatePostID(postIDTextField, postIDExceptionLabel)) {
			int targetPostID = Integer.parseInt(postIDTextField.getText());
			Post specificPost = findPost(targetPostID);

			File file = getSaveFileDialog("post_" + specificPost.getPostID() + ".csv");
			if (file != null) {
				resources.exportPostToFile(specificPost, file);
			} else {
				postIDExceptionLabel.setText("Export canceled by the user.");
			}
		}
	}
	

	// Handler method to Update User Information on Secondary Stage
	@FXML
	public void switchUpdateUserInforStageHandler(ActionEvent event) {

		UpdateUserInforScene updateUserInforScene = new UpdateUserInforScene(userMap, user, userPosts,
				welcomeMessageTextLabel);

		secondaryStage.setTitle(updateUserInforScene.getTitle());
		secondaryStage.setScene(updateUserInforScene.getScene());

		secondaryStage.show();
	}

	// Handler method to Add ONE post on Secondary Stage
	@FXML
	public void switchAddAPostStage(ActionEvent event) {

		AddAPostScene addAPostScene = new AddAPostScene(postMap, user, userPosts);

		secondaryStage.setTitle(addAPostScene.getTitle());
		secondaryStage.setScene(addAPostScene.getScene());

		secondaryStage.show();

		updateTableView(userPosts);
	}

	// Handler method to Upgrade User status on Secondary Stage
	@FXML
	public void upgradeUserSceneHandler(ActionEvent event) {
		Label messageLabel = new Label("Upgrade your account to VIP for free?");
		messageLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

		Button upgradeButton = new Button("Upgrade");
		upgradeButton.setVisible(true);

		upgradeButton.setOnAction(e -> {
			// Button to Upgrade the user's status to VIP
			resources.updateUserStatusDB(user);

			messageLabel.setText("Upgrade successful! Please Log-out and Sign-in agains");
			upgradeButton.setVisible(false);
		});

		VBox vbox = new VBox(20, messageLabel, upgradeButton);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox, 400, 200);

		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Upgrade User");

		secondaryStage.show();
	}

	// Handler method of Logout button
	@FXML
	public void logOutButtonHandler(ActionEvent event) {

		secondaryStage.close();

		primaryStage.close();
	}

	
	// Helper methods
	
	// Dynamic update TableView
	private void updateTableView(List<Post> userPosts) {
		observableUserPosts.setAll(userPosts);
	}
	
	// Find Post in userPosts
	private Post findPost(int targetPostID) throws ExistingException {
		Post specificPost = null;

		for (Post post : userPosts) {
			if (post.getPostID() == targetPostID) {
				specificPost = post;
				break;
			}
		}

		if (specificPost == null) {
			throw new ExistingException("Post not found");
		}
		return specificPost;
	}

	// Validate the Post ID entered in the text field
	private boolean validatePostID(TextField postIDTextField, Label postIDExceptionLabel) {
		postIDExceptionLabel.setText("");
		String postIDText = postIDTextField.getText();

		// PostID field must not be empty
		if (postIDText.isEmpty()) {
			postIDExceptionLabel.setText("Post ID is empty. Please enter a valid number.");

			return false;
		}

		try {
			int targetPostID = Integer.parseInt(postIDText); // And must be a valid number
			findPost(targetPostID); // to find Post

			return true;

		} catch (NumberFormatException e) {
			postIDExceptionLabel.setText("Invalid post ID: Please enter a valid number.");
		} catch (ExistingException e) {

			postIDExceptionLabel.setText(e.getMessage());
		}

		return false;
	}
	
	// Show FileChooser on Secondary Stage to Export ONE Post
	public File getSaveFileDialog(String initialFileName) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Post");
		fileChooser.setInitialFileName(initialFileName);
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
		fileChooser.getExtensionFilters().add(csvFilter);
		return fileChooser.showSaveDialog(secondaryStage);
	}
}
