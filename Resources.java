import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import sqlitedb.DatabaseConnection;

public class Resources {
		
	private HashMap<String, User> userMap = new HashMap<>();
	private HashMap<Integer, Post> postMap = new HashMap<>();


	private static final String deletePostQuery = "DELETE FROM Post WHERE postID = ?";
	private static final String deleteUserQuery = "DELETE FROM User WHERE username = ?";
	private static final String updateUserquery = "UPDATE User SET username = ?, password = ?, firstname = ?, lastname = ? WHERE username = ?";
	private static final String updatePostQuery = "INSERT INTO Post (postID, content, author, likes, shares, datetime)" +
			" VALUES (?, ?, ?, ?, ?, ?)";
	private static final String updateUserStatusQuery = "UPDATE User SET status = 'VIP' WHERE username = ?";
	private static final String findAllPostQuery = "SELECT * FROM Post";
	private static final String findAllUsersQuery = "SELECT * FROM User;";
	private static final String updateAuthorInPostQuery = "UPDATE Post SET author = ? WHERE author = ?";
	private static final String insertUserQuery = "INSERT INTO User (username, password, firstname, lastname, status) VALUES (?, ?, ?, ?, ?)";


	
	public void loadPostData() {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(findAllPostQuery)) {
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            int postID = resultSet.getInt("postID");
	            String content = resultSet.getString("content");
	            String author = resultSet.getString("author");
	            int likes = resultSet.getInt("likes");
	            int shares = resultSet.getInt("shares");
	            String datetime = resultSet.getString("datetime");

	            Post post;
	            try {
	                post = new Post(postID, content, author, likes, shares, datetime);
	                this.postMap.put(postID, post);
	            } catch (CreatingPostException e) {
	                // Handle exceptions related to post creation, if needed
	                e.printStackTrace();
	                // Skip this result and continue to the next one
	                continue;
	            }
	        }
	    } catch (SQLException e) {
	        // Handle any SQL-related exceptions here
	        e.printStackTrace();
	    }
	}

	
	public void loadUserData() {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(findAllUsersQuery)) {
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            String password = resultSet.getString("password");
	            String firstname = resultSet.getString("firstname");
	            String lastname = resultSet.getString("lastname");
	            String status = resultSet.getString("status");

	            // Check the status and create the appropriate user instance
	            User user;
	            try {
	                if ("VIP".equals(status)) {
	                    user = new VipUser(username, password, firstname, lastname);
	                } else {
	                    user = new User(username, password, firstname, lastname);
	                }
	            
	                // Add the user to the userMap
	                userMap.put(username, user);
	            } catch (UserRegistrationException e) {
	                // Handle exceptions related to user registration, if needed
	                e.printStackTrace();
	                // Skip this result and continue to the next one
	                continue;
	            }
	        }
	    } catch (SQLException e) {
	        // Handle any SQL-related exceptions here
	        e.printStackTrace();
	    }
	}
	
	public User findUser(String username) {
		
		User user = userMap.get(username);	
		return user;
	}
	
	
	public void updateUserInforDB(User user) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(updateUserquery)) {
	        // Set parameters for the query based on the user object
	        preparedStatement.setString(1, user.getUsername());
	        preparedStatement.setString(2, user.getPassword());
	        preparedStatement.setString(3, user.getFirstname());
	        preparedStatement.setString(4, user.getLastname());
	        preparedStatement.setString(5, user.getUsername()); 

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void insertUserDB(User user) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(insertUserQuery)) {
	        // Set parameters for the insert query based on the user object
	        preparedStatement.setString(1, user.getUsername());
	        preparedStatement.setString(2, user.getPassword());
	        preparedStatement.setString(3, user.getFirstname());
	        preparedStatement.setString(4, user.getLastname());
	        preparedStatement.setString(5, "Normal");  // Set the default status to "Normal"

	        // Execute the insert query
	        int rowsInserted = preparedStatement.executeUpdate();

	        if (rowsInserted != 1) {
	            // Handle the case where the user insert was not successful
	            // You can log this as an error or throw a custom exception if needed
	        }
	    } catch (SQLException e) {
	        // Handle any SQL-related exceptions
	        e.printStackTrace();
	        // You can log this as an error or throw a custom exception if needed
	    }
	}

	
	
	public void updatePostAuthorDB(String newUsername, String oldUsername) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(updateAuthorInPostQuery)) {
	        preparedStatement.setString(1, newUsername);
	        preparedStatement.setString(2, oldUsername);

	        // Execute the Post table author update query
	        int rowsUpdated = preparedStatement.executeUpdate();

	        if (rowsUpdated < 1) {
	            // Handle the case where no posts were updated (optional)
	        }

	    } catch (SQLException e) {
	        // Handle any SQL-related exceptions when updating the Post table
	        e.printStackTrace();}
	        // You can log this as an error or throw a custom exception if needed
	    }
	

	
	public void updateUserStatusDB(User user) {
		String username = user.getUsername();

		try (Connection con = DatabaseConnection.getConnection();
		     PreparedStatement preparedStatement = con.prepareStatement(updateUserStatusQuery)) {
		    preparedStatement.setString(1, username);  

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePostDB(Post post) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(deletePostQuery)) {
	        // Set the parameter for the postID you want to delete
	        preparedStatement.setInt(1, post.getPostID());

	        // Execute the delete query
	        int rowsDeleted = preparedStatement.executeUpdate();

	        if (rowsDeleted != 1) {
	            // Handle the case where the post delete was not successful
	            // You can log this as an error or throw a custom exception if needed
	        }
	    } catch (SQLException e) {
	        // Handle any SQL-related exceptions
	        e.printStackTrace();
	        // You can log this as an error or throw a custom exception if needed
	    }
	}
	
	public void exportPostToFile(Post specificPost, File file) {
		// Create a CSV string with the specific post data
		StringBuilder csvContent = new StringBuilder();
		csvContent.append("Post ID,Author ID,Content,Likes,Shares,Datetime\n");
		csvContent.append(specificPost.getPostID()).append(",");
		csvContent.append(specificPost.getAuthor()).append(",");
		csvContent.append(specificPost.getContent()).append(",");
		csvContent.append(specificPost.getLikes()).append(",");
		csvContent.append(specificPost.getShares()).append(",");
		csvContent.append(specificPost.getDatetime().toString()).append("\n");



		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(csvContent.toString());
		} catch (IOException e) {
			 e.printStackTrace();
		}

	}


	
	
}
