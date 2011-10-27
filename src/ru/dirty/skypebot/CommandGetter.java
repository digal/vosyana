package ru.dirty.skypebot;

import com.skype.*;
import ru.dirty.skypebot.domain.IChat;
import ru.dirty.skypebot.domain.IUser;
import ru.dirty.skypebot.domain.Tag;
import ru.dirty.skypebot.logging.Logger;
import ru.dirty.skypebot.managers.ChatManager;
import ru.dirty.skypebot.managers.ONNNManager;
import ru.dirty.skypebot.managers.UserManager;
import ru.dirty.skypebot.services.CurrencyConverter;
import ru.dirty.skypebot.services.WeatherReporter;
import ru.dirty.skypebot.services.TwitterService;
import ru.dirty.skypebot.storage.DBManager;
import ru.dirty.skypebot.stats.StatsManager;

import javax.swing.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.SQLException;

public class CommandGetter extends JFrame
{ 
    
    private static final Logger logger = Logger.getInstance();
    private static String owner; 
    
	private Properties myProps;
	private Properties myMessages;
//	private ArrayList<String> myDisabledChats;
	private ArrayList<QuotesGenerator> myQGens;
	private ArrayList<QuotesGenerator> myPrivates;
	//private ArrayList<ONNN> myONNNs;                    com
	private static CommandGetter ourInstance;
	private boolean food = false;
    private String myMaster="";
    private final String myName;
	
	public static CommandGetter getInstance(String owner)
	{
		if (ourInstance == null)
		{
		    if (owner != null) CommandGetter.owner = owner;
		    logger.log("assigned owner " + owner);
			ourInstance = new CommandGetter();
		}
		return ourInstance;
	}	
	
	public static CommandGetter getInstance() {
	    return getInstance(null);
	}
	
	private CommandGetter()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		loadProperties();
		loadMessages();
		myName = myProps.getProperty("twUser");
        try {
            DBManager.getInstance().init();
            TwitterService.getInstance().init(myProps);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(1);
        }
        myMaster = myProps.getProperty("master");

