package ru.dirty.skypebot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class ListStorage
{
	public static void saveList(ArrayList<String> list, String filename) throws IOException
	{
		File f = new File(filename);
		f.createNewFile();
		PrintStream ps = new PrintStream(f);
		for (String s: list)
		{
			ps.println(s);
		}
		ps.flush();
		ps.close();
	}
	
	public static ArrayList<String> loadList(String filename)
	{
		ArrayList<String> list = new ArrayList<String>();
		File f = new File(filename);
		if (f.exists() && f.canRead())
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String s;
				while ((s = reader.readLine())!=null)
				{
					if (s!=null && s.length()>0)
					{
						list.add(s);
					}
				}
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		return list;
	}
	
	
}
