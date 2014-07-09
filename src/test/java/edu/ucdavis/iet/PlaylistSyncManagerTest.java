package edu.ucdavis.iet;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PlaylistSyncManagerTest
{
	// Create an instance of the PlaylistSyncManager class to test
	PlaylistSyncManager psm = new PlaylistSyncManager();
	
	@Test
	public void testWritetoJSONFile() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, MalformedURLException
	{
		// Create a list of text files of playlists to process
		ArrayList<FileInputStream> xmlFiles = new ArrayList<FileInputStream>();
		xmlFiles.add(new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/playlistInfo.xml"));
		xmlFiles.add(new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/chillStuffPlaylist.xml"));
		//xmlFiles.add(new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/playlist1.xml")); // Empty playlist, test will fail on this
				
		for (int i = 0; i < xmlFiles.size(); i++) 
		{
			// Create playlist file
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    Document xmlDocument = builder.parse(xmlFiles.get(i));
		    XPath xPath = XPathFactory.newInstance().newXPath();
		
		    String downloadableTrack = "/playlist/tracks/track[downloadable = 'true']/title";
		    String downloadURL = "/playlist/tracks/track[downloadable = 'true']/download-url";
		    NodeList trackList = (NodeList) xPath.compile(downloadableTrack).evaluate(xmlDocument, XPathConstants.NODESET);
		    NodeList urlList = (NodeList) xPath.compile(downloadURL).evaluate(xmlDocument, XPathConstants.NODESET);
		
		    // Testing function's arguments
		    psm.writetoJSONFile(trackList, urlList);
					
		    // Both input arguments must be NodeLists
		    assertTrue(trackList instanceof NodeList && urlList instanceof NodeList);
		 		
		    // Lists cannot be null and must be non-empty
		    assertNotNull(trackList);
		    assertNotNull(urlList);
		    assertTrue(trackList.getLength() > 0 && urlList.getLength() > 0);
		    		    	    		
		    // Validating correct URL
		    // Iterate list and create an object for every URL. If the URL is an invalid type, a MalformedURLException will be thrown
		    for (int k = 0; k < urlList.getLength(); k++)
		    {
		    	String trackURL = urlList.item(k).getFirstChild().getNodeValue();
	    		URL url = new URL(trackURL);	    	
		    }
		} 
	} 
} 
