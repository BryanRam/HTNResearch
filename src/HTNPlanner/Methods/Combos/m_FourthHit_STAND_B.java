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

public class m_FourthHit_STAND_B extends Method 
{
	public m_FourthHit_STAND_B()
	{
		super();
		
		this.name = "m_FourthHit_STAND_B";	
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.STAND_B));
		this.tasksToDecompose.add(new p_STAND_B());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//stand a a b - b
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 3)
		{
			holds = false;
			return holds;
		}
		
		int ind = 0;
		for(Iterator<Action> i = currentSimCharacters.m_a.GetPrevActions().iterator();i.hasNext();)
		{
			Action act = i.next();
			if(ind == 0 && act!= Action.STAND_A)
			{
				holds = false;
				break;
			}
			else if(ind == 1 && act!= Action.STAND_A)
			{
				holds = false;
				break;
			}
			else if(ind == 2 && act!= Action.STAND_B)
			{
				holds = false;
				break;
			}
			ind++;
		}
		
		return holds;
	}
}
