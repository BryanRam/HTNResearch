/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

import static java.nio.file.StandardOpenOption.READ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;

import mizunoAI_simulator.SimCharacter;
import mizunoAI_simulator.Simulator;
import enumerate.Action;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTasks.*;

public class UCBPlanner extends Planner
{
	public static float C = 0;
	public static float EPSILON = (float) 0.1;
	
	private String pathString;
	private String pathStringLog;
	private File logFile;
	private File file;
	
	public UCBPlanner(boolean player, GameData gameData) 
	{
		super(player, gameData);
		
		this.goalTask = new c_Act();
		
		Planner.INSTANCE = this;
		
		pathString =  "data/aiData/HTNFighter/UCB_"+ this.gameData.getCharacterName(this.player)+".txt"; 
		pathStringLog =  "data/aiData/HTNFighter/UCB_"+ this.gameData.getCharacterName(this.player)+"Log.txt";
	}
	
	public void ReadAllUCBValues()
	{

		Path path = null;
		path = Paths.get(pathString);
		
		int index = 0;
		
		try (InputStream in = Files.newInputStream(path, READ); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) 
		{
		    String line = null;
		    while ((line = reader.readLine()) != null) 
		    {
		    	if(line.isEmpty())
		    	{
		    		continue;
		    	}

		    	String[] all = line.split(";");
		    	
		    	Class c = null;
				try {
					c = Class.forName("HTNPlanner.CompoundTasks." + all[0]);
					CompoundTask t = (CompoundTask)c.newInstance();
					t.SetUCBValues(all);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					System.out.println("class not found m_" + all[0]);
					continue;
				}
		    }
		} 
		catch (IOException x) 
		{
		    System.err.println(x);
		}  

	}
	
	
	public void PrintAllUCBValues()
	{
		String outp = "";
		
		
		file = new File(pathString);
		
		try 
		{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			((CompoundTask)this.goalTask).PrintAllUCBValuesOfTask(pw);
			System.out.println(outp);
			pw.close();
			
		} catch (IOException e) {
		}
		
	}

	@Override
	public Method SelectNextMethod(CompoundTask cTask, Pair<SimCharacter, SimCharacter> currentSimCharacters, int prevIndex, int[] methodsTried) 
	{
		int bestIndex = -1;
		float maxVal = -1;
		
		float currentVal = Float.MAX_VALUE;
		if(prevIndex >= 0)
		{
			currentVal = cTask.GetExplorationValForMethod(prevIndex) + cTask.GetExploitationValForMethod(prevIndex);
		}
		
		logFile = new File(pathStringLog);
		try 
		{
		BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
		PrintWriter pw = new PrintWriter(bw);
		
			for(int i = 0; i < cTask.methods.size(); ++i)
			{
				if(cTask.methods.get(i).CheckPreconditions(currentSimCharacters))
				{
					//get UCB value
					float exploration = cTask.GetExplorationValForMethod(i);
					float exploitation = cTask.GetExploitationValForMethod(i);
					float ucb = exploration + exploitation;
					if(Helper.DEBUG_UCB_STATICTICS)
					{
						String log = "UCB value of " + cTask.methods.get(i).name + " = " + ucb;
						System.out.println(log);
						
						pw.append(log);
						bw.newLine();
							
						
						
					}
					if(ucb >= maxVal && ucb <= currentVal && methodsTried[i] != 1)
					{
						maxVal = ucb;
						bestIndex = i;		
					}
					
				}
			}
			pw.close();
		} catch (IOException e) {
		}
		

		if(bestIndex < 0)
		{
			if(Helper.DEBUG_METHOD_DECOMPOSITION)
			{
				System.out.println("Task "+ cTask.name + " could not be decomposed");
			}
			return null;
		}
		
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("best ind for " + cTask.name + " is " + bestIndex);
		}
		if(Helper.DEBUG_METHOD_DECOMPOSITION)
		{
			System.out.println("Method "+ cTask.methods.get(bestIndex).name + " was used to decompose task " + cTask.name);
		}
		return cTask.methods.get(bestIndex);
	}
	
	public Method SelectNextMethodUCB(CompoundTask cTask, Pair<SimCharacter, SimCharacter> currentSimCharacters, int prevIndex, int[] methodsTried) 
	{
		int bestIndex = -1;
		float maxVal = -1;
		
		float currentVal = Float.MAX_VALUE;
		if(prevIndex >= 0)
		{
			currentVal = cTask.GetExplorationValForMethod(prevIndex) + cTask.GetExploitationValForMethod(prevIndex);
		}
		
		for(int i = 0; i < cTask.methods.size(); ++i)
		{
			if(cTask.methods.get(i).CheckPreconditions(currentSimCharacters))
			{
				//get UCB value
				float exploration = cTask.GetExplorationValForMethod(i);
				float exploitation = cTask.GetExploitationValForMethod(i);
				float ucb = exploration + exploitation;
				if(Helper.DEBUG_UCB_STATICTICS)
				{
					System.out.println("UCB value of " + cTask.methods.get(i).name + " = " + ucb);
				}
				if(ucb >= maxVal && ucb <= currentVal && methodsTried[i] != 1)
				{
					maxVal = ucb;
					bestIndex = i;	
				}
				
			}
		}

		if(bestIndex < 0)
		{
			if(Helper.DEBUG_METHOD_DECOMPOSITION)
			{
				System.out.println("Task "+ cTask.name + " could not be decomposed");
			}
			return null;
		}
		
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("best ind for " + cTask.name + " is " + bestIndex);
		}
		
		if(Helper.DEBUG_METHOD_DECOMPOSITION)
		{
			System.out.println("Method "+ cTask.methods.get(bestIndex).name + " was used to decompose task " + cTask.name);
		}
		return cTask.methods.get(bestIndex);
	}

}


