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

public class p_STAND_D_DF_FC extends PrimitiveTask
{
	public p_STAND_D_DF_FC()
	{
		super();
		
		this.name = "p_STAND_D_DF_FC";	
		this.myAction = Action.STAND_D_DF_FC;
	}
	
}
