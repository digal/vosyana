package ru.dirty.skypebot.services;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.dirty.skypebot.Command;

public class CurrencyConverter extends AsyncSOAPClient
{
	private static final String ourUrl = "http://www.webservicex.net/CurrencyConvertor.asmx";
	private static final String ourSoapAct = "http://www.webserviceX.NET/ConversionRate";
	private static Dictionary<String, String> ourCurrencyCodes;
	
	static{
		ourCurrencyCodes = new Hashtable<String, String>();
		ourCurrencyCodes.put("AFA", "Afghanistan Afghani");
		ourCurrencyCodes.put("ALL", "Albanian Lek");
		ourCurrencyCodes.put("DZD", "Algerian Dinar");
		ourCurrencyCodes.put("ARS", "Argentine Peso");
		ourCurrencyCodes.put("AWG", "Aruba Florin");
		ourCurrencyCodes.put("AUD", "Australian Dollar");
		ourCurrencyCodes.put("BSD", "Bahamian Dollar");
		ourCurrencyCodes.put("BHD", "Bahraini Dinar");
		ourCurrencyCodes.put("BDT", "Bangladesh Taka");
		ourCurrencyCodes.put("BBD", "Barbados Dollar");
		ourCurrencyCodes.put("BZD", "Belize Dollar");
		ourCurrencyCodes.put("BMD", "Bermuda Dollar");
		ourCurrencyCodes.put("BTN", "Bhutan Ngultrum");
		ourCurrencyCodes.put("BOB", "Bolivian Boliviano");
		ourCurrencyCodes.put("BWP", "Botswana Pula");
		ourCurrencyCodes.put("BRL", "Brazilian Real");
		ourCurrencyCodes.put("GBP", "British Pound");
		ourCurrencyCodes.put("BND", "Brunei Dollar");
		ourCurrencyCodes.put("BIF", "Burundi Franc");
		ourCurrencyCodes.put("XOF", "CFA Franc (BCEAO)");
		ourCurrencyCodes.put("XAF", "CFA Franc (BEAC)");
		ourCurrencyCodes.put("KHR", "Cambodia Riel");
		ourCurrencyCodes.put("CAD", "Canadian Dollar");
		ourCurrencyCodes.put("CVE", "Cape Verde Escudo");
		ourCurrencyCodes.put("KYD", "Cayman Islands Dollar");
		ourCurrencyCodes.put("CLP", "Chilean Peso");
		ourCurrencyCodes.put("CNY", "Chinese Yuan");
		ourCurrencyCodes.put("COP", "Colombian Peso");
		ourCurrencyCodes.put("KMF", "Comoros Franc");
		ourCurrencyCodes.put("CRC", "Costa Rica Colon");
		ourCurrencyCodes.put("HRK", "Croatian Kuna");
		ourCurrencyCodes.put("CUP", "Cuban Peso");
		ourCurrencyCodes.put("CYP", "Cyprus Pound");
		ourCurrencyCodes.put("CZK", "Czech Koruna");
		ourCurrencyCodes.put("DKK", "Danish Krone");
		ourCurrencyCodes.put("DJF", "Dijibouti Franc");
		ourCurrencyCodes.put("DOP", "Dominican Peso");
		ourCurrencyCodes.put("XCD", "East Caribbean Dollar");
		ourCurrencyCodes.put("EGP", "Egyptian Pound");
		ourCurrencyCodes.put("SVC", "El Salvador Colon");
		ourCurrencyCodes.put("EEK", "Estonian Kroon");
		ourCurrencyCodes.put("ETB", "Ethiopian Birr");
		ourCurrencyCodes.put("EUR", "Euro");
		ourCurrencyCodes.put("FKP", "Falkland Islands Pound");
		ourCurrencyCodes.put("GMD", "Gambian Dalasi");
		ourCurrencyCodes.put("GHC", "Ghanian Cedi");
		ourCurrencyCodes.put("GIP", "Gibraltar Pound");
		ourCurrencyCodes.put("XAU", "Gold Ounces");
		ourCurrencyCodes.put("GTQ", "Guatemala Quetzal");
		ourCurrencyCodes.put("GNF", "Guinea Franc");
		ourCurrencyCodes.put("GYD", "Guyana Dollar");
		ourCurrencyCodes.put("HTG", "Haiti Gourde");
		ourCurrencyCodes.put("HNL", "Honduras Lempira");
		ourCurrencyCodes.put("HKD", "Hong Kong Dollar");
		ourCurrencyCodes.put("HUF", "Hungarian Forint");
		ourCurrencyCodes.put("ISK", "Iceland Krona");
		ourCurrencyCodes.put("INR", "Indian Rupee");
		ourCurrencyCodes.put("IDR", "Indonesian Rupiah");
		ourCurrencyCodes.put("IQD", "Iraqi Dinar");
		ourCurrencyCodes.put("ILS", "Israeli Shekel");
		ourCurrencyCodes.put("JMD", "Jamaican Dollar");
		ourCurrencyCodes.put("JPY", "Japanese Yen");
		ourCurrencyCodes.put("JOD", "Jordanian Dinar");
		ourCurrencyCodes.put("KZT", "Kazakhstan Tenge");
		ourCurrencyCodes.put("KES", "Kenyan Shilling");
		ourCurrencyCodes.put("KRW", "Korean Won");
		ourCurrencyCodes.put("KWD", "Kuwaiti Dinar");
		ourCurrencyCodes.put("LAK", "Lao Kip");
		ourCurrencyCodes.put("LVL", "Latvian Lat");
		ourCurrencyCodes.put("LBP", "Lebanese Pound");
		ourCurrencyCodes.put("LSL", "Lesotho Loti");
		ourCurrencyCodes.put("LRD", "Liberian Dollar");
		ourCurrencyCodes.put("LYD", "Libyan Dinar");
		ourCurrencyCodes.put("LTL", "Lithuanian Lita");
		ourCurrencyCodes.put("MOP", "Macau Pataca");
		ourCurrencyCodes.put("MKD", "Macedonian Denar");
		ourCurrencyCodes.put("MGF", "Malagasy Franc");
		ourCurrencyCodes.put("MWK", "Malawi Kwacha");
		ourCurrencyCodes.put("MYR", "Malaysian Ringgit");
		ourCurrencyCodes.put("MVR", "Maldives Rufiyaa");
		ourCurrencyCodes.put("MTL", "Maltese Lira");
		ourCurrencyCodes.put("MRO", "Mauritania Ougulya");
		ourCurrencyCodes.put("MUR", "Mauritius Rupee");
		ourCurrencyCodes.put("MXN", "Mexican Peso");
		ourCurrencyCodes.put("MDL", "Moldovan Leu");
		ourCurrencyCodes.put("MNT", "Mongolian Tugrik");
		ourCurrencyCodes.put("MAD", "Moroccan Dirham");
		ourCurrencyCodes.put("MZM", "Mozambique Metical");
		ourCurrencyCodes.put("MMK", "Myanmar Kyat");
		ourCurrencyCodes.put("NAD", "Namibian Dollar");
		ourCurrencyCodes.put("NPR", "Nepalese Rupee");
		ourCurrencyCodes.put("ANG", "Neth Antilles Guilder");
		ourCurrencyCodes.put("NZD", "New Zealand Dollar");
		ourCurrencyCodes.put("NIO", "Nicaragua Cordoba");
		ourCurrencyCodes.put("NGN", "Nigerian Naira");
		ourCurrencyCodes.put("KPW", "North Korean Won");
		ourCurrencyCodes.put("NOK", "Norwegian Krone");
		ourCurrencyCodes.put("OMR", "Omani Rial");
		ourCurrencyCodes.put("XPF", "Pacific Franc");
		ourCurrencyCodes.put("PKR", "Pakistani Rupee");
		ourCurrencyCodes.put("XPD", "Palladium Ounces");
		ourCurrencyCodes.put("PAB", "Panama Balboa");
		ourCurrencyCodes.put("PGK", "Papua New Guinea Kina");
		ourCurrencyCodes.put("PYG", "Paraguayan Guarani");
		ourCurrencyCodes.put("PEN", "Peruvian Nuevo Sol");
		ourCurrencyCodes.put("PHP", "Philippine Peso");
		ourCurrencyCodes.put("XPT", "Platinum Ounces");
		ourCurrencyCodes.put("PLN", "Polish Zloty");
		ourCurrencyCodes.put("QAR", "Qatar Rial");
		ourCurrencyCodes.put("ROL", "Romanian Leu");
		ourCurrencyCodes.put("RUB", "Russian Rouble");
		ourCurrencyCodes.put("WST", "Samoa Tala");
		ourCurrencyCodes.put("STD", "Sao Tome Dobra");
		ourCurrencyCodes.put("SAR", "Saudi Arabian Riyal");
		ourCurrencyCodes.put("SCR", "Seychelles Rupee");
		ourCurrencyCodes.put("SLL", "Sierra Leone Leone");
		ourCurrencyCodes.put("XAG", "Silver Ounces");
		ourCurrencyCodes.put("SGD", "Singapore Dollar");
		ourCurrencyCodes.put("SKK", "Slovak Koruna");
		ourCurrencyCodes.put("SIT", "Slovenian Tolar");
		ourCurrencyCodes.put("SBD", "Solomon Islands Dollar");
		ourCurrencyCodes.put("SOS", "Somali Shilling");
		ourCurrencyCodes.put("ZAR", "South African Rand");
		ourCurrencyCodes.put("LKR", "Sri Lanka Rupee");
		ourCurrencyCodes.put("SHP", "St Helena Pound");
		ourCurrencyCodes.put("SDD", "Sudanese Dinar");
		ourCurrencyCodes.put("SRG", "Surinam Guilder");
		ourCurrencyCodes.put("SZL", "Swaziland Lilageni");
		ourCurrencyCodes.put("SEK", "Swedish Krona");
		ourCurrencyCodes.put("TRY", "Turkey Lira");
		ourCurrencyCodes.put("CHF", "Swiss Franc");
		ourCurrencyCodes.put("SYP", "Syrian Pound");
		ourCurrencyCodes.put("TWD", "Taiwan Dollar");
		ourCurrencyCodes.put("TZS", "Tanzanian Shilling");
		ourCurrencyCodes.put("THB", "Thai Baht");
		ourCurrencyCodes.put("TOP", "Tonga Pa'anga");
		ourCurrencyCodes.put("TTD", "Trinidad&amp;Tobago Dollar");
		ourCurrencyCodes.put("TND", "Tunisian Dinar");
		ourCurrencyCodes.put("TRL", "Turkish Lira");
		ourCurrencyCodes.put("USD", "U.S. Dollar");
		ourCurrencyCodes.put("AED", "UAE Dirham");
		ourCurrencyCodes.put("UGX", "Ugandan Shilling");
		ourCurrencyCodes.put("UAH", "Ukraine Hryvnia");
		ourCurrencyCodes.put("UYU", "Uruguayan New Peso");
		ourCurrencyCodes.put("VUV", "Vanuatu Vatu");
		ourCurrencyCodes.put("VEB", "Venezuelan Bolivar");
		ourCurrencyCodes.put("VND", "Vietnam Dong");
		ourCurrencyCodes.put("YER", "Yemen Riyal");
		ourCurrencyCodes.put("YUM", "Yugoslav Dinar");
		ourCurrencyCodes.put("ZMK", "Zambian Kwacha");
		ourCurrencyCodes.put("ZWD", "Zimbabwe Dollar");
	}
	
