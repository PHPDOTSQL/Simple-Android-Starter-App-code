package com.codebridge.getstockquotes_dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class StockInfoActivity extends Activity {
	
	// Used to identify the app in the LogCat, so I
	// can output messages and debug the program
	
	private static final String TAG = "STOCKQUOTE";
	
	// Define the TextViews I use in activity_stock_info.xml
	
	TextView companyNameTextView;
	TextView yearLowTextView;
	TextView yearHighTextView;
	TextView daysLowTextView;
	TextView daysHighTextView;
	TextView lastTradePriceOnlyTextView;
	TextView changeTextView;
	TextView daysRangeTextView;
	
	// XML node keys
	static final String KEY_ITEM = "quote"; // parent node
	static final String KEY_NAME = "Name";
	static final String KEY_YEAR_LOW = "YearLow";
	static final String KEY_YEAR_HIGH = "YearHigh";
	static final String KEY_DAYS_LOW = "DaysLow";
	static final String KEY_DAYS_HIGH = "DaysHigh";
	static final String KEY_LAST_TRADE_PRICE = "LastTradePriceOnly";
	static final String KEY_CHANGE = "Change";
	static final String KEY_DAYS_RANGE = "DaysRange";
	
	// XML Data to Retrieve
	String name = "";
	String yearLow = "";
	String yearHigh = "";
	String daysLow = "";
	String daysHigh = "";
	String lastTradePriceOnly = "";
	String change = "";
	String daysRange = "";
	
	// Used to make the URL to call for XML data
	String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	String [][] xmlPullParserArray = {{"AverageDailyVolume", "0",}, {"Change", "0",},
			{"DaysLow", "0",}, {"DaysHigh", "0",}, {"YearLow", "0",}, 
			{"YearHigh", "0",}, {"MarketCapitalization", "0",}, {"LastTradePriceOnly", "0",},
			{"DaysRange", "0",}, {"Name", "0",}, {"Symbol", "0",},
			{"Volume", "0",}, {"StockExchange", "0",}};
	
	int parserArrayIncrement = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Creates the window used for the UI
		setContentView(R.layout.activity_stock_info);
		
		// Get the message from the intent that has the stock symbol
		Intent intent = getIntent();
		String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);
		
		// Initialize TextViews
		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
		yearLowTextView = (TextView) findViewById(R.id.yearLowTextView);
		yearHighTextView = (TextView) findViewById(R.id.yearHighTextView);
		daysLowTextView = (TextView) findViewById(R.id.daysLowTextView);
		daysHighTextView = (TextView) findViewById(R.id.daysHighTextView);
		lastTradePriceOnlyTextView = (TextView) findViewById(R.id.lastTradePriceOnlyTextView);
		changeTextView = (TextView) findViewById(R.id.changeTextView);
		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);
		
		// Sends a message to the LogCat
		Log.d(TAG, "Before URL Creation " + stockSymbol);
		
		// Create the YQL query
		final String yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
		
		// The Android UI toolkit is not thread safe and must always be 
		// manipulated on the UI thread. This means if I want to perform
		// any network operations like grabbing xml data, I have to do it
		// in its own thread. The problem is that you can't write to the
		// GUI from outside the main activity. AsyncTask solves those problems
		
		new MyAsyncTask().execute(yqlURL);

	}
	
	// Use AsyncTask if you need to perform background tasks, but also need
	// to change components on the GUI. Put the background operations in
	// doInBackground. Put the GUI manipulation code in onPostExecute
	
	private class MyAsyncTask extends AsyncTask<String, String, String>{

		// String... arg0 is the same as String[] args
		protected String doInBackground(String... args) {

			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				
				factory.setNamespaceAware(true);
				
				XmlPullParser parser = factory.newPullParser();
				
				parser.setInput(new InputStreamReader(getUrlData(args [0])));
				
				beginDocument(parser, "query");
				
				int eventType = parser.getEventType();
			
				do{
					
					nextElement(parser);
					
					parser.next();
					
					eventType = parser.getEventType();
					
					if(eventType == XmlPullParser.TEXT){
						
					
						String valueFromXML = parser.getText();
						
						xmlPullParserArray[parserArrayIncrement++][1] = valueFromXML;
					}
				
				} while (eventType != XmlPullParser.END_DOCUMENT);
				
			} 
			
			catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				finally{ };
				
		return null;
		}
		


		public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException{
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet method = new HttpGet(new URI(url));
			HttpResponse res = client.execute(method);
			return res.getEntity().getContent();
			
		}
		
		public final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException{
			
			int type;
			
			while ((type=parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT)
				;
	
			if(type != parser.START_TAG)
				throw new XmlPullParserException("No Start Tag Found");
	
			if (!parser.getName().equals(firstElementName)){
			
			throw new XmlPullParserException("Unexpected Start Tag Found" + parser.getName() + ": expected" + firstElementName);
			}
		}
		
		
			/*	This way uses the DOM approach which is more processor intensive and
			 * uses more battery and of course is slower than using XMLPullParser
			 * which is what we are going to do next instead.
			 * 
					
			try {
			      
				// Get the XML URL that was passed in
				
			      URL url = new URL(args[0]);
			      
			      // connection is the communications link between the 
			      // application and a URL that we will read from.
			      
			      URLConnection connection;
			      connection = url.openConnection();

			      // Used to take advantage of HTTP specific features. 
			      // Provides tools that tell us if a connection was
			      // made, lost and other HTTP Status Codes
			      
			      HttpURLConnection httpConnection = (HttpURLConnection)connection;
			      
			      // Did we connect properly the the URL?
			      
			      int responseCode = httpConnection.getResponseCode();
			      
			        // Tests if responseCode == 200 Good Connection
			     
			        if (responseCode == HttpURLConnection.HTTP_OK) {
			    	 
			    	// Reads data from the connection
			        
			        InputStream in = httpConnection.getInputStream();

			        // Provides a way to parse DOM object trees from XML documents
			        
			        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			        
			        // Provides a DOM Document from an xml page
			        
			        DocumentBuilder db = dbf.newDocumentBuilder();

			        // Parse the Yahoo Financial YQL Stock XML File
			        
			        Document dom = db.parse(in);
			        
			        // The root element is query
			        
			        Element docEle = dom.getDocumentElement();

			        // Get a list of quote nodes
			        
			        NodeList nl = docEle.getElementsByTagName("quote");
			        
			        
			        // Checks to make sure we found a quote tag
			        
			        if (nl != null && nl.getLength() > 0) {
			        	
			        // Cycles through if we find multiple quote tags
			        // Mainly used for demonstration purposes
			        
			        for (int i = 0 ; i < nl.getLength(); i++) {
			            
			        // Passes the root element of the XML page, so 
			       	// that the function below can search for the 
			        // information needed
			      
			            StockInfo theStock = getStockInformation(docEle);
			            
			            // Gets the values stored in the StockInfo object
			            
			            daysLow = theStock.getDaysLow();
			            daysHigh = theStock.getDaysHigh();
			            yearLow = theStock.getYearLow();
			            yearHigh = theStock.getYearHigh();
			            name = theStock.getName();
			            lastTradePriceOnly = theStock.getLastTradePriceOnly();
			            change = theStock.getChange();
			            daysRange = theStock.getDaysRange();
			            
			            // Outputs information for tracking reasons only
			            
			            Log.d(TAG, "Stock Name " + name);
			            Log.d(TAG, "Stock Year High " + yearHigh);
			            Log.d(TAG, "Stock Year Low " + yearLow);
			            Log.d(TAG, "Stock Days High " + daysHigh);
			            Log.d(TAG, "Stock Days Low " + daysLow);

			            
			          }
			        }
			      }
			    } catch (MalformedURLException e) {
			      Log.d(TAG, "MalformedURLException", e);
			    } catch (IOException e) {
			      Log.d(TAG, "IOException", e);
			    } catch (ParserConfigurationException e) {
			      Log.d(TAG, "Parser Configuration Exception", e);
			    } catch (SAXException e) {
			      Log.d(TAG, "SAX Exception", e);
			    }
			    finally {
			    }
			    
			    END OF REMOVED CODE FROM DOING DOM PARSING*/
			

		
		// Changes the values for a bunch of TextViews on the GUI
		protected void onPostExecute(String result){
			
			companyNameTextView.setText(xmlPullParserArray[9][1]);
			yearLowTextView.setText("Year Low: " + xmlPullParserArray[4][1]);
			yearHighTextView.setText("Year High: " + xmlPullParserArray[5][1]);
			daysLowTextView.setText("Days Low: " + xmlPullParserArray[2][1]);
			daysHighTextView.setText("Days High: " + xmlPullParserArray[3][1]);
			lastTradePriceOnlyTextView.setText("Last Price: " + xmlPullParserArray[7][1]);
			changeTextView.setText("Change: " + xmlPullParserArray[1][1]);
			daysRangeTextView.setText("Daily Price Range: " + xmlPullParserArray[8][1]);
			
		}

		public void nextElement(XmlPullParser parser) throws XmlPullParserException, 
            IOException{
		
		int type;
		
		while((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT){
				
			;
		}
	}
				
}
	
}

	
/*	Also removed in favor of the XML Pull Parser Method 
 * that is replacing this DOM Method of doing things.
 * 
 * // Sends the root xml tag and the tag name we are searching for to
	// getTextValue for processing. Then uses that information to create
	// a new StockInfo object
	private StockInfo getStockInformation(Element entry){
		
		String Name = getTextValue(entry, "Name");
		String YearLow = getTextValue(entry, "YearLow");
		String YearHigh = getTextValue(entry, "YearHigh");
		String DaysLow = getTextValue(entry, "DaysLow");
		String DaysHigh = getTextValue(entry, "DaysHigh");
		String lastTradePriceOnlyTextView = getTextValue(entry, "LastTradePriceOnly");
		String Change = getTextValue(entry, "Change");
		String DaysRange = getTextValue(entry, "DaysRange");
		
		StockInfo theStock = new StockInfo(DaysLow, DaysHigh, YearLow,
			YearHigh, Name, lastTradePriceOnlyTextView,
			Change, DaysRange);
		
		return theStock;
		
	}
	
	// Searches through the XML document for a tag that matches 
	// the tagName passed in. Then it gets the value from that
	// tag and returns it
	
	private String getTextValue(Element entry, String tagName){
		
		String tagValueToReturn = null;
		
		NodeList nl = entry.getElementsByTagName(tagName);
		
		if(nl != null && nl.getLength() > 0){
			
			Element element = (Element) nl.item(0);
			
			tagValueToReturn = element.getFirstChild().getNodeValue();
		}
		
		return tagValueToReturn;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info.stock_info, menu);
		return true;
	}

}*/


