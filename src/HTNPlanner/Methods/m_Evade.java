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
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Evade extends Method 
{
	public m_Evade()
	{
		super();
		
		this.name = "m_Evade";
	//	this.tasksToDecompose.add(new c_KnockDownAttack());
		this.tasksToDecompose.add(new p_FOR_JUMP());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		//for speedrunning league always approach opponent
		if(currentSimCharacters.m_a.getHp() > 1000)
		{
			return false;
		}
		
		boolean jumpUpAllowed = true;	
		Deque<SimAttack> simAttacks = Planner.INSTANCE.GetSimAttack();
		for(Iterator<SimAttack> i = simAttacks.iterator() ; i.hasNext() ; )
		{
			SimAttack simAttack = i.next();
			HitArea hitNow = simAttack.getHitAreaNow();
			//don't jump when projectile is in air/higher than standing
			if(hitNow.getBottom() < currentSimCharacters.m_a.getTop())
			{
				if(currentSimCharacters.m_a.getLeft() < 300)
				{
					if(simAttack.getSpeedX() < 0)
					{
						if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println(" a projectile is above me - don't jump out of corner");
						
						jumpUpAllowed = false;
						return false;
					}
				}
				else
				{
					if(simAttack.getSpeedX() > 0)
					{
						if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println(" a projectile is above me - don't jump out of corner");
						
						jumpUpAllowed = false;
						return false;
					}
					
				}
			}
		}
		
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if(dist >280 && dist <370 )
		{
			holds = false;
		}
		//avoid being in corners
		if( (currentSimCharacters.m_a.getLeft()>40 && currentSimCharacters.m_a.isFront()) || (currentSimCharacters.m_a.getRight() < Planner.INSTANCE.GetGameData().getStageWidth()-40 && !currentSimCharacters.m_a.isFront()))
		{
			holds = false;			
		}
	
		return holds;
	}
}
