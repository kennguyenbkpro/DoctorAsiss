package ken.innovation.doctorassis.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.PatientResource;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class PatientForm extends JFrame {
	
	private static PatientForm patientForm;
	
	public static PatientForm getInstance(){
		if (patientForm == null){
			patientForm = new PatientForm();
		}
		return patientForm;
	}
	
	private DataChangeListener dataChangeListener;

	private JPanel contentPane;

	private JTextField nameText;
	private JTextField yearText;
	private JTextField addressText;
	private JTextField phoneText;
	private JTextField insuaText;
	private final ButtonGroup sexButtonGroup = new ButtonGroup();
	private JLabel toastLabel;
	
	private JRadioButton manRadBut;
	private JRadioButton womanRadBut;
	private JRadioButton otherRadBut;
	
	private Patient patient;
	private JButton addPatient;
	private JButton findExcelButton;
	
	public void setDataChangeListener(DataChangeListener listener){
		this.dataChangeListener = listener;
	}
	
	public void setPatient(Patient patient) {
		toastLabel.setText("");
		this.patient = patient;
		if (patient == null){
			clearText();
		} else {
			nameText.setText(patient.getName());
			nameText.setEnabled(false);
			yearText.setText(patient.getYearOfBirth() + "");
			addressText.setText(patient.getAddress());
			phoneText.setText(patient.getPhoneNumber());
			insuaText.setText(patient.getInsuranceNumber());
			sexButtonGroup.clearSelection();
			if (patient.getGender() == 0){
				manRadBut.setSelected(true);
			} else if (patient.getGender() == 1){
				womanRadBut.setSelected(true);
			} else {
				otherRadBut.setSelected(true);
			}
			addPatient.setText("Cập nhật");
		}
	}
	
	public void fillPatient(Patient patient) {
		toastLabel.setText("");
		if (patient != null){
			nameText.setText(patient.getName());
			yearText.setText(patient.getYearOfBirth() + "");
			addressText.setText(patient.getAddress());
			phoneText.setText(patient.getPhoneNumber());
			insuaText.setText(patient.getInsuranceNumber());
			sexButtonGroup.clearSelection();
			if (patient.getGender() == 0){
				manRadBut.setSelected(true);
			} else if (patient.getGender() == 1){
				womanRadBut.setSelected(true);
			} else {
				otherRadBut.setSelected(true);
			}
			addPatient.setText("Thêm bệnh nhân");
		}
	}
	
	public void setPatientName(String name){
		nameText.setText(name);
	}

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PatientForm frame = new PatientForm();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	private PatientForm() {
		setIconImage(UIUtils.getImageIcon("user-16.png"));
		setTitle("Bệnh nhân");
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        clearText();
		    }
		});
		setBounds(100, 100, 300, 358);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 287, 316);
		contentPane.add(panel);
		panel.setLayout(new MigLayout("", "[][][grow][grow]", "[][][][][][][][][][][][]"));
		
		JLabel lblTn = new JLabel("T\u00EAn (*)");
		panel.add(lblTn, "cell 0 0");
		
		nameText = new JTextField();
		panel.add(nameText, "cell 2 0,growx");
		nameText.setColumns(10);
		
		JLabel lblNmSinh = new JLabel("N\u0103m sinh (*)");
		panel.add(lblNmSinh, "cell 0 1");
		
		yearText = new JTextField();
		panel.add(yearText, "cell 2 1,growx");
		yearText.setColumns(10);
		
		JLabel lblaCh = new JLabel("\u0110\u1ECBa ch\u1EC9");
		panel.add(lblaCh, "cell 0 2");
		
		addressText = new JTextField();
		panel.add(addressText, "cell 2 2,growx");
		addressText.setColumns(10);
		
		JLabel lblinThoi = new JLabel("\u0110i\u1EC7n tho\u1EA1i");
		panel.add(lblinThoi, "cell 0 3");
		
		phoneText = new JTextField();
		panel.add(phoneText, "cell 2 3,growx");
		phoneText.setColumns(10);
		
		JLabel lblGiiTnh = new JLabel("Gi\u1EDBi t\u00EDnh (*)");
		panel.add(lblGiiTnh, "cell 0 4");
		
		manRadBut = new JRadioButton("Nam");
		sexButtonGroup.add(manRadBut);
		panel.add(manRadBut, "flowx,cell 2 4");
		
		JLabel lblSThBhxh = new JLabel("S\u1ED1 th\u1EBB BHXH");
		panel.add(lblSThBhxh, "cell 0 5");
		
		insuaText = new JTextField();
		panel.add(insuaText, "cell 2 5,growx");
		insuaText.setColumns(10);
		
		womanRadBut = new JRadioButton("N\u1EEF");
		sexButtonGroup.add(womanRadBut);
		panel.add(womanRadBut, "cell 2 4");
		
		otherRadBut = new JRadioButton("Kh\u00E1c");
		sexButtonGroup.add(otherRadBut);
		panel.add(otherRadBut, "cell 2 4");
		
		toastLabel = new JLabel("\u0110i\u1EC1n \u0111\u1EE7 c\u00E1c tr\u01B0\u1EDDng c\u00F3 d\u1EA5u (*)");
		panel.add(toastLabel, "cell 0 7 3 1");
		
		addPatient = new JButton("Th\u00EAm b\u1EC7nh nh\u00E2n");
		addPatient.setMinimumSize(new Dimension(140, 23));
		addPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (patient == null){
					if (inseartPatient()){
						clearText();
						toastLabel.setText("Insert successfully!");
					} else {
						toastLabel.setText("Insert failed!");
					}
				} else {
					if (updatePatient()){
						clearText();
						toastLabel.setText("Update successfully!");
						if (dataChangeListener != null){
							dataChangeListener.onDataChange();
						}
					} else {
						toastLabel.setText("Update failed!");
					}
				}
			}
		});
		panel.add(addPatient, "cell 1 9 2 1");
		
		findExcelButton = new JButton("Tìm từ dữ liệu excel");
		findExcelButton.setIcon(new ImageIcon(PatientForm.class.getResource("/ken/innovation/doctorassis/res/excel16.png")));
		findExcelButton.setMinimumSize(new Dimension(140, 23));
		findExcelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = nameText.getText().trim();
				if (!TextUtils.isEmpty(name)){
					PatientResourceChooser.getInstance().searchKeyword(nameText.getText());
					PatientResourceChooser.getInstance().setVisible(true);
				} else {
					UIUtils.showError("Tên bệnh nhân không được để trống!");
				}
			}
		});
		panel.add(findExcelButton, "cell 1 11 2 1");
	}
	
	private boolean inseartPatient() {
		Patient patient = new Patient();
		if (!updatePatient(patient)) return false;
		try {
			Patient.insertToDB(DBConnection.getInstance().createStatement(), patient.getName(), patient.getAddress(), patient.getYearOfBirth(), 
					patient.getPhoneNumber(), patient.getGender(), patient.getInsuranceNumber());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError(e.getMessage());
		}
		return false;
	}
	
	private boolean updatePatient(){
		if (!updatePatient(patient)) return false;
		try {
			patient.updateToDB(DBConnection.getInstance().createStatement());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError(e.getMessage());
		}
		return false;
	}
	
	private boolean updatePatient(Patient patient){
		patient.setName(nameText.getText());
		if (TextUtils.isEmpty(patient.getName())){
			return false;
		}
		try {
			patient.setYearOfBirth(Integer.valueOf(yearText.getText()));
		} catch (NumberFormatException e){
			return false;
		}
		if (manRadBut.isSelected()){
			patient.setGender(0);
		} else if (womanRadBut.isSelected()){
			patient.setGender(1);
		} else if (otherRadBut.isSelected()){
			patient.setGender(2);
		} else {
			return false;
		}
		
		patient.setAddress(addressText.getText());
		patient.setPhoneNumber(phoneText.getText());
		patient.setInsuranceNumber(insuaText.getText());
		return true;
	}
	
	private void clearText(){
		patient = null;
		nameText.setText("");
		nameText.setEnabled(true);
		addressText.setText("");
		yearText.setText("");
		phoneText.setText("");
		insuaText.setText("");
		sexButtonGroup.clearSelection();
		addPatient.setText("Thêm mới");
	}

}
