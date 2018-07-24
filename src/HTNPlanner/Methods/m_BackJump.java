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

public class m_BackJump extends Method 
{
	public m_BackJump()
	{
		super();
		
		this.name = "m_BackJump";
		this.tasksToDecompose.add(new p_BACK_JUMP());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//reached border of stage/opponent
		
		//looking left
		if(!currentSimCharacters.m_a.isFront() && 
				currentSimCharacters.m_a.getRight() >= Planner.INSTANCE.GetGameData().getStageWidth()- (currentSimCharacters.m_b.getRight()-currentSimCharacters.m_b.getLeft()))
		{
			return false;
		}
		
		if(currentSimCharacters.m_a.isFront() && 
				currentSimCharacters.m_a.getLeft() <= (currentSimCharacters.m_b.getRight()-currentSimCharacters.m_b.getLeft()))
		{
			return false;
		}
		
		
		return true;
	}
}
