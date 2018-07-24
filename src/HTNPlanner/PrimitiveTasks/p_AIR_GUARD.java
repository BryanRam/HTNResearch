/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;

public class p_AIR_GUARD extends PrimitiveTask{

	public p_AIR_GUARD()
	{
		super();
		
		this.name = "p_AIR_GUARD";	
		this.myAction = Action.AIR_GUARD;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		MotionData mData = motionData.elementAt(this.myAction.ordinal());

		//check whether we have enough energy to perform the action
		int enRequired = mData.getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			return false;
		}
		
		int oppAttackType = motionData.elementAt(currentSimCharacters.m_b.getAction().ordinal()).attackType;
		
		//doesn't work for low attacks
		if(oppAttackType == 3)
		{
			//holds = false;
		}
		
		//we are both standing
		/*if(currentSimCharacters.m_b.getTop() > 430 && currentSimCharacters.m_a.getTop() > 430)
		{
			holds = false;
		}*/
		
		return holds;
	}
    


}
