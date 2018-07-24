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

public class p_AIR extends PrimitiveTask{

	public p_AIR()
	{
		super();
		
		this.name = "p_AIR";	
		this.myAction = Action.AIR;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		
		//check whether we have enough energy to perform the action
		int enRequired = motionData.elementAt(this.myAction.ordinal()).getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			return false;
		}
		return holds;
	}
    


}
