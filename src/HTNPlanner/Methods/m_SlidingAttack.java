/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_SlidingAttack extends Method 
{
	public m_SlidingAttack()
	{
		super();
		
		this.name = "m_SlidingAttack";

		this.tasksToDecompose.add(new c_SlidingAttack());		

	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		int distH = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if( distH >250 ) 
		{
			return false;
		}
		
		return true;
	}
}
