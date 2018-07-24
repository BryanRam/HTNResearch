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

public class m_MiddleAttack extends Method 
{
	public m_MiddleAttack()
	{
		super();
		
		this.name = "m_MiddleAttack";
		this.tasksToDecompose.add(new c_MiddleAttack());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		
		//middle attacks are blocked in this case
		if(currentSimCharacters.m_b.getAction().equals(Action.STAND_GUARD)
				|| currentSimCharacters.m_b.getAction().equals(Action.AIR_GUARD))
		{
			holds = false;
		}
		
		if(currentSimCharacters.m_a.getBottom() < 550 || currentSimCharacters.m_b.getBottom() < 550)
		{
			holds = false;
		}

		if(Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight()) >= 400)
		{
			if(Helper.DEBUG_PRECONDITIONS)
			{
				if(Helper.DEBUG_PRECONDITIONS)
				{
					System.out.println("too far");
				}
			}
			holds = false;
		}
		
		return holds;
	}
}
