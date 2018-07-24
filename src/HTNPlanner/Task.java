/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

import java.util.LinkedList;

import util.Pair;
import mizunoAI_simulator.SimCharacter;

public abstract class Task 
{
	public String name; 
	
    public Pair<SimCharacter, SimCharacter> prevSimCharacters;
    
	protected LinkedList<Task> linkedTasks;
      
    // for compound tasks this means 'decompose'; for primitive - 'simulate'
	public abstract Pair<SimCharacter,SimCharacter> Decompose(Pair<SimCharacter, SimCharacter> prevSimCharacters, LinkedList<Task> prevTasks);

	
	public abstract void IncreaseSuccessStatistics(float currentReward);
	
	public float GetBonus(){return 0;}
	
	public void AddBonus(float bonus){}
	
	protected abstract void FlushMethodRewards();
}
