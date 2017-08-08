package ken.innovation.doctorassis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import ken.innovation.doctorassis.dbmodel.DBModel;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.PatientResource;
import ken.innovation.doctorassis.helper.DBConnection;
import ken.innovation.doctorassis.ui.model.PatientTableModel;
import ken.innovation.doctorassis.utils.UIUtils;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.BoxLayout;

public class PatientResourceChooser extends JFrame {
	private static PatientResourceChooser patientResourceChooser;
	
	public static PatientResourceChooser getInstance(){
		if (patientResourceChooser == null){
			patientResourceChooser = new PatientResourceChooser();
		}
		return patientResourceChooser;
	}

	private JPanel contentPane;
	private AutoResizeTable table;
	private PatientTableModel model;
	private int selectedRow = 0;

	
	public void searchKeyword(String name){
		try {
			ArrayList<DBModel> results = PatientResource.searchPatientByName(DBConnection.getInstance().createStatement(), name);
			model.setListData(results);
			model.fireTableDataChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	private PatientResourceChooser() {
		setIconImage(UIUtils.getImageIcon("excel16.png"));
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		table = new AutoResizeTable();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		scrollPane.setMaximumSize(new Dimension(800, 600));
		contentPane.add(scrollPane);
		
		table.setRowHeight(30);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		    	if (!isSelected && !hasFocus){
		    		final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    		c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFFCDCDCD));
		    		return c;
		    	} else {
		    		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    	}
		    }
		});
		
		model = new PatientTableModel();
		table.setModel(model);
		
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        selectedRow = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	PatientForm.getInstance().fillPatient((Patient) model.getData(selectedRow)); 
		        	PatientForm.getInstance().setVisible(true);
		        	PatientResourceChooser.this.setVisible(false);
		        }
		    }
		});
	}

}
