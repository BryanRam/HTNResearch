/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;
import enumerate.Action;

public class p_DO_NOTHING extends PrimitiveTask
{
	public p_DO_NOTHING()
	{
		super();
		
		this.name = "p_DO_NOTHING";	
		this.myAction = Action.NEUTRAL;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		boolean holds = true;
		if(!this.EnoughSpaceForward(currentSimCharacters.m_a, currentSimCharacters.m_b, 5))
		{
			System.out.println("............................................Don't move!");
			//holds = false;
		}
		return holds;
	}
    
}
