/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.yelpdb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.simple.JSONArray;

import java.net.UnknownHostException;
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
public class ParseJSONObj {
    
    /* Use this part when parsing the mongoDB object directly from DB */
        public static void parseDBObj(){
            
             /* Mongo Client for connecting to MongoDB */
            MongoClient mongo;
  
            /* Mongo Database */
            DB db;
  
            /* Collection / table in Database */
            DBCollection table;
            
            try{
                mongo = new MongoClient("localhost", 27017);
                db = mongo.getDB("YelpDB");
                table = db.getCollection("businessIDs");
                
                System.out.println("Printing from DataBase");
                try{
                FileWriter writer = new FileWriter("/home/rmurali7/Desktop/yelp.csv");
                    writer.append("name ");
                    writer.append("url");
                    writer.append("phone");
              
                DBCursor cursor = table.find();
                while(cursor.hasNext()){
                    DBObject dbObj = cursor.next();
                    //System.out.println("From DB : " + dbObj);
                    //Object val = dbObj.get("name");
                    System.out.println("Name : " + dbObj.get("name"));
                    System.out.println("URL : " + dbObj.get("mobile_url"));
                    System.out.println("Phone : " + dbObj.get("phone"));
                    System.out.println("Location : " + dbObj.get("location"));
                    

                }
            }catch(IOException e) {
                    e.printStackTrace();
            }
            } catch(UnknownHostException e){
                e.printStackTrace();
           
            }
        }
        
    public static void main(String[] args) throws FileNotFoundException, JSONException{
        /*1. Use this part if parsing JSON object from file
        **
        **
        ** */
        //String JsonObj = "";
        BufferedReader buffer = null;
        
        try{
            String entryLine;
            buffer = new BufferedReader(new FileReader("/home/rmurali7/Desktop/yelp_restaurants.json"));
            FileWriter writer = new FileWriter("/home/rmurali7/Desktop/yelp.csv");
            writer.append("name");
            writer.append(',');
            writer.append("url");
            writer.append(',');
            writer.append("phone");
            writer.append(',');
            writer.append("category");
            writer.append(',');
            writer.append("latitude");
            writer.append(',');
            writer.append("longitude");
            writer.append(',');
            writer.append("Address");
            writer.append(',');
            writer.append("City");
            writer.append(',');
            writer.append("Pincode");
            writer.append('\n');
            
            while((entryLine = buffer.readLine()) != null) {
                System.out.println(entryLine);
                
                JSONObject json = new JSONObject(entryLine);
                JSONObject location = json.getJSONObject("location");                
                org.json.JSONArray address = location.getJSONArray("display_address");                                                                          
                //org.json.JSONArray address = location.getJSONArray("address");
                
                String name = json.getString("name");
                name = name.replaceAll(",", " ");                          
                String url = json.getString("mobile_url");                                 
                writer.append(name);
                writer.append(',');
                writer.append(url);
                writer.append(',');
                
                if(json.has("display_phone")){
                    String phone = json.getString("display_phone");                            
                    writer.append(phone);
                    writer.append(',');
                }
                else{
                    writer.append("no phone");
                    writer.append(',');
                }
                            
                if(json.has("categories")){
                    org.json.JSONArray categories = json.getJSONArray("categories");                                                
                    String cat = "";
                    String str;                    
                    for(int itr = 0; itr < categories.length(); itr++) {
                        org.json.JSONArray category = categories.getJSONArray(itr);
                        for(int jtr = 0; jtr < category.length(); jtr++){
                            str = category.getString(jtr);
                            str = str.replaceAll(",", " ");
                            cat += str + "; ";
                        }                        
                    }                    
                    writer.append(cat);                                 
                    writer.append(',');                 
                }
                else{
                    writer.append("no category");
                    writer.append(';');
                    writer.append(',');                 
                }
                            
                if(location.has("coordinate")){
                    JSONObject coordinate = location.getJSONObject("coordinate");
                    Double latitude = coordinate.getDouble("latitude");
                    Double longitude = coordinate.getDouble("longitude");                           
                    writer.append(Double.toString(latitude));
                    writer.append(',');
                    writer.append(Double.toString(longitude));
                    writer.append(',');
                }
                else{
                    writer.append("99999");
                    writer.append(',');
                    writer.append("99999");
                    writer.append(',');
                }
                String addr0 = address.getString(0);                
                String addr1 = "";
                if(address.length() == 1){
                    writer.append("no address" + ", ");
                    writer.append(addr0);
                }
                else if(address.length() == 2){
                    addr0 = addr0.replaceAll(",", " ");
                    addr1 = address.getString(1);
                    writer.append(addr0 + ", ");
                    writer.append(addr1);
                }
                else if(address.length() == 3) {
                    addr0 = addr0.replaceAll(",", " ");
                    addr1 = address.getString(2);
                    writer.append(addr0 + ", ");
                    writer.append(addr1);
                }
                                
                /*
                String addr0 = address.getString(0);
                addr0 = addr0.replaceAll(",", " ");
                if(address.length() == 1){
                    writer.append(addr0 + ", " + "NULL");
                }
                else if (address.length() == 2){                                                               
                    writer.append(addr0 + ", " + address.getString(1));
                }
                */
                writer.append('\n');            
            }   
            writer.flush();
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null)
                    buffer.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }                      
    }
}
