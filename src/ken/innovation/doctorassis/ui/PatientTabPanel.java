package ken.innovation.doctorassis.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.PatientResource;
import ken.innovation.doctorassis.dbmodel.Prescription;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.helper.MedicineImportHelper;
import ken.innovation.doctorassis.helper.PatientResourceImportHelper;
import ken.innovation.doctorassis.ui.PrescriptionViewer.CONTENT_MODE;
import ken.innovation.doctorassis.ui.model.PatientTableModel;
import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;

public class PatientTabPanel extends TabSearchPanel implements DataChangeListener{
	private PatientForm patientForm;
	private PatientTableModel model;
	private int selectedRow = 0;
	private JButton historyButton;
	private JButton updateExcelButton;
	final JFileChooser fc = new JFileChooser();
	/**
	 * Create the panel.
	 */
	public PatientTabPanel() {
		addButton.setHorizontalAlignment(SwingConstants.RIGHT);
		SpringLayout springLayout = (SpringLayout) panel_1.getLayout();
		updateToPres.setEnabled(false);
		updateToPres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Patient patient = (Patient) model.getData(selectedRow);
				MainApplicationForm.getInstance().updatePatientToCurrentPrescription(patient);
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateToPres.setEnabled(false);
				historyButton.setEnabled(false);
				if (TextUtils.isEmpty(searchTextField.getText().trim())) return;
				try {
					ArrayList<DBModel> results = Patient.searchPatientByName(DBConnection.getInstance().createStatement(), searchTextField.getText().trim());
					model.setListData(results);
					model.fireTableDataChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		patientForm = PatientForm.getInstance();
		patientForm.setDataChangeListener(this);
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				patientForm.setPatient(null);
				patientForm.setPatientName(searchTextField.getText());
				patientForm.setVisible(true);
			}
		});
		model = new PatientTableModel();
		table.setModel(model);
		
		historyButton = new JButton("L\u1ECBch s\u1EED b\u1EC7nh");
		historyButton.setEnabled(false);
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Patient patient = (Patient) model.getData(selectedRow);
				int patientId = patient.getId();
				try {
					ArrayList<FullPrescription> data = Prescription.getByPatientID(DBConnection.getInstance().createStatement(), patientId);
					PrescriptionViewer.getInstance().setTitle("Bệnh nhân: " + patient.getName());
					PrescriptionViewer.getInstance().showData(data, CONTENT_MODE.PATIENT_MODE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, historyButton, 0, SpringLayout.NORTH, updateToPres);
		springLayout.putConstraint(SpringLayout.WEST, historyButton, 7, SpringLayout.EAST, updateToPres);
		panel_1.add(historyButton);
		
		updateExcelButton = new JButton("Cập nhật dữ liệu excel");
		updateExcelButton.setIcon(new ImageIcon(PatientTabPanel.class.getResource("/ken/innovation/doctorassis/res/excel16.png")));
		updateExcelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!TextUtils.isEmpty(Config.getInstance().lastXlsxFile)){
					fc.setCurrentDirectory(new File(Config.getInstance().lastXlsxFile));
				}
				int returnVal = fc.showOpenDialog(PatientTabPanel.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	try {
		        		DBConnection.getInstance().createStatement().execute(PatientResource.DROP_TABLE);
						DBConnection.getInstance().createStatement().execute(PatientResource.CREATE_TABLE);
		        	}catch (Exception e){
		        		UIUtils.showError(e.getMessage());
		        		return;
		        	}
		            File file = fc.getSelectedFile();
		            Config.getInstance().lastXlsxFile = file.getPath();
		            try {
						Config.getInstance().save();
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
		            try {
						ArrayList<PatientResource> listPatientResources = PatientResourceImportHelper.loadPatientResourceFromExcel(file);
						PatientResource.insertToDB(DBConnection.getInstance().createStatement(), listPatientResources);
						JOptionPane.showMessageDialog(PatientTabPanel.this, "Thêm thành công");
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.showError(e.getMessage());
					}
		        }
			}
		});
		
		panel.add(updateExcelButton);
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        selectedRow = table.rowAtPoint(p);
		        updateToPres.setEnabled(true);
		        historyButton.setEnabled(true);
		        if (me.getClickCount() == 2) {
		            patientForm.setPatient((Patient) model.getData(selectedRow)); 
		            patientForm.setVisible(true);
		        }
		    }
		});
	}
	@Override
	public void onDataChange() {
		model.fireTableDataChanged();
	}
}
