package ken.innovation.doctorassis.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.helper.MedicineImportHelper;
import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class MedicineForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1406713717074324970L;
	private JPanel contentPane;
	private JTextField medicineText;
	private JTextField ingredientText;
	private JTextField doseText;
	private JTextField usingText;
	private JTextField unitText;
	private JLabel toastLabel;
	private JButton addButton;
	
	private Medicine medicine;
	private DataChangeListener dataChangeListener;
	
	final JFileChooser fc = new JFileChooser();
	
	public void setDataChangeListener(DataChangeListener listener){
		this.dataChangeListener = listener;
	}
	
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
		if (medicine == null){
			clearText();
		} else {
			medicineText.setText(medicine.getName());
			ingredientText.setText(medicine.getIngredient());
			doseText.setText(medicine.getDose());
			usingText.setText(medicine.getUsing());
			unitText.setText(medicine.getUnit());
			
			addButton.setText("Cập nhật");
		}
	}
	
	public void setMedicineName(String name){
		medicineText.setText(name);
	}



	/**
	 * Create the frame.
	 */
	public MedicineForm() {
		setIconImage(UIUtils.getImageIcon("medicine-16.png"));
		setTitle("Thuốc");
		setResizable(false);
		setBounds(100, 100, 320, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tên thuốc");
		lblNewLabel.setBounds(10, 14, 90, 14);
		contentPane.add(lblNewLabel);
		
		medicineText = new JTextField();
		medicineText.setBounds(97, 11, 187, 20);
		contentPane.add(medicineText);
		medicineText.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Dược chất");
		lblNewLabel_1.setBounds(10, 41, 90, 14);
		contentPane.add(lblNewLabel_1);
		
		ingredientText = new JTextField();
		ingredientText.setBounds(97, 38, 187, 20);
		contentPane.add(ingredientText);
		ingredientText.setColumns(10);
		
		addButton = new JButton("Thêm");
		addButton.setBounds(97, 168, 119, 23);
		contentPane.add(addButton);
		
		JButton addFromExcelButton = new JButton("Thêm từ File Excel");
		addFromExcelButton.setBounds(97, 250, 119, 23);
		contentPane.add(addFromExcelButton);
		addFromExcelButton.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(36, 202, 225, 14);
		contentPane.add(separator);
		
		toastLabel = new JLabel("");
		toastLabel.setBounds(96, 155, 187, 14);
		contentPane.add(toastLabel);
		
		JLabel lblLiuDng = new JLabel("Liều dùng");
		lblLiuDng.setBounds(10, 66, 46, 14);
		contentPane.add(lblLiuDng);
		
		doseText = new JTextField();
		doseText.setBounds(97, 66, 187, 20);
		contentPane.add(doseText);
		doseText.setColumns(10);
		
		JLabel lblngDng = new JLabel("Đường dùng");
		lblngDng.setBounds(10, 94, 77, 14);
		contentPane.add(lblngDng);
		
		usingText = new JTextField();
		usingText.setBounds(97, 91, 187, 20);
		contentPane.add(usingText);
		
		JLabel lblnV = new JLabel("Đơn vị");
		lblnV.setBounds(10, 119, 77, 14);
		contentPane.add(lblnV);
		
		unitText = new JTextField();
		unitText.setBounds(97, 122, 187, 20);
		contentPane.add(unitText);
		
		JButton deleteAllBtn = new JButton("Xóa tất cả thuốc");
		deleteAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int r =JOptionPane.showConfirmDialog(MedicineForm.this, "Bạn có chắc muốn xóa tất cả thuốc đã lưu?", 
						"Xóa tất cả thuốc", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (r == JOptionPane.YES_OPTION){
					try {
						DBConnection.getInstance().createStatement().execute(Medicine.DROP_TABLE);
						DBConnection.getInstance().createStatement().execute(Medicine.CREATE_TABLE);
						JOptionPane.showMessageDialog(MedicineForm.this, "Xóa thành công");
						if (dataChangeListener != null){
							dataChangeListener.onDataChange();
						}
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.showError(e.getMessage());
					}
				}
			}
		});
		deleteAllBtn.setBounds(97, 214, 119, 23);
		contentPane.add(deleteAllBtn);
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (medicine == null){
					if (insertMedicine()){
						clearText();
						toastLabel.setText("Insert successfully!");
					}else {
						toastLabel.setText("Insert failed!");
					}
				} else {
					if (updateMedicine()){
						clearText();
						toastLabel.setText("Update successfully!");
						if (dataChangeListener != null){
							dataChangeListener.onDataChange();
						}
					}else {
						toastLabel.setText("Update failed!");
					}
				}
			}
		});
		
		addFromExcelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!TextUtils.isEmpty(Config.getInstance().lastXlsxFile)){
					fc.setCurrentDirectory(new File(Config.getInstance().lastXlsxFile));
				}
				int returnVal = fc.showOpenDialog(MedicineForm.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            Config.getInstance().lastXlsxFile = file.getPath();
		            try {
						Config.getInstance().save();
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
		            try {
						ArrayList<Medicine> listMedicines = MedicineImportHelper.loadMedicineFromExcel(file);
						Medicine.insertToDB(DBConnection.getInstance().createStatement(), listMedicines);
						JOptionPane.showMessageDialog(MedicineForm.this, "Thêm thành công");
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.showError(e.getMessage());
					}
		        }
			}
		});
	}
	
	private boolean insertMedicine(){
		Medicine medicine = new Medicine();
		if (updateMedicine(medicine)){
			try {
				Medicine.insertToDB(DBConnection.getInstance().createStatement(), medicine);
				return true;
			} catch (Exception e1) {
				e1.printStackTrace();
				UIUtils.showError(e1.getMessage());
			}
		}
		return false;
	}
	
	private boolean updateMedicine(){
		if (updateMedicine(medicine)){
			try {
				medicine.updateToDB(DBConnection.getInstance().createStatement());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				UIUtils.showError(e.getMessage());
			}
		}
		return false;
	}
	
	private boolean updateMedicine(Medicine medicine){
		if (TextUtils.isEmpty(medicineText.getText()) || TextUtils.isEmpty(ingredientText.getText())) return false;
		medicine.setName(medicineText.getText());
		medicine.setIngredient(ingredientText.getText());
		medicine.setDose(doseText.getText());
		medicine.setUsing(usingText.getText());
		medicine.setUnit(unitText.getText());
		return true;
	}
	
	private void clearText(){
		medicine = null;
		medicineText.setText(null);
		ingredientText.setText(null);
		doseText.setText(null);
		usingText.setText(null);
		unitText.setText(null);
		addButton.setText("Thêm mới");
	}
}
