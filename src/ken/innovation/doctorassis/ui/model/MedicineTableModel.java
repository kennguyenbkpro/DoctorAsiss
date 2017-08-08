package ken.innovation.doctorassis.ui.model;

import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.ui.TabSearchPanel;

public class MedicineTableModel extends SmartTableModel{
	private String[] columnName = {
			"STT", "Tên thuốc", "Thành phần chính", "Liều dùng", "Đường dùng", "Đơn vị"
	};

	public MedicineTableModel() {
		super();
		setColumnName(columnName);
	}

	@Override
	protected Object getValueAt(DBModel model, int columnIndex, int rowIndex) {
		Medicine medicine = (Medicine) model;
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return medicine.getName();
		case 2:
			return medicine.getIngredient();
		case 3:
			return medicine.getDose();
		case 4:
			return medicine.getUsing();
		default:
			return medicine.getUnit();
		}
	}
	
	@Override
	public int[] getColumnWidth() {
		return new int[]{40, 220, 200, 200, 80};
	}

}
