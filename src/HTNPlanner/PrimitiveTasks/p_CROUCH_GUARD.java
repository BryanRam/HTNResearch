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

public class p_CROUCH_GUARD extends PrimitiveTask{

	public p_CROUCH_GUARD()
	{
		super();
		
		this.name = "p_CROUCH_GUARD";	
		this.myAction = Action.CROUCH_GUARD;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		
		int oppAttackType = motionData.elementAt(currentSimCharacters.m_b.getAction().ordinal()).attackType;
		
		//doesn't work for middle and throw attacks
		if(oppAttackType == 2 || oppAttackType == 4)
		{
			//holds = false;
		}
		
		//i'm in air
		if(currentSimCharacters.m_a.getTop() < 430)
		{
			holds = false;
		}
		return holds;
	}
    


}
