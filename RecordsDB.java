import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import sqlitedb.DatabaseConnection;

public class RecordsDB {

	private User user;
	private List<Post> userPosts;

	private static final String deletePostQuery = "DELETE FROM Post WHERE postID = ?";
	private static final String findUserQuery = "SELECT * FROM User WHERE username = ? AND password = ?";
	private static final String deleteUserQuery = "DELETE FROM User WHERE username = ?";
	private static final String updateUserquery = "INSERT INTO User (username, userID, firstname, lastname, password)"
			+ " VALUES (?, ?, ?, ?, ?)";
	private static final String findUserPostsquery = "SELECT * FROM Post WHERE author = ?";
	private static final String updatePostsQuery = "INSERT INTO Post (postID, content, author, likes, shares, datetime)" +
			" VALUES (?, ?, ?, ?, ?, ?)";
	
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	
	public RecordsDB(String username, String password)
			throws SQLException, ExistingException, UserRegistrationException, CreatingPostException {
		setUser(username, password);
		setUserPosts(username);
	}

	public User setUser(String username, String password)
			throws SQLException, ExistingException, UserRegistrationException {
		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = con.prepareStatement(findUserQuery)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String userUsername = resultSet.getString("username");
				String userID = resultSet.getString("userID");
				String userFirstname = resultSet.getString("firstname");
				String userLastname = resultSet.getString("lastname");
				String userPassword = resultSet.getString("password");

				this.user = new User(userUsername, userID, userFirstname, userLastname, userPassword);
				return this.user;
			} else {
				// No matching user found, throw the custom exception
				throw new ExistingException("User with the provided credentials does not exist.");
			}
		}
	}

	public List<Post> setUserPosts(String username) throws SQLException, CreatingPostException {

		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = con.prepareStatement(findUserPostsquery)) {
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int postID = resultSet.getInt("postID");
				String content = resultSet.getString("content");
				String author = resultSet.getString("author");
				int likes = resultSet.getInt("likes");
				int shares = resultSet.getInt("shares");
				String datetime = resultSet.getString("datetime");

				Post post = new Post(postID, content, author, likes, shares, datetime);
				this.userPosts.add(post);
			}
			return this.userPosts;

		}
	}

	public User getUser() {
		return user;
	}
	
	public List<Post> getUserPosts() {
		return this.userPosts;
	}
	
	public void DeletePostDB(Post post) throws SQLException {
		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement stmt = con.prepareStatement(deletePostQuery)) {
			stmt.setInt(1, post.getPostID()); // Set the postID parameter
			stmt.executeUpdate();
		}
	}

	
	public void UpdateUserInforDB() throws SQLException {
	    try (Connection con = DatabaseConnection.getConnection()) {
	        // First, delete the existing user record
	        try (PreparedStatement deleteStmt = con.prepareStatement(deleteUserQuery)) {
	            deleteStmt.setString(1, this.user.getUsername());
	            deleteStmt.executeUpdate();
	        }
	        
	        // Then, insert the new user record
	        try (PreparedStatement insertStmt = con.prepareStatement(updateUserquery)) {
	            insertStmt.setString(1, this.user.getUsername());
	            insertStmt.setString(2, this.user.getUserID());
	            insertStmt.setString(3, this.user.getFirstname());
	            insertStmt.setString(4, this.user.getLastname());
	            insertStmt.setString(5, this.user.getPassword());
	            
	            // Execute the insert query
	            insertStmt.executeUpdate();
	        }
	    }
	}

	
	
	public void UpdatePostsDB(List<Post> posts) throws SQLException {
		
		try (Connection con = DatabaseConnection.getConnection(); 
				PreparedStatement preparedStmt = con.prepareStatement(updatePostsQuery)){
			
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
