import java.time.LocalDateTime;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PostsTableView {
		
	private TableView<Post> tableView;

	public PostsTableView(Integer rows, List<Post> posts) {
		int actualQuantity = Math.min(rows, posts.size());
		
		if (rows < 0) {
            throw new IllegalArgumentException("Invalid input! Quantity must be greater than 0!");
        }

        posts.subList(0, actualQuantity);
		
		for(Post post : posts) {
			tableView.getItems().add(post);
		}
		
		TableColumn<Post, String> postIDColumn = new TableColumn<>("PostID");
		postIDColumn.setCellValueFactory(new PropertyValueFactory<>("postID"));
		
		
		TableColumn<Post, String> contentColumn = new TableColumn<>("Content");
		contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
		
		TableColumn<Post, Integer> likesColumn = new TableColumn<>("Likes");
		likesColumn.setCellValueFactory(new PropertyValueFactory<>("likes"));
		
		TableColumn<Post, Integer> sharesColumn = new TableColumn<>("Content");
		sharesColumn.setCellValueFactory(new PropertyValueFactory<>("shares"));
		
		TableColumn<Post, LocalDateTime> datetimeColumn = new TableColumn<>("Date & Time");
		datetimeColumn.setCellValueFactory(new PropertyValueFactory<>("datetime"));

		
		tableView.getColumns().add(postIDColumn);
		tableView.getColumns().add(contentColumn);
		tableView.getColumns().add(likesColumn);
		tableView.getColumns().add(sharesColumn);
		tableView.getColumns().add(datetimeColumn);
		
		
	}
	
	
	

}
