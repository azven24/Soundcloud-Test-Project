package edu.ucdavis.iet;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.Object;
import java.util.HashMap;
import java.util.Map;

public class PlaylistSyncManager implements Callable 
{
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception
	{
		parseXMLconvertJSON();
		return null;
	}

	public void parseXMLconvertJSON() throws SAXException, IOException, XPathExpressionException, ParserConfigurationException
	{
		// Build DOM XML Parser
		FileInputStream xmlFile = new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/playlistInfo.xml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(xmlFile);
		XPath xPath = XPathFactory.newInstance().newXPath();

		// Display titles and URLS of all tracks that are downloadable
		String downloadableTrack = "/playlist/tracks/track[downloadable = 'true']/title";
		String downloadURL = "/playlist/tracks/track[downloadable = 'true']/download-url";
		NodeList trackList = (NodeList) xPath.compile(downloadableTrack).evaluate(xmlDocument, XPathConstants.NODESET);
		NodeList urlList = (NodeList) xPath.compile(downloadURL).evaluate(xmlDocument, XPathConstants.NODESET);
		System.out.println("\n");

		for (int i = 0; i < trackList.getLength(); i++)
			System.out.println("Track title: " + trackList.item(i).getFirstChild().getNodeValue() + "\n" + "Download URL: " + urlList.item(i).getFirstChild().getNodeValue() + "\n");

		writetoJSONFile(trackList, urlList);
	}

	public void writetoJSONFile(NodeList trackList, NodeList urlList) throws IOException 
	{		
		Map<String, Boolean> config = new HashMap<String, Boolean>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		
		JsonBuilderFactory jsonObjFactory = Json.createBuilderFactory(config);
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
				
		// Create an object for each track/URL, then add it to the array of objects
	    for (int i = 0; i < trackList.getLength(); i++) 
		{
			 JsonObject trackObj = Json.createObjectBuilder()
			.add("track", trackList.item(i).getFirstChild().getNodeValue())
			.add("url", urlList.item(i).getFirstChild().getNodeValue())
			.build(); 
		
			 arrayBuilder.add(trackObj);
		} 
				
	    // Build the contents of the array 
	    JsonArray tracksArray = arrayBuilder.build();
	    	
	    // Build a JSON object containing all the tracks and their URLs
	    JsonObject jsonObj = jsonObjFactory.createObjectBuilder().add("Tracks", tracksArray).build();
	    		
		// Write JSON object to text file			
		FileWriter jfile = new FileWriter("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/data.txt");
		JsonWriter jsonWriter = Json.createWriterFactory(config).createWriter(jfile);
		jsonWriter.writeObject(jsonObj);
		jsonWriter.close();
	}
}
