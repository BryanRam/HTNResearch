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

public class m_SecondHit_CROUCH_A extends Method 
{
	public m_SecondHit_CROUCH_A()
	{
		super();
		
		this.name = "m_SecondHit_CROUCH_A";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.CROUCH_A));

		this.tasksToDecompose.add(new p_CROUCH_A());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		//TODO: first was CROUCH_A?
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 1)
		{
			holds = false;
			return holds;
		}
		
		if(currentSimCharacters.m_a.GetPrevActions().getLast() != Action.CROUCH_A)
		{
			holds = false;
		}
		
		return holds;
	}
}
