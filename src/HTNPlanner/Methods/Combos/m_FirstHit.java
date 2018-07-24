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

public class m_FirstHit extends Method 
{
	public m_FirstHit()
	{
		super();
		
		this.name = "m_FirstHit";

		this.tasksToDecompose.add(new c_PerformFirstHit());
		this.tasksToDecompose.add(new c_PerformSecondHit());
		this.tasksToDecompose.add(new c_PerformThirdHit());
		this.tasksToDecompose.add(new c_PerformFourthHit());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//&& prev hit starts a combo? 
		if(currentSimCharacters.m_a.getHitNumber() != 0)
		{
			return false;
		}
		
		return true;
	}
}
