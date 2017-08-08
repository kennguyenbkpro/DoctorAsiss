package ken.innovation.doctorassis.dbmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.helper.DBConnection;

public class Prescription implements DBModel{
	
	public static final String CREATE_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS Prescription("
			+ "ID INT IDENTITY(1,1) PRIMARY KEY, "
			+ "PATIENT_ID INT,"
			+ "DISEASE_ID INT,"
			+ "TIME BIGINT,"
			+ "DOCTOR VARCHAR(255), "
			+ "NOTE VARCHAR(255)"
			+ ")";
	
	public static final String INSERT_TABLE = 
			"INSERT INTO Prescription ("
			+ "PATIENT_ID, DISEASE_ID, TIME, DOCTOR, NOTE) "
			+ "VALUES(%d, %d, %d, '%s', '%s')";
	
	public static final String GET_BY_ID =
			"SELECT * FROM Prescription "
			+ "WHERE ID = %d";
	
	public static final String GET_BY_PATIENT_ID =
			"SELECT * FROM Prescription "
			+ "WHERE PATIENT_ID = %d ORDER BY TIME DESC";
	
	public static final String GET_BY_DISEASE_ID =
			"SELECT * FROM Prescription "
			+ "WHERE DISEASE_ID = %d ORDER BY TIME DESC";
	
	
	public static final String UPDATE_ROW = 
			"UPDATE Prescription "
			+ "SET "
			+ "PATIENT_ID=%d,"
			+ "DISEASE_ID=%d,"
			+ "TIME=%d, "
			+ "DOCTOR='%s',"
			+ "NOTE='%s' "
			+ " WHERE ID = %d";

	private int id = -1;
	private int patientId;
	private int diseaseId;
	private long time;
	private String doctor;
	private String note;
	
	public Prescription(){}
	
	public Prescription(int id, int patientId, int diseaseId, long time, String doctor, String note) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.diseaseId = diseaseId;
		this.time = time;
		this.doctor = doctor;
		this.note = note;
	}
	
	public void updateToDB(Statement statement) throws SQLException{
		statement.executeUpdate(String.format(UPDATE_ROW, patientId, diseaseId, time, doctor, note, id));
	}
	
	public static void insertToDB(Statement statement, Prescription prescription) throws SQLException{
		statement.executeUpdate(String.format(INSERT_TABLE, prescription.patientId, prescription.diseaseId, prescription.time, prescription.doctor, prescription.note), Statement.RETURN_GENERATED_KEYS);
		ResultSet result = statement.getGeneratedKeys();
		if (result.next()){
			prescription.setId(result.getInt(1));
		}
	}
	
	public static Prescription getByID(Statement statement, int id) throws SQLException{
		ResultSet result = statement.executeQuery(String.format(GET_BY_ID, id));
		if (result.next()){
			Prescription object = new Prescription(id, result.getInt("PATIENT_ID"), result.getInt("DISEASE_ID"), result.getLong("TIME"),
					result.getString("DOCTOR"), result.getString("NOTE"));
			return object;
		}
		return null;
	}
	
	public static ArrayList<FullPrescription> getByPatientID(Statement statement, int patientID) throws Exception{
		ArrayList<FullPrescription> arrayList = new ArrayList<>();
		ResultSet result = statement.executeQuery(String.format(GET_BY_PATIENT_ID, patientID));
		Statement detailStatement = DBConnection.getInstance().createStatement();
		while (result.next()){
			Prescription object = new Prescription(result.getInt("ID"), result.getInt("PATIENT_ID"), result.getInt("DISEASE_ID"), result.getLong("TIME"),
					result.getString("DOCTOR"), result.getString("NOTE"));
			arrayList.add(new FullPrescription(detailStatement, object));
		}
		return arrayList;
	}
	
	public static ArrayList<FullPrescription> getByDiseaseID(Statement statement, int diseaseID) throws Exception{
		ArrayList<FullPrescription> arrayList = new ArrayList<>();
		ResultSet result = statement.executeQuery(String.format(GET_BY_DISEASE_ID, diseaseID));
		Statement detailStatement = DBConnection.getInstance().createStatement();
		while (result.next()){
			Prescription object = new Prescription(result.getInt("ID"), result.getInt("PATIENT_ID"), result.getInt("DISEASE_ID"), result.getLong("TIME"),
					result.getString("DOCTOR"), result.getString("NOTE"));
			arrayList.add(new FullPrescription(detailStatement, object));
		}
		return arrayList;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public int getDiseaseId() {
		return diseaseId;
	}
	public void setDiseaseId(int diseaseId) {
		this.diseaseId = diseaseId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
