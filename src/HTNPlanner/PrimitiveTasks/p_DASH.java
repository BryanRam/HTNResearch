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

public class p_DASH extends PrimitiveTask{

	public p_DASH()
	{
		super();
		
		this.name = "p_DASH";	
		this.myAction = Action.DASH;
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
			holds = false;
		}
		
	/*	if(!this.EnoughSpaceForward(currentSimCharacters.m_a, currentSimCharacters.m_b, mData.getSpeedX()))
		{
			//holds = false;
		}*/

		//i'm in air
		if(currentSimCharacters.m_a.getTop() < 430)
		{
			holds = false;
		}
		return holds;
	}
    


}
