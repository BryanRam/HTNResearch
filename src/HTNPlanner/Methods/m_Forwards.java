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

public class m_Forwards extends Method 
{
	public m_Forwards()
	{
		super();
		
		this.name = "m_Forwards";
		this.tasksToDecompose.add(new c_Forwards());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		//avoid being in corners
		if(currentSimCharacters.m_a.getLeft()<10 || currentSimCharacters.m_a.getRemainingFrame() > Planner.INSTANCE.GetGameData().getStageWidth()-10)
		{
			return true;			
		}
				
		//reached border of stage/opponent
		
		
		//looking right
		if(currentSimCharacters.m_a.isFront() && 
				currentSimCharacters.m_a.getRight() >= Planner.INSTANCE.GetGameData().getStageWidth()- (currentSimCharacters.m_b.getRight()-currentSimCharacters.m_b.getLeft()))
		{
			return false;
		}
		
		if(!currentSimCharacters.m_a.isFront() && 
				currentSimCharacters.m_a.getLeft() <= (currentSimCharacters.m_b.getRight()-currentSimCharacters.m_b.getLeft()))
		{
			return false;
		}
		
		return true;
	}
}
