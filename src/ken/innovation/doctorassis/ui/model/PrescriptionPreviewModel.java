package ken.innovation.doctorassis.ui.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.ui.PrescriptionViewer.CONTENT_MODE;
import ken.innovation.doctorassis.utils.TextUtils;

public class PrescriptionPreviewModel extends GeneralTableModel{
	
	private CONTENT_MODE contentMode;
	
	private String[] columnName = {"STT", "Ngày", "Nội dung"};
	
	private ArrayList<FullPrescription> listData;
	
	public PrescriptionPreviewModel(){
		super();
	}
	
	public void setListData(ArrayList<FullPrescription> listData, CONTENT_MODE mode) {
		this.listData = listData;
		contentMode = mode;
	}

	@Override
	public int getColumnCount() {
		if (columnName != null){
			return columnName.length;
		}
		return 0;
	}

	@Override
	public int getRowCount() {
		if (listData != null){
			return listData.size();
		}
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		FullPrescription fullPrescription = listData.get(rowIndex);
		if (columnIndex == 0){
			return rowIndex + 1;
		} else if (columnIndex == 1){
			return TextUtils.getDateStringFrom(fullPrescription.getPrescription().getTime());
		} else if (columnIndex == 2){
			if (contentMode == CONTENT_MODE.PATIENT_MODE){
				return fullPrescription.getDisease().getName();
			} else if (contentMode == CONTENT_MODE.DISEASE_MODE){
				return fullPrescription.getPatient().getName();
			}
		}
		return "";
	}
	
	public FullPrescription getData(int index){
		return listData.get(index);
	}

	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}

	@Override
	public int[] getColumnWidth() {
		return new int[]{30, 80, 190};
	}
}
