/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.PrimitiveTasks;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import util.Pair;
import HTNPlanner.PrimitiveTask;

public class p_DOWN extends PrimitiveTask{

	public p_DOWN()
	{
		super();
		
		this.name = "p_DOWN";	
		this.myAction = Action.DOWN;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) {
		// TODO Auto-generated method stub
		return true;
	}
    


}
