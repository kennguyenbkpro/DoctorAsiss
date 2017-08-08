package ken.innovation.doctorassis.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;

import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.ui.model.MedicineTableModel;
import ken.innovation.doctorassis.ui.model.PatientTableModel;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;

public class MedicineTabPanel extends TabSearchPanel implements DataChangeListener{
	private MedicineForm medicineForm;
	private MedicineTableModel model;
	private int selectedRow = 0;
	/**
	 * Create the panel.
	 */
	public MedicineTabPanel() {
		super();
		updateToPres.setEnabled(false);
		updateToPres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainApplicationForm.getInstance().updateMedicineToCurrentPrescription((Medicine) model.getData(selectedRow));
			}
		});
		
		medicineForm = new MedicineForm();
		medicineForm.setDataChangeListener(this);
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onSearch();
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				medicineForm.setMedicine(null);
				medicineForm.setVisible(true);
				medicineForm.setMedicineName(searchTextField.getText());
			}
		});
		model = new MedicineTableModel();
		table.setModel(model);
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        selectedRow = table.rowAtPoint(p);
		        updateToPres.setEnabled(true);
		        if (me.getClickCount() == 2) {
		        	medicineForm.setMedicine((Medicine) model.getData(selectedRow)); 
		        	medicineForm.setVisible(true);
		        }
		    }
		});
	}
	
	private void onSearch(){
		updateToPres.setEnabled(false);
		if (TextUtils.isEmpty(searchTextField.getText().trim())) return;
		try {
			ArrayList<DBModel> results = Medicine.searchMedicineByName(DBConnection.getInstance().createStatement(), searchTextField.getText().trim());
			model.setListData(results);
			model.fireTableDataChanged();
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError(e.getMessage());
		}
	}
	
	@Override
	public void onDataChange() {
		onSearch();
	}

}
