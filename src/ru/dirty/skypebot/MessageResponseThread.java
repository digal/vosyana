package ru.dirty.skypebot;

import com.skype.ChatMessage;
import ru.dirty.skypebot.domain.IChat;

public class MessageResponseThread implements Runnable
{
	private ChatMessage receivedChatMessage;
	private IChat chat;

	public MessageResponseThread(ChatMessage message, IChat ch)
	{
		receivedChatMessage = message;
    chat = ch;
	}
	
	private void sendDelayed(String s) throws Exception
	{
        Thread.sleep(4000+(int)(Math.random()*2000));
		receivedChatMessage.getChat().send(s);
	}
	
	public void run()
	{
        //Logger.getInstance().log("Message response thread run");
        try
		{
			if (receivedChatMessage.getContent().length()==0)
			{
				return;
			}
			if (Math.random()<0.0001)
			{
	    		String name = receivedChatMessage.getSender().getFullName();
	    		if (name == null || name.length()==0)
	    		{
	    			name = receivedChatMessage.getSender().getId();
	    		}
				String s = "/topic \""+receivedChatMessage.getContent()+"\" (c) "+name;
				sendDelayed(s);
			}else
			{
				if (receivedChatMessage.getChat().getAllMembers().length<=2)
				{
					for (QuotesGenerator qg : CommandGetter.getInstance().getPrivates())
					{
						if (qg.matches(receivedChatMessage.getContent()))
						{
							String s = qg.getQuote(receivedChatMessage.getSender());
							if (s!=null && s.length()!=0)
							{
								sendDelayed(s);
								return;
							}
						}
					}
				}
				for (QuotesGenerator qg : CommandGetter.getInstance().getQuoteGenerators())
				{
					if ((chat.isResponseEnabled() || qg.isAlwaysRespond())
               && qg.matches(receivedChatMessage.getContent()))
					{
						String s = qg.getQuote(receivedChatMessage.getSender());
						if (s!=null && s.length()!=0)
						{
							sendDelayed(s);
							return;
						}
					}
				}
			}
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}

		
	}

}
