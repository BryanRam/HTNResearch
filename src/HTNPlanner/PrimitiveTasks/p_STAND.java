/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import util.Pair;
import HTNPlanner.PrimitiveTask;

public class p_STAND extends PrimitiveTask{

	public p_STAND()
	{
		super();
		
		this.name = "p_STAND";	
		this.myAction = Action.STAND;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		// TODO be on ground?
		return true;
	}
    


}
