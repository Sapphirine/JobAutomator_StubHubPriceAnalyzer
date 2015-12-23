package Deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import Container.ContainerEvents;
import Container.Event;
import Container.Listing;
import Container.ContainerListing;
import Container.tokenResponse;
import DAO.ListingDAO;

@SpringBootApplication
public class StartMain 
{
	final static int[] events = {9348004, 9343265, 9346833, 9396673, 9400862, 9464068, 9394538, 9452994, 9370799, 9370764, 9370823, 9370771, 9370839};

	static ApplicationContext dbctx = new ClassPathXmlApplicationContext("Beans.xml");
	static ListingDAO cntDAO = dbctx.getBean(ListingDAO.class);
	
	public static void main(String[] args) 
	{
		
	    final Logger log = LoggerFactory.getLogger(StartMain.class);

	    if(args.length==0)
	    {
	    	stubhubPull();
	    }
	    else 
	    {
	    	if (args[0].equals("generateCSV"))
	    	{
	    		generateCSV();
	    	}
	    	
	    }
	   
		    	    
	}
	
	
	
	
	
	public static void stubhubPull()
	{
		RestTemplate restTemplate = new RestTemplate();

//		The API access token does not work for their events API --> Stubhub bug
//		final String eventsFetchURL =  "https://api.stubhub.com/search/catalog/events/v3?status=active |contingent&city=\"New York\"&start=0&rows=20&geoExpansion=false&sort=eventDateLocal asc";

		for(int i=0; i<events.length; i++)
		{
			
			final String listingsURL = "https://api.stubhub.com/search/inventory/v1/?eventid="+events[i];
	
	
			HttpEntity entity = getCredentials();
			
	//		ContainerEvents eventsResponse = restTemplate.getForObject(eventsFetchURL, ContainerEvents.class);
			ResponseEntity<ContainerListing> tickets = restTemplate.exchange(listingsURL, HttpMethod.GET, entity, ContainerListing.class);
	
			
			
			System.out.println("========================");
			System.out.println(tickets.getBody().getListing().get(0).getListingId());
			
			cntDAO.batchInsert(tickets.getBody());
		
		}
		System.out.println("Stubhub API Data Store complete");
	}
	
	public static void generateCSV()
	{
		
		cntDAO.read();
		
				
		System.out.println("CSV file generation complete");
	}
	
	
	
	
	
	
	//utility methods
	
	public static HttpEntity getCredentials()
	{
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-Type","application/x-www-form-urlencoded");
		requestHeaders.add("Authorization", "Bearer dd3b7bb585b3f3b1a1a99291c237bdf5");
		requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("grant_type","password");
		body.add("username","jd3225@columbia.edu");
		body.add("password","bigdatafa2015");
		body.add("scope","PRODUCTION");
		
		HttpEntity entity = new HttpEntity(body, requestHeaders);

		return entity;
	}
	
	public void fetchAccessToken()
	{

		final String url = "https://api.stubhub.com/login";	
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-Type","application/x-www-form-urlencoded");
		requestHeaders.add("Authorization", "Basic TENuazRjN2NsRVBCR0wzN2ZGMVVBMVRHY0ZBYTpZb1JSNm1VeVlxZm44VXBXbk5ibzlTOU5RbHNh");
		requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("grant_type","password");
		body.add("username","jd3225@columbia.edu");
		body.add("password","bigdatafa2015");
		body.add("scope","PRODUCTION");
		
		HttpEntity entity = new HttpEntity(body, requestHeaders);
		
		
//	    tokenResponse access = restTemplate.postForObject(url, entity, tokenResponse.class);
	}

}
