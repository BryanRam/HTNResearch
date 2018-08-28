/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import enumerate.State;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_ProjectileAttack extends Method 
{
	public m_ProjectileAttack()
	{
		super();
		
		this.name = "m_ProjectileAttack";

		this.tasksToDecompose.add(new c_ProjectileAttack());

	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{		
		boolean holds = true;
		/*
		//Projectile attacks have too much startup to be a good risk to take at lower health levels
		if(currentSimCharacters.m_b.getHp() < 100 || currentSimCharacters.m_a.getHp() < 100)
		{
			return false;
		}
		*/
		
		if(Planner.INSTANCE.player == currentSimCharacters.m_a.isPlayerNumber())
		{
				if(currentSimCharacters.m_a.getHp() < 50)
			{
				return false;
			}
		}
		else
		{
			if(currentSimCharacters.m_b.getHp() < 50)
			{
				return false;
			}
			
		}

		if (!currentSimCharacters.m_b.isControl() && !currentSimCharacters.m_b.getState().equals(State.AIR))
		{
			//System.out.println("opponent is not controllable - no projectile");
		//	holds = false;
		}
		
		int diffV= currentSimCharacters.m_b.getTop() -currentSimCharacters.m_a.getBottom();
		int diffH=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
				 currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
			
	//	System.out.println("diff v " + diffV + " diff h " + diffH);
		
		
		if(diffV > 50 && diffH < 55)
		{
			System.out.println("i'm above - no projectile");
			holds = false;
		}
		if(diffV > 10 && diffH < 15)
		{
			System.out.println("i'm above - no projectile");
			holds = false;
		}
		
		int diffVopp= currentSimCharacters.m_a.getTop() -currentSimCharacters.m_b.getBottom();
		int diffHopp=Helper.DistanceBetweenBoxes(currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight(), 
				 currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight());
			
	//	System.out.println("diff v " + diffV + " diff h " + diffH);
		
	/*	
		if(diffVopp > 50 && diffHopp < 55)
		{
	//		System.out.println("he is above - no projectile");
			holds = false;
		}
		if(diffVopp > 10 && diffHopp < 15)
		{
	//		System.out.println("he is above - no projectile");
			holds = false;
		}
		*/
		return holds;
	}
}
