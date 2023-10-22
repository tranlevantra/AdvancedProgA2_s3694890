import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VipUserSceneController implements Initializable {

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
		setWelcomeMessage();
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

	// Show a subset of UserPosts, quantity to subset is specified with Quantity
	// Text field input
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

	// Delete ONE POST using postID obtained from postIDTextField
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

	// Log-out
	@FXML
	public void logOutButtonHandler(ActionEvent event) {

		secondaryStage.close();

		primaryStage.close();

	}

	// Handler method to Update User Information on Secondary Stage
	@FXML
	public void switchUpdateUserInforStageHandler(ActionEvent event) {

		UpdateUserInforScene updateUserInforScene = new UpdateUserInforScene(userMap, user, userPosts,
				welcomeMessageTextLabel);
		secondaryStage.setTitle(updateUserInforScene.getTitle());
		secondaryStage.setScene(updateUserInforScene.getScene());

		secondaryStage.show();

		setWelcomeMessage();
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

	// Handler method to Import MULTIPLE posts on secondary stage
	@FXML
	public void bulkImportPostsHandler() {

		secondaryStage.close();
		// Allow the user to choose a file on the secondaryStage
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a CSV file for bulk import");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

		File file = fileChooser.showOpenDialog(secondaryStage);

		if (file != null) {
			int newPostIDStarter = calculateNewPostID();

			List<Post> newlyImportedPosts = resources.bulkImportPosts(newPostIDStarter, user, file);

			if (newlyImportedPosts != null && !newlyImportedPosts.isEmpty()) {
				userPosts.addAll(newlyImportedPosts);
				for (Post post : newlyImportedPosts) {
					postMap.put(post.getPostID(), post);
				}

				resources.insertPostDB(newlyImportedPosts);
				updateTableView(userPosts);
			}
		}
	}

	// Handler method to Analyze post by Shares on Secondary Stage
	@FXML
	public void analyseButtonHandler() {
		List<Integer> components = countNoShares();

		int totalPosts = components.get(0) + components.get(1) + components.get(2);

		if (totalPosts == 0) {
			// If there are no posts to analyze, display a message
			Label noPostsLabel = new Label("No post to analyze");
			noPostsLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

			VBox vbox = new VBox(noPostsLabel);
			vbox.setAlignment(Pos.CENTER);

			Scene scene = new Scene(vbox, 400, 200);

			secondaryStage.setScene(scene);
			secondaryStage.setTitle("Shares Analysis");
		} else {
			// If there are posts, create and show the pie chart
			PieChart pieChart = new PieChart();

			PieChart.Data redComponent = new PieChart.Data("0-99", components.get(0));
			PieChart.Data blueComponent = new PieChart.Data("100-999", components.get(1));
			PieChart.Data greenComponent = new PieChart.Data("1000+", components.get(2));

			pieChart.getData().addAll(redComponent, blueComponent, greenComponent);

			Scene scene = new Scene(pieChart, 600, 400);

			secondaryStage.setScene(scene);
			secondaryStage.setTitle("Shares Analysis");
		}

		secondaryStage.show();
	}

	// Helper class

	// Creating A list of counts for 3 components
	private List<Integer> countNoShares() {
		List<Integer> components = new ArrayList<>(3);
		components.add(0);
		components.add(0);
		components.add(0);

		for (Post post : postMap.values()) {
			int shares = post.getShares();
			if (shares >= 0 && shares <= 99) {
				components.set(0, components.get(0) + 1);
			} else if (shares >= 100 && shares <= 999) {
				components.set(1, components.get(1) + 1);
			} else if (shares >= 1000) {
				components.set(2, components.get(2) + 1);
			}
		}

		return components;
	}

	// Auto generate PostID by incrementing Max PostID from current PostMap
	private int calculateNewPostID() {
		int maxID = 0;
		for (int postID : postMap.keySet()) {
			maxID = Math.max(maxID, postID);
		}
		return maxID + 1;
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

}
