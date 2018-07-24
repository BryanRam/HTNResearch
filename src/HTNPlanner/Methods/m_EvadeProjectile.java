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
//import fighting.Attack;
import aiinterface.AIInterface;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.HitArea;
import struct.MotionData;
import struct.AttackData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_EvadeProjectile extends Method 
{
	public m_EvadeProjectile()
	{
		super();
		
		this.name = "m_EvadeProjectile";
		//this.tasksToDecompose.add(new p_FOR_JUMP());  //done in preconditions checking
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		
		this.tasksToDecompose.clear();
		
		
		boolean holds = false;
		
		boolean jumpUpAllowed = true;
		boolean close = false;

		Deque<SimAttack> simAttacks = Planner.INSTANCE.GetSimAttack();
		int j = 0;
		for(Iterator<SimAttack> i = simAttacks.iterator() ; i.hasNext() ; )
		{
			SimAttack simAttack = i.next();
			
 			//System.out.println("method: attack " + j + " is from player: " + (simAttack.isPlayerNumber() == Planner.INSTANCE.player ? " yes" : "no"));
 			j++;
			
			//0 for p1, 1 for p2										
			if(simAttack.isPlayerNumber() == Planner.INSTANCE.player)
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j + " my projectile is in the air");
				continue;
			}
			if(!simAttack.checkProjectile())
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println("no projectile attack");
				continue;
			}
			
			HitArea hitNow = simAttack.getHitAreaNow();
			int distToProjectile = Helper.DistanceBetweenBoxes(hitNow.getLeft(), hitNow.getRight(),
					currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight());
			//don't jump when projectile is in air/higher than standing
			if(hitNow.getBottom() < currentSimCharacters.m_a.getTop() && distToProjectile < 300)
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j +" a projectile is above me");
				
				Planner.INSTANCE.projectileAboveMe = true;
				jumpUpAllowed = false;
				continue;
			}
			if(simAttack.getSpeedY() != 0 && distToProjectile < 400)
			{
				if(Helper.DEBUG_PRECONDITIONS)
					System.out.println(j +" a diagonal projectile is in air");
					
				
				
				holds = true;
				close = true;
				Planner.INSTANCE.projectileAboveMe = true;
				//jumpUpAllowed = false;
				continue;
			}
			if(distToProjectile <= 80)
			{
				if(Helper.DEBUG_PRECONDITIONS)
					System.out.println(j +" a projectile is very close!");
				close = true;
				holds = true;
				continue;
			}
			
			if(currentSimCharacters.m_a.isFront() && hitNow.getLeft() >= currentSimCharacters.m_a.getRight())
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j +" a projectile is on my level - JUMP FORWARDS!");
				holds = true;
				
				int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
						currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
				if(dist <=280 || dist >=370 )
				{
					this.tasksToDecompose.add(new p_FOR_JUMP());
				}
				else
				{
					this.tasksToDecompose.add(new p_STAND_GUARD());
				}
			}
			else if(!currentSimCharacters.m_a.isFront() && hitNow.getRight() <= currentSimCharacters.m_a.getLeft())
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j +" a projectile is on my level - JUMP FORWARDS!");
				holds = true;
				
				int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
						currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
				if(dist <=280 || dist >=370 )
				{
					this.tasksToDecompose.add(new p_FOR_JUMP());
				}
				else
				{
					this.tasksToDecompose.add(new p_STAND_GUARD());
				}
			}
			else
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j + " a projectile is on my level, but behind me!");
			}
		}
		
		if(!jumpUpAllowed)
		{
			this.tasksToDecompose.clear();
			if(Helper.DEBUG_PRECONDITIONS)
			System.out.println("jumping not allowed - do nothing");
			
			//this.tasksToDecompose.add(new p_STAND_GUARD());
		}
		if(close)	
		{
			this.tasksToDecompose.clear();
			if(Helper.DEBUG_PRECONDITIONS)
			System.out.println("projectile close - p_STAND_GUARD");
			if(!Planner.INSTANCE.GetGameData().getCharacterName(true).equals("GARNET"))
			{
				this.tasksToDecompose.add(new p_STAND_GUARD());
			}
			else
			{
				this.tasksToDecompose.add(new p_BACK_JUMP());
			}
		}
		
		//TODO: instead of stand_guard use guard c_task
		return holds;
	}
	
}
