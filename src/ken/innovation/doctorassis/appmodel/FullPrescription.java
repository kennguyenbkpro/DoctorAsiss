package ken.innovation.doctorassis.appmodel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.Prescription;

public class FullPrescription {
	private Prescription prescription;
	private Disease disease;
	private Patient patient;
	private ArrayList<MediPres> listMedicine;
	
	
	public FullPrescription(){
		listMedicine = new ArrayList<>();
		prescription = new Prescription();
	}

	public FullPrescription(Statement statement, Prescription prescription) throws SQLException {
		this.prescription = prescription;
		disease = Disease.getByID(statement, prescription.getDiseaseId());
		patient = Patient.getByID(statement, prescription.getPatientId());
		listMedicine = MediPres.getByPresID(statement, prescription.getId());
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public ArrayList<MediPres> getListMedicine() {
		return listMedicine;
	}

	public void setListMedicine(ArrayList<MediPres> listMedicine) {
		this.listMedicine = listMedicine;
	}
	
	
	
	
}
