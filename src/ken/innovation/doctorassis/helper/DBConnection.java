package ken.innovation.doctorassis.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ken.innovation.doctorassis.utils.Config;

public class DBConnection {
	
	private static DBConnection mDbConnection;
	
	public static DBConnection getInstance() throws Exception{
		if (mDbConnection == null){
			mDbConnection = new DBConnection();
		}
		return mDbConnection;
	}
	
	private Connection connection;
	
	private DBConnection() throws Exception{
		Class.forName("org.h2.Driver");
		connection = DriverManager.
            getConnection("jdbc:h2:" + Config.DB_PATH , "sa", "");
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void commit() throws SQLException{
		connection.commit();
	}
	
	public void close() throws SQLException{
		connection.close();
	}
	
	public Statement createStatement() throws SQLException{
		return connection.createStatement();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
}
