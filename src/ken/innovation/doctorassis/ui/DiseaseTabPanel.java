package ken.innovation.doctorassis.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.Prescription;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.ui.PrescriptionViewer.CONTENT_MODE;
import ken.innovation.doctorassis.ui.model.DiseaseTableModel;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;
import javax.swing.JButton;
import javax.swing.SpringLayout;

public class DiseaseTabPanel extends TabSearchPanel implements DataChangeListener{
	private DiseaseForm diseaseForm;
	private DiseaseTableModel model;
	private int selectedRow = 0;
	private JButton reuseButton;
	/**
	 * Create the panel.
	 */
	public DiseaseTabPanel() {
		super();
		SpringLayout springLayout = (SpringLayout) panel_1.getLayout();
		updateToPres.setEnabled(false);
		updateToPres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Disease disease = (Disease) model.getData(selectedRow);
				MainApplicationForm.getInstance().updateDiseaseToCurrentPresciption(disease);
			}
		});
		model = new DiseaseTableModel();
		diseaseForm = DiseaseForm.getInstance();
		diseaseForm.setDataChangeListener(this);
		table.setModel(model);
		
		reuseButton = new JButton("\u0110\u01A1n thu\u1ED1c c\u00F3 s\u1EB5n");
		reuseButton.setEnabled(false);
		reuseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Disease disease = (Disease) model.getData(selectedRow);
				int diseaseId = disease.getId();
				try {
					ArrayList<FullPrescription> data = Prescription.getByDiseaseID(DBConnection.getInstance().createStatement(), diseaseId);
					PrescriptionViewer.getInstance().setTitle("Bệnh: " + disease.getName());
					PrescriptionViewer.getInstance().showData(data, CONTENT_MODE.DISEASE_MODE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, reuseButton, 6, SpringLayout.EAST, updateToPres);
		springLayout.putConstraint(SpringLayout.SOUTH, reuseButton, 0, SpringLayout.SOUTH, updateToPres);
		panel_1.add(reuseButton);
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				diseaseForm.setDisease(null);
				diseaseForm.setDiseaseName(searchTextField.getText());
				diseaseForm.setVisible(true);
			}
		});
		
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				updateToPres.setEnabled(false);
				reuseButton.setEnabled(false);
				if (TextUtils.isEmpty(searchTextField.getText().trim())) return;
				try {
					ArrayList<DBModel> results = Disease.searchDiseaseByName(DBConnection.getInstance().createStatement(), searchTextField.getText().trim());
					model.setListData(results);
					model.fireTableDataChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        selectedRow = table.rowAtPoint(p);
		        reuseButton.setEnabled(true);
		        updateToPres.setEnabled(true);
		        if (me.getClickCount() == 2) {
		        	diseaseForm.setDisease((Disease) model.getData(selectedRow)); 
		        	diseaseForm.setVisible(true);
		        }
		    }
		});
		
	}

	@Override
	public void onDataChange() {
		model.fireTableDataChanged();
	}
}
