/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimCharacter;
import struct.Key;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_MoveInActionRange extends Method 
{
	public m_MoveInActionRange()
	{
		super();
		
		this.name = "m_MoveInActionRange";
	}
	
	public m_MoveInActionRange(Action action)
	{
		super();
		
		this.myAct = action;
		this.name = "m_MoveInActionRange";
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		this.tasksToDecompose.clear();
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		
		//throw attack
		if(motionData.elementAt(this.myAct.ordinal()).attackActive >= Helper.PROJECTILE_ACTIVE_FRAMES )
		{
			//this.tasksToDecompose.add(new p_BACK_STEP());
			return true;
		}
				
		int move = 0; 
		try
		{
			AttackAction.valueOf(this.myAct.name());
			move = Helper.MovementNeededForAction(motionData.elementAt(this.myAct.ordinal()), currentSimCharacters.m_a, currentSimCharacters.m_b);
		}
		catch (Exception e)
		{
			 
		}
		
		//not close enough
		if(move > 0)
		{
			this.tasksToDecompose.add(new p_FORWARD_WALK());
		}//too close
		else if( move < 0)
		{
			//this.tasksToDecompose.add(new p_BACK_STEP());
		}

		return true;
	}
}
