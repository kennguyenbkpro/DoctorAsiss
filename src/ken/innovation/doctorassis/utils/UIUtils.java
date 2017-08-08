package ken.innovation.doctorassis.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class UIUtils {
	
	public interface DataChangeListener {
		void onDataChange();
	}
	
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	public static void showNotYetImplemented(){
		JOptionPane.showMessageDialog(null, "Chức năng này chưa có!");
	}
	
	public static void showError(String err){
		JOptionPane.showMessageDialog(null, err);
	}
	
	private static ComboBoxModel<String> usingMedicineModel;
	public static ComboBoxModel<String> getUsingMedicineModel(){
		if (usingMedicineModel == null){
			String[] data = {"Uống", "Tiêm", "Bôi ngoài da", "Xịt", "Khác"};
			usingMedicineModel = new DefaultComboBoxModel<>(data);
		}
		return usingMedicineModel;
	}
	
	
	private static ComboBoxModel<String> unitMedicineModel;
	public static ComboBoxModel<String> getUnitMedicineModel(){
		if (unitMedicineModel == null){
			String[] data = {"Viên", "Gói", "Lọ", "ml", ""};
			unitMedicineModel = new DefaultComboBoxModel<>(data);
		}
		return unitMedicineModel;
	}
	
	public static Image getImageIcon(String name){
		java.net.URL url = ClassLoader.getSystemResource("ken/innovation/doctorassis/res/" + name);
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		return img;
	}
}
