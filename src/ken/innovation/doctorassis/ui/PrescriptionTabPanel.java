package ken.innovation.doctorassis.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.Prescription;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.helper.PrescriptionExportHelper;
import ken.innovation.doctorassis.ui.MediPresItem.OnCloseButtonClick;
import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.print.DocFlavor;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;

public class PrescriptionTabPanel extends JPanel implements OnCloseButtonClick {
	private FullPrescription fullPrescription;
	private JButton patientNameText;
	private JLabel patientYearText;
	private JLabel patientGenderText;
	private JLabel patientAddressText;
	private JLabel patientPhoneText;
	private JLabel insuaText;
	private JButton diseaseNameText;
	private JLabel diseaseCodeText;
	private JTextArea adviceText;
	
	private ArrayList<MediPresItem> listRecycledMedipres;
	private JPanel medipresContainer;
	private JTextArea noteText;
	private JButton newButton;
	private JButton saveButton;
	private JButton reuseButton;
	private JButton addMedicineButton;
	private JLabel timeText;
	private JLabel doctorText;
	private boolean viewOnly;
	private JButton openExccelBtn;
	
	public void setFullPrescription(FullPrescription fullPrescription){
		Component[] components = medipresContainer.getComponents();
		for (Component component : components){
			if (component instanceof MediPresItem){
				medipresContainer.remove(component);
				listRecycledMedipres.add((MediPresItem) component);
			}
		}
		this.fullPrescription.getListMedicine().clear();
		if (fullPrescription == null){
			reuseButton.setEnabled(false);
			openExccelBtn.setEnabled(false);
			setDisease(null);
			setPatient(null);
			noteText.setText(null);
			timeText.setText("Ngày " + TextUtils.getDateStringFrom(System.currentTimeMillis()));
			doctorText.setText(Config.getInstance().doctor);
		} else {
			reuseButton.setEnabled(true);
			openExccelBtn.setEnabled(true);
			setDisease(fullPrescription.getDisease());
			setPatient(fullPrescription.getPatient());
			ArrayList<MediPres> listMedicines = fullPrescription.getListMedicine();
			for (MediPres mediPres : listMedicines){
				addMediPres(mediPres);
			}
			medipresContainer.revalidate();
			
			this.fullPrescription.getPrescription().setId(fullPrescription.getPrescription().getId());
			noteText.setText(fullPrescription.getPrescription().getNote());
			timeText.setText("Ngày " + TextUtils.getDateStringFrom(fullPrescription.getPrescription().getTime()));
			doctorText.setText(fullPrescription.getPrescription().getDoctor());
			this.fullPrescription.getListMedicine().addAll(fullPrescription.getListMedicine());
		}
	}
	
	public void setDisease(Disease disease){
		fullPrescription.setDisease(disease);
		if (disease == null){
			diseaseNameText.setText(null);
			diseaseCodeText.setText(null);
			adviceText.setText(null);
		} else {
			diseaseNameText.setText(disease.getName());
			diseaseCodeText.setText(disease.getCode());
			adviceText.setText(disease.getAdvice());
		}
	}
	
	public void setPatient(Patient patient){
		fullPrescription.setPatient(patient);
		if (patient == null){
			patientNameText.setText(null);
			patientYearText.setText(null);
			patientGenderText.setText(null);
			patientAddressText.setText(null);
			patientPhoneText.setText(null);
			insuaText.setText(null);
		} else {
			patientNameText.setText(patient.getName());
			patientYearText.setText(patient.getYearOfBirth() +"");
			patientGenderText.setText(TextUtils.convertSex(patient.getGender()));
			patientAddressText.setText(patient.getAddress());
			patientPhoneText.setText(patient.getPhoneNumber());
			insuaText.setText(patient.getInsuranceNumber());
		}
	}
	
	public void addMedicine(Medicine medicine){
		MediPres mediPres = new MediPres(medicine);
		addMediPres(mediPres);
		medipresContainer.revalidate();
	}
	
	public void addMediPres(MediPres mediPres){
		MediPresItem mediPresItem;
		if (listRecycledMedipres.size() > 0){
			mediPresItem = listRecycledMedipres.remove(listRecycledMedipres.size() - 1);
		} else {
			mediPresItem = new MediPresItem();
			mediPresItem.setOnCloseButtonClick(this);
			mediPresItem.setMaximumSize(new Dimension(2147483647, 60));
		}
		mediPresItem.setData(mediPres);
		mediPresItem.setViewOnly(viewOnly);
		medipresContainer.add(mediPresItem);
	}
	
	@Override
	public void onClick(MediPresItem mediPresItem, MediPres mediPres) {
		medipresContainer.remove(mediPresItem);
		medipresContainer.revalidate();
		listRecycledMedipres.add(mediPresItem);
	}
	
