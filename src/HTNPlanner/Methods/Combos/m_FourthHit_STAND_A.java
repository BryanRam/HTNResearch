/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Combos;

import java.util.Iterator;
import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_FourthHit_STAND_A extends Method 
{
	public m_FourthHit_STAND_A()
	{
		super();
		
		this.name = "m_FourthHit_STAND_A";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_A));
		this.tasksToDecompose.add(new p_STAND_A());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//&& prev all stand a
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 3)
		{
			holds = false;
			return holds;
		}
		
		for(Iterator<Action> i = currentSimCharacters.m_a.GetPrevActions().iterator();i.hasNext();)
		{
			Action act = i.next();
			if(act!= Action.STAND_A)
			{
				holds = false;
				break;
			}
		}
		
		return holds;
	}
}
