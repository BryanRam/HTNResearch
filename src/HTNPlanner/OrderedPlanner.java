/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

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

public class OrderedPlanner extends Planner
{
	public OrderedPlanner(boolean player, GameData gameData) 
	{
		super(player, gameData);
		
		this.goalTask = new c_Act();
		
		Planner.INSTANCE = this;
	}
	

	@Override
	public Method SelectNextMethod(CompoundTask cTask, Pair<SimCharacter, SimCharacter> currentSimCharacters, int nextIndex, int[] methodsTried) 
	{
		if(nextIndex >= cTask.methods.size())
		{
			if(Helper.DEBUG_METHOD_DECOMPOSITION)
			{
				System.out.println("Task "+ cTask.name + " could not be decomposed");
			}
			return null;
		}
			
		for(int i = nextIndex+1; i < cTask.methods.size(); ++i)
		{
			if(cTask.methods.get(i).CheckPreconditions(currentSimCharacters))
			{
				if(Helper.DEBUG_METHOD_DECOMPOSITION)
				{
					System.out.println("Method "+ cTask.methods.get(i).name + " was used to decompose task " + cTask.name);
				}
				return cTask.methods.get(i);
			}
		}

		if(Helper.DEBUG_METHOD_DECOMPOSITION)
		{
			System.out.println("Task "+ cTask.name + " could not be decomposed");
		}
		return null;
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
					if(Helper.DEBUG_UCB_STATICTICS)
					{
						System.out.println("best ind for " + cTask.name + " is " + bestIndex);
					}
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
		
		if(Helper.DEBUG_METHOD_DECOMPOSITION)
		{
			System.out.println("Method "+ cTask.methods.get(bestIndex).name + " was used to decompose task " + cTask.name);
		}
		return cTask.methods.get(bestIndex);
	}

}


