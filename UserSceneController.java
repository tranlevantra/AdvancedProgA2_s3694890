import java.io.File;
import java.io.IOException;
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

public class UserSceneController implements Initializable {

	private Resources resources;

	private Stage primaryStage;
	private Stage secondaryStage;
	private HashMap<String, User> userMap;
	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;

	public void setPrimaryStage(Stage primaryStage, Stage secondaryStage, HashMap<String, User> userMap,
			HashMap<Integer, Post> postMap, User user, List<Post> userPosts) {
		this.primaryStage = primaryStage;
		this.secondaryStage = secondaryStage;
		this.userMap = userMap;
		this.postMap = postMap;
		this.user = user;
		this.userPosts = userPosts;

		updateTableView(userPosts);
		setWelcomeMessage();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		postID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPostID()).asObject());
		content.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContent()));
		likes.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLikes()).asObject());
		shares.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShares()).asObject());
		datetime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatetime()));

		observableUserPosts.sort((post1, post2) -> post2.getLikes() - post1.getLikes());
	
		table.setItems(observableUserPosts);

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

	private ObservableList<Post> observableUserPosts = FXCollections.observableArrayList();

	@FXML
	public void showSpecificPostHandler() throws ExistingException {
		if (validatePostID(postIDTextField, postIDExceptionLabel)) {
			int targetPostID = Integer.parseInt(postIDTextField.getText());
			Post specificPost;
			specificPost = findPost(targetPostID);
			
			updateTableView(FXCollections.observableArrayList(specificPost));

		}
	}

	@FXML
	public void deletePostHandler() throws ExistingException {
		if (validatePostID(postIDTextField, postIDExceptionLabel)) {
			int targetPostID = Integer.parseInt(postIDTextField.getText());
			Post specificPost = findPost(targetPostID);
			
			resources.deletePostDB(specificPost);
			postMap.remove(specificPost.getPostID());
			userPosts.remove(specificPost);
			
			updateTableView(userPosts);
		}
	}

	@FXML
	public void showSubUserPostsHandler() {
	    quantityExceptionLabel.setText(""); // Clear any previous error message

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

	public File getSaveFileDialog(String initialFileName) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Post");
		fileChooser.setInitialFileName(initialFileName);
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
		fileChooser.getExtensionFilters().add(csvFilter);
		return fileChooser.showSaveDialog(secondaryStage);
	}


	private boolean validatePostID(TextField postIDTextField, Label postIDExceptionLabel) {
		postIDExceptionLabel.setText(""); // Clear any previous error message
		String postIDText = postIDTextField.getText();
		if (postIDText.isEmpty()) {
			postIDExceptionLabel.setText("Post ID is empty. Please enter a valid number.");
			return false;
		}
		try {
			int targetPostID = Integer.parseInt(postIDText);
			findPost(targetPostID);
			return true;
		} catch (NumberFormatException e) {
			postIDExceptionLabel.setText("Invalid post ID: Please enter a valid number.");
		} catch (ExistingException e) {
			postIDExceptionLabel.setText(e.getMessage());
		}
		return false;
	}

	@FXML
	public void logOutButtonHandler(ActionEvent event) {
		
		
            secondaryStage.close();
	        primaryStage.close();

	}

	@FXML
	public void switchUpdateUserInforStageHandler(ActionEvent event) {

		UpdateUserInforScene updateUserInforScene = new UpdateUserInforScene(userMap, user, userPosts, welcomeMessageTextLabel);
		secondaryStage.setTitle(updateUserInforScene.getTitle());
		secondaryStage.setScene(updateUserInforScene.getScene());

		secondaryStage.show();

		setWelcomeMessage();
	}

	@FXML
	public void switchAddAPostStage(ActionEvent event) {

		AddAPostScene addAPostScene = new AddAPostScene(postMap, user, userPosts);
		secondaryStage.setTitle(addAPostScene.getTitle());
		secondaryStage.setScene(addAPostScene.getScene());

		secondaryStage.show();

		updateTableView(userPosts);

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

	private Post findPost(int targetPostID) throws ExistingException {
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

}