	private Command myCommand;

	public String getArgsError()
	{
		if (myCommand.getArgs().size()!=2 && myCommand.getArgs().size()!=0)
		{
			return "Usage:\nconvert: !curr currency1 currency2\n" +
					"get currency codes: !curr";
			
		}
		String c1 = myCommand.getArgs().get(0).toUpperCase();
		String c2 = myCommand.getArgs().get(1).toUpperCase();
		if (myCommand.getArgs().size()!=0 && ourCurrencyCodes.get(c1)==null)
		{
			return "Unknown currency: \""+c1+"\"";
		}else if (myCommand.getArgs().size()!=0 && ourCurrencyCodes.get(c2)==null)
		{
			return "Unknown currency: \""+c2+"\"";
		}else
		{
			return null;
		}
	}
	
	public static String getHelp()
	{
		StringBuffer ret = new StringBuffer("Command usage:\n!curr currency1 currency2\nCurrency codes:\n");
		Enumeration<String> codes = ourCurrencyCodes.keys();
		while (codes.hasMoreElements())
		{
			String code = codes.nextElement();
			String desc = ourCurrencyCodes.get(code);
			ret.append(code).append(" - ").append(desc).append("\n");
		}
		return ret.toString();
	}
	
	public CurrencyConverter(Command command)
	{
		myCommand = command;
	}
	
