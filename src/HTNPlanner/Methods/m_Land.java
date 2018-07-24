/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import enumerate.Action;
import enumerate.AttackAction;
import enumerate.State;
import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.OrderedPlanner;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Land extends Method
{
	public m_Land()
	{
		super();
		
		this.name = "m_Land";
		this.tasksToDecompose.add(new c_Land());
	}
	
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		if(!currentSimCharacters.m_a.getState().equals(State.AIR))
		{
			holds = false;
		}

		return holds;
	}


}
