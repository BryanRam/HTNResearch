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
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Jump extends Method 
{
	public m_Jump()
	{
		super();
		
		this.name = "m_Jump";
		this.tasksToDecompose.add(new p_JUMP());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		if(currentSimCharacters.m_b.getTop() <= 430)
		{
			holds = false;
		}
		
		if(!Planner.INSTANCE.nextOppAct.isEmpty())
		{
			try
			{
				Action act = Planner.INSTANCE.nextOppAct.getFirst();
				//attacking
				if(AttackAction.valueOf(act.name())!= null)
				{
					Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
					//in air 
					if(act.name().contains("AIR"))
					{
						holds = false;
						System.out.println("opponent is attacking in air - don't jump");
					}
					//projectile in air
					if(currentSimCharacters.m_b.getAttack().checkProjectile())
					{
						holds = false;
						System.out.println("opponent is attacking in air - don't jump"); //TODO: check where the projectile is
					}
					
				}
			}catch (Exception e)
			{
			}
		}
	
		return holds;
	}
}