	@Override
	protected void Error(Exception ex)
	{
		try
		{
			myCommand.getChatMessage().getChat().send("Error: "+ex.getMessage());	
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected String GetRequestXML()
	{
		String c1 = myCommand.getArgs().get(0).toUpperCase();
		String c2 = myCommand.getArgs().get(1).toUpperCase();
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buf.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		buf.append("<soap:Body>");
		buf.append("<ConversionRate xmlns=\"http://www.webserviceX.NET/\">");
		buf.append("<FromCurrency>").append(c1).append("</FromCurrency>");
		buf.append("<ToCurrency>").append(c2).append("</ToCurrency>");
		buf.append("</ConversionRate>");
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
		try
		{
		Element e = doc.getDocumentElement();
		NodeList nl = e.getChildNodes();
    	for (int i=0; i<nl.getLength(); i++)
    	{
    		Node n = nl.item(i);
    		if (n instanceof Element)
			{
				Element element = (Element) n;
				if (element.getTagName().equalsIgnoreCase("soap:Body"))
				{
					NodeList nl2 = element.getChildNodes();
			    	for (int j=0; j<nl2.getLength(); j++)
			    	{
			    		Node n2 = nl2.item(j);
			    		if (n2 instanceof Element)
						{
			    			Element element2 = (Element) n2;
	
		    				if (element2.getTagName().equalsIgnoreCase("ConversionRateResponse"))
		    				{
		    					NodeList nl3 = element2.getChildNodes();
		    			    	for (int k=0; k<nl3.getLength(); k++)
		    			    	{
		    			    		Node n3 = nl3.item(k);
		    			    		if (n3 instanceof Element)
		    						{
		    			    			Element element3 = (Element) n3;
		    		    				if (element3.getTagName().equalsIgnoreCase("ConversionRateResult"))
		    		    				{
		    		    					double rate = Double.parseDouble(element3.getTextContent());
		    		    					String c1 = myCommand.getArgs().get(0).toUpperCase();
		    		    					String c2 = myCommand.getArgs().get(1).toUpperCase();
		    		    					myCommand.getChatMessage().getChat().send(
		    		    							c1 + " to " + c2 + " rate is "+rate+".");
		    		    					return;
		    		    				}
		    						}
	    						}
	    			    	}
	    				}

					}
				}
			}
    	}
    	
		}catch (Exception ex)
		{
			Error(ex);
		}
		
	}


}
