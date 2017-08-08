package ken.innovation.doctorassis.ui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MediPresItem extends JPanel {
	private MediPres mediPres;
	
	public void setData(MediPres mediPres){
		this.mediPres = mediPres;
		if (mediPres == null){
			doseText.setText(null);
			medicineNameText.setText(null);
			unitText.setText(null);
			amountText.setText(null);
		} else {
			doseText.setText(mediPres.getDose());
			medicineNameText.setText(mediPres.getMedicine());
			unitText.setText(mediPres.getUnit());
			amountText.setText(mediPres.getAmount() + "");
		}
	}
	
	public MediPres getData() {
		mediPres.setMedicine(medicineNameText.getText());
		mediPres.setAmount(Integer.valueOf(amountText.getText()));
		mediPres.setDose(doseText.getText());
		return mediPres;
	}
	
	
	public interface OnCloseButtonClick {
		void onClick(MediPresItem mediPresItem, MediPres mediPres);
	}
	
	private OnCloseButtonClick onCloseButtonClick;
	
	public void setOnCloseButtonClick(OnCloseButtonClick onCloseButtonClick){
		this.onCloseButtonClick = onCloseButtonClick;
	}
	
	public void setViewOnly(boolean viewOnly){
		doseText.setEnabled(!viewOnly);
		amountText.setEnabled(!viewOnly);
		medicineNameText.setEnabled(!viewOnly);
		deleteMedicine.setEnabled(!viewOnly);
	}
	
	private JTextField doseText;
	private JTextField medicineNameText;
	private JTextField amountText;
	private JLabel unitText;
	private JButton deleteMedicine;

	/**
	 * Create the panel.
	 */
	public MediPresItem() {
		setBackground(new Color(245, 245, 245));
		setMaximumSize(new Dimension(10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBackground(UIUtils.TRANSPARENT);
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(horizontalBox);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea_1);
		
		medicineNameText = new JTextField("Panadol Vi\u00EAn s\u1EE7i 500mg");
		medicineNameText.setMinimumSize(new Dimension(260, 20));
		medicineNameText.setHorizontalAlignment(SwingConstants.LEADING);
		medicineNameText.setMaximumSize(new Dimension(260, 23));
		horizontalBox.add(medicineNameText);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea);
		
		JLabel lblSLng = new JLabel("S\u1ED1 l\u01B0\u1EE3ng: ");
		horizontalBox.add(lblSLng);
		
		amountText = new JFormattedTextField(TextUtils.getIntFormater());
		amountText.setMinimumSize(new Dimension(50, 20));
		amountText.setMaximumSize(new Dimension(50, 40));
		horizontalBox.add(amountText);
		
		unitText = new JLabel("Vi\u00EAn");
		unitText.setMinimumSize(new Dimension(40, 14));
		unitText.setHorizontalAlignment(SwingConstants.CENTER);
		unitText.setMaximumSize(new Dimension(40, 14));
		horizontalBox.add(unitText);
		
		deleteMedicine = new JButton("");
		deleteMedicine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (onCloseButtonClick != null){
					onCloseButtonClick.onClick(MediPresItem.this, MediPresItem.this.mediPres);
				}
			}
		});
		deleteMedicine.setToolTipText("");
		deleteMedicine.setIcon(new ImageIcon(MediPresItem.class.getResource("/ken/innovation/doctorassis/res/x-mark-2-16.png")));
		horizontalBox.add(deleteMedicine);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		horizontalBox_1.setBackground(UIUtils.TRANSPARENT);
		add(horizontalBox_1);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_1.add(rigidArea_2);
		
		JLabel lblLiuDng = new JLabel("Li\u1EC1u d\u00F9ng: ");
		horizontalBox_1.add(lblLiuDng);
		
		doseText = new JTextField();
		doseText.setMinimumSize(new Dimension(100, 20));
		doseText.setMaximumSize(new Dimension(320, 40));
		doseText.setAlignmentX(Component.LEFT_ALIGNMENT);
		horizontalBox_1.add(doseText);
		doseText.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);

	}

}
