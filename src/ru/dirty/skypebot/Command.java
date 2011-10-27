package ru.dirty.skypebot;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Command
{
	public static final String CMD_ENABLE = "enable";
	public static final String CMD_DISABLE = "disable";
	public static final String CMD_ADMINADD = "admin";
    public static final String CMD_ADMINREMOVE = "user";
	public static final String CMD_ADMINLIST = "admins";
	public static final String CMD_QUOTES = "quotes";
	public static final String CMD_RELAOD_QUOTES = "reloadquotes";
	public static final String CMD_QUOTE_INFO = "quoteinfo";
	public static final String CMD_QUOTE_ADD = "quote_add";
	public static final String CMD_CURRENCY = "curr";
	public static final String CMD_HELP = "help";
	public static final String CMD_WEATHER = "wz";
	public static final String CMD_FOOD = "food";
	public static final String CMD_RELOADMESSAGES = "reloadmessages";
	public static final String CMD_SETLISTENER = "shutup";
	public static final String CMD_KICK = "kick";
	public static final String CMD_KICKBAN = "kickban";
	public static final String CMD_MODER = "moder";
	public static final String CMD_USER = "forgive";
	public static final String CMD_ADD = "add";
	public static final String CMD_FLOODERS = "flooders";
	public static final String CMD_ONNN = "onnn";
	public static final String CMD_ONNN_START = "startonnn";
	public static final String CMD_UNONNN = "unonnn";
    public static final String CMD_CHAT_STATS = "chatstats";
    public static final String CMD_USR_STATS = "stats";
    public static final String CMD_TOPIC = "topic";
    public static final String CMD_EXITCOST = "exitcost";
    public static final String CMD_BILLING = "billing";
    public static final String CMD_RSS = "rss";
    public static final String CMD_SQK = "sql";
    public static final String CMD_UPD = "upd";

	
	
	private ArrayList<String> myArgs;
	private String myArgsString="";
	private String myType;
	private String myText;
	private ChatMessage myMessage;

    public static String getHelp()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Available commands:\n");
		sb.append("!help - this help\n");
		sb.append("!onnn - get ONNN list)\n");
		sb.append("!onnn [skype_id] - vote ONNN for user .\n");
		sb.append("!unonnn [skype_id] - unvote ONNN for user.\n");
		sb.append("!curr [currency1] [currency2] - currency exchange rates\n");
        sb.append("!stats [skype_id] - get user statistics for this chat\n");
        sb.append("!chatstats - get chat statistics\n");
        sb.append("!rss [url] - subscribe chat to rss\n");
        //sb.append("!unrss - unsubscribe chat from rss\n");
		return sb.toString();
	}
	
	public void reply(String message) throws SkypeException {
		myMessage.getChat().send(message);
	}
	
	public String getArgsString()
	{
		return myArgsString.trim();		
	}
	
	
	public Command(ChatMessage message) throws SkypeException
	{
		myMessage = message;
		String command = message.getContent().trim();
		if (!command.startsWith("!") || command.length()<=1)
		{
			throw new IllegalArgumentException("Incorrect syntax");	
		}
		myText = command;
		String s = myText.substring(1); 
		StringTokenizer tokenizer = new StringTokenizer(s, " ");
		if (tokenizer.hasMoreTokens())
		{
			myType = tokenizer.nextToken();
		}else
		{
			throw new IllegalArgumentException("Incorrect syntax");
		}
		myArgs = new ArrayList<String>();
		while (tokenizer.hasMoreTokens())
		{
			myArgs.add(tokenizer.nextToken());
		}
		if (myArgs.size()>0)
		{
			myArgsString = s.substring(myType.length());
		}
	}
	
	public boolean isTypeOf(String type)
	{
		return myType.trim().equalsIgnoreCase(type.trim());
	}
	
	public ArrayList<String> getArgs()
	{
		return myArgs;
	}
	
	public ChatMessage getChatMessage()
	{
		return myMessage;
	}
	
}
