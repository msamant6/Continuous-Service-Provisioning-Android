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


/**
 *
 * @author rmurali7
 */
public class YelpDataSetParse {
      public static void main(String[] args) throws FileNotFoundException, JSONException{
        /*1. Use this part if parsing JSON object from file
        **
        **
        ** */
        //String JsonObj = "";
        BufferedReader buffer = null;
        
        try{
            String entryLine;
            buffer = new BufferedReader(new FileReader("/home/rmurali7/Yelp_Dataset/yelp_academic_dataset_business.json"));
            FileWriter writer = new FileWriter("/home/rmurali7/Desktop/yelp_bus.csv");
            writer.append("name");
            writer.append(',');
            writer.append("url");
            writer.append(',');
            writer.append("phone");
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
                
                            String name = json.getString("name");
                            name = name.replaceAll(",", " ");  
                            Double lat = json.getDouble("latitude");
                            Double lon = json.getDouble("longitude");
                            String address = json.getString("full_address");
                            address = address.replaceAll("\n", " ");
                            String[] addr = address.split(",");
                            address = addr[0];                                                        
                            String pincode = addr[1];
                            String city = json.getString("city");
                            
                            
                            writer.append(name);
                            writer.append(',');
                            writer.append("no url");
                            writer.append(',');
                            writer.append("no phone");
                            writer.append(',');
                            writer.append(Double.toString(lat));
                            writer.append(',');
                            writer.append(Double.toString(lon));
                            writer.append(',');
                            writer.append(address);
                            writer.append(',');
                            writer.append(city);
                            writer.append(',');
                            writer.append(pincode);                            
                            
                            writer.append('\n');            
                            
                              //System.out.println("name: " + name);
                              //System.out.println("city: " + city);
                              //System.out.println("pincode: " + pincode);
                              //System.out.println("latitude: " + lat);
                              //System.out.println("longitude: " + lon);
                              //System.out.println("address: " + address);
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
