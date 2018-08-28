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

public class m_QuickAttack extends Method 
{
	public m_QuickAttack()
	{
		super();
		
		this.name = "m_QuickAttack";

		this.tasksToDecompose.add(new c_QuickAttack());
		this.tasksToDecompose.add(new c_QuickAttack());
		//this.tasksToDecompose.add(new c_KnockBackAttack());
		//this.tasksToDecompose.add(new c_KnockBackAttack());
		this.tasksToDecompose.add(new c_KnockDownAttack());
		//this.tasksToDecompose.add(new c_KnockBackAttack());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//TODO: here
		int distH = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		//TODO: here
		if( distH > 200 ) //|| distH < 30 )
		{
			return false;
		}
		return true;
	}
}
