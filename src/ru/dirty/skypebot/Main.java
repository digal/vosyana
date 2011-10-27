package ru.dirty.skypebot;

import com.skype.Skype;
import com.skype.SkypeException;

public class Main
{
	public static void main(String[] args)
	{
		CommandGetter getter = (args.length > 0) ? CommandGetter.getInstance(args[0]) :
		                       CommandGetter.getInstance();
        MessageProcessor mp = new MessageProcessor();
        new Thread(mp).start();
        try {
            Skype.addChatMessageListener(mp);
        } catch (SkypeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(1);
        }
        getter.setVisible(true);
	}
}
	