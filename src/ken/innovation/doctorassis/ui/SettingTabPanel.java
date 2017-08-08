package ken.innovation.doctorassis.ui;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.TextUtils;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.awt.event.ActionEvent;

public class SettingTabPanel extends JPanel {
	
	private JTextField doctorText;
	private JTextField leftFooter;
	private JTextField rightFooter;
	private JTextField printLoop;

	/**
	 * Create the panel.
	 */
	public SettingTabPanel() {
		setLayout(new MigLayout("", "[][][grow]", "[][][][][][][][][][]"));
		
		JLabel lblBcS = new JLabel("B\u00E1c s\u0129 ");
		add(lblBcS, "cell 0 0");
		
		doctorText = new JTextField();
		doctorText.setText(Config.getInstance().doctor);
		add(doctorText, "cell 2 0,growx");
		doctorText.setColumns(10);
		
		JButton btnCpNht = new JButton("C\u1EADp nh\u1EADt");
		btnCpNht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Config.getInstance().doctor = doctorText.getText();
				Config.getInstance().rightFooter = rightFooter.getText();
				Config.getInstance().leftFooter = leftFooter.getText();
				Config.getInstance().printLoop = Integer.valueOf(printLoop.getText());
				try {
					Config.getInstance().save();
					MainApplicationForm.getInstance().updateConfig();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
					UIUtils.showError(e.getMessage());
				}
			}
		});
		
		JLabel lblFooterTri = new JLabel("Footer tr\u00E1i");
		add(lblFooterTri, "cell 0 1");
		
		leftFooter = new JTextField();
		add(leftFooter, "cell 2 1,growx");
		leftFooter.setColumns(10);
		
		JLabel lblFooterPhi = new JLabel("Footer ph\u1EA3i");
		add(lblFooterPhi, "cell 0 2");
		
		rightFooter = new JTextField();
		add(rightFooter, "cell 2 2,growx");
		rightFooter.setColumns(10);
		
		JLabel lblSBnIn = new JLabel("S\u1ED1 b\u1EA3n in");
		add(lblSBnIn, "cell 0 3");
		
		printLoop = new JFormattedTextField(TextUtils.getIntFormater());
		add(printLoop, "cell 2 3,growx");
		printLoop.setColumns(10);
		add(btnCpNht, "cell 2 9");
		
		rightFooter.setText(Config.getInstance().rightFooter);
		leftFooter.setText(Config.getInstance().leftFooter);
		printLoop.setText("" + Config.getInstance().printLoop);
	}
	
}
