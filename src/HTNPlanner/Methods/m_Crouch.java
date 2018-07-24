/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Crouch extends Method 
{
	public m_Crouch()
	{
		super();
		
		this.name = "m_Crouch";
		this.tasksToDecompose.add(new p_CROUCH());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//cannot crouch in air
		if(currentSimCharacters.m_a.getBottom() < Planner.INSTANCE.GetGameData().getStageHeight())
		{
			return false;
		}
		
		return true;
	}
}