	public void savePrescription() throws Exception{
		Prescription prescription = fullPrescription.getPrescription();
		if (fullPrescription.getDisease() != null){
			prescription.setDiseaseId(fullPrescription.getDisease().getId());
		}
		if (fullPrescription.getPatient() != null){
			prescription.setPatientId(fullPrescription.getPatient().getId());
		}
		prescription.setDoctor(Config.getInstance().doctor);
		prescription.setNote(noteText.getText());
		prescription.setTime(System.currentTimeMillis());

		if (prescription.getId() == -1){
			Prescription.insertToDB(DBConnection.getInstance().createStatement(), prescription);
			saveMediPresList(DBConnection.getInstance().createStatement(), fullPrescription.getListMedicine(), prescription.getId());
		} else {
			prescription.updateToDB(DBConnection.getInstance().createStatement());
			saveMediPresList(DBConnection.getInstance().createStatement(), fullPrescription.getListMedicine(), prescription.getId());
		}
	}
	
	private void saveMediPresList(Statement statement, ArrayList<MediPres> mediPresList, int presId) throws SQLException{
		for (MediPres oldMediPres : mediPresList){
			MediPres.delete(statement, oldMediPres.getId());
		}
		
		mediPresList.clear();
		
		
		Component[] components = medipresContainer.getComponents();
		for (Component component : components){
			if (component instanceof MediPresItem){
				MediPres mediPres = ((MediPresItem) component).getData();
				mediPres.setPrescriptionId(presId);
				MediPres.insertToDB(statement, mediPres);
				mediPresList.add(mediPres);
			}
		}
	}
	
	public void setViewOnly(boolean viewOnly){
		this.viewOnly = viewOnly;
		newButton.setVisible(!viewOnly);
		saveButton.setVisible(!viewOnly);
		reuseButton.setVisible(viewOnly);
		openExccelBtn.setVisible(viewOnly);
		diseaseNameText.setEnabled(!viewOnly);
		patientNameText.setEnabled(!viewOnly);
		addMedicineButton.setEnabled(!viewOnly);
	}
	
	public void updateConfig(){
		doctorText.setText(Config.getInstance().doctor);
	}
	
	private boolean checkNullInfo(){
		if (fullPrescription.getPatient() == null){
			JOptionPane.showMessageDialog(this, "Chưa chọn bệnh nhân!");
			return false;
		}
		if (fullPrescription.getDisease() == null){
			JOptionPane.showMessageDialog(this, "Chưa chọn loại bệnh!");
			return false;
		}
		return true;
	}

