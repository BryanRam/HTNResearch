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

public class p_STAND_A extends PrimitiveTask {

	public p_STAND_A()
	{
		super();
		
		this.name = "p_STAND_A";	
		this.myAction = Action.STAND_A;
	}
	
}
