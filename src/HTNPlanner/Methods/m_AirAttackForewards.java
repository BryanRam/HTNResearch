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
import enumerate.State;
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

public class m_AirAttackForewards extends Method 
{
	public m_AirAttackForewards()
	{
		super();
		
		this.name = "m_AirAttackForewards";
		this.tasksToDecompose.add(new c_AirAttackForewards());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		/*
		boolean holds = true;
		
	
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if(dist >300)
		{
			holds = false;
		}
		//avoid being in corners
		if( (currentSimCharacters.m_a.getLeft()>60 && currentSimCharacters.m_a.isFront()) || (currentSimCharacters.m_a.getRight() < Planner.INSTANCE.GetGameData().getStageWidth()-60 && !currentSimCharacters.m_a.isFront()))
		{
			holds = false;			
		}
		*/
		
		boolean holds = true;
		
		//jump over a projectile
		boolean projectile = CheckForProjectiles(currentSimCharacters);
		holds = holds || projectile;
		
		//opp far and no projectile - further checks not needed
		if(!holds)
		{
			return holds;
		}
		//projectile close - further checks for opp not needed
		else if(projectile)
		{
			this.tasksToDecompose.add(new c_AirAttackForewards());
			return holds;
		}
		
		//hol
	
		return holds;
	}
	
	private boolean CheckForProjectiles(Pair<SimCharacter, SimCharacter> currentSimCharacters)
	{
		boolean holds = false;
		
		Deque<SimAttack> simAttacks = Planner.INSTANCE.GetSimAttack();
		int j = 0;
		for(Iterator<SimAttack> i = simAttacks.iterator() ; i.hasNext() ; )
		{
			SimAttack simAttack = i.next();
			
 			j++;
			
			//0 for p1, 1 for p2										
			if(simAttack.isPlayerNumber() == Planner.INSTANCE.player)
			{
				if(Helper.DEBUG_PRECONDITIONS)
				System.out.println(j + " my projectile is in the air");
				continue;
			}

			HitArea hitNow = simAttack.getHitAreaNow();
			int distToProjectile = Helper.DistanceBetweenBoxes(hitNow.getLeft(), hitNow.getRight(),
					currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight());
			
			if(distToProjectile > 200)
			{
				continue;
			}
			//projectile below me
			if(hitNow.getTop() > currentSimCharacters.m_a.getBottom())
			{
				if(Helper.DEBUG_PRECONDITIONS)		
					System.out.println(" attack " + j + " is below me - move away in air!");
	 			Planner.INSTANCE.moveAwayInAir = true;
			}
		}
		
		return holds;
	}
}
