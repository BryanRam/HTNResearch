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

public class m_FirstHit_CROUCH_B extends Method 
{
	public m_FirstHit_CROUCH_B()
	{
		super();
		
		this.name = "m_FirstHit_CROUCH_B";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.CROUCH_B));

		this.tasksToDecompose.add(new p_CROUCH_B());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//&& prev hit starts a combo? 
		if(currentSimCharacters.m_a.getHitNumber() != 0)
		{
			return false;
		}
		
		return true;
	}
}
