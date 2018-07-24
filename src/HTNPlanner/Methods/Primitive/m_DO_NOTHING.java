/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Primitive;

import java.util.Vector;

import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTasks.*;

public class m_DO_NOTHING extends Method 
{
	public m_DO_NOTHING()
	{
		super();
		
		this.name = "m_DO_NOTHING";
		this.tasksToDecompose.add(new p_DO_NOTHING());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		

		
		return holds;
	}

}
