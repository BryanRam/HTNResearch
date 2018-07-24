/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;
import enumerate.Action;
import enumerate.State;

public class p_CROUCH_B extends PrimitiveTask
{
	public p_CROUCH_B()
	{
		super();
		
		this.name = "p_CROUCH_B";	
		this.myAction = Action.CROUCH_B;
	}
	

}
