package ru.dirty.skypebot.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class AsyncXMLClient implements Runnable
{
	protected abstract String GetURL();
	protected abstract void Error(Exception ex);
	protected abstract void ProcessAnswer(Document doc) throws Exception;
	protected abstract void SetConnProperties(HttpURLConnection connection) throws ProtocolException;
	protected abstract String GetPostData(); 
	
	public void run()
	{
		try
		{
		      URL u = new URL(GetURL());
		      URLConnection uc = u.openConnection();
		      HttpURLConnection connection = (HttpURLConnection) uc;

		      //String xml = GetRequestXML();

		      SetConnProperties(connection);
		      
		      /*connection.setDoOutput(true);
		      connection.setDoInput(true);
		      connection.setRequestMethod("POST");
		      connection.setRequestProperty("SOAPAction", GetSoapAction());
		      connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		      connection.setRequestProperty("Content-Length", ""+xml.length());*/
		      
		      String postData = GetPostData();
		      if (postData!=null)
		      {
			      OutputStream out = connection.getOutputStream();
			      Writer wout = new OutputStreamWriter(out);
			      wout.write(postData);
			      wout.flush();
			      wout.close();
		      }
		      
		      InputStream in = connection.getInputStream();
		      //StringBuffer sb = new StringBuffer();
		      /*int c;
		      while ((c = in.read()) != -1) sb.append((char)c);
		      in.close();*/
  	    	  DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	    	  DocumentBuilder b = f.newDocumentBuilder();
	    	  Document document = b.parse(in);
	    	  ProcessAnswer(document);
		}catch (Exception ex)
		{
			Error(ex);
		}
		
	}

}
