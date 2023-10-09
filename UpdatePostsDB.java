import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import sqlitedb.DatabaseConnection;

public class UpdatePostsDB {
	
	private static final String TABLE_NAME = "Post";
	
	private static final String query = "INSERT INTO " + TABLE_NAME + " (postID, content, author, likes, shares, datetime)" +
			" VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public UpdatePostsDB(List<Post> posts) throws SQLException {
		
		try (Connection con = DatabaseConnection.getConnection(); // Batch processing 
				PreparedStatement preparedStmt = con.prepareStatement(query)){
			
			for(Post post: posts) {
				preparedStmt.setInt(1, post.getPostID());
				preparedStmt.setString(2, post.getContent());
				preparedStmt.setString(3, post.getAuthor());
				preparedStmt.setInt(4, post.getLikes());
				preparedStmt.setInt(5, post.getShares());
				preparedStmt.setString(6, post.getDatetime().format(formatter));
				
				
				preparedStmt.addBatch();
				
				}
			preparedStmt.executeBatch();

		}
		
		
	}
}
