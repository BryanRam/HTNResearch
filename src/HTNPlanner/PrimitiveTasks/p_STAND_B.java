/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import java.util.Vector;

import enumerate.Action;
import enumerate.State;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;

public class p_STAND_B extends PrimitiveTask {

	public p_STAND_B()
	{
		super();
		
		this.name = "p_STAND_B";	
		this.myAction = Action.STAND_B;
	}
	

}
