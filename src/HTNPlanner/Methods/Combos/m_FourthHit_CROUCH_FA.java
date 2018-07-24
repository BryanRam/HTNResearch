/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Combos;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_FourthHit_CROUCH_FA extends Method 
{
	public m_FourthHit_CROUCH_FA()
	{
		super();
		
		this.name = "m_FourthHit_CROUCH_FA";
		this.tasksToDecompose.add(new c_MoveInActionRange(Action.CROUCH_FA));
		this.tasksToDecompose.add(new p_CROUCH_FA());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		//if(currentSimCharacters.m_a.getHitNumber() != 1)
		if(currentSimCharacters.m_a.GetPrevActions().size() != 3)
		{
			holds = false;
			return holds;
		}
		
		//crouch b sa sa - fa
		if(currentSimCharacters.m_a.GetPrevActions().getFirst() != Action.CROUCH_A
				&& currentSimCharacters.m_a.GetPrevActions().getFirst() != Action.CROUCH_B)
		{
			holds = false;
			return holds;
		}
			
		//crouch a a a - fa
		if(currentSimCharacters.m_a.GetPrevActions().getFirst() == Action.CROUCH_A
				&& currentSimCharacters.m_a.GetPrevActions().getLast() != Action.CROUCH_A)
		{
			holds = false;
			return holds;
		}

				
		return holds;
	}
}
