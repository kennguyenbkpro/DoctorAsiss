package ken.innovation.doctorassis.dbmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patient implements DBModel{
	public static String getTableName(){
		return "Patient";
	}
	
	public static final String CREATE_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS " + getTableName() + "("
			+ "ID INT IDENTITY(1,1) PRIMARY KEY, "
			+ "NAME VARCHAR(255), "
			+ "ADDRESS VARCHAR(255),"
			+ "YEAR INT,"
			+ "PHONE VARCHAR(16),"
			+ "GENDER INT,"
			+ "INSUARANCE VARCHAR(32),"
			+ ")";
	
	public static final String MODIFY = ""
			+ "ALTER TABLE " + getTableName() + " "
			+ "ALTER COLUMN INSUARANCE VARCHAR(32)";
	
	public static final String INSERT_TABLE = 
			"INSERT INTO " + getTableName() + " ("
			+ "NAME, ADDRESS, YEAR, PHONE, GENDER, INSUARANCE) "
			+ "VALUES('%s', '%s', %d, '%s', %d, '%s')";
	
	public static final String GET_BY_ID =
			"SELECT * FROM " + getTableName() + " "
			+ "WHERE ID = %d";
	
	public static final String GET_BY_NAME =
			"SELECT * FROM " + getTableName() + " "
			+ "WHERE LOWER(NAME) LIKE LOWER('%%%s%%') ORDER BY ID DESC";
	
	
	public static final String UPDATE_ROW = 
			"UPDATE " + getTableName() + " "
			+ "SET "
			+ "NAME='%s',"
			+ "ADDRESS='%s',"
			+ "YEAR=%d, "
			+ "PHONE='%s',"
			+ "GENDER=%d, "
			+ "INSUARANCE='%s' "
			+ "WHERE ID = %d";

	protected int id;
	protected String name;
	protected String address;
	protected int yearOfBirth;
	protected String phoneNumber;
	protected int gender;
	protected String insuranceNumber;
	
	public Patient(){}
	
	public Patient(int id, String name, String address, int yearOfBirth, String phoneNumber, int gender,
			String insuranceNumber) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.yearOfBirth = yearOfBirth;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.insuranceNumber = insuranceNumber;
	}
	
	public void updateToDB(Statement statement) throws SQLException{
		statement.executeUpdate(String.format(UPDATE_ROW, name, address, yearOfBirth, phoneNumber, gender, insuranceNumber, id));
	}
	
	public static void insertToDB(Statement statement, String name, String address, int yearOfBirth, String phoneNumber, int gender,
			String insuranceNumber) throws SQLException{
		statement.executeUpdate(String.format(INSERT_TABLE, name, address, yearOfBirth, phoneNumber, gender, insuranceNumber));
	}
	
	public static Patient getByID(Statement statement, int id) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_ID, id));
		if (result.next()){
			Patient object = new Patient(id, result.getString("NAME"), result.getString("ADDRESS"), result.getInt("YEAR"), 
					result.getString("PHONE"), result.getInt("GENDER"), result.getString("INSUARANCE"));
			return object;
		}
		return null;
	}
	
	public static ArrayList<DBModel> searchPatientByName(Statement statement, String name) throws SQLException{
		ArrayList<DBModel> arrayList = new ArrayList<>();
		ResultSet result = statement.executeQuery(String.format(GET_BY_NAME, name));
		while (result.next()){
			Patient object = new Patient(result.getInt("ID"), result.getString("NAME"), result.getString("ADDRESS"), result.getInt("YEAR"), 
					result.getString("PHONE"), result.getInt("GENDER"), result.getString("INSUARANCE"));
			arrayList.add(object);
		}
		return arrayList;
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
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getYearOfBirth() {
		return yearOfBirth;
	}
	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getInsuranceNumber() {
		return insuranceNumber;
	}
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	
	
}
