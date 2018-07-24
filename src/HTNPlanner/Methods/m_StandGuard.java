/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTasks.*;

public class m_StandGuard extends Method 
{
	public m_StandGuard()
	{
		super();
		
		this.name = "m_StandGuard";
		this.tasksToDecompose.add(new p_STAND_GUARD());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
	
		Action act = Planner.INSTANCE.nextOppAct.getFirst();
		
		try
		{
			if(AttackAction.valueOf(act.name())!= null)
			{
				Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
				int attackType = motionData.elementAt(act.ordinal()).getAttackType();
				if(attackType != 1 && attackType != 2)
				{
					holds = false;
				}
			}
		}catch (Exception e)
		{
			holds = false;
		}
			

		return true;
	}
}
