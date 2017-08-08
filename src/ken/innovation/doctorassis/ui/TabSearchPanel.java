package ken.innovation.doctorassis.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;

public class TabSearchPanel extends JPanel {
	protected JTextField searchTextField;
	protected JButton addButton;
	protected JButton searchButton;
	protected AutoResizeTable table;
	protected JPanel panel_1;
	protected JPanel panel;
	protected JButton updateToPres;

	/**
	 * Create the panel.
	 */
	public TabSearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		panel_1 = new JPanel();
		add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		panel = new JPanel();
		panel_1.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		searchTextField = new JTextField();
		panel.add(searchTextField);
		searchTextField.setColumns(30);
		
		searchButton = new JButton("T\u00ECm ki\u1EBFm");
		searchButton.setIcon(new ImageIcon(TabSearchPanel.class.getResource("/ken/innovation/doctorassis/res/search-2-16.png")));
		panel.add(searchButton);
		
		addButton = new JButton("Th\u00EAm m\u1EDBi");
		addButton.setIcon(new ImageIcon(TabSearchPanel.class.getResource("/ken/innovation/doctorassis/res/add-user-3-16.png")));
		panel.add(addButton);
		
		table = new AutoResizeTable();
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
		sl_panel_1.putConstraint(SpringLayout.WEST, table, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, table, -10, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, table, -10, SpringLayout.EAST, panel_1);
		JScrollPane scrollPane = new JScrollPane(table);
		sl_panel_1.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, panel_1);
		panel_1.add(scrollPane);
		
		updateToPres = new JButton("C\u1EADp nh\u1EADt v\u00E0o \u0111\u01A1n thu\u1ED1c");
		sl_panel_1.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, updateToPres);
		sl_panel_1.putConstraint(SpringLayout.NORTH, table, 10, SpringLayout.SOUTH, updateToPres);
		sl_panel_1.putConstraint(SpringLayout.NORTH, updateToPres, 10, SpringLayout.SOUTH, panel);
		sl_panel_1.putConstraint(SpringLayout.WEST, updateToPres, 10, SpringLayout.WEST, panel_1);
		panel_1.add(updateToPres);
		
		searchTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchButton.doClick();
			}
		});
		
	}
}
