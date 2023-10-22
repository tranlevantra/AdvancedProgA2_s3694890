import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import sqlitedb.DatabaseConnection;

public class Resources {

	private static final String deletePostQuery = "DELETE FROM Post WHERE postID = ?";
	private static final String updateUserquery = "UPDATE User SET username = ?, password = ?, firstname = ?, lastname = ? WHERE username = ?";
	private static final String insertPostQuery = "INSERT INTO Post (postID, content, author, likes, shares, datetime)" +
			" VALUES (?, ?, ?, ?, ?, ?)";
	private static final String updateUserStatusQuery = "UPDATE User SET status = ? WHERE username = ?";
	private static final String findAllPostQuery = "SELECT * FROM Post";
	private static final String findAllUsersQuery = "SELECT * FROM User;";
	private static final String updateAuthorInPostQuery = "UPDATE Post SET author = ? WHERE author = ?";
	private static final String insertUserQuery = "INSERT INTO User (username, password, firstname, lastname, status) VALUES (?, ?, ?, ?, ?)";
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// Load PostMap
	public HashMap<Integer, Post> loadPostData() {
		HashMap<Integer, Post> postMap = new HashMap<>();
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
	                postMap.put(postID, post);
	            } catch (CreatingPostException e) {
	                e.printStackTrace();
	                continue;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return postMap;
	}

	//Load UserMap
	public HashMap<String, User> loadUserData() {
		HashMap<String, User> userMap = new HashMap<>();
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(findAllUsersQuery)) {
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            String password = resultSet.getString("password");
	            String firstname = resultSet.getString("firstname");
	            String lastname = resultSet.getString("lastname");
	            String status = resultSet.getString("status");

	            User user;
	            try {
	                if ("VIP".equals(status)) {
	                    user = new VipUser(username, password, firstname, lastname);
	                } else {
	                    user = new User(username, password, firstname, lastname);
	                }
	            
	                userMap.put(username, user);
	            } catch (UserRegistrationException e) {
	                e.printStackTrace();
	                continue;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return userMap;
	}
	
	// Insert newly registered user to database, default status is normal user
	public void insertUserDB(User user) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(insertUserQuery)) {
	     
	        preparedStatement.setString(1, user.getUsername());
	        preparedStatement.setString(2, user.getPassword());
	        preparedStatement.setString(3, user.getFirstname());
	        preparedStatement.setString(4, user.getLastname());
	        preparedStatement.setString(5, "Normal");  

	        preparedStatement.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Update Post table when an existing user update his/her username
	public void updatePostAuthorDB(String newUsername, String oldUsername) {
		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement preparedStatement = con.prepareStatement(updateAuthorInPostQuery)) {
			preparedStatement.setString(1, newUsername);
			preparedStatement.setString(2, oldUsername);

			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Change Status to VIP when Normal User chooses to upgrade
	public void updateUserStatusDB(User user) {
		String username = user.getUsername();

		try (Connection con = DatabaseConnection.getConnection();
		     PreparedStatement preparedStatement = con.prepareStatement(updateUserStatusQuery)) {
			preparedStatement.setString(1, "VIP");
		    preparedStatement.setString(2, username); 
		    
		    preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePostDB(Post post) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(deletePostQuery)) {
	        preparedStatement.setInt(1, post.getPostID());

	        preparedStatement.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void exportPostToFile(Post specificPost, File file) {

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

    // Update User table in case of any changes
	public void updateUserInforDB(String newUsername, User user) {
	    try (Connection con = DatabaseConnection.getConnection();
	           PreparedStatement preparedStatement = con.prepareStatement(updateUserquery)) {
	           preparedStatement.setString(1, newUsername);
	           preparedStatement.setString(2, user.getPassword());
	           preparedStatement.setString(3, user.getFirstname());
	           preparedStatement.setString(4, user.getLastname());
	           preparedStatement.setString(5, user.getUsername());

	           preparedStatement.executeUpdate();

	       } catch (SQLException e) {
	    
	           e.printStackTrace();
	       }
	}

	// Bulk Insert Posts to DB
	public void insertPostDB(List<Post> newPosts) {
	    try (Connection con = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(insertPostQuery)) {

	        for (Post post : newPosts) {
	            preparedStatement.setInt(1, post.getPostID());
	            preparedStatement.setString(2, post.getContent());
	            preparedStatement.setString(3, post.getAuthor());
	            preparedStatement.setInt(4, post.getLikes());
	            preparedStatement.setInt(5, post.getShares());
	            preparedStatement.setString(6, post.getDatetime());
	            preparedStatement.addBatch();
	        }

	        preparedStatement.executeBatch(); 

	       
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Bulk import Posts to program
	public List<Post> bulkImportPosts(Integer postIDStarter, User user, File file) {
	    List<Post> newPosts = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split("[,;]");

	            if (values.length >= 3) {
	                try {
	                    String content = values[0];
	                    int likes = Integer.parseInt(values[1]);
	                    int shares = Integer.parseInt(values[2]);
	                    
	                    String datetime = getCurrentDatetimeAsString();
	                    
	                    int newPostID = postIDStarter;
	                    postIDStarter++;
	                    
	                    Post newPost = new Post(newPostID, content, user.getUsername(), likes, shares, datetime);
	                    newPosts.add(newPost);
	                } catch (NumberFormatException | CreatingPostException e) {
	                    e.printStackTrace(); // Handle or log the exception if needed
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return newPosts;
	}


	private String getCurrentDatetimeAsString() {
	    LocalDateTime currentDateTime = LocalDateTime.now();
	    return currentDateTime.format(formatter);
	}
	

	
	
}
