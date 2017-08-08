package ken.innovation.doctorassis.ui.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ken.innovation.doctorassis.dbmodel.DBModel;

public abstract class SmartTableModel extends GeneralTableModel{
	private String[] columnName;
	private ArrayList<DBModel> listData;
	
	public SmartTableModel() {
		super();
	}
	
	

	public void setColumnName(String[] columnName) {
		this.columnName = columnName;
	}

	public void setListData(ArrayList<DBModel> listData) {
		this.listData = listData;
	}
	
	public DBModel getData(int row){
		if (listData != null && row >= 0 && row < listData.size()){
			return listData.get(row);
		}
		return null;
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
		if (listData != null){
			return getValueAt(listData.get(rowIndex), columnIndex, rowIndex);
		}
		return null;
	}
	
	
	
	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}



	protected abstract Object getValueAt(DBModel model, int columnIndex, int rowIndex);

}
