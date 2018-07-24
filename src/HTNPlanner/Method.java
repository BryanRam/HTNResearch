/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;


public abstract class Method 
{
	public Action myAct;
	
	public String name; 
	
	protected ArrayList<Task> tasksToDecompose;
	
	protected int prevPlanSize = 0;
	
	public abstract boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters);

	public Method()
	{
		tasksToDecompose = new ArrayList<Task>();
	}
	
	public void PrintAllUCBValuesOfMethod(PrintWriter pw)
	{
		if(this.tasksToDecompose.size()== 0)
		{
			return;
		}
		
		for(int i = 0; i < this.tasksToDecompose.size(); ++i)
		{
			if(this.tasksToDecompose.get(i).getClass().getSimpleName().contains("p_"))
			{
				continue;
			}
			((CompoundTask)this.tasksToDecompose.get(i)).PrintAllUCBValuesOfTask(pw);
		}
	}
	
	public Pair<SimCharacter, SimCharacter> DecomposeTask(Pair<SimCharacter, SimCharacter> currentSimCharacters, LinkedList<Task> prevTasks) 
	{
		Pair<SimCharacter, SimCharacter> prevSimCharacters = currentSimCharacters;
        SimCharacter mySimCharacter = prevSimCharacters.m_a.clone();
        SimCharacter oppSimCharacter = prevSimCharacters.m_b.clone(); 
        Pair<SimCharacter,SimCharacter> toChangeCharacterData = new Pair<SimCharacter,SimCharacter>(mySimCharacter, oppSimCharacter);       
        
        prevPlanSize = Planner.INSTANCE.GetCurrentPlanLength();
        
        if(this.tasksToDecompose.size() == 0)
        {
        	if(Helper.DEBUG_METHOD_DECOMPOSITION)
        	{
        		System.out.println("no tasks defined for method " + this.name);
        	}
        	return toChangeCharacterData; 
        }
        
        long currTime;
        
		for(int i = 0; i<this.tasksToDecompose.size(); ++i)
		{
			currTime = System.currentTimeMillis();
			if(currTime - Planner.INSTANCE.time >= 16.6)
			{
				if(Helper.DEBUG_TIME_OUT)
				{
					System.out.println("Method " + this.name + " : no computation time left.>>>>>>>>>>>>>");
				}
				return null;
			}
			
			toChangeCharacterData = this.tasksToDecompose.get(i).Decompose(toChangeCharacterData, (LinkedList<Task>)prevTasks.clone());
			
			// one of the tasks could not be decomposed by any methods
			if(toChangeCharacterData == null)
			{	
				currTime = System.currentTimeMillis();
				
				//if prim tasks were added by this method, remove them again
				int diff = Planner.INSTANCE.GetCurrentPlanLength() - prevPlanSize;
				if(diff != 0 && currTime - Planner.INSTANCE.time < 16.6)
				{
					for(int j = 0; j < diff; ++j)
					{
						Planner.INSTANCE.RemoveLastPlanStep();
					}
				}
					
				break;
			}			
		}

		return toChangeCharacterData;
	}
	
	public int GetAmountOfTasks()
	{
		return this.tasksToDecompose.size();
	}
}
