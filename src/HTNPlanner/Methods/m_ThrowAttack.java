/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_ThrowAttack extends Method 
{
	public m_ThrowAttack()
	{
		super();
		
		this.name = "m_ThrowAttack";
		this.tasksToDecompose.add(new c_ThrowAttack());
		this.tasksToDecompose.add(new p_FOR_JUMP());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		
		//throw attacks miss in this case
		if(currentSimCharacters.m_b.getAction().equals(Action.AIR_GUARD))
		{
			holds = false;
		}

		//int diff=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getTop(), currentSimCharacters.m_a.getBottom(), 
		//		currentSimCharacters.m_b.getTop(), currentSimCharacters.m_b.getBottom());
		//we are on different heights
		/*if(diff >= 150)
		{
			System.out.println("we are on different heights, no THROW ATTACK");
			holds = false;
		}*/
		
		return holds;
	}
}
