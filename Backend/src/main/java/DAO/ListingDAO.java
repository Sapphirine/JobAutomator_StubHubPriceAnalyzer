package DAO;

import org.supercsv.cellprocessor.ift.CellProcessor;

import Container.Listing;
import Container.ContainerListing;

public interface ListingDAO 
{	
	public void batchInsert(ContainerListing tickets);

	public void read();
		
}
