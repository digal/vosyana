package ru.dirty.skypebot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.skype.SkypeException;
import com.skype.User;
import ru.dirty.skypebot.logging.Logger;


public class QuotesGenerator implements Comparable<QuotesGenerator>
{
    private ArrayList<String> myQuotes;
    private Pattern myPattern;
    private String myName;
    private String myPath;
    private boolean alwaysRespond=false;
    private int myPriority=0;
    private int myLastChoice=-1;
    private double myActionChance;
    private static Random ourRandom = new Random();
    
    public QuotesGenerator(String filename) throws ParserConfigurationException, SAXException, IOException
    {
      Logger.getInstance().log("Loading quotes from "+filename);
    	myQuotes = new ArrayList<String>();
    	myPath = filename;
    	DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    	DocumentBuilder b = f.newDocumentBuilder();
    	Document document = b.parse(new File(filename));
    	Element e = document.getDocumentElement();
    	fillFromElement(e);
    	//System.out.println(toString());
    }
    
    public boolean isAlwaysRespond() {
      return alwaysRespond;
    }

    public int getPriority()
    {
    	return myPriority;
    }
    
    public String getInfo()
    {
    	return myName + " chance = "+myActionChance + "; priority = "+ myPriority + "; phrases count = "+myQuotes.size();
    }
    
    public String toString()
    {
    	return "[QuotesGenerator: \""+myName+"\", pattern: \""+myPattern.pattern()+"\" chance: "+myActionChance+"]";
    }
    
    public boolean matches(String message)
    {
    	if (Math.random()<=myActionChance)
    	{
        	Matcher m = myPattern.matcher(message);
        	return m.find();
    	}
    	return false;
    }
    
    public String getQuote(User user) throws SkypeException
    {
    	int n = myQuotes.size();
    	//int i = (int)Math.round(Math.random()*(n))-1;
    	int i;
    	do
    	{
    		i = ourRandom.nextInt(n);
    	}while (i==myLastChoice && n>1);
    	myLastChoice = i;
    	if (i>=0)
    	{
    		String message = myQuotes.get(i);
    		String name = user.getFullName();
    		if (name == null || name.length()==0)
    		{
    			name = user.getId();
    		}
    		message = message.replace("{name}", name).replace("{id}", user.getId());
    		return message;
    	}
    	return null;
    	
    	
    }
    
    private void fillFromElement(Element e)
    {
    	myPattern = Pattern.compile(e.getAttribute("pattern"), Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
    	myName = e.getAttribute("name");
    	try
    	{
    		myActionChance = Double.parseDouble(e.getAttribute("chance"));
    		String s = e.getAttribute("priority"); 
    		if (s!=null && s.length()>0)
    		{
    			myPriority=Integer.parseInt(s);
    		}
    	}catch (NumberFormatException ex) {
    		ex.printStackTrace();
    	}
      if ("yes".equals(e.getAttribute("alwaysRespond"))) {
        System.out.println(""+e.getAttribute("name")+": alwaysRespond=true");
        alwaysRespond = true;
      }
    	NodeList list = e.getChildNodes();
    	for (int i=0; i<list.getLength(); i++)
    	{
    		Node n = list.item(i);
    		if (n instanceof Element)
			{
				Element element = (Element) n;
				if (element.getTagName().equalsIgnoreCase("quote"))
				{
					myQuotes.add(element.getTextContent());
				}
			}
    	}
    }

	public int compareTo(QuotesGenerator o)
	{
		if (myPriority==o.myPriority)
		{
			return new Double(myActionChance).compareTo(o.myActionChance);	
		}else
		{
			return new Integer(o.myPriority).compareTo(myPriority);
		}
		
	}
}
