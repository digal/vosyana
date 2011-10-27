/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: May 12, 2009
 * Time: 3:14:50 AM
 */
package ru.dirty.skypebot.services;

import twitter4j.*;
import twitter4j.http.*;

import java.util.Properties;
import java.io.UnsupportedEncodingException;

import ru.dirty.skypebot.logging.Logger;

public class TwitterService extends TwitterAdapter {
    private AsyncTwitter twitter;
    private static TwitterService ourInstance = new TwitterService();

    public static TwitterService getInstance() {
        return ourInstance;
    }

    private TwitterService() {
    }

    @Override 
    public void updatedStatus(Status status) {
	  System.out.println("Successfully updated the status to [" +
                   status.getText() + "].");
	}

	@Override
	public void onException(TwitterException ex, TwitterMethod method)  {
		System.err.println("Twitter method "+method.name()+" caused an exception:");
		System.out.println("Twitter method "+method.name()+" caused an exception!");
		ex.printStackTrace();
	}

    public void init(Properties props) {
        System.out.println("twToken = "+props.getProperty("twToken"));
        System.out.println("twSecret = "+props.getProperty("twSecret"));
        System.out.println("twCKey = "+props.getProperty("twCKey"));
        System.out.println("twCSecret = "+props.getProperty("twCSecret"));


        AccessToken token = new AccessToken(
            props.getProperty("twToken"),
            props.getProperty("twSecret"));

        twitter = new AsyncTwitterFactory(this).getOAuthAuthorizedInstance(
            props.getProperty("twCKey"),
            props.getProperty("twCSecret"),
            token);
    }

    public void send(String message) {
        if (twitter!=null) {
            if (message.length() > 140) {
                message = message.substring(0, 138)+"…";
            }
            twitter.updateStatus(message);
        } else {
			System.err.println("Twitter is null!");
		}
    }
}
