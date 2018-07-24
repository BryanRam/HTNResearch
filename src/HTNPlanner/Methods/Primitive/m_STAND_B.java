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

public class m_STAND_B extends Method 
{
	public m_STAND_B()
	{
		super();
		
		this.name = "m_STAND_B";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_B));
		this.tasksToDecompose.add(new p_STAND_B());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		return holds;
	}
}