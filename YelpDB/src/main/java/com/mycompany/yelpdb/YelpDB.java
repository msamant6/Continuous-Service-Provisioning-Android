/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.yelpdb;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.net.UnknownHostException;
import java.util.Date;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 *
 * @author rmurali7
 */
public class YelpDB {
  private static final String API_HOST = "api.yelp.com";
  private static final String DEFAULT_TERM = "dinner";
  private static final String DEFAULT_LOCATION = "Atlanta, GA";
  private static final int SEARCH_LIMIT = 20;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";

  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */
  private static final String CONSUMER_KEY = "xLeatpCzSpwLHXalsdraIg";
  private static final String CONSUMER_SECRET = "SMc-dpGTBoTynGR9YZz4nwjYHv8";
  private static final String TOKEN = "QYQEzAM09J6elNHW7SMrTf7yWPz0AATn";
  private static final String TOKEN_SECRET = "5zfNEzBgo2Wybm3otaBTizPPsS0";
  
  /* Mongo Client for connecting to MongoDB */
  private static MongoClient mongo;
  
  /* Mongo Database */
  private static DB db;
  
  /* Collection / table in Database */
  private static DBCollection table;

  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public YelpDB(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service =
        new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
            .apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
    
    try{
        /* Create a Mongo Client for connecting to MongoDB */
        this.mongo = new MongoClient("localhost", 27017);
        
        /* Create a Database */
        this.db = mongo.getDB("YelpDB");
    
        /* Create a Collection / table in YelpDB */
        this.table  = db.getCollection("businessIDs");
        
    }catch(UnknownHostException e){
        e.printStackTrace();
    }
  }

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   *
   * @param term <tt>String</tt> of the search term to be queried
   * @param location <tt>String</tt> of the location
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, String location) {
    String latitude = "33.783063"; 
    String longitude = "-84.400202"; 
    String radius_filter = "40000";
 
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    //request.addQuerystringParameter("location", location);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("radius_filter",radius_filter);
    request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and sends a request to the Business API by business ID.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
   * for more info.
   *
   * @param businessID <tt>String</tt> business ID of the requested business
   * @return <tt>String</tt> JSON Response
   */
  public String searchByBusinessId(String businessID) {
    OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   *
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
    return request;
  }

  /**
   * Sends an {@link OAuthRequest} and returns the {@link Response} body.
   *
   * @param request {@link OAuthRequest} corresponding to the API request
   * @return <tt>String</tt> body of API response
   */
  private String sendRequestAndGetResponse(OAuthRequest request) {
    System.out.println("Querying " + request.getCompleteUrl() + " ...");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  /**
   * Queries the Search API based on the command line arguments and takes the first result to query
   * the Business API.
   *
   * @param yelpApi <tt>YelpAPI</tt> service instance
   * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
   */
  private static void queryAPI(YelpDB yelpApi, YelpAPICLI yelpApiCli) {
    String searchResponseJSON =
        yelpApi.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location);

    JSONParser parser = new JSONParser();
    JSONObject response = null;
    try {
      response = (JSONObject) parser.parse(searchResponseJSON);
    } catch (ParseException pe) {
      System.out.println("Error: could not parse JSON response:");
      System.out.println(searchResponseJSON);
      System.exit(1);
    }

    JSONArray businesses = (JSONArray) response.get("businesses");
    for(int itr = 0; itr < businesses.size(); itr++){
    	JSONObject firstBusiness = (JSONObject) businesses.get(itr);
    	String firstBusinessID = firstBusiness.get("id").toString();
	System.out.println();
    	System.out.println(String.format(
        	"%s businesses found, querying business info for result %d - \"%s\" ...",
        	businesses.size(), itr+1, firstBusinessID));
        
    	// Select the first business and display business details
    	String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
    	System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
    	System.out.println(businessResponseJSON);
        
        /* Add the JSON object into the DB as DBObject */
        DBObject dbObj = (DBObject) JSON.parse(businessResponseJSON);
        table.insert(dbObj);
    }
    
    System.out.println("Printing from DataBase");
    DBCursor cursor = table.find();
    while(cursor.hasNext()){
        DBObject dbObj = cursor.next();
        System.out.println("From DB : " + dbObj);
        Object val = dbObj.get("name");
        System.out.println(val);
    }
  }

  /**
   * Command-line interface for the sample Yelp API runner.
   */
  private static class YelpAPICLI {
    @Parameter(names = {"-q", "--term"}, description = "Search Query Term")
    public String term = DEFAULT_TERM;

    @Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
    public String location = DEFAULT_LOCATION;
  }

  /**
   * Main entry for sample Yelp API requests.
   * <p>
   * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to run this example.
   */
  public static void main(String[] args) {
    YelpAPICLI yelpApiCli = new YelpAPICLI();
    new JCommander(yelpApiCli, args);

    YelpDB yelpApi = new YelpDB(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
    queryAPI(yelpApi, yelpApiCli);
  }    
}
