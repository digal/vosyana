/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 12:39:30
 */
package ru.dirty.skypebot.managers;

import ru.dirty.skypebot.ONNNException;
import ru.dirty.skypebot.domain.IONNNEntry;
import ru.dirty.skypebot.domain.IONNNStatElement;
import ru.dirty.skypebot.domain.IUser;
import ru.dirty.skypebot.logging.Logger;
import ru.dirty.skypebot.storage.DBManager;

import java.sql.SQLException;
import java.util.List;

public class ONNNManager {
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_VOTED = 0;
    public static final int RESULT_REMOVED = 1;
    public static final int RESULT_READONLY = 2;
    public static final int RESULT_KICK = 3;

    //TODO: use params from config
    private int myAllowedTargets=5;
    private int myReadOnlyTreshold=7;
    private int myKickTreshold=15;
    
    private static ONNNManager ourInstance = new ONNNManager();

    public static ONNNManager getInstance() {
        return ourInstance;
    }

    private ONNNManager() {

    }

    public int addVote(String voter, String target, String chat_id) throws ONNNException {
        try {
            IONNNEntry check = DBManager.getInstance().getONNN(voter, target, chat_id);
            if (check!=null) {
                throw new ONNNException(voter, target, chat_id, "Вы уже проголосовали за пользователя "+target+"!");
            }

            IUser uvoter = UserManager.getInstance().get(voter);
            IUser utarget = UserManager.getInstance().get(target);
            if (uvoter == null) {
                throw new ONNNException(voter, target, chat_id, "Не знаю такого пользователя: "+voter+"!");
            }
            if (utarget == null) {
                throw new ONNNException(voter, target, chat_id, "Не знаю такого пользователя: "+target+"!");
            }

            int v = DBManager.getInstance().getVoterVotesCount(voter, chat_id);

            if (v>=myAllowedTargets) {
                throw new ONNNException(voter, target, chat_id, "Пользователь "+voter+" не может больше голосовать!");
            }

            IONNNEntry inserted = DBManager.getInstance().addONNNEntry(voter, target, chat_id);
            if (inserted != null) {
                int votes = DBManager.getInstance().getTargetVotesCount(target, chat_id);
                if (votes == myReadOnlyTreshold) {
                    return RESULT_READONLY;
                } else if (votes==myKickTreshold) {
                    return RESULT_KICK;
                } else {
                    return RESULT_VOTED;
                }
            } else {
                Logger.getInstance().log("ONNN Entry was not added");
                throw new ONNNException(voter, target, chat_id, "ONNN Entry was not added");
            }
        } catch (SQLException ex) {
            Logger.getInstance().log("ONNN DB ERROR", ex);
            ex.printStackTrace();
            throw new ONNNException(voter, target, chat_id, "ONNN DB ERROR");
        }
    }

    public int removeVote(String voter, String target, String chat_id) throws ONNNException {
        try {
            if (DBManager.getInstance().removeONNNEntry(voter, target, chat_id)) {
                return RESULT_REMOVED;
            } else {
                Logger.getInstance().log("ONNN Entry was not removed");
                throw new ONNNException(voter, target, chat_id, "ONNN Entry was not removed");
            }
        } catch (SQLException ex) {
            Logger.getInstance().log("ONNN Entry was not removed. Exception: ", ex);
            ex.printStackTrace();
            throw new ONNNException(voter, target, chat_id, "ONNN Entry was not removed due to exception.");
        }
    }

    public String getPrivateStats(String userId, String chat_id) {
        return "Not implemented yet";
    }
    
    public String getVoterStats(String userId, String chatId) throws ONNNException {        
        try {
            List<String> targets = DBManager.getInstance().getTargets(userId, chatId);
            List<String> voters = DBManager.getInstance().getVoters(userId, chatId);
            
            String s = "You ("+userId+") voted against: ";
            
            if (targets.size() == 0) {
                s += " -> [none]";
            } else {            
                for (String target: targets) {
                    s+= target + ", ";
                }
            }
            
            s += "\nThese people voted against you ("+userId+"): ";
            
            if (voters.size() == 0) {
                s += " -> [none]";
            } else {
                for (String voter: voters) {
                    s+= voter + ", ";
                }
            }
            
            return s;
            
        } catch (SQLException ex) {
            Logger.getInstance().log("ONNN DB ERROR", ex);
            ex.printStackTrace();
            throw new ONNNException(null, null, chatId, "ONNN DB ERROR");
        }
        
    }

	public String getStats(String chatId) throws ONNNException {
        try {
            String s = "Skype ONNN v0.9 alpha";
            s += "\nReadonly treshold: "+myReadOnlyTreshold;
            s += "\nKick treshold: "+myKickTreshold;
            s += "\nAllowed votes: "+myAllowedTargets;
            s += "\n-------------------------";

            List<IONNNStatElement> els = DBManager.getInstance().getONNNStats(chatId);

            for (IONNNStatElement el : els)
            {
                s += "\n";
                int votes = el.getVotes();
                if (votes>=myKickTreshold) {
                    s+="[KICK] ";
                } else if (votes>=myReadOnlyTreshold) {
                    s+="[R/O] ";
                }
                s += el.getTarget()+": ";
                s += votes;
            }
            s += "\n-------------------------";
            return s;
        } catch (SQLException ex) {
            Logger.getInstance().log("ONNN DB ERROR", ex);
            ex.printStackTrace();
            throw new ONNNException(null, null, chatId, "ONNN DB ERROR");
        }
    }

    public int getVotes(String target, String chat) throws ONNNException {
        try {
            return DBManager.getInstance().getTargetVotesCount(target, chat);
        } catch (SQLException ex) {
            Logger.getInstance().log("ONNN DB ERROR", ex);
            ex.printStackTrace();
            throw new ONNNException(null, null, chat, "ONNN DB ERROR");
        }
    }

}
