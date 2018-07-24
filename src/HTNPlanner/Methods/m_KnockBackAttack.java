/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
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

public class m_KnockBackAttack extends Method 
{
	public m_KnockBackAttack()
	{
		super();
		
		this.name = "m_KnockBackAttack";

		this.tasksToDecompose.add(new c_KnockBackAttack());
		this.tasksToDecompose.add(new c_KnockBackAttack());
		this.tasksToDecompose.add(new c_KnockDownAttack());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//TODO: here
		int distH = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		//TODO: here
		if( distH >300 ) //|| distH < 30 )
		{
			return false;
		}
		return true;
	}
}
