package ken.innovation.doctorassis.dbmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Medicine implements DBModel{

	public static final String CREATE_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS Medicine("
			+ "ID INT IDENTITY(1,1) PRIMARY KEY, "
			+ "NAME VARCHAR(255), "
			+ "INGREDIENT VARCHAR(255), "
			+ "DOSE VARCHAR(255), "
			+ "USING VARCHAR(255),"
			+ "UNIT VARCHAR(255)"
			+ ")";
	
	public static final String DROP_TABLE = ""
			+ "DROP TABLE Medicine";
	
	public static final String INSERT_TABLE = 
			"INSERT INTO Medicine ("
			+ "NAME, INGREDIENT, DOSE, USING, UNIT) "
			+ "VALUES('%s', '%s', '%s', '%s', '%s')";
	
	public static final String GET_BY_ID =
			"SELECT * FROM Medicine "
			+ "WHERE ID = %d";
	
	public static final String GET_BY_NAME_OR_INGREDIENT =
			"SELECT * FROM Medicine "
			+ "WHERE LOWER(NAME) LIKE LOWER('%%%s%%')"
			+ " OR LOWER(INGREDIENT) LIKE LOWER('%%%s%%')";
	
	public static final String UPDATE_ROW = 
			"UPDATE Medicine "
			+ "SET "
			+ "NAME='%s',"
			+ "INGREDIENT='%s', "
			+ "DOSE='%s',"
			+ "USING='%s',"
			+ "UNIT='%s' "
			+ " WHERE ID = %d";
	
	private int id;
	private String name;
	private String ingredient;
	private String dose;
	private String using;
	private String unit;
	
	public Medicine(){}
	
	public Medicine(int id, String name, String ingredient, String dose, String using, String unit) {
		this.id = id;
		this.name = name;
		this.ingredient = ingredient;
		this.dose = dose;
		this.using = using;
		this.unit = unit;
	}
	
	public void updateToDB(Statement statement) throws SQLException{
		statement.executeUpdate(String.format(UPDATE_ROW, name, ingredient, dose, using, unit, id));
	}
	
	public static void insertToDB(Statement statement, ArrayList<Medicine> medicines) throws SQLException{
		for (Medicine medicine : medicines){
			insertToDB(statement, medicine);
		}
	}
	
	public static void insertToDB(Statement statement, Medicine medicine) throws SQLException{
		insertToDB(statement, medicine.name, medicine.ingredient, medicine.dose, medicine.using, medicine.unit);
	}
	
	public static void insertToDB(Statement statement, String name, String ingredient, String dose, String using, String unit) throws SQLException{
		statement.executeUpdate(String.format(INSERT_TABLE, name, ingredient, dose, using, unit));
	}
	
	public static Medicine getByID(Statement statement, int id) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_ID, id));
		if (result.next()){
			Medicine medicine = new Medicine(id, result.getString("NAME"), result.getString("INGREDIENT"),
					result.getString("DOSE"), result.getString("USING"), result.getString("UNIT"));
			return medicine;
		}
		return null;
	}
	
	public static ArrayList<DBModel> searchMedicineByName(Statement statement, String name) throws SQLException{
		ArrayList<DBModel> arrayList = new ArrayList<>();
		ResultSet result = statement.executeQuery(String.format(GET_BY_NAME_OR_INGREDIENT, name, name));
		while (result.next()){
			Medicine object = new Medicine(result.getInt("ID"), result.getString("NAME"), result.getString("INGREDIENT"),
					result.getString("DOSE"), result.getString("USING"), result.getString("UNIT"));
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

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public String getUsing() {
		return using;
	}

	public void setUsing(String using) {
		this.using = using;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
