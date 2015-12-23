import java.io.FileReader;

import javax.swing.table.AbstractTableModel;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class MyTableModel extends AbstractTableModel{
	  private String[] columnNames = {"listingId",    	
				"amount",
				"currency",
				"percentChange",
				"quantity",
				"row",
				"sectionId",
				"sectionName",
				"zoneId",
				"zoneName"};
//Read and store the data
	  static Object[][] data = new Object[10][10];
	  public void getData(){
		  try {
			  data = new Object[10][10];
			  readWithCsvBeanReader();
		  } catch (Exception e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
	  }

/*public final Object[] longValues = {"listingId",    	
"amount",
"currency",
"percentChange",
"quantity",
"row",
"sectionId",
"sectionName",
"zoneId",
"zoneName"};*/

public int getColumnCount() {
// TODO Auto-generated method stub
return columnNames.length;
}
public int getRowCount() {
// TODO Auto-generated method stub
return data.length;
}
public Object getValueAt(int row, int col) {
// TODO Auto-generated method stub
return data[row][col];
}
public String getColumnName(int col) {
return columnNames[col];
}


/*
* JTable uses this method to determine the default renderer/
* editor for each cell.  If we didn't implement this method,
* then the last column would contain text ("true"/"false"),
* rather than a check box.
*/
public Class getColumnClass(int c) {
return getValueAt(0, c).getClass();
}

/*
* Don't need to implement this method unless your table's
* editable.
*/
public boolean isCellEditable(int row, int col) {
//Note that the data/cell address is constant,
//no matter where the cell appears onscreen.
if (col < 2) {
return false;
} else {
return true;
}
}

/*
* Don't need to implement this method unless your table's
* data can change.
*/
public void setValueAt(Object value, int row, int col) {
/*if (DEBUG) {
System.out.println("Setting value at " + row + "," + col
         + " to " + value
         + " (an instance of "
         + value.getClass() + ")");
}*/

data[row][col] = value;
fireTableCellUpdated(row, col);

/*if (DEBUG) {
System.out.println("New value of data:");
printDebugData();
}*/
}

//Set up the processors used for the examples. 	 
	private static CellProcessor[] getProcessors() {
		
			final CellProcessor[] processors = new CellProcessor[] { 
	        		new UniqueHashCode(), // listingId (must be unique)
	                new NotNull(), // amount
	                new NotNull(), // currency
	                new NotNull(), // percentChange
	                new NotNull(),  // quantity
	                new NotNull(), // row
	                new NotNull(), // sectionId
	                new NotNull(), // sectionName
	                new NotNull(), // zoneId
	                new NotNull() // zoneName
	        };
	        
	        return processors;
	}	
	
	

//Read data from the CSV file using CsvBeanReader.	
	private static void readWithCsvBeanReader() throws Exception {
	        
	        ICsvBeanReader beanReader = null;
	        try {
	                beanReader = new CsvBeanReader(new FileReader("listings.csv"), CsvPreference.STANDARD_PREFERENCE);
	                
	                // the header elements are used to map the values to the bean (names must match)
	                final String[] header = beanReader.getHeader(true);
	                final CellProcessor[] processors = getProcessors();
	                
	                Listing customer;
	                int i=0;
	                while( (customer = beanReader.read(Listing.class, header, processors)) != null && i<10) {
	                	data[i][0] = customer.getListingId();
	                	//System.out.println(data[i][0]);
	                	data[i][1] = customer.getAmount();
	                	data[i][2] = customer.getCurrency();
	                	data[i][3] = customer.getPercentChange();
	                	data[i][4] = customer.getQuantity();
	                	data[i][5] = customer.getRow();
	                	data[i][6] = customer.getSectionId();
	                	data[i][7] = customer.getSectionName();
	                	data[i][8] = customer.getZoneId();
	                	data[i][9] = customer.getZoneName();
	                	i=i+1;
	                }
	                if(data==null){
	                	for(i=0;i<10;i++){
	                		data[0][i] = "/t";
	                	}	                	
	                }
	                
	        }
	        finally {
	                if( beanReader != null ) {
	                        beanReader.close();
	                }
	        }
	}

}
