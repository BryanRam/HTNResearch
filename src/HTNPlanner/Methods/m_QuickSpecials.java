/*******************************************************************************
 * Keep Away Fighter
 * Created by Bradley Ramsay 2018
 * Inspired by HTN Fighter which was created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_QuickSpecials extends Method 
{
	public m_QuickSpecials()
	{
		super();
		
		this.name = "m_QuickAttack";

		//this.tasksToDecompose.add(new c_QuickSpecials());
		this.tasksToDecompose.add(new p_STAND_F_D_DFB());
		//this.tasksToDecompose.add(new c_QuickAttack());
		
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{ 
		/*if(Planner.INSTANCE.player == currentSimCharacters.m_a.isPlayerNumber())
		{
				if(currentSimCharacters.m_a.getEnergy() < 50)
			{
				return false;
			}
		}
		else
		{
			if(currentSimCharacters.m_b.getEnergy() < 50)
			{
				return false;
			}
			
		}*/
		
		//System.out.println("In Quick Specials");
		
		//TODO: here
		int distH = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		//TODO: here
		//System.out.println("Quick Special range: " + distH);
		if( distH > 400 )
		{
			
			return false;
		}
		return true;
	}
}
