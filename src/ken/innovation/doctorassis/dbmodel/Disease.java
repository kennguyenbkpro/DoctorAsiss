package ken.innovation.doctorassis.dbmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Disease implements DBModel{
	
	public static final String CREATE_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS Disease("
			+ "ID INT IDENTITY(1,1) PRIMARY KEY, "
			+ "NAME VARCHAR(255), "
			+ "CODE VARCHAR(16),"
			+ "ADVICE VARCHAR(255))";
	
	public static final String INSERT_TABLE = 
			"INSERT INTO Disease ("
			+ "NAME, CODE, ADVICE) "
			+ "VALUES('%s', '%s', '%s')";
	
	public static final String GET_BY_ID =
			"SELECT * FROM Disease "
			+ "WHERE ID = %d";
	
	public static final String SEARCH_BY_NAME_OR_CODE =
			"SELECT * FROM Disease "
			+ "WHERE LOWER(NAME) LIKE LOWER('%%%s%%')"
			+ "OR LOWER(CODE) LIKE LOWER('%%%s%%')";
	
	
	public static final String UPDATE_ROW = 
			"UPDATE Disease "
			+ "SET "
			+ "NAME='%s',"
			+ "CODE='%s',"
			+ "ADVICE='%s' "
			+ "WHERE ID = %d";
	
	private int id;
	private String name;
	private String code;
	private String advice;
	
	public Disease(){}
	
	public Disease(int id, String name, String code, String advice) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.advice = advice;
	}
	
	public static ArrayList<DBModel> searchDiseaseByName(Statement statement, String name) throws SQLException{
		ArrayList<DBModel> arrayList = new ArrayList<>();
		ResultSet result = statement.executeQuery(String.format(SEARCH_BY_NAME_OR_CODE, name, name));
		while (result.next()){
			Disease object = new Disease(result.getInt("ID"), result.getString("NAME"), result.getString("CODE"),
					result.getString("ADVICE"));
			arrayList.add(object);
		}
		return arrayList;
	}
	
	public void updateToDB(Statement statement) throws SQLException{
		statement.executeUpdate(String.format(UPDATE_ROW, name, code, advice, id));
	}
	
	public static void insertToDB(Statement statement, String name, String code, String advice) throws SQLException{
		statement.executeUpdate(String.format(INSERT_TABLE, name, code, advice));
	}
	
	public static Disease getByID(Statement statement, int id) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_ID, id));
		if (result.next()){
			Disease object = new Disease(id, result.getString("NAME"), result.getString("CODE"), result.getString("ADVICE"));
			return object;
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getNameWithCode() {
		return name + " (" + code + ")";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	
	

}
