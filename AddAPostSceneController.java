import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddAPostSceneController {
	
	private Resources resources = new Resources();
	
	private HashMap<Integer, Post> postMap;
	private User user;
	private List<Post> userPosts;
	
	@FXML
	private TextField contentTextField;
	@FXML
	private TextField likesTextField;
	@FXML
	private TextField sharesTextField;
	@FXML
	private Label exceptionTextLabel;
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public void setSecondaryStage(HashMap<Integer, Post> postMap, User user, List<Post> userPosts) {
		this.postMap = postMap;
		this.user = user;
		this.userPosts = userPosts;
	}
	
	// Add A Post:
	// Content, Number of Likes and Shares are given by User inputs.
	// PostID: Incrementing from Max postID in postMap + Date Time of creation is auto generated
	@FXML
	public void addButtonHandler(ActionEvent event) {
	    String content = contentTextField.getText();
	    String likesText = likesTextField.getText();
	    String sharesText = sharesTextField.getText();
        List<Post> newPostList = new ArrayList<>();

	    try {
	        int likes = Integer.parseInt(likesText);
	        int shares = Integer.parseInt(sharesText);
	        int newPostID = calculateNewPostID();
	        String datetime = getCurrentDatetimeAsString();

	        Post newPost = new Post(newPostID, content, user.getUsername(), likes, shares, datetime);
	        
	        // Insert new Post to userPosts
	        userPosts.add(newPost);
	        // Insert new Post to PostMap
	        postMap.put(newPost.getPostID(), newPost);
	        // Insert new Posts to database 
	        newPostList.add(newPost);
	        resources.insertPostDB(newPostList);

	        contentTextField.clear();
	        likesTextField.clear();
	        sharesTextField.clear();
	   
	        exceptionTextLabel.setText("Post added successfully");
	        
	    } catch (NumberFormatException e) {
	        exceptionTextLabel.setText("Likes and shares must be valid integers.");
	    } catch (CreatingPostException e) {
	        exceptionTextLabel.setText(e.getMessage());
	    }
	}

	// Helper method
	
	private int calculateNewPostID() {
	    int maxID = 0;
	    for (int postID : postMap.keySet()) {
	        maxID = Math.max(maxID, postID);
	    }
	    return maxID + 1;
	}
	

	private String getCurrentDatetimeAsString() {
	    LocalDateTime currentDateTime = LocalDateTime.now();

	    return currentDateTime.format(formatter);
	}


	
	
	
}
