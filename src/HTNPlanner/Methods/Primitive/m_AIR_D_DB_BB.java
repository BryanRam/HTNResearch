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

public class m_AIR_D_DB_BB extends Method 
{
	public m_AIR_D_DB_BB()
	{
		super();
		
		this.name = "m_AIR_D_DB_BB";
		//this.tasksToDecompose.add(new c_MoveInActionRange(Action.AIR_D_DB_BA));
		this.tasksToDecompose.add(new p_AIR_D_DB_BB());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		return holds;
	}
}