        myQGens = QuotesLoader.getGeneratorsList("data/Phrases/");
		myPrivates = QuotesLoader.getGeneratorsList("data/Privates/");
//		myDisabledChats = ListStorage.loadList("data/disabled.dat");
//		System.out.println(myDisabledChats.size()+" chats disabled.");
	}

    
	

	public boolean isValidUser4Chat(String id)
	{
		try
		{
			if (Skype.getProfile().getId().equals(id))
			{
				return false;
			}
			User u = Skype.getUser(id);
			u.getAbout();
			return true;
		}catch (Throwable ex)
		{
			return false;	
		}
		
	}
	

	public Chat startChat(String[] members) throws SkypeException
	{
		return Skype.chat(members);
	}

    public boolean isMaster(String userId) {
        return myMaster.equals(userId);
    }


	public boolean isAdmin(String userId)
	{
        if (myMaster.equals(userId)) {
            return true;
        }
        IUser u = UserManager.getInstance().get(userId);
        if (u==null) {
            return false;
        }
        if (IUser.ROLE_ADMIN.equals(u.getRole())
                || IUser.ROLE_MASTER.equals(u.getRole()) )  {
            return true;
        } else {
            return false;
        }
    }
	
	private void loadMessages()
	{
		myMessages = new Properties();
                File pFile = new File("data/messages.xml");
		if (pFile.exists())
		{
			try
			{
				myMessages.loadFromXML(new FileInputStream(pFile));	
			}catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private void loadProperties()
	{
		
		myProps = new Properties();
		myProps.put(Constants.PROP_BOT_NAME, "vosyana");
		myProps.put(Constants.PROP_ONNN_VOTES, 5);
		myProps.put(Constants.PROP_ONNN_RO, 5);
		myProps.put(Constants.PROP_ONNN_KICK, 10);
		File pFile = new File((owner != null) 
		                        ? "data/properties." + owner + ".xml" 
		                        : "data/properties.xml");
		if (pFile.exists())
		{
			try
			{
				myProps.loadFromXML(new FileInputStream(pFile));	
			}catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}else
		{
			try
			{
				pFile.createNewFile();
				myProps.storeToXML(new FileOutputStream(pFile), null, "UTF-16");
			}catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

    public void processRecievedMessage(ChatMessage receivedChatMessage)
	{
		try
        {
		    logger.log("in received, passing to managers");
            if (!ChatManager.getInstance().chatExists(receivedChatMessage.getChat().getId())) {
                ChatManager.getInstance().addChat(receivedChatMessage.getChat().getId());
            }
            if (!UserManager.getInstance().userExists(receivedChatMessage.getSenderId())) {
                UserManager.getInstance().addUser(receivedChatMessage.getSenderId());
            }

            logger.log("check fo command");            
            //CheckContactAuthorized(receivedChatMessage.getSender());
			if (receivedChatMessage.getContent().startsWith("!"))
			{
                logger.log("call process command");			    
				processCommand(new Command(receivedChatMessage));
                logger.log("finished call process command");				
			} else if (Tag.isTagString(receivedChatMessage.getContent())) {
                logger.log("call process tag");			    
                processTag(receivedChatMessage);
                logger.log("finished call process tag");                
            } else {
                logger.log("call process message");                
				processMessage(receivedChatMessage);
                logger.log("finished call process message");				
			}

            logger.log("passing to stats");			
            StatsManager.getInstance().processMessage(receivedChatMessage);
            logger.log("finished passing to stats");            

        }catch (SkypeException ex)
		{
			ex.printStackTrace();
		}catch (Exception ex)
		{
            ex.printStackTrace();
 		}
	}

    private void processTag(ChatMessage receivedChatMessage) throws SkypeException {
        String tagContent = Tag.getTagContent(receivedChatMessage.getContent());
        IChat chat = ChatManager.getInstance().get(receivedChatMessage.getChat().getId());
        if (tagContent!=null && tagContent.length() > 0) {
            try {
                chat.addTag(tagContent, receivedChatMessage.getSenderId());
                if (tagContent.length() < 140) {
                    Skype.getProfile().setMoodMessage(tagContent+Tag.TAG_SUFFIX);
                } else { 
                    Skype.getProfile().setMoodMessage(tagContent.substring(0, 138)+"…"+Tag.TAG_SUFFIX);
                }
                receivedChatMessage.getChat().send(chat.getTagsString());
            } catch (IllegalAccessException ex) {
                receivedChatMessage.getChat().send(ex.getMessage());
            }
        } else {
            receivedChatMessage.getChat().send(chat.getTagsString());
        }
    }




   

  public Iterable<QuotesGenerator> getQuoteGenerators()
	{
		return myQGens;
	}
	
	public Iterable<QuotesGenerator> getPrivates()
	{
		return myPrivates;
	}
	
	private void processMessage(final ChatMessage receivedChatMessage) throws SkypeException
	{
	    logger.log("before time");
		if (System.currentTimeMillis() - receivedChatMessage.getTime().getTime() > 60000) {
			return;
		}
        logger.log("after time");
    IChat ch = ChatManager.getInstance().get(receivedChatMessage.getChat().getId());
    ChatMessage.Type type = receivedChatMessage.getType();
    ChatMessage.LeaveReason lr = receivedChatMessage.getLeaveReason();
    logger.log("after collected");    

    if (type == ChatMessage.Type.LEFT
            && lr == ChatMessage.LeaveReason.UNSUBSCRIBE) {
        logger.log("unsubsribe");        
        StringBuffer b = new StringBuffer()
                                .append("User ")
                                .append(receivedChatMessage.getSenderId())
                                .append(" left the chat ")
                                .append(receivedChatMessage.getChat().getId());
        Logger.getInstance().log(b.toString());
        IChat chat = ChatManager.getInstance().get(receivedChatMessage.getChat().getId());
        if (chat!=null) {
            logger.log("chat not null");            
            final String sender = receivedChatMessage.getSenderId();
            final String chatId = receivedChatMessage.getChat().getId();
            final int i = chat.userExit(sender);
            Logger.getInstance().log("User exit. Chat="+receivedChatMessage.getChat().getId()+"; user="+sender+"; debt="+i);

            if (i>0 && receivedChatMessage.getChat().getAllMembers().length>1) {
                logger.log("many members");                
                receivedChatMessage.getChat().addUser(User.getInstance(sender));
                receivedChatMessage.getChat().send("Выход из чата платный.");
                receivedChatMessage.getChat().send("Задолженность пользователя " + sender + " составляет уже Є" + i + ".");
            } else {
                Logger.getInstance().log("Exit free chat.");
            }
        }
    }
		else /*if (ch.isResponseEnabled()
				|| receivedChatMessage.getChat().getAllMembers().length<=2)*/
		{
            logger.log("no leave");		    
            Thread t=null;
            //ChatMessage.Type type = receivedChatMessage.getType();
            if (type == ChatMessage.Type.SAID || type == ChatMessage.Type.EMOTED ) {
                logger.log("said/emoted");                
                t = new Thread(new MessageResponseThread(receivedChatMessage, ch));
            } else if (type== ChatMessage.Type.ADDEDMEMBERS) {
                logger.log("add");                
                StringBuffer b = new StringBuffer("Users added to chat "+receivedChatMessage.getChat().getId()+": ");
                for (User u: receivedChatMessage.getAllUsers()) {
                    b.append(u.getId()).append(";");
                }
                Logger.getInstance().log(b.toString());
//            } else if (type== ChatMessage.Type.JOINEDASAPPLICANT) {
//                StringBuffer b = new StringBuffer()
//                                        .append("User ")
//                                        .append(receivedChatMessage.getSenderId())
//                                        .append(" joined to chat ")
//                                        .append(receivedChatMessage.getChat().getId())
//                                        .append(" as applicant. ");
//                Logger.getInstance().log(b.toString());

            } else if (type== ChatMessage.Type.SETTOPIC) {
                logger.log("topic");                
                StringBuffer b = new StringBuffer()
                                        .append("User ")
                                        .append(receivedChatMessage.getSenderId())
                                        .append(" set topic of chat ")
                                        .append(receivedChatMessage.getChat().getId())
                                        .append(" to \"")
                                        .append(receivedChatMessage.getContent())
                                        .append("\"");
                Logger.getInstance().log(b.toString());
            } else {
                logger.log("else");                
                StringBuffer b = new StringBuffer()
                                        .append("User ")
                                        .append(receivedChatMessage.getSenderId())
                                        .append(" has sent message \"")
                                        .append(receivedChatMessage.getContent())
                                        .append("\" to chat")
                                        .append(receivedChatMessage.getChat().getId())
                                        .append(". Type: ")
                                        .append(type.toString())
                                        .append(".");
                Logger.getInstance().log(b.toString());

            }
            if (t!=null) {
                logger.log("start t");                
                t.start();
                logger.log("success");                
            }
        }
	}

	private void disableChat(Chat chat) throws SkypeException, SQLException {
        IChat ch = ChatManager.getInstance().get(chat.getId());
		if (!ch.isResponseEnabled())
		{
			String reply = (String)myMessages.get(Constants.MSG_ALREADY_DISABLED);
			if (reply!=null)
			{
				chat.send(reply);	
			}
		}else
		{
            ChatManager.getInstance().setChatEnabledState(ch.getSkypeId(), false);
			String reply = (String)myMessages.get(Constants.MSG_DISABLED);
			if (reply!=null)
			{
				chat.send(reply);	
			}
		}
	}
	
	private void enableChat(Chat chat) throws SkypeException, SQLException {
        IChat ch = ChatManager.getInstance().get(chat.getId());
		if (ch.isResponseEnabled())
		{
			String reply = (String)myMessages.get(Constants.MSG_ALREADY_ENABLED);
			if (reply!=null)
			{
				chat.send(reply);	
			}
		}else
		{
            ChatManager.getInstance().setChatEnabledState(ch.getSkypeId(), true);
			String reply = (String)myMessages.get(Constants.MSG_ENABLED);
			if (reply!=null)
			{
				chat.send(reply);
			}

		}
	}
	
	private void processCommand(Command command) throws Exception
	{
        Logger.getInstance().log("Command recieved: \""+command.getChatMessage().getContent() + "\" from user "+command.getChatMessage().getSenderId() +" in chat "+command.getChatMessage().getChat().getId());
        if (isMaster(command.getChatMessage().getSender().getId())) {
            if (command.isTypeOf(Command.CMD_ADMINREMOVE)) {
                if (command.getArgs().size()>0) {
                    String id =command.getArgs().get(0);
                    if (isAdmin(id)) {
                        removeAdmin(id);
                        command.getChatMessage().getChat().send("Admin removed.");
                    } else {
                        command.getChatMessage().getChat().send(id+" is not in admins list.");
                    }
                    return;
                }
            }
        }  //if isMaster
        
        if (isAdmin(command.getChatMessage().getSender().getId())) {
			if (command.isTypeOf(Command.CMD_RELOADMESSAGES))
			{
				loadMessages();
				String reply = (String)myMessages.get(Constants.MSG_MESSAGES_RELOADED);
				if (reply!=null)
				{
					command.getChatMessage().getChat().send(reply);	
				}
				return;
			}
            else if (command.isTypeOf(Command.CMD_TOPIC))
            {
                String topic = command.getArgsString();
                command.reply("/topic "+topic);
                return;
            }
			else if (command.isTypeOf(Command.CMD_FOOD))
			{
				food = !food;
				if (food)
				{
					command.getChatMessage().getChat().send("Food reminder is on.");
				}else
				{
					command.getChatMessage().getChat().send("Food reminder is off.");
				}
				return;
			}
			else if (command.isTypeOf(Command.CMD_ADMINADD))
			{
				if (command.getArgs().size()>0)
				{
					String id =command.getArgs().get(0);  
					if (!isAdmin(id))
					{
						addAdmin(id);
						command.getChatMessage().getChat().send("Admin added.");
					}
					else
					{
						command.getChatMessage().getChat().send(id+" is already in admins list.");
					}
				}else
				{
					command.getChatMessage().getChat().send("Invalid command syntax.");
				}
				return;
			}else if (command.isTypeOf(Command.CMD_ADMINLIST))
			{
				String msg = "Robot admins: \n";
				for (IUser admin : UserManager.getInstance().getAdmins())
				{
					msg+=admin.getSkypeId()+"\n";
				}
				command.getChatMessage().getChat().send(msg);
				return;
			}else if (command.isTypeOf(Command.CMD_RELAOD_QUOTES))
			{
				myQGens = QuotesLoader.getGeneratorsList("data/Phrases/");
				myPrivates = QuotesLoader.getGeneratorsList("data/Privates/");
				String reply = (String)myMessages.get(Constants.MSG_QUOTES_RELOADED);
				if (reply!=null)
				{
					command.getChatMessage().getChat().send(reply);	
				}
				return;
			}else if (command.isTypeOf(Command.CMD_QUOTES))
			{
				String reply="Quote generators:\n";
				int i=1;
				for (QuotesGenerator gen : myQGens)
				{
					reply+=((i++)+". "+gen.getInfo()+"\n");
				}
				command.getChatMessage().getChat().send(reply);
				return;
			}else if (command.isTypeOf(Command.CMD_ENABLE))
			{
				enableChat(command.getChatMessage().getChat());
				return;
			}
			else if (command.isTypeOf(Command.CMD_DISABLE))
			{
				disableChat(command.getChatMessage().getChat());
				return;
			}
			else if (command.isTypeOf(Command.CMD_SETLISTENER))
			{
				setRole(command, "LISTENER");
				return;
			}
			else if (command.isTypeOf(Command.CMD_MODER))
			{
				setRole(command, "MASTER");
				return;
			}
			else if (command.isTypeOf(Command.CMD_USER))
			{
				setRole(command, "USER");
				return;
			}
			else if (command.isTypeOf(Command.CMD_KICK))
			{
				skypeCommand(command, "/kick {name}");
				return;
			}
			else if (command.isTypeOf(Command.CMD_KICKBAN))
			{
				skypeCommand(command, "/kickban {name}");
				return;
			}
			else if (command.isTypeOf(Command.CMD_ADD))
			{
				skypeCommand(command, "/add {name}");
				return;
			}
            else if (command.isTypeOf(Command.CMD_UPD)) {
                command.reply(DBManager.getInstance().update(command.getArgsString()));
                return;
            }
			else if (command.isTypeOf(Command.CMD_ONNN_START)) {
				//addONNN(command.getChatMessage().getChat());
				return;
				
			} else if (command.isTypeOf(Command.CMD_EXITCOST)) {
                setExitCost(command);
                return;
            }
		}//if isAdmin

		if (command.isTypeOf(Command.CMD_CURRENCY))
		{
			currency(command);
			return;
			
		}else if (command.isTypeOf(Command.CMD_WEATHER))
		{
			weather(command);
			return;
		} 
		else if (command.isTypeOf(Command.CMD_HELP))
		{
			command.getChatMessage().getChat().send(Command.getHelp());
			return;
		} 
		else if (command.isTypeOf(Command.CMD_ONNN)) 
		{
			processONNN(command);
			return;
		}
		else if (command.isTypeOf(Command.CMD_UNONNN)) 
		{
			processUnONNN(command);
			return;
		}
        else if (command.isTypeOf(Command.CMD_RSS))
        {
            processRSS(command);
            return;
        }
        else if (command.isTypeOf(Command.CMD_CHAT_STATS)) {
            command.reply(StatsManager.getInstance().getChatStatsInfo(command.getChatMessage().getChat().getId(),
                    (command.getArgs().size() > 0) ? command.getArgs().get(0) : null));
            return;
        }
        else if (command.isTypeOf(Command.CMD_USR_STATS)) {
            if (command.getArgs().size()==0) {
                command.reply(StatsManager.getInstance().getUserStatsInfo(command.getChatMessage().getChat().getId(),
                        command.getChatMessage().getSenderId()));
            } else {
                command.reply(StatsManager.getInstance().getUserStatsInfo(command.getChatMessage().getChat().getId(),
                        command.getArgs().get(0)));
            }
            return;
        }

        String reply = (String)myMessages.get(Constants.MSG_UNKNOWN_CMD);
		if (reply!=null)
		{
			command.getChatMessage().getChat().send(reply);	
		}
	}

    private void processRSS(Command command) throws Exception {
        IChat ch = ChatManager.getInstance().get(command.getChatMessage().getChat().getId());
        if (command.getArgs().size()>0) {
            ch.addFeed(command.getArgs().get(0), command.getChatMessage().getSenderId());
            command.reply("Чат подписан на "+command.getArgs().get(0));
        }
    }

    private void setExitCost(Command command) throws SkypeException, SQLException {
        if (command.getArgs().size()>0) {
            try {
                int i = Integer.parseInt(command.getArgs().get(0));
                boolean success = ChatManager.getInstance().setChatExitCost(command.getChatMessage().getChat().getId(), i);
                if (success) {
                    if (i>0) {
                        command.reply("Стоимость выхода из чата: Є"+i+".");
                    } else {
                        command.reply("Выход из чата бесплатный.");
                    }
                } else {
                    command.reply("нет такого чата лол");
                }
            } catch (NumberFormatException ex) {
                command.reply("неверный синтаксис лол");
            }
        } else {
            command.reply("неверный синтаксис лол");
        }
    }

    private void processUnONNN(Chat chat, Command command)
	{
		try {
			IChat c = ChatManager.getInstance().get(chat.getId());

            if (c == null || !c.isONNNEnabled()) {
				command.reply("Onnn is disabled!");
				return;
			}
			if (command.getArgs().size()>0) {
				String target = command.getArgs().get(0);
				String voter = command.getChatMessage().getSenderId();
                String chat_id = chat.getId();
                try {
/*
					User tu = null;
					for (User user : chat.getAllMembers())	{
						if (user.getId().equals(target)) {
							tu = user;
							break;
						}
					}
*/

					int result = ONNNManager.getInstance().removeVote(voter, target, chat_id);
					if (result == ONNNManager.RESULT_REMOVED) {
						chat.send("You unvoted for user  " + target + ". His ONNN value now: "+ONNNManager.getInstance().getVotes(target, chat_id)+"." );
					}
				}catch (ONNNException ex){
                    Logger.getInstance().log("ONNN error", ex);
                    command.reply(ex.getMessage());
				}
			}
		} catch (SkypeException ex) {
			ex.printStackTrace();
		}
	}

	private void processUnONNN(Command command)
	{
		try {
			if (command.getChatMessage().getChat().getAllMembers().length==2) {
                command.reply("Secret unonnn is not implemented yet");
            }else {
				processUnONNN(command.getChatMessage().getChat(), command);
			}
		} catch (SkypeException ex){
			ex.printStackTrace();
		}
	}
	private void processONNN(Command command)
	{
		try {
			if (command.getChatMessage().getChat().getAllMembers().length==2) {
                command.reply("Secret onnn is not implemented yet");
			}else {
				processONNN(command.getChatMessage().getChat(), command);
			}
		} catch (SkypeException ex){
			ex.printStackTrace();
		}
	}
	
	private void processONNN(Chat chat, Command command)
	{
		try {
            IChat c = ChatManager.getInstance().get(chat.getId());

            if (c == null || !c.isONNNEnabled()) {
                command.reply("Onnn is disabled!");
                return;
            }
            if (command.getArgs().size()==0) {
                if (chat.getId().equals(command.getChatMessage().getChat().getId())) {
                    chat.send(ONNNManager.getInstance().getStats(chat.getId()) + "\n" 
                            + ONNNManager.getInstance().getVoterStats(
                                    command.getChatMessage().getSenderId(), 
                                    command.getChatMessage().getChat().getId()));
                } else {
                    command.reply(ONNNManager.getInstance().getPrivateStats(command.getChatMessage().getSenderId(), chat.getId()));
                }
            } else {
                String target = command.getArgs().get(0);
                String voter = command.getChatMessage().getSenderId();
                String chat_id = chat.getId();
                logger.log("onnn-vote by " + voter + " against " + target);
                if (target.equals(myName))
                    target = voter;
                    logger.log("he votes against me!");
                try {
                    User tu = null;
                    User vo = null;
                    for (User user : chat.getAllMembers())	{
                        if (user.getId().equals(target)) {
                            tu = user;
                        }
                        if (user.getId().equals(voter)) {
                            vo = user;
                        }
                    }
                    if (vo==null) {
                        return;
                    }
                    if (tu==null) {
                        command.reply("No such user:: "+target+", in chat ["+chat.getId()+"]!");
                        return;
                    }
                    int result = ONNNManager.getInstance().addVote(voter, target, chat_id);
                    if (result != ONNNManager.RESULT_ERROR) {
                        chat.send("User " + target + " has "+ONNNManager.getInstance().getVotes(target, chat_id) + " ONNN votes now." );
                    }

                    if (result == ONNNManager.RESULT_READONLY) {
                        chat.send("User " + target + " is in readonly mode now." );
                        chat.send("/setrole "+target+" listener");
                    } else if (result == ONNNManager.RESULT_KICK) {
                        chat.send("User, " + target + " will be kicked from chat." );
                        chat.send("/setrole "+target+" listener");
                        chat.send("/kickban " + target);
                        //ONNNManagerremoveEntriesWithUser(target);
                    }
                }catch (SkypeException ex){
                    //command.reply(ex.getMessage());
                }catch (ONNNException ex){
                    Logger.getInstance().log("ONNN error", ex);
                    command.reply(ex.getMessage());
                }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}


	private void setRole(Command command, String rolename)
	{
		skypeCommand(command, "/setrole {name} "+rolename);
	}

	private void skypeCommand(Command command, String skypeCommand)
	{
		try
		{
			if (command.getArgs().size()<0)
			{
				command.getChatMessage().getChat().send("Specify username.");
			}else
			{
				String userName = command.getArgs().get(0);
				String s = skypeCommand.replace("{name}", userName);
				command.getChatMessage().getChat().send(s);
			}
			
		}catch (SkypeException ex)
		{
			/*try
			{
				command.re;
			}catch (SkypeException ex2){}*/
		}
	}

	private void weather(Command command) throws SkypeException
	{
		if (command.getArgsString().length()==0)
		{
			command.getChatMessage().getChat().send("Error"/*CurrencyConverter.getHelp()*/);
		}else
		{
			WeatherReporter c = new WeatherReporter(command);
/*			String s = c.getArgsError();
			if (s!=null)
			{
				command.getChatMessage().getChat().send(s);
			}else
			{*/
				Thread t = new Thread(c);
				t.run();
			//}
		}
		
	}

	private void currency(Command command) throws SkypeException
	{
		if (command.getArgs().size()==0)
		{
			command.getChatMessage().getChat().send(CurrencyConverter.getHelp());
		}else
		{
			CurrencyConverter c = new CurrencyConverter(command);
			String s = c.getArgsError();
			if (s!=null)
			{
				command.getChatMessage().getChat().send(s);
			}else
			{
				Thread t = new Thread(c);
				t.run();
			}
		}
	}

	private void addAdmin(String user) throws IOException, SQLException {
		if (!myMaster.equals(user)) {
           UserManager.getInstance().setRole(user, IUser.ROLE_ADMIN);
        } 	
	}

    private void removeAdmin(String user) throws IOException, SQLException {
           UserManager.getInstance().setRole(user, IUser.ROLE_USER);
    }


	public String getMessage(String key)
	{
		return (String)myMessages.get(key);
	}
	
	public void processSentMessage(ChatMessage receivedChatMessage)
	{
		try
		{
			if (receivedChatMessage.getContent().startsWith("!"))
			{
				processCommand(new Command(receivedChatMessage));
			}
            StatsManager.getInstance().processMessage(receivedChatMessage);
            
        }
		catch (SkypeException ex)
		{
			ex.printStackTrace();
		}catch (Exception ex)
		{
			try
			{
				receivedChatMessage.getChat().send(ex.getMessage());
                ex.printStackTrace();
            }
			catch(SkypeException ex2) { }
		}
	}
}
