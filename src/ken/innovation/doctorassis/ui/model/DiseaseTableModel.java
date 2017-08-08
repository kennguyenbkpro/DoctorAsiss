package ken.innovation.doctorassis.ui.model;

import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Disease;

public class DiseaseTableModel extends SmartTableModel{
	private String[] columnName = {
			"STT", "Tên bệnh", "Mã bệnh", "Lời dặn"
	};
	
	public DiseaseTableModel() {
		super();
		setColumnName(columnName);
	}
	
	@Override
	protected Object getValueAt(DBModel model, int columnIndex, int rowIndex) {
		Disease disease = (Disease) model;
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return disease.getName();
		case 2:
			return disease.getCode();
		case 3:
			return disease.getAdvice();
		default:
			return "";
		}
	}
	
	@Override
	public int[] getColumnWidth() {
		return new int[]{40, 220, 100};
	}

}
