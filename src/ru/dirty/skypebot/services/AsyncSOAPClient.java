package ru.dirty.skypebot.services;


import java.net.HttpURLConnection;
import java.net.ProtocolException;


public abstract class AsyncSOAPClient extends AsyncXMLClient
{
	protected abstract String GetSoapAction();
	protected abstract String GetRequestXML();
	
	protected void SetConnProperties(HttpURLConnection connection) throws ProtocolException
	{
	      connection.setDoOutput(true);
	      connection.setDoInput(true);
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("SOAPAction", GetSoapAction());
	      connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	      connection.setRequestProperty("Content-Length", ""+GetRequestXML().length());
	}

	protected String GetPostData()
	{
		return GetRequestXML();
	}
	
	/*public void run()
	{
		try
		{
		      URL u = new URL(GetURL());
		      URLConnection uc = u.openConnection();
		      HttpURLConnection connection = (HttpURLConnection) uc;

		      String xml = GetRequestXML();

		      
		      SetConnProperties(connection);
		      
		      OutputStream out = connection.getOutputStream();
		      Writer wout = new OutputStreamWriter(out);
		      wout.write(xml);
		      wout.flush();
		      wout.close();
		      
		      InputStream in = connection.getInputStream();
		      //StringBuffer sb = new StringBuffer();
  	    	  DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	    	  DocumentBuilder b = f.newDocumentBuilder();
	    	  Document document = b.parse(in);
	    	  ProcessAnswer(document);
		}catch (Exception ex)
		{
			Error(ex);
		}
		
	}*/

}
