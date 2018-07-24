/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Combos;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_FourthHit_STAND_FA extends Method 
{
	public m_FourthHit_STAND_FA()
	{
		super();
		
		this.name = "m_FourthHit_STAND_FA";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_FA));

		this.tasksToDecompose.add(new p_STAND_FA());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 3)
		{
			holds = false;
			return holds;
		}
		
		if(currentSimCharacters.m_a.GetPrevActions().getFirst() != Action.STAND_A
				&& currentSimCharacters.m_a.GetPrevActions().getFirst() != Action.STAND_B)
		{
			holds = false;
			return holds;
		}
			
		//stand b b fa - fa
		if(currentSimCharacters.m_a.GetPrevActions().getFirst() == Action.STAND_B
				&& currentSimCharacters.m_a.GetPrevActions().getLast() != Action.STAND_FA)
		{
			holds = false;
			return holds;
		}
		
		//stand a a a - fa
		//stand a b a - fa
		//stand a b fa - fa		
		if(currentSimCharacters.m_a.GetPrevActions().getFirst() == Action.STAND_A
				&& !(currentSimCharacters.m_a.GetPrevActions().getLast() == Action.STAND_A
				||currentSimCharacters.m_a.GetPrevActions().getLast() == Action.STAND_FA))
		{
			holds = false;
			return holds;
		}
				
		return holds;
	}
}
