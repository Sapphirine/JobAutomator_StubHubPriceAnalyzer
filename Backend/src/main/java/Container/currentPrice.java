package Container;

import javax.persistence.Embeddable;

@Embeddable
public class currentPrice 
{
	
	double amount;
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	String currency;
}
