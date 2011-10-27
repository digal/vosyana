package ru.dirty.skypebot.services;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.dirty.skypebot.Command;

public class WeatherReporter extends AsyncSOAPClient
{
	
	private static final String ourUrl = "http://www.webservicex.net/globalweather.asmx";
	private static final String ourSoapAct = "http://www.webserviceX.NET/GetWeather";

	private Command myCommand;
	
	private void send(String s) throws Exception
	{
		myCommand.getChatMessage().getChat().send(s);
	}
	
	public WeatherReporter(Command command)
	{
		myCommand = command;
	}
	
	@Override
	protected void Error(Exception ex)
	{
		ex.printStackTrace();

	}

	@Override
	protected String GetRequestXML()
	{
		String c1 = myCommand.getArgsString();
		String c2 = "";
		int comma = c1.indexOf(',');
		if (comma>0)
		{
			c2 = c1.substring(comma+1).trim();
			c1 = c1.substring(0, comma).trim();
			System.out.println(c1);
			System.out.println(c2);

		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buf.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		buf.append("<soap:Body>");
		buf.append("<GetWeather xmlns=\"http://www.webserviceX.NET\">");
		buf.append("<CityName>").append(c1).append("</CityName>");
		//buf.append("<CityName>St. Peterburg</CityName>");
		buf.append("<CountryName>").append(c2).append("</CountryName>");
		buf.append("</GetWeather>");
		buf.append("</soap:Body>");
		buf.append("</soap:Envelope>");
		return buf.toString();
	}

	@Override
	protected String GetSoapAction()
	{
		return ourSoapAct;
	}

	@Override
	protected String GetURL()
	{
		return ourUrl;
	}

	@Override
	protected void ProcessAnswer(Document doc)
	{
		//System.out.println(doc.getTextContent());
		//System.out.println(doc.toString());
		try
		{
			NodeList nl = doc.getElementsByTagName("GetWeatherResult");
			if (nl.getLength()==0)
			{
				send("No data found :(");
			}else
			{
				Element e = (Element)nl.item(0);
				String s = e.getTextContent().trim();
				System.out.println(s);
				s=s.replace("\n", "");
				/*System.out.println(Integer.toHexString((int)s.charAt(0)));
				System.out.println(Integer.toHexString((int)s.charAt(1)));
				System.out.println(Integer.toHexString((int)s.charAt(38)));
				System.out.println(Integer.toHexString((int)s.charAt(39)));*/
				s=s.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
				
				try
				{
		  	    	DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			    	DocumentBuilder b = f.newDocumentBuilder();
			    	Document document = b.parse(new ByteArrayInputStream(s.getBytes()));
					InnerProcessAnswer(document);
				}catch(Exception ex)
				{
					ex.printStackTrace();
					send (s.toString());
				}
		    	
			}
		}catch (Exception ex)
		{
			Error(ex);
		}
	}

	private void InnerProcessAnswer(Document document) throws Exception
	{
		NodeList nl1 = document.getElementsByTagName("CurrentWeather");
		if (nl1.getLength()>0)
		{
			StringBuffer s = new StringBuffer(":");
			Element e = (Element)nl1.item(0);
			NodeList nl2 = e.getChildNodes();
			for (int i=0; i<nl2.getLength(); i++)
			{
				Node n = nl2.item(i);
				if (n instanceof Element)
				{
					Element el = (Element)n;
					s.append("\n").append(el.getTagName()).append(": ").append(el.getTextContent());
				}
			}
			send (s.toString());
		}
		else
		{
			send (".");
			return;
		}
		
		

		
	}

}
