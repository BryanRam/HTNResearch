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

public class m_ThirdHit_STAND_FA extends Method 
{
	public m_ThirdHit_STAND_FA()
	{
		super();
		
		this.name = "m_ThirdHit_STAND_FA";
		
		//this.tasksToDecompose.add(new p_CROUCH_GUARD());
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_FA));
		this.tasksToDecompose.add(new p_STAND_FA());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//TODO: second was STAND_B 
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 2)
		{
			holds = false;
			return holds;
		}
		
		if(currentSimCharacters.m_a.GetPrevActions().getLast() != Action.STAND_B)
		{
			holds = false;
		}
		
		return holds;
	}
}
