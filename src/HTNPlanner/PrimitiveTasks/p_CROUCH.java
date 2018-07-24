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

public class p_CROUCH extends PrimitiveTask{

	public p_CROUCH()
	{
		super();
		
		this.name = "p_CROUCH";	
		this.myAction = Action.CROUCH;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		
		//check whether we have enough energy to perform the action
		int enRequired = motionData.elementAt(this.myAction.ordinal()).getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			holds = false;
		}
		
		//i'm in air
		if(currentSimCharacters.m_a.getTop() < 430)
		{
			holds = false;
		}
		return holds;
	}
    


}
