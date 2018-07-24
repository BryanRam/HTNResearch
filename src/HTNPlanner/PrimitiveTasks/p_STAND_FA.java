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
import aiinterface.AIInterface;

public class p_STAND_FA extends PrimitiveTask
{
	public p_STAND_FA()
	{
		super();
		
		this.name = "p_STAND_FA";	
		this.myAction = Action.STAND_FA;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		if(!Planner.INSTANCE.GetGameData().getCharacterName(true).equals("GARNET"))
			return super.CheckPreconditions(currentSimCharacters);
		
		//for GARNET
		if(currentSimCharacters.m_b.getBottom()<630)
		{
			return false;
		}
		
		//GARNET & opponent not in air
		return super.CheckPreconditions(currentSimCharacters);

	}
	
	
}
