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

public class m_SecondHit_STAND_B extends Method 
{
	public m_SecondHit_STAND_B()
	{
		super();
		
		this.name = "m_SecondHit_STAND_B";
		
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_B));

		this.tasksToDecompose.add(new p_STAND_B());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//TODO: first was STAND_A or STAND_B
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 1)
		{
			holds = false;
			return holds;
		}
		
		if(currentSimCharacters.m_a.GetPrevActions().getLast() != Action.STAND_A
				&& currentSimCharacters.m_a.GetPrevActions().getLast() != Action.STAND_B)
		{
			holds = false;
		}
		
		return holds;
	}
}
