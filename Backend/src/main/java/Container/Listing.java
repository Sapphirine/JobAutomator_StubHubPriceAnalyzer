package Container;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table(name="pricelistings")
public class Listing implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="listingid")
	int listingId;
	
	@Embedded
	currentPrice currentPrice;
	
	int sectionId;
	
	String sectionName;
	
	String row;
	
	int quantity;
	
	int zoneId;

	String zoneName;
	
	double percentChange;
	

	
	
	
	
	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getListingId() {
		return listingId;
	}

	public void setListingId(int listingId) {
		this.listingId = listingId;
	}

	public currentPrice getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(currentPrice currentPrice) {
		this.currentPrice = currentPrice;
	}
	public double getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(double percentChange) {
		this.percentChange = percentChange;
	}
	
	public double getAmount()
	{
		return this.currentPrice.getAmount();
	}
	public String getCurrency()
	{
		return this.currentPrice.getCurrency();
	}
}
