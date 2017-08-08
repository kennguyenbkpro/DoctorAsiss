package ken.innovation.doctorassis.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.ui.model.PrescriptionPreviewModel;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import java.awt.Dimension;

public class PrescriptionViewer extends JFrame {
	public enum CONTENT_MODE {
		PATIENT_MODE,
		DISEASE_MODE
	}

	private static PrescriptionViewer prescriptionViewer;
	
	public static PrescriptionViewer getInstance(){
		if (prescriptionViewer == null){
			prescriptionViewer = new PrescriptionViewer();
		}
		return prescriptionViewer;
	}
	
	private JPanel contentPane;
	private AutoResizeTable table;
	private PrescriptionPreviewModel tableModel;
	private PrescriptionTabPanel prescriptionPanel;
	private int lastRow = -1;
	
	public void showData(ArrayList<FullPrescription> fullPrescriptions, CONTENT_MODE mode){
		tableModel.setListData(fullPrescriptions, mode);
		tableModel.fireTableDataChanged();
		if (fullPrescriptions.size() == 0){
			prescriptionPanel.setFullPrescription(null);
		} else {
			table.setRowSelectionInterval(0, 0);
			prescriptionPanel.setFullPrescription(fullPrescriptions.get(0));
		}
		setVisible(true);
	}


	/**
	 * Create the frame.
	 */
	private PrescriptionViewer() {
		setIconImage(UIUtils.getImageIcon("paper-16.png"));
		setBounds(50, 50, 940, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		table = new AutoResizeTable();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 402));
		scrollPane.setMaximumSize(new Dimension(300, 32767));
		contentPane.add(scrollPane);
		tableModel = new PrescriptionPreviewModel();
		table.setModel(tableModel);
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int selectedRow = table.rowAtPoint(p);
		        if (selectedRow != lastRow){
		        	lastRow = selectedRow;
		        	prescriptionPanel.setFullPrescription(tableModel.getData(selectedRow));
		        }
		    }
		});
		
		prescriptionPanel = new PrescriptionTabPanel();
		prescriptionPanel.setViewOnly(true);
		contentPane.add(prescriptionPanel);
	}

}