	/**
	 * Create the panel.
	 */
	public PrescriptionTabPanel() {
		listRecycledMedipres = new ArrayList<>();
		
		fullPrescription = new FullPrescription();
		fullPrescription.getPrescription().setDoctor(Config.getInstance().doctor);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(new Color(253, 253, 253));
		panel.setMaximumSize(new Dimension(600, 32767));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(panel);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(horizontalBox_2);
		
		newButton = new JButton("Tạo đơn mới");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(PrescriptionTabPanel.this, 
			            "Tạo đơn mới thì sẽ không chỉnh sửa được đơn cũ nữa. Bạn muốn tiếp tục?", "Tạo đơn mới", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
							fullPrescription = new FullPrescription();
							fullPrescription.getPrescription().setDoctor(Config.getInstance().doctor);
							setFullPrescription(null);
			    }
			}
		});
		horizontalBox_2.add(newButton);
		
		saveButton = new JButton("L\u01B0u \u0111\u01A1n n\u00E0y");
		horizontalBox_2.add(saveButton);
		
		reuseButton = new JButton("Dùng đơn này");
		reuseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Disease disease = fullPrescription.getDisease();
				MainApplicationForm.getInstance().updateDiseaseToCurrentPresciption(disease);
				ArrayList<MediPres> listData = fullPrescription.getListMedicine();
				for (MediPres mediPres : listData){
					MainApplicationForm.getInstance().updateMedicineToCurrentPrescription(mediPres);
				}
			}
		});
		horizontalBox_2.add(reuseButton);
		
		openExccelBtn = new JButton("Mở file excel");
		openExccelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File( Config.MAIN_PATH + "\\Prescription\\"
										+ fullPrescription.getPrescription().getId() + ".xlsx"));
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(PrescriptionTabPanel.this, e.getMessage());
				}
			}
		});
		horizontalBox_2.add(openExccelBtn);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Prescription prescription = fullPrescription.getPrescription();
				if (!checkNullInfo()){
					return;
				}
				if (prescription.getDiseaseId() == -1 || prescription.getPatientId() == -1){
					JOptionPane.showMessageDialog(PrescriptionTabPanel.this, "Chọn bệnh nhân và bệnh!");
				} else {
					Savepoint savepoint = null;
					try {
						DBConnection.getInstance().getConnection().setAutoCommit(false);
						savepoint = DBConnection.getInstance().getConnection().setSavepoint();
						savePrescription();
						try {
							File file = PrescriptionExportHelper.readTemplate(Config.TEMPLATE_PRES_PATH, fullPrescription,  Config.MAIN_PATH + "\\Prescription\\"
									+ fullPrescription.getPrescription().getId() + ".xlsx");
							Object[] options = { "Mở file", "In", "Bỏ qua" };
							int choice = JOptionPane.showOptionDialog(PrescriptionTabPanel.this, "Mời lựa chọn", "Lưu file thành công!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
							if (choice == JOptionPane.YES_OPTION){
								Desktop.getDesktop().open(file);
							} else if (choice == JOptionPane.NO_OPTION){
								for (int i = 0; i < Config.getInstance().printLoop; i ++){
									Desktop.getDesktop().print(file);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							UIUtils.showError(e.getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (savepoint != null){
							try {
								DBConnection.getInstance().getConnection().rollback(savepoint);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						prescription.setId(-1);
						JOptionPane.showMessageDialog(PrescriptionTabPanel.this, "Lưu không thành công, kiểm tra lại các loại thuốc!");
					} finally {
						try {
							DBConnection.getInstance().getConnection().releaseSavepoint(savepoint);
							DBConnection.getInstance().getConnection().setAutoCommit(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		JLabel lblnThuc = new JLabel("\u0110\u01A0N THU\u1ED0C");
		lblnThuc.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblnThuc.setHorizontalAlignment(SwingConstants.CENTER);
		lblnThuc.setMaximumSize(new Dimension(600, 30));
		panel.add(lblnThuc);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 0, 0, 0));
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblHVTn = new JLabel("H\u1ECD v\u00E0 t\u00EAn ng\u01B0\u1EDDi b\u1EC7nh: ");
		lblHVTn.setMaximumSize(new Dimension(120, 14));
		panel_1.add(lblHVTn);
		
		patientNameText = new JButton("Nguy\u1EC5n V\u0103n A");
		patientNameText.setHorizontalAlignment(SwingConstants.LEFT);
		patientNameText.setMaximumSize(new Dimension(200, 23));
		panel_1.add(patientNameText);
		
		Component rigidArea = Box.createRigidArea(new Dimension(60, 20));
		rigidArea.setMinimumSize(new Dimension(20, 20));
		rigidArea.setMaximumSize(new Dimension(20, 20));
		panel_1.add(rigidArea);
		
		JLabel lblNmSinh = new JLabel("N\u0103m sinh: ");
		lblNmSinh.setMinimumSize(new Dimension(60, 14));
		panel_1.add(lblNmSinh);
		
		patientYearText = new JLabel("1960");
		patientYearText.setMinimumSize(new Dimension(30, 14));
		panel_1.add(patientYearText);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(60, 20));
		rigidArea_1.setMaximumSize(new Dimension(20, 20));
		rigidArea_1.setMinimumSize(new Dimension(20, 20));
		panel_1.add(rigidArea_1);
		
		JLabel lblGiiTnh = new JLabel("Gi\u1EDBi t\u00EDnh: ");
		lblGiiTnh.setMinimumSize(new Dimension(60, 14));
		lblGiiTnh.setMaximumSize(new Dimension(60, 14));
		panel_1.add(lblGiiTnh);
		
		patientGenderText = new JLabel("Nam");
		patientGenderText.setMaximumSize(new Dimension(40, 14));
		panel_1.add(patientGenderText);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 0, 0, 0));
		panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblaCh = new JLabel("\u0110\u1ECBa ch\u1EC9: ");
		lblaCh.setMaximumSize(new Dimension(60, 14));
		lblaCh.setPreferredSize(new Dimension(60, 14));
		panel_2.add(lblaCh);
		
		patientAddressText = new JLabel("S\u1ED1 26 \u0110\u01B0\u1EDDng 3/2 Qu\u1EADn 10");
		patientAddressText.setMaximumSize(new Dimension(360, 14));
		patientAddressText.setMinimumSize(new Dimension(200, 14));
		panel_2.add(patientAddressText);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(60, 20));
		rigidArea_2.setMaximumSize(new Dimension(20, 20));
		panel_2.add(rigidArea_2);
		
		JLabel lblSt = new JLabel("S\u0110T: ");
		lblSt.setMaximumSize(new Dimension(40, 14));
		panel_2.add(lblSt);
		
		patientPhoneText = new JLabel("0912345678");
		patientPhoneText.setMaximumSize(new Dimension(100, 14));
		panel_2.add(patientPhoneText);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(0, 0, 0, 0));
		panel_8.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(panel_8);
		panel_8.setLayout(new BoxLayout(panel_8, BoxLayout.X_AXIS));
		
		JLabel lblSThBhxh = new JLabel("S\u1ED1 th\u1EBB BHXH: ");
		panel_8.add(lblSThBhxh);
		
		insuaText = new JLabel("0192929292");
		panel_8.add(insuaText);
		
		JPanel panel_3 = new JPanel();
		panel_3.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_3.setBackground(new Color(0, 0, 0, 0));
		panel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JLabel lblBnh = new JLabel("B\u1EC7nh: ");
		lblBnh.setMaximumSize(new Dimension(60, 14));
		panel_3.add(lblBnh);
		
		diseaseNameText = new JButton("Vi\u00EAm ph\u1EBF qu\u1EA3n c\u1EA5p v\u00E0 vi\u00EAm xoang d\u1ECB \u1EE9ng");
		diseaseNameText.setMaximumSize(new Dimension(300, 23));
		diseaseNameText.setHorizontalAlignment(SwingConstants.LEFT);
		panel_3.add(diseaseNameText);
		
		Component rigidArea_6 = Box.createRigidArea(new Dimension(20, 20));
		panel_3.add(rigidArea_6);
		
		JLabel lblMBnh = new JLabel("M\u00E3 b\u1EC7nh: ");
		lblMBnh.setMaximumSize(new Dimension(60, 14));
		panel_3.add(lblMBnh);
		
		diseaseCodeText = new JLabel("VPQVXDU");
		diseaseCodeText.setMaximumSize(new Dimension(60, 14));
		panel_3.add(diseaseCodeText);
		
		medipresContainer = new JPanel();
		medipresContainer.setBackground(UIUtils.TRANSPARENT);
		medipresContainer.setMaximumSize(new Dimension(600, 1200));
		panel.add(medipresContainer);
		medipresContainer.setLayout(new BoxLayout(medipresContainer, BoxLayout.Y_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		medipresContainer.add(verticalStrut);
		
		addMedicineButton = new JButton("Th\u00EAm thu\u1ED1c");
		addMedicineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainApplicationForm.getInstance().switchToMedicineTab();
			}
		});
		addMedicineButton.setIcon(new ImageIcon(PrescriptionTabPanel.class.getResource("/ken/innovation/doctorassis/res/add-user-3-16.png")));
		medipresContainer.add(addMedicineButton);
		
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(horizontalBox);
		
		JLabel lblNewLabel_1 = new JLabel("L\u1EDDi d\u1EB7n: ");
		lblNewLabel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		horizontalBox.add(lblNewLabel_1);
		
		adviceText = new JTextArea("abc\nefa");
		adviceText.setForeground(Color.BLACK);
		adviceText.setFont(new Font("Tahoma", Font.PLAIN, 13));
		adviceText.setEnabled(false);
		adviceText.setMaximumSize(new Dimension(800, 100));
		adviceText.setAlignmentY(Component.TOP_ALIGNMENT);
		horizontalBox.add(adviceText);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setAlignmentX(0.0f);
		panel.add(horizontalBox_1);
		
		JLabel label = new JLabel("Ghi ch\u00FA: ");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setAlignmentY(0.0f);
		horizontalBox_1.add(label);
		
		noteText = new JTextArea("");
		noteText.setMaximumSize(new Dimension(800, 100));
		noteText.setForeground(Color.BLACK);
		noteText.setFont(new Font("Tahoma", Font.PLAIN, 13));
		noteText.setAlignmentY(0.0f);
		horizontalBox_1.add(noteText);
		
		timeText = new JLabel("Ngày " + TextUtils.getDateStringFrom(System.currentTimeMillis()));
		timeText.setMaximumSize(new Dimension(600, 14));
		panel.add(timeText);
		
		
		doctorText = new JLabel(Config.getInstance().doctor);
		doctorText.setMaximumSize(new Dimension(600, 14));
		panel.add(doctorText);
		
		
		scrollPane.setMaximumSize(new Dimension(600, 800));
		this.add(scrollPane);
		
		patientNameText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fullPrescription.getPatient() != null){
					PatientForm.getInstance().setPatient(fullPrescription.getPatient());
					PatientForm.getInstance().setVisible(true);
				} else {
					MainApplicationForm.getInstance().switchToPatientTab();
				}
			}
		});
		
		diseaseNameText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainApplicationForm.getInstance().switchToDiseaseTab();
			}
		});
	}

}
