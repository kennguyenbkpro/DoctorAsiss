package ken.innovation.doctorassis.dbmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MediPres implements DBModel{
	
	public static final String CREATE_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS MediPres("
			+ "ID INT IDENTITY(1,1) PRIMARY KEY, "
			+ "MED_ID  VARCHAR(255), "
			+ "PRES_ID INT,"
			+ "DOSE VARCHAR(255), "
			+ "USING VARCHAR(255), "
			+ "AMOUNT INT, "
			+ "UNIT VARCHAR(15)"
			+ ")";
	
	public static final String INSERT_TABLE = 
			"INSERT INTO MediPres ("
			+ "MED_ID, PRES_ID, DOSE, USING, AMOUNT, UNIT) "
			+ "VALUES('%s', %d, '%s', '%s', %d, '%s')";
	
	public static final String GET_BY_ID =
			"SELECT * FROM MediPres "
			+ "WHERE ID = %d";
	
	public static final String GET_BY_PRES_ID =
			"SELECT * FROM MediPres "
			+ "WHERE PRES_ID = %d";
	
	public static final String DELETE_BY_ID = 
			"DELETE FROM MediPres "
			+ "WHERE ID = %d";
	
	
	public static final String UPDATE_ROW = 
			"UPDATE MediPres "
			+ "SET "
			+ "MED_ID=%s, "
			+ "PRES_ID=%d, "
			+ "DOSE='%s',"
			+ "USING='%s',"
			+ "AMOUNT=%d,"
			+ "UNIT='%s' "
			+ "WHERE ID = %d";

	private int id = -1;
	private String medicine;
	private int prescriptionId;
	private String dose;
	private String using;
	private int amount;
	private String unit;
	
	public MediPres(){}
	
	public MediPres(Medicine medicine){
		this.medicine = medicine.getName() + " (" + medicine.getIngredient() + ")";
		this.dose = medicine.getUsing() + ": " + medicine.getDose();
		this.unit = medicine.getUnit();
	}
	
	public MediPres(int id, String medicine, int prescriptionId, String dose, String using, int amount, String unit) {
		super();
		this.id = id;
		this.medicine = medicine;
		this.prescriptionId = prescriptionId;
		this.dose = dose;
		this.using = using;
		this.amount = amount;
		this.unit = unit;
	}
	
	public static void delete(Statement statement, int id) throws SQLException{
		statement.executeUpdate(String.format(DELETE_BY_ID, id));
	}
	
	public void updateToDB(Statement statement) throws SQLException{
		statement.executeUpdate(String.format(UPDATE_ROW, medicine, prescriptionId, dose, using, amount, unit, id));
	}
	
	public static void insertToDB(Statement statement, MediPres mediPres) throws SQLException{
		statement.executeUpdate(String.format(INSERT_TABLE, mediPres.medicine, mediPres.prescriptionId, mediPres.dose, mediPres.using, mediPres.amount, mediPres.unit), Statement.RETURN_GENERATED_KEYS);
		ResultSet result = statement.getGeneratedKeys();
		if (result.next()){
			mediPres.setId(result.getInt(1));
		}
	}
	
	public static MediPres getByID(Statement statement, int id) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_ID, id));
		if (result.next()){
			MediPres object = new MediPres(id, result.getString("MED_ID"), result.getInt("PRES_ID"),
					result.getString("DOSE"), result.getString("USING"), result.getInt("AMOUNT"), result.getString("UNIT"));
			return object;
		}
		return null;
	}
	
	public static ArrayList<MediPres> getByPresID(Statement statement, int presId) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_PRES_ID, presId));
		ArrayList<MediPres> listReturn = new ArrayList<>();
		while (result.next()){
			MediPres object = new MediPres(result.getInt("ID"), result.getString("MED_ID"), presId,
					result.getString("DOSE"), result.getString("USING"), result.getInt("AMOUNT"), result.getString("UNIT"));
			listReturn.add(object);
		}
		return listReturn;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMedicine() {
		return medicine;
	}
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	public int getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId = prescriptionId;
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
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
