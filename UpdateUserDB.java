import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sqlitedb.DatabaseConnection;

public class UpdateUserDB {
	
	private static final String TABLE_NAME = "User";
	
	private static final String query = "INSERT INTO " + TABLE_NAME + " (username, userID, password, firstname, lastname)" +
			" VALUES (?, ?, ?, ?, ?)";

	
	public UpdateUserDB(User user) throws SQLException{
		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getUserID());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.executeUpdate();
		} 
	}	
		

}
