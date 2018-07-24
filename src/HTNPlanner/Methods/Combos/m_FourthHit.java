/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods.Combos;

import java.util.Vector;

import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_FourthHit extends Method 
{
	public m_FourthHit()
	{
		super();
		
		this.name = "m_FourthHit";
		this.tasksToDecompose.add(new c_PerformFourthHit());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		if(currentSimCharacters.m_a.getHitNumber() != 3)
		{
			return false;
		}
		
		return true;
	}
}
