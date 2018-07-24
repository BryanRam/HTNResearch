/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Deque;
import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import struct.HitArea;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Guard extends Method 
{
	public m_Guard()
	{
		super();
		
		this.name = "m_Guard";
		this.tasksToDecompose.add(new c_Guard());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		if(Planner.INSTANCE.nextOppAct.isEmpty())// && Planner.INSTANCE.GetSimAttack().isEmpty())
		{
			holds = false;
			System.out.println("not guarding - opp has no action");
			return holds;
		}
		
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		Action act = Planner.INSTANCE.nextOppAct.getFirst();
		System.out.println("opponent's action = " + act.name());
		try
		{
			//opponent is attacking and his attack could hit me
			if(AttackAction.valueOf(act.name())!= null)
			{
				System.out.println("guarding - opp is attacking!");
				//motionData.elementAt(act.ordinal()).getAttackType()!= 4 &&
				if(!Helper.BoxesIntersect( motionData.elementAt(act.ordinal()).getAttackHitArea(), currentSimCharacters.m_b, currentSimCharacters.m_a))
				{	
					System.out.println("not guarding - no intersection");
					holds = false;
				//return  true;
				}
			}
		}catch (Exception e)
		{
			holds = false;
		}
		
		return holds;
	}
}
