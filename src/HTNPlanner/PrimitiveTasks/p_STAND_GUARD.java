/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import org.omg.PortableServer.CurrentOperations;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;

public class p_STAND_GUARD extends PrimitiveTask{

	public p_STAND_GUARD()
	{
		super();
		
		this.name = "p_STAND_GUARD";	
		this.myAction = Action.STAND_GUARD;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(false);
		
		int oppAttackType = motionData.elementAt(currentSimCharacters.m_b.getAction().ordinal()).attackType;
		
		//doesn't work for low and throw attacks
		if(oppAttackType == 3 || oppAttackType == 4)
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
