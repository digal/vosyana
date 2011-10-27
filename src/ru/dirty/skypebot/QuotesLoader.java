package ru.dirty.skypebot;

import ru.dirty.skypebot.logging.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class QuotesLoader
{
	public static ArrayList<QuotesGenerator> getGeneratorsList(String path)
	{
		ArrayList<QuotesGenerator> list = new ArrayList<QuotesGenerator>();
		File f = new File(path);
		if (f.exists() && f.isDirectory())
		{
			File[] files = f.listFiles();
			for (File file : files)
			{
				if (file.getName().endsWith(".xml"))
				{
					try
					{
						QuotesGenerator qg = new QuotesGenerator(file.getPath());	
						list.add(qg);
					}catch (Exception ex)
					{
                        Logger.getInstance().log("Loading quotes from "+file.getPath()+" failed.", ex);
						ex.printStackTrace();
					}
				}
			}
		}
		QuotesGenerator[] arr = new QuotesGenerator[list.size()];
		list.toArray(arr);
		Arrays.sort(arr);
		list = new ArrayList<QuotesGenerator>(Arrays.asList(arr));
		return list;
	}
	
}
