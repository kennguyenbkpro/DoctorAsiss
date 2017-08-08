package ken.innovation.doctorassis.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ken.innovation.doctorassis.dbmodel.Disease;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;
import ken.innovation.doctorassis.utils.UIUtils.DataChangeListener;

import javax.swing.SpringLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DiseaseForm extends JFrame {
	private static DiseaseForm diseaseForm;
	
	public static DiseaseForm getInstance(){
		if (diseaseForm == null){
			diseaseForm = new DiseaseForm();
		}
		return diseaseForm;
	}
	
	private JPanel contentPane;
	private JTextField nameText;
	private JTextField codeText;
	private JButton addButton;
	private JLabel toastLabel;
	private JTextPane adviceText;
	
	private Disease disease;
	private DataChangeListener dataChangeListener;
	
	public void setDataChangeListener(DataChangeListener listener){
		this.dataChangeListener = listener;
	}
	
	public void setDisease(Disease disease){
		this.disease = disease;
		toastLabel.setText("");
		if (disease == null){
			clearText();
		} else {
			nameText.setText(disease.getName());
			nameText.setEnabled(false);
			codeText.setText(disease.getCode());
			adviceText.setText(disease.getAdvice());
			addButton.setText("Cập nhật");
		}
	}
	
	public void setDiseaseName(String name){
		nameText.setText(name);
	}
	
	private void clearText(){
		disease = null;
		nameText.setText(null);
		nameText.setEnabled(true);
		codeText.setText(null);
		adviceText.setText(null);
		addButton.setText("Thêm mới");
	}
	
	private boolean insertDisease(){
		Disease disease = new Disease();
		if (updateDisease(disease)){
			try {
				Disease.insertToDB(DBConnection.getInstance().createStatement(), disease.getName(), disease.getCode(), disease.getAdvice());
				return true;
			} catch (Exception e){
				e.printStackTrace();
				UIUtils.showError(e.getMessage());
			}
		}
		return false;
	}
	
	private boolean updateDisease(){
		if (updateDisease(disease)){
			try {
				disease.updateToDB(DBConnection.getInstance().createStatement());
				return true;
			} catch (Exception e){
				e.printStackTrace();
				UIUtils.showError(e.getMessage());
			}
		}
		return false;
	}
	
	private boolean updateDisease(Disease disease){
		if (TextUtils.isEmpty(nameText.getText()) || TextUtils.isEmpty(codeText.getText())) return false;
		disease.setName(nameText.getText());
		disease.setCode(codeText.getText());
		disease.setAdvice(adviceText.getText());
		return true;
	}

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					DiseaseForm frame = new DiseaseForm();
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
	private DiseaseForm() {
		setIconImage(UIUtils.getImageIcon("disease-16.png"));
		setTitle("Bệnh");
		setBounds(100, 100, 300, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][][grow]", "[][][grow][][]"));
		
		JLabel lblTnBnh = new JLabel("T\u00EAn b\u1EC7nh: ");
		contentPane.add(lblTnBnh, "cell 0 0");
		
		nameText = new JTextField();
		contentPane.add(nameText, "cell 2 0,growx");
		nameText.setColumns(10);
		
		JLabel lblMBnh = new JLabel("M\u00E3 b\u1EC7nh: ");
		contentPane.add(lblMBnh, "cell 0 1");
		
		codeText = new JTextField();
		contentPane.add(codeText, "cell 2 1,growx");
		codeText.setColumns(10);
		
		JLabel lblLiDn = new JLabel("L\u1EDDi d\u1EB7n: ");
		contentPane.add(lblLiDn, "cell 0 2");
		
		adviceText = new JTextPane();
		contentPane.add(adviceText, "cell 2 2,grow");
		
		toastLabel = new JLabel("Insert succesfully!");
		contentPane.add(toastLabel, "cell 2 3");
		
		addButton = new JButton("Th\u00EAm m\u1EDBi");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (disease == null){
					if (insertDisease()){
						clearText();
						toastLabel.setText("Insert successfully!");
					}else {
						toastLabel.setText("Insert failed!");
					}
				} else {
					if (updateDisease()){
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
		contentPane.add(addButton, "cell 2 4");
	}
}
