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

public class p_BACK_STEP extends PrimitiveTask{

	public p_BACK_STEP()
	{
		super();
		
		this.name = "p_BACK_STEP";	
		this.myAction = Action.BACK_STEP;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		MotionData mData = motionData.elementAt(this.myAction.ordinal());

		//check whether we have enough energy to perform the action
		int enRequired = mData.getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			return false;
		}
		
		if(!this.EnoughSpaceBackward(currentSimCharacters.m_a, mData.getSpeedX()))
		{
			holds = false;
		}
			
		return holds;
	}
    


}
