import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class main extends JPanel{
	
	private JTable table;
	private JTextField filterText;
   // private JTextField statusText;
	private TableRowSorter<MyTableModel> sorter;

	JFrame frame;
	main newContentPane;
//	JTextField textfield;
//	static Object[][] data = new Object[10][10];
	
	public static void main(String[] args)
	{
		main gui = new main();
		gui.createAndShowGUI();
		 //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });*/

	}
	

	/*private boolean DEBUG = false;*/   
    
    //Create the table for information display.	
    public void SimpleTable() {
       /* super(new GridLayout(1,0));*/    	
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));    	
    	//Create a table with a sorter.
    	MyTableModel model = new MyTableModel();
        sorter = new TableRowSorter<MyTableModel>(model);
        table = new JTable(model);
        //Set up column sizes.
        initColumnSizes(table);
        table.setRowSorter(sorter);

        table.setPreferredScrollableViewportSize(new Dimension(700, 160));
        table.setFillsViewportHeight(true);
      //Better to have a single selection.
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       //Selection response 
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        int viewRow = table.getSelectedRow();
                        String id="http://www.stubhub.com/event/9407922/?ticket_id="+table.getValueAt(viewRow, 0)+"&cb=1";
                        //System.out.println(id);
                        Desktop desktop = Desktop.getDesktop();
                        try {
							desktop.browse(new URI(id));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
        );

 
        /*if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }*/
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table); 
 
        //Add the scroll pane to this panel.
        add(scrollPane);
        
        //Create a separate form for filterText and statusText
        JPanel form = new JPanel(new SpringLayout());

    	ImageIcon icon = new ImageIcon("stubhub.jpg");
        JLabel l1 = new JLabel("Filter by listingId:", icon, JLabel.LEFT);
        form.add(l1);
        filterText = new JTextField();
        //Whenever filterText changes, invoke newFilter.
        filterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
        l1.setLabelFor(filterText);
        form.add(filterText);
        /*JLabel l2 = new JLabel("Status:", SwingConstants.TRAILING);
        form.add(l2);
        statusText = new JTextField();
        l2.setLabelFor(statusText);
        form.add(statusText);*/        
        SpringUtilities.makeCompactGrid(form, 1, 2, 6, 6, 6, 6);
        add(form);


    }
    
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

 
    /*private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
 
        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }*/
   
 
    private void initColumnSizes(JTable table) {
    	MyTableModel model = (MyTableModel)table.getModel();        
        model.getData();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        //Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();
 
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
 
            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
 
            /*comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, longValues[i],
                                 false, false, 0, i);*/
            cellWidth = comp.getPreferredSize().width;
 
            /*if (DEBUG) {
                System.out.println("Initializing width of column "
                                   + i + ". "
                                   + "headerWidth = " + headerWidth
                                   + "; cellWidth = " + cellWidth);
            }*/
 
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("StubHub ticket price-drop reporter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        BorderLayout layout =new BorderLayout();
//		JPanel panel = new JPanel(layout);
		
		JButton updatebutton = new JButton("Update");
		updatebutton.addActionListener(new UpdateListener());
 
        //Create and set up the content pane.
        newContentPane = new main();
        newContentPane.SimpleTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        //frame.setContentPane(newContentPane);
        
               
        frame.add(BorderLayout.CENTER, newContentPane);
        frame.add(BorderLayout.NORTH, updatebutton);
        
//        frame.add(BorderLayout.CENTER, newContentPane);
//        frame.add(BorderLayout.SOUTH,searchbutton);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
 	
	class UpdateListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			newContentPane = new main();
	        newContentPane.SimpleTable();
	        frame.repaint();
		}
	}
	
}


	
