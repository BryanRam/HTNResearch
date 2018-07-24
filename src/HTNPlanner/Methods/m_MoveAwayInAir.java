/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.omg.PortableInterceptor.CurrentOperations;

import enumerate.Action;
import enumerate.AttackAction;
import enumerate.State;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.HitArea;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.OrderedPlanner;
import HTNPlanner.Planner;
import HTNPlanner.Task;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_MoveAwayInAir extends Method
{
	public m_MoveAwayInAir()
	{
		super();
		
		this.name = "m_MoveAwayInAir";
		this.tasksToDecompose.add(new c_MoveAwayInAir());
	}
	
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		//if many frames of attack remain, if not a projectile attack
		if(!currentSimCharacters.m_a.getState().equals(State.AIR))
		{
			holds = false;
			return false;
		}
		
		Vector<MotionData> mData = Planner.INSTANCE.GetMotionData(false);
		Action oppAction = currentSimCharacters.m_b.getAction();
		
		if(Helper.DEBUG_PRECONDITIONS)		
		System.out.println("opp action  = " + oppAction.name());

		if(Helper.DEBUG_PRECONDITIONS)		
			System.out.println("rem frame = " + currentSimCharacters.m_b.getRemainingFrame());
		
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if(Helper.DEBUG_PRECONDITIONS)		
			System.out.println("dist to opp = " + dist);
		
		int distLeftBorder = currentSimCharacters.m_a.getLeft();
		int distRightBorder = 920 - currentSimCharacters.m_a.getRight();
		
		if(dist > 200)
		{
			holds = false;
			if(Helper.DEBUG_PRECONDITIONS)		
				System.out.println("dist to opp too big");
		}
		
		this.tasksToDecompose.clear();
		
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
		
		//holds because opp close
		//facing right
		if(currentSimCharacters.m_a.isFront())
		{
			if(Helper.DEBUG_PRECONDITIONS)		
				System.out.println("i am facing right");
			//opponent is in front of me
			if(currentSimCharacters.m_a.getLeft()<currentSimCharacters.m_b.getLeft())
			{
				if(Helper.DEBUG_PRECONDITIONS)		
					System.out.println("opp is in front of me");
				if(!currentSimCharacters.m_b.isFront())
				{
					if(distRightBorder > 200)
					{
						Planner.INSTANCE.moveAwayInAir = true;
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me - move forewards!");
					}
					else
					{
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me, close to corner - dont move forewards!");
						holds = false;
					}

				}
				else
				{
					holds = false;
					if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println("opp is not facing me - right!");

				}
			}
			//opponent is behind me
			else
			{
				if(Helper.DEBUG_PRECONDITIONS)		
					System.out.println("opp is behind me");
				if(currentSimCharacters.m_b.isFront())
				{
					if(distRightBorder > 200)
					{
						Planner.INSTANCE.moveAwayInAir = true;
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me - move forewards!");
					}
					else
					{
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me, close to corner - dont move forewards!");
						holds = false;
					}

				}
				else
				{
					holds = false;
					if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println("opp is not facing me - left!");
				}
			}
		}
		//facing left
		else
		{
			if(Helper.DEBUG_PRECONDITIONS)		
				System.out.println("i am facing left");
			//opponent is in front of me
			if(currentSimCharacters.m_a.getLeft()>currentSimCharacters.m_b.getLeft())
			{
				if(Helper.DEBUG_PRECONDITIONS)		
					System.out.println("opp is in front of me");
				if(currentSimCharacters.m_b.isFront())
				{	
					if(distLeftBorder > 200)
					{
						Planner.INSTANCE.moveAwayInAir = true;
						//this.tasksToDecompose.add(new c_AirAttackForewards());
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me - move forewards!");
					}
					else
					{
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me, close to corner - dont move forewards!");
						holds = false;
					}
				}
				else
				{
					holds = false;
					if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println("opp is not facing me - left!");

				}
			
			}
			//opponent is behind me
			else
			{
				if(Helper.DEBUG_PRECONDITIONS)		
					System.out.println("opp is behind me");
				if(!currentSimCharacters.m_b.isFront())
				{
					if(distLeftBorder > 200)
					{
						Planner.INSTANCE.moveAwayInAir = true;
						this.tasksToDecompose.add(new c_AirAttackForewards());
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me - move forewards!");
					}
					else
					{
						if(Helper.DEBUG_PRECONDITIONS)		
							System.out.println("opp is facing me, close to corner - dont move forewards!");
						holds = false;
					}
				}
				else
				{
					holds = false;
					if(Helper.DEBUG_PRECONDITIONS)		
						System.out.println("opp is not facing me - right!");

				}
			}
		}
		
		if(holds)
		{
			this.tasksToDecompose.add(new c_AirAttackForewards());
		}
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
