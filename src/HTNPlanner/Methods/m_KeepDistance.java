/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Deque;
import java.util.Iterator;
import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.HitArea;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_KeepDistance extends Method 
{
	public m_KeepDistance()
	{
		super();
		
		this.name = "m_KeepDistance";
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = false;
		
		if(Planner.INSTANCE.freeProjectileAvailable)
		{
			int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
					currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
			
			this.tasksToDecompose.clear();
			if(dist > 400)
			{
				this.tasksToDecompose.add(new p_DASH());
			}
			else
			{
				this.tasksToDecompose.add(new p_BACK_STEP());
			}
			return true;
		}

		return holds;
	}
}
