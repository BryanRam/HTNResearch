/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.OrderedPlanner;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_UseCombo extends Method
{
	public m_UseCombo()
	{
		super();
		
		this.name = "m_UseCombo";
		this.tasksToDecompose.add(new c_Combo());
		//this.tasksToDecompose.add(new c_Guard());
	}
	
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		/*
		if(Planner.INSTANCE.secondsLeft <= 10 && currentSimCharacters.m_a.getEnergy() >=150)
		{
			holds = false;
		}
		*/
		
		if(currentSimCharacters.m_b.getHp() < 50 || currentSimCharacters.m_a.getHp() < 50)
		{
			return false;
		}
		
		int diffH=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
				 currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		
		if(diffH > 200)
		{
			holds = false;
		}
		/*
		if(!Planner.INSTANCE.nextOppAct.isEmpty())
		{
			Action act = Planner.INSTANCE.nextOppAct.getFirst();
			System.out.println("opponent's action = " + act.name());
			try
			{
				if(AttackAction.valueOf(act.name())!= null)
				{
					System.out.println("no combo - opp is attacking!");
					holds = false;
				}
			}catch (Exception e)
			{
			}
		}*/
		return holds;
	}


}
