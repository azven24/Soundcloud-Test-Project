package edu.ucdavis.iet;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	public void testWritetoJSONFile() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException 
	{
		// Create a list of textfiles of playlists to process
		ArrayList<FileInputStream> xmlFiles = new ArrayList<FileInputStream>();
		xmlFiles.add(new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/playlistInfo.xml"));
		xmlFiles.add(new FileInputStream("/Users/azven224/Documents/sc/esb/soundcloudapp/flows/playlist1.xml")); // Empty playlist
		int fileNum = 0; // Keeps track of file # while iterating playlists
		
		for (int i = 0; i < xmlFiles.size(); i++) 
		{
			fileNum++;
			System.out.print("START OF FILE #" + fileNum + "\n");
			
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
					
		    // Testing valid input: both must be NodeLists
		    assertTrue(trackList instanceof NodeList);
		    assertTrue(urlList instanceof NodeList);
		
		    // Testing valid input: both must be non-empty
		    assertNotNull(trackList);
		    assertNotNull(urlList);
		
		    // Testing invalid input: if one of the URL's is invalid
		
	
		    for (int j = 0; j < trackList.getLength(); j++)
		    	System.out.println("Track title: " + trackList.item(j).getFirstChild().getNodeValue() + "\n" + "Download URL: " + urlList.item(j).getFirstChild().getNodeValue() + "\n");
		
		    System.out.println("END OF FILE #" + fileNum + "\n");
		}
	}
}
