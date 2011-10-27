/**
 * Created by IntelliJ IDEA.
 * User: Digal
 * Date: 26.09.2008
 * Time: 11:37:31
 * To change this template use File | Settings | File Templates.
 */
package ru.dirty.skypebot.stats;

import ru.dirty.skypebot.domain.StatsEntry;
import ru.dirty.skypebot.logging.Logger;
import ru.dirty.skypebot.storage.DBManager;

import java.util.*;
import java.sql.SQLException;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class StatsManager {
    private static StatsManager ourInstance = new StatsManager();
    
    private static final Logger logger = Logger.getInstance();

    public static StatsManager getInstance() {
        return ourInstance;
    }

    //<ChatName, HashMap<UserName, StatsEntry>>
    private HashMap<String, HashMap<String, StatsEntry>> entries;

    private StatsManager() {
        entries = new HashMap<String, HashMap<String, StatsEntry>>();
        loadData();
    }

    private void loadData() {
        try {
            List<StatsEntry> list = DBManager.getInstance().getAllStats();
            for (StatsEntry se : list) {
                HashMap<String, StatsEntry> chatStats = entries.get(se.getChatID());
                if (chatStats == null) {
                    chatStats = new HashMap<String, StatsEntry>();
                    entries.put(se.getChatID(), chatStats);
                }
                chatStats.put(se.getUserID(), se);
            }
        } catch (Throwable ex) {
            throw new RuntimeException("Cannot initialize StatsManager", ex);
        }
    }

    public void processMessage(ChatMessage cm) throws SkypeException, SQLException {
        logger.log("in chat message");
        if (cm.getChat().getAllMembers().length<=2) {
            //do not log privates
            logger.log("only two members");
            return;
        }
        logger.log("passed");        
        String chat = cm.getChat().getId();
        String usr = cm.getSenderId();
        HashMap<String, StatsEntry> chatStats = entries.get(chat);
        logger.log("collected stats");        
        synchronized (entries) {
            logger.log("entries");
            if (chatStats==null) {
                chatStats = new HashMap<String, StatsEntry>();
                entries.put(chat, chatStats);
            }
        }
        logger.log("statsentry");        
        StatsEntry se = chatStats.get(usr);
        synchronized (chatStats) {
            logger.log("chatstats");            
            if (se==null) {
                se = new StatsEntry(chat, usr);
                chatStats.put(usr, se);
            }
        }
        logger.log("exit synchro");        
        if (ChatMessage.Type.SETTOPIC.equals(cm.getType())) {
            se.countTopicChange();
        } else if (ChatMessage.Type.EMOTED.equals(cm.getType())
                    || ChatMessage.Type.SAID.equals(cm.getType())){
            se.apply(cm.getContent());
        }

        logger.log("count changes done");        
        DBManager.getInstance().insertOrUpdateStats(se);
        logger.log("db request done");                
    }

    public String getChatStatsInfo(String chat, String field) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, StatsEntry> chatStats = entries.get(chat);
        if(chatStats==null || chatStats.isEmpty()) {
            sb.append("Для этого чата нет статистики.");
        } else {
            ArrayList<StatsEntry> list = new ArrayList<StatsEntry>(chatStats.values());
            if (field == null) {
                Collections.sort(list, new Comparator<StatsEntry>(){
                    public int compare(StatsEntry o1, StatsEntry o2) {
                        return new Long(o2.getSymbols()).compareTo(o1.getSymbols());
                    }
                });
            } else if (field.equals("msg")) {
                Collections.sort(list, new Comparator<StatsEntry>(){
                    public int compare(StatsEntry o1, StatsEntry o2) {
                        return new Long(o2.getMessages()).compareTo(o1.getMessages());
                    }
                });                
            } else if (field.equals("wrd")) {
                Collections.sort(list, new Comparator<StatsEntry>(){
                    public int compare(StatsEntry o1, StatsEntry o2) {
                        return new Long(o2.getWords()).compareTo(o1.getWords());
                    }
                });  
            };
            sb.append("----\n   \tUser\t| Symbols\t| Words\t| Messages\t| Topics: \n");
            int pos = 0;
            for (StatsEntry se : list) {
                if (se.getSymbols()>0) {
                    sb
                            .append(pos + ".\t")
                            .append(se.getUserID())
                            .append("\t| ")
                            .append(se.getSymbols())
                            .append("\t| ")
                            .append(se.getWords())                            
                            .append("\t| ")
                            .append(se.getMessages())
                            .append("\t| ")
                            .append(se.getTopicChanges())
                            .append('\n');
                }
            }
        }
        return sb.toString();
    }

    public String getUserStatsInfo(String chat, String usr) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, StatsEntry> chatStats = entries.get(chat);
        if(chatStats==null || chatStats.isEmpty()) {
            sb.append("Для этого чата нет статистики.");
        } else {
            StatsEntry se = chatStats.get(usr);
            if (se==null) {
                sb.append("Для пользователя ").append(usr).append(" в этом чате нет статистики.");
            } else {
                sb.append("User :").append(usr).append('\n');
                sb.append("messages :").append(se.getMessages()).append('\n');
                sb.append("words :").append(se.getWords()).append('\n');
                sb.append("symbols :").append(se.getSymbols()).append('\n');
                sb.append("commands :").append(se.getCommands()).append('\n');
                sb.append("topic changes :").append(se.getTopicChanges()).append('\n');
                sb.append("stats since :").append(se.getStarted()).append('\n');
            }
        }
        return sb.toString();
    }
}
