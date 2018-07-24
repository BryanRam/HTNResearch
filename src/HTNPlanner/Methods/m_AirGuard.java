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
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTasks.*;

public class m_AirGuard extends Method 
{
	public m_AirGuard()
	{
		super();
		
		this.name = "m_AirGuard";
		this.tasksToDecompose.add(new p_AIR_GUARD());
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
				if(attackType == 3)  //TODO: check for 4
				{
					holds = false;
				}
			}
		}catch (Exception e)
		{
			holds = false;
		}
			

		Deque<SimAttack> simAttacks = Planner.INSTANCE.GetSimAttack();
		for(Iterator<SimAttack> i = simAttacks.iterator() ; i.hasNext() ; )
		{
			SimAttack simAttack = i.next();
			
			//0 for p1, 1 for p2
			if(simAttack.isPlayerNumber() == Planner.INSTANCE.player)
			{
				System.out.println("my projectile is in the air");
				continue;
			}
			if(!simAttack.checkProjectile())
			{
				continue;
			}
			HitArea hitNow = simAttack.getHitAreaNow();
			//don't jump when projectile is in air/higher than standing
			if(hitNow.getBottom() < currentSimCharacters.m_a.getTop())
			{
				System.out.println("a projectile is above me, don't jump");
				holds = false;
			}
			System.out.println("a projectile!");
		}
		

		return holds;
	}
}
