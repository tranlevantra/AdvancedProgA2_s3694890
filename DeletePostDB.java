import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sqlitedb.DatabaseConnection;

public class DeletePostDB {
	
	private static final String TABLE_NAME = "Post";
	private static final String query = "DELETE FROM " + TABLE_NAME + " WHERE postID = ?";
	
	public DeletePostDB(Integer postID) throws SQLException{
		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, postID); // Set the postID parameter
			stmt.executeUpdate();
		} 
	}	
}

