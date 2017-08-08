package ken.innovation.doctorassis.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import ken.innovation.doctorassis.ui.model.GeneralTableModel;

public class AutoResizeTable extends JTable{
	
	public AutoResizeTable() {
		super();
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void setModel(GeneralTableModel dataModel) {
		super.setModel(dataModel);
		int[] columnWidth = dataModel.getColumnWidth();
		int size = dataModel.getColumnCount();
		for (int i = 0; i < columnWidth.length; i ++){
			if (i >= size) break;
			getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
		}
	}
}
