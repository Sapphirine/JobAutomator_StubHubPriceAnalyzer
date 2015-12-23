package Container;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ContainerListing 
{

	List<Listing> listing;

	public List<Listing> getListing() {
		return listing;
	}

	public void setListing(List<Listing> listings) {
		this.listing = listings;
	}
	
}
