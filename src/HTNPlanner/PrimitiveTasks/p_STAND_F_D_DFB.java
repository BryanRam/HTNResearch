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

public class p_STAND_F_D_DFB extends PrimitiveTask
{
	public p_STAND_F_D_DFB()
	{
		super();
		
		this.name = "p_STAND_F_D_DFB";	
		this.myAction = Action.STAND_F_D_DFB;
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters)
	{
		boolean holds = super.CheckPreconditions(currentSimCharacters);
		if(!Planner.INSTANCE.GetGameData().getCharacterName(true).equals("ZEN") ||!holds)
		{
			return holds;
		}
		
		//for ZEN, when holds
		if(currentSimCharacters.m_b.getBottom() < 550)
		{
			holds = false;
			if(Helper.DEBUG_PRECONDITIONS)
			{
				System.out.println("opponent is in air, do not execute " + this.name);
			}
		}
		else
		{
			if(Helper.DEBUG_PRECONDITIONS)
			{
				System.out.println("o_bottom = " + currentSimCharacters.m_b.getBottom());
			}
		}
		return holds;
	}
}
