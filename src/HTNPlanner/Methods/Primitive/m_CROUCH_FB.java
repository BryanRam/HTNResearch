/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Primitive;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_CROUCH_FB extends Method 
{
	public m_CROUCH_FB()
	{
		super();
		
		this.name = "m_CROUCH_FB";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.CROUCH_FB));
		this.tasksToDecompose.add(new p_CROUCH_FB());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		return holds;
	}
}
