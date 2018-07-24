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

public class m_CrouchGuard extends Method 
{
	public m_CrouchGuard()
	{
		super();
		
		this.name = "m_CrouchGuard";
		this.tasksToDecompose.add(new p_CROUCH_GUARD());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		Action act = Planner.INSTANCE.nextOppAct.getFirst();
		System.out.println("opponent's action = " + act.name());
		
		try
		{
			if(AttackAction.valueOf(act.name())!= null)
			{
				Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
				int attackType = motionData.elementAt(act.ordinal()).getAttackType();
				if(attackType != 1 && attackType != 3)
				{
					holds = false;
				}
			}
		}catch (Exception e)
		{
			holds = false;
		}
			

		return holds;
	}
}
