/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;

public class p_FOR_JUMP extends PrimitiveTask{

	public p_FOR_JUMP()
	{
		super();
		
		this.name = "p_FOR_JUMP";	
		this.myAction = Action.FOR_JUMP;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		MotionData mData = motionData.elementAt(this.myAction.ordinal());

		//check whether we have enough energy to perform the action
		int enRequired = mData.getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			return false;
		}
	
		//facing left
		if(!currentSimCharacters.m_a.isFront() && currentSimCharacters.m_a.getLeft() < mData.getSpeedX())
		{
			return false;
		}
		
		//facing right
		if(currentSimCharacters.m_a.isFront() && currentSimCharacters.m_a.getRight() > Planner.INSTANCE.GetGameData().getStageWidth() - mData.getSpeedX())
		{
			return false;
		}

		return holds;
	}
    


}
