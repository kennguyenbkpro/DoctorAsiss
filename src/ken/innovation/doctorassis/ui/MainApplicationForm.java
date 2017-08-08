package ken.innovation.doctorassis.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.helper.DBHelper;
import ken.innovation.doctorassis.ui.model.PatientTableModel;
import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;

public class MainApplicationForm {

	private JFrame frame;
	
	
	private static MainApplicationForm mainApplicationForm;
	private PrescriptionTabPanel prescriptionTab;
	private JTabbedPane tabbedPane;
	private TabSearchPanel patientTab;
	private TabSearchPanel diseaseTab;
	private TabSearchPanel medicineTab;
	
	public static MainApplicationForm getInstance(){
		if (mainApplicationForm == null){
			mainApplicationForm = new MainApplicationForm();
		} 
		return mainApplicationForm;
	}
	
	public void updateDiseaseToCurrentPresciption(Disease disease){
		prescriptionTab.setDisease(disease);
		tabbedPane.setSelectedComponent(prescriptionTab);
	}
	
	public void updatePatientToCurrentPrescription(Patient patient){
		prescriptionTab.setPatient(patient);
		tabbedPane.setSelectedComponent(prescriptionTab);
	}
	
	public void updateMedicineToCurrentPrescription(Medicine medicine){
		prescriptionTab.addMedicine(medicine);
		tabbedPane.setSelectedComponent(prescriptionTab);
	}
	
	public void updateMedicineToCurrentPrescription(MediPres mediPres){
		prescriptionTab.addMediPres(mediPres);
		tabbedPane.setSelectedComponent(prescriptionTab);
	}
	
	public void switchToPatientTab(){
		tabbedPane.setSelectedComponent(patientTab);
	}
	
	public void switchToDiseaseTab(){
		tabbedPane.setSelectedComponent(diseaseTab);
	}
	
	public void switchToMedicineTab(){
		tabbedPane.setSelectedComponent(medicineTab);
	}
	
	public void updateConfig(){
		prescriptionTab.updateConfig();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e){
			e.printStackTrace();
		}
		try {
			Config.getInstance().load();
		} catch (IOException e2) {
			e2.printStackTrace();
			UIUtils.showError(e2.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			DBHelper.createTablesIfNotExists(DBConnection.getInstance().createStatement());
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cơ sở dữ liệu đang bị khóa");
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApplicationForm window = MainApplicationForm.getInstance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private MainApplicationForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(UIUtils.getImageIcon("Assistant-icon.png"));
		frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		frame.setTitle("Trợ lý bác sĩ");
		frame.setBounds(0, 0, 1080, 600);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Bạn muốn thoát ứng dụng?", "Thoát", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	try {
						DBConnection.getInstance().close();
					} catch (Exception e) {
						e.printStackTrace();
					}
		            System.exit(0);
		        }
		    }
		});
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
		frame.getContentPane().add(tabbedPane);
		
		prescriptionTab = new PrescriptionTabPanel();
		prescriptionTab.setDisease(null);
		prescriptionTab.setPatient(null);
		prescriptionTab.setViewOnly(false);
		tabbedPane.addTab("\u0110\u01A1n thu\u1ED1c", new ImageIcon(MainApplicationForm.class.getResource("/ken/innovation/doctorassis/res/paper-16.png")), prescriptionTab, null);
		
		patientTab = new PatientTabPanel();
		tabbedPane.addTab("B\u1EC7nh Nh\u00E2n", new ImageIcon(MainApplicationForm.class.getResource("/ken/innovation/doctorassis/res/user-16.png")), patientTab, null);
		
		diseaseTab = new DiseaseTabPanel();
		tabbedPane.addTab("B\u1EC7nh", new ImageIcon(MainApplicationForm.class.getResource("/ken/innovation/doctorassis/res/disease-16.png")), diseaseTab, null);
		
		medicineTab = new MedicineTabPanel();
		tabbedPane.addTab("Thu\u1ED1c", new ImageIcon(MainApplicationForm.class.getResource("/ken/innovation/doctorassis/res/medicine-16.png")), medicineTab, null);
		
		JPanel settingPanel = new SettingTabPanel();
		tabbedPane.addTab("C\u00E0i \u0111\u1EB7t", new ImageIcon(MainApplicationForm.class.getResource("/ken/innovation/doctorassis/res/settings-4-16.png")), settingPanel, null);
	}
	
}
