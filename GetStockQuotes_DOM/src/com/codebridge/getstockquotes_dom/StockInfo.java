package com.codebridge.getstockquotes_dom;

public class StockInfo {
	
	private String daysLow = "";
	private String daysHigh = "";
	private String yearLow = "";
	private String yearHigh = "";
	private String name = "";
	private String lastTradePriceOnly = "";
	private String change = "";
	private String daysRange = "";
	
	public StockInfo(String daysLow, String daysHigh, String yearLow,
			String yearHigh, String name, String lastTradePriceOnly,
			String change, String daysRange) {
		this.daysLow = daysLow;
		this.daysHigh = daysHigh;
		this.yearLow = yearLow;
		this.yearHigh = yearHigh;
		this.name = name;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.change = change;
		this.daysRange = daysRange;
	}

	public String getDaysLow() { return daysLow; }
	/**
	 * 
	 */
	public StockInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}
	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}
	public void setYearLow(String yearLow) {
		this.yearLow = yearLow;
	}
	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLastTradePriceOnly(String lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	}
	public String getDaysHigh() { return daysHigh; }
	public String getYearLow() { return yearLow; }
	public String getYearHigh() { return yearHigh; }
	public String getName() { return name; }
	  
	public String getLastTradePriceOnly() { return lastTradePriceOnly; }
	public String getChange() { return change;}
	public String getDaysRange() {return daysRange;}
	



}
