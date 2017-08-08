package ken.innovation.doctorassis.ui.model;

import javax.swing.table.DefaultTableModel;

import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.utils.TextUtils;

public class PatientTableModel extends SmartTableModel {
	private String[] columnName = {
		"STT", "Tên", "Năm sinh", "Địa chỉ", "Điện thoại", "Giới tính", "Số thẻ BH"
	};

	public PatientTableModel() {
		super();
		setColumnName(columnName);
	}

	@Override
	protected Object getValueAt(DBModel model, int columnIndex, int rowIndex) {
		Patient patient = (Patient) model;
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return patient.getName();
		case 2:
			return patient.getYearOfBirth();
		case 3:
			return patient.getAddress();
		case 4: 
			return patient.getPhoneNumber();
		case 5:
			return TextUtils.convertSex(patient.getGender());
		case 6:
			return patient.getInsuranceNumber();
		default:
			return "";
		}
	}
	
	@Override
	public int[] getColumnWidth() {
		return new int[]{30, 160, 60, 300, 100, 60};
	}
	
}
