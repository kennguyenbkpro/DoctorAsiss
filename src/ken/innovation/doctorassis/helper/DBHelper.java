package ken.innovation.doctorassis.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.PatientResource;
import ken.innovation.doctorassis.dbmodel.Prescription;

public class DBHelper {
	public static void createTablesIfNotExists(Statement statement) throws SQLException{
		statement.execute(Disease.CREATE_TABLE);
		statement.execute(Medicine.CREATE_TABLE);
		statement.execute(Patient.CREATE_TABLE);
		statement.execute(Prescription.CREATE_TABLE);
		statement.execute(MediPres.CREATE_TABLE);
		statement.execute(PatientResource.CREATE_TABLE);
		
		statement.execute(Patient.MODIFY);
	}
}
