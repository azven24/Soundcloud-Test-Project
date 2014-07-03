package edu.ucdavis.iet;

public class TrackInfo
{
	public TrackInfo(String trackTitle, String trackURL)
	{
		this.trackTitle = trackTitle;
		this.trackURL = trackURL;		
	}
	
	public String getTitle()
	{
		return trackTitle;
	}
	
	public String getURL()
	{
		return trackURL;
	}
	
	private String trackTitle;
	private String trackURL;
}
