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
import HTNPlanner.PrimitiveTask;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_ForwardJump extends Method 
{
	public m_ForwardJump()
	{
		super();
		
		this.name = "m_ForwardJump";
		this.tasksToDecompose.add(new p_FOR_JUMP());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		//avoid being in corners
		if((currentSimCharacters.m_a.getLeft()<10 && currentSimCharacters.m_b.getLeft() <70)
				|| (currentSimCharacters.m_a.getRight() > Planner.INSTANCE.GetGameData().getStageWidth()-10
						&& currentSimCharacters.m_b.getRight() > Planner.INSTANCE.GetGameData().getStageWidth()-70))
		{
			System.out.println("stuck in corner: jump!");
			return true;			
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
					if(currentSimCharacters.m_b.getTop() <= 430 || act.name().contains("AIR"))
					{
						holds = false;
						System.out.println("opponent is attacking in air - don't jump");
						return holds;
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
				//not attacking
				return true;
			}
		}
	

		return holds;
	}
}
