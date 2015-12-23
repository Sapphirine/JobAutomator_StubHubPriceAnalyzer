package DAO;

import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import Container.ContainerListing;
import Container.Listing;

public class ListingDAOImp implements ListingDAO
{

	@Autowired
	private static SessionFactory sessFact;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		sessFact = sessionFactory;
	}

	public void read()
	{
		
		Session sesh = sessFact.openSession();
		List<Listing> getList = new ArrayList<Listing>();

		Transaction tx = sesh.beginTransaction();
		try
		{	

			Query query = sesh.createQuery("from Listing where percentChange != '0'");

			getList = query.list();

			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
		}
		finally
		{
			sesh.close();
		}

		ContainerListing output = new ContainerListing();
		output.setListing(getList);
		
		try {
			writeToCSV(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void batchInsert(ContainerListing tickets) 
	{
		
		
		Session session = sessFact.openSession();
		Transaction tx = session.beginTransaction();

		try
		{			
			for(Listing listing : tickets.getListing())
			{				
				
				if(!checkEntityExists(session, Listing.class, listing.getListingId()))
				{
					System.out.println("not inserted");
					session.save(listing);

				}
				else
				{
					updatePrice(session, Listing.class, listing.getListingId(), listing.getCurrentPrice().getAmount());
				}
			}
			tx.commit();

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			session.close();
			return;
		}

		session.close();
	}
	
	public static boolean checkEntityExists( Session session, Class<?> ObjectClass, Serializable testKey) {
		
		return  session.get(ObjectClass, testKey) != null;

	}
	
	public static void updatePrice( Session session, Class<?> ObjectClass, Serializable testKey, double newPrice) {
		
		Listing listing = (Listing)session.get(ObjectClass, testKey);
		
		double oldPrice = listing.getCurrentPrice().getAmount();
		
		listing.getCurrentPrice().setAmount(newPrice);
		listing.setPercentChange((oldPrice-newPrice)/100 * 100);
		
		session.update(listing);

	}
	
	
private static void writeToCSV(ContainerListing tickets) throws Exception {
        
        
        ICsvBeanWriter beanWriter = null;
        try {
                beanWriter = new CsvBeanWriter(new FileWriter("./listings.csv"),
                        CsvPreference.STANDARD_PREFERENCE);
                
                // the header elements are used to map the bean values to each column (names must match)
                final String[] header = new String[] { "listingid", "amount", "currency", "percentChange",
                        "quantity", "row", "sectionId", "sectionName", "zoneId", "zoneName" };
                final CellProcessor[] processors = getProcessors();
                
                // write the header
                beanWriter.writeHeader(header);
                
                // write the beans
                for(Listing ticket : tickets.getListing() ) 
                {
                        beanWriter.write(ticket, header, processors);
                }
                
        }
        finally {
                if( beanWriter != null ) {
                        beanWriter.close();
                }
        }
	}
	
	
	private static CellProcessor[] getProcessors() {
        
        final CellProcessor[] processors = new CellProcessor[] { 
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional(),
        		 new Optional()
        		 
        };
        
        return processors;
	}

	
}
